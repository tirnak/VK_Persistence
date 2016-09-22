package tirnak.persistence;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.NullObjects;
import tirnak.persistence.common.VkSeleniumGeneric;
import tirnak.persistence.model.Audio;
import tirnak.persistence.model.Person;
import tirnak.persistence.model.Post;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WallExtractor extends VkSeleniumGeneric {

    private static final String RE_POST_CLASS = "copy_quote";
    private static final String XPATH_QUERY_CLASS_EXCEPT = ".//*[@class!=\"" + RE_POST_CLASS + "\"]/*[@class=\"{}\"]";

    private static String getXpathQueryClassExcept(String className) {
        return XPATH_QUERY_CLASS_EXCEPT.replace("{}", className);
    }
    private static final String POSTS_FROM_PAGE_QUERY = ".//*[@id=\"page_wall_posts\"]/*[starts-with(@id,\"post\")]";
    private static final String POST_QUERY = getXpathQueryClassExcept("_post");
    private static final String RE_POST_QUERY = getXpathQueryClassExcept(RE_POST_CLASS);
    private static final String POST_TEXT_QUERY = getXpathQueryClassExcept("wall_post_text");
    private static final String POST_DATE_QUERY = getXpathQueryClassExcept("published_by_date");
    private static final String POST_AUDIO_RECORD_QUERY = getXpathQueryClassExcept("audio_row");
    private static final String SCROLL_WALL_BUTTON_QUERY = getXpathQueryClassExcept("wall_more_link");
    private static final String LIKE_QUERY = getXpathQueryClassExcept("like_tt_owner");

    long userId;

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

    public Post parsePost(WebElement postDiv) {
        return new PostParser(postDiv).parse();
    }

    private class PostParser {
        private static final String AUDIO_PERFORMER_CLASS = "audio_performer";
        private static final String AUDIO_TITLE_CLASS = "audio_title";
        private Post post;
        private WebElement postDom;

        public PostParser(WebElement postEl) {
            this.postDom = postEl;
        }

        public Post parse() {
            post = new Post();
            post.setText(getText());
            post.setDate(getDate());
            for (Audio audio : getAudios()) {
                audio.setPost(post); post.addAudio(audio);
            }
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

        private Audio[] getAudios() {
            try {
                return postDom.findElements(By.className(POST_AUDIO_RECORD_QUERY)).stream().map(el -> {
                    Audio audio = new Audio();
                    audio.setId(el.getAttribute("id").replace("audio_", ""));
                    audio.setPerformer(el.findElement(By.className(AUDIO_PERFORMER_CLASS)).getText());
                    audio.setTrack(el.findElement(By.className(AUDIO_TITLE_CLASS)).getText());
                    return audio;
                }).toArray(Audio[]::new);
            } catch (NoSuchElementException e) {
                return NullObjects.getEmptyArray();
            }
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
