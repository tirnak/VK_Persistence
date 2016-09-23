package tirnak.persistence;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.DomIterator;
import tirnak.persistence.common.NullObjects;
import tirnak.persistence.common.Parsable;
import tirnak.persistence.common.VkSeleniumGeneric;
import tirnak.persistence.model.Audio;
import tirnak.persistence.model.Person;
import tirnak.persistence.model.Post;
import tirnak.persistence.model.Text;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WallExtractor extends VkSeleniumGeneric {

    public static final String RE_POST_CLASS = "copy_quote";
    public static final String XPATH_QUERY_CLASS_EXCEPT = "(self::div|.//*[@class!=\"copy_quote\"])/*[@class=\"{}\"]";

    public static String getXpathQueryClassExcept(String className) {
        return XPATH_QUERY_CLASS_EXCEPT.replace("{}", className);
    }
    public static final String POSTS_FROM_PAGE_QUERY = ".//*[@id=\"page_wall_posts\"]/*[starts-with(@id,\"post\")]";
    public static final String POST_QUERY = getXpathQueryClassExcept("_post");
    public static final String RE_POST_QUERY = getXpathQueryClassExcept(RE_POST_CLASS);
    public static final String POST_TEXT_QUERY = getXpathQueryClassExcept("wall_post_text");
    public static final String POST_DATE_QUERY = getXpathQueryClassExcept("published_by_date");
    public static final String POST_AUDIO_RECORD_QUERY = getXpathQueryClassExcept("audio_row");
    public static final String SCROLL_WALL_BUTTON_QUERY = getXpathQueryClassExcept("wall_more_link");
    public static final String LIKE_QUERY = getXpathQueryClassExcept("like_tt_owner");

    long userId;
    private static DomIterator domIterator;
    static {
        Map<Predicate<WebElement>, BiConsumer<WebElement, Post>> checkingFunctions = new HashMap<>();
        Class[] classes = {
            Text.class,
            Audio.class,

        };
        for (Class clazz : classes) {
            try {
                Parsable instance = (Parsable) clazz.newInstance();
                checkingFunctions.put(instance.getPredicateIfAppropriateDom(), instance.getFunctionForParsing());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        domIterator = new DomIterator(checkingFunctions);
    }

    public WallExtractor(WebDriver driver, long userId) {
        super(driver);
        this.userId = userId;
    }

    public void goToWall() {
        driver.navigate().to(baseUrl + "/id" + userId);
    }
    /**
     * A method to scroll down the wall aimed to cope with lazy initialization of wall
     */
    public void scrollToEnd() throws InterruptedException {
        try {
            while (needsToBeLoaded()) {
                driver.findElement(By.id(SCROLL_WALL_BUTTON_QUERY)).click();
            }
        } catch (Exception ignored) {}

    }

    private boolean needsToBeLoaded() {
        try {
            driver.findElement(By.id(SCROLL_WALL_BUTTON_QUERY));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public List<WebElement> getPostDivs() {
        return driver.findElements(By.xpath(POSTS_FROM_PAGE_QUERY));
    }

    public WebElement getRepostDivs(WebElement post) {
        try {
            return driver.findElement(By.xpath(RE_POST_QUERY));
        } catch (NoSuchElementException e) {
            return null;
        }

    }

    public Post parsePost(WebElement postDiv) {
        return new PostParser(postDiv).parse();
    }

    private class PostParser {
        private Post post;
        private WebElement postDom;

        public PostParser(WebElement postEl) {
            this.postDom = postEl;
        }

        public Post parse() {
            post = new Post();
            post.setDate(getDate());
            for (Person person : getLikedBy()) {
                person.addLike(post);
                post.addLikedBy(person);
            }
            post.setRepostOf(getRepost());
            return post;
        }

        private Post getRepost() {
            try {
                return new PostParser(postDom.findElement(By.xpath(RE_POST_QUERY))).parse();
            } catch (NoSuchElementException e) {
                return null;
            }

        }

        private String getText() {
            try {
                return postDom.findElement(By.xpath(POST_TEXT_QUERY)).getText();
            } catch (NoSuchElementException e) {
                return NullObjects.getEmptyString();
            }
        }

        private String getDate() {
            return postDom.findElement(By.xpath(POST_DATE_QUERY)).getText();
        }

        private Set<Person> getLikedBy() {
            try {
                return postDom.findElements(By.className(LIKE_QUERY)).stream().map(el -> {
                    Person person = new Person();
                    person.setHref(el.getAttribute("href"));
                    person.setFullName(el.getAttribute("title"));
                    return person;
                }).collect(Collectors.toSet());
            } catch (NoSuchElementException e) {
                return NullObjects.getEmptySet();
            }
        }
    }
}
