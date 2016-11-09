package tirnak.persistence.wall;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.internal.WrapsDriver;
import tirnak.persistence.common.DomIterator;
import tirnak.persistence.model.Like;
import tirnak.persistence.model.Post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static tirnak.persistence.common.StringEnhanced.wrapString;

/**
 * Wrapper of posts DomElement
 * Executes some lazy init of likes and reposts
 * Validates element and checks consistency
 */
public class PostDivWrapper {
    public static final String POSTS_FROM_PAGE_QUERY = ".//*[@id=\"page_wall_posts\"]/*[starts-with(@id,\"post\")]";
    public static final String POST_CSS_CLASS = "_post";
    private final Post post = new Post();
    private final Supplier<WebElement> elSupplier;
    private final ChromeDriver driver;


    public PostDivWrapper(Supplier<WebElement> supplier) {
        elSupplier = supplier;
        driver = ((ChromeDriver)((WrapsDriver) elSupplier.get()).getWrappedDriver());
        if (!wrapString(elSupplier.get().getAttribute("class")).contains(POST_CSS_CLASS)) {
            throw new IllegalArgumentException("webElement must contain class '_post'");
        }
    }

    public static Collection<PostDivWrapper> getPostDivs(WebDriver driver) {
        List<PostDivWrapper> elements = new ArrayList<>();
        List<Supplier<WebElement>> suppliers = new ArrayList<>();
        try {
            driver.findElements(By.xpath(POSTS_FROM_PAGE_QUERY)).forEach(_el -> {
                String id = _el.getAttribute("id");
                suppliers.add(() -> driver.findElement(By.id(id)));
            });
        } catch (Exception ignored) {}
        for (Supplier s : suppliers) {
            elements.add(new PostDivWrapper(s));
        }
        return elements;
    }

    public boolean hasLikes() {
        return elementWithClassContainsPositiveNumber("post_like_count");
    }

    public boolean hasReposts() {
        return elementWithClassContainsPositiveNumber("post_share_count");
    }

    private boolean elementWithClassContainsPositiveNumber(String cssClass) {
        try {
            String likeCount = elSupplier.get().findElement(By.className(cssClass)).getText();
            return Integer.parseInt(likeCount) > 0;
        } catch (NoSuchElementException | NumberFormatException e) {
            return false;
        }
    }

    public void showLikesOfPost() {
        showLazyOf("post_like");
    }

    public void showRepostedOfPost() {
        showLazyOf("post_share");
    }

    private void showLazyOf(String cssClassOfInitiator) {
        String script = elSupplier.get().findElement(By.className(cssClassOfInitiator)).getAttribute("onmouseover");
        driver.executeScript(script);
        try {
            driver.executeScript("arguments[0].querySelector('.like_tt').style.display='block'", elSupplier.get());
        } catch (WebDriverException e) {
            System.out.println(elSupplier.get().getAttribute("id") + " has no likes");
        }
    }

    public void iterateBy(DomIterator iterator) {
        iterator.visit(elSupplier.get(), post);
    }

    public Post getPost() {
        checkConsistency();
        return post;
    }

    public void checkConsistency() {
        if (hasLikes() && post.getLikes() == null) {
            throw new IllegalStateException("there is a positive number of likes in div and no likes in Post");
        }
        if (hasReposts()) {
            boolean hasReposts = post.getLikes().stream().anyMatch(Like::isReposted);
            if (!hasReposts) {
                throw new IllegalStateException("there is a positive number of reposts in div and no reposts in Post");
            }
        }
    }

    public boolean areLikesConsistent() {
        if (post.getLikes() == null || post.getLikes().size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean areRepostedsConsistent() {
        if (post.getLikes() != null && post.getLikes().stream().anyMatch(Like::isReposted)) {
            return true;
        } else {
            return true;
        }
    }
}
