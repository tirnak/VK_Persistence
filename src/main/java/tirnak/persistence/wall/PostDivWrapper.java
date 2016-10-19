package tirnak.persistence.wall;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import tirnak.persistence.common.DomIterator;
import tirnak.persistence.model.Post;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class PostDivWrapper {
    public static final String POSTS_FROM_PAGE_QUERY = ".//*[@id=\"page_wall_posts\"]/*[starts-with(@id,\"post\")]";
    public static final String POST_CSS_CLASS = "_post";
    private final Post post = new Post();
    private final WebElement el;
    private final FirefoxDriver driver;


    public PostDivWrapper(WebElement postDiv) {
        if (!wrapString(postDiv.getAttribute("class")).contains(POST_CSS_CLASS)) {
            throw new IllegalArgumentException("webElement must contain class '_post'");
        }
        el = postDiv;
        driver = ((FirefoxDriver)((WrapsDriver) el).getWrappedDriver());
    }

    public static PostDivWrapper[] getPostDivs(WebDriver driver) {
        return driver.findElements(By.xpath(POSTS_FROM_PAGE_QUERY))
            .stream().map(PostDivWrapper::new).toArray(PostDivWrapper[]::new);
    }

    public boolean hasLikes() {
        return elementWithClassContainsPositiveNumber("post_like_count");
    }

    public boolean hasReposts() {
        return elementWithClassContainsPositiveNumber("post_share_count");
    }

    private boolean elementWithClassContainsPositiveNumber(String cssClass) {
        try {
            String likeCount = el.findElement(By.className(cssClass)).getText();
            return Integer.parseInt(likeCount) > 0;
        } catch (NoSuchElementException | NumberFormatException e) {
            return false;
        }
    }

    public void showLikesOfPost() {
        String script = el.findElement(By.className("post_like")).getAttribute("onmouseover");
        driver.executeScript(script);
        try {
            driver.executeScript("arguments[0].querySelector('.like_tt').style.display='block'", el);
        } catch (WebDriverException e) {
            System.out.println(el.getAttribute("id") + " has no likes");
        }
    }

    public void showRepostedOfPost() {
        String script = el.findElement(By.className("post_share")).getAttribute("onmouseover");
        driver.executeScript(script);
        try {
            driver.executeScript("arguments[0].querySelector('.like_tt').style.display='block'", el);
        } catch (WebDriverException e) {
            System.out.println(el.getAttribute("id") + " has no likes");
        }
    }

    public void iterateBy(DomIterator baseDomIterator) {
        baseDomIterator.visit(el, post);
    }

    public Post getPost() {
        return post;
    }
}
