package tirnak.persistence.wall;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import tirnak.persistence.common.DomIterator;
import tirnak.persistence.common.VkSeleniumGeneric;
import tirnak.persistence.handlers.containers.BasePostHandlerContainer;
import tirnak.persistence.handlers.containers.LikeHandlerContainer;
import tirnak.persistence.handlers.containers.RepostHandlerContainer;
import tirnak.persistence.model.Post;

import java.util.List;

public class WallExtractor extends VkSeleniumGeneric {

    public static final String XPATH_QUERY_CLASS_EXCEPT = "(self::div|.//*[@class!=\"copy_quote\"])/*[@class=\"{}\"]";
    public static String getXpathQueryClassExcept(String className) {
        return XPATH_QUERY_CLASS_EXCEPT.replace("{}", className);
    }
    public static final String POSTS_FROM_PAGE_QUERY = ".//*[@id=\"page_wall_posts\"]/*[starts-with(@id,\"post\")]";
    public static final String SCROLL_WALL_BUTTON_QUERY = "wall_more_link";
    public static final String EXPAND_CLASS = "wall_post_more";

    long userId;
    private DomIterator baseDomIterator = new DomIterator(new BasePostHandlerContainer());
    private DomIterator likeDomIterator = new DomIterator(new LikeHandlerContainer());
    private DomIterator repostDomIterator = new DomIterator(new RepostHandlerContainer());

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
        while (needsToBeLoaded()) {
            scrollDown();
        }
        for (WebElement link : driver.findElements(By.className(EXPAND_CLASS))) {
            link.click();
        }
    }

    /**
     * A method to scroll down the wall aimed to cope with lazy initialization of wall
     */
    public void scrollDownNTimes(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            scrollDown();
        }
    }

    private void scrollDown() {
        try {
            driver.findElement(By.id(SCROLL_WALL_BUTTON_QUERY)).click();
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

    public Post parsePost(WebElement el) {
        Post post = new Post();

        baseDomIterator.visit(el, post);
        showLikesOfPost(el);
        likeDomIterator.visit(el, post);
        showRepostedOfPost(el);
        repostDomIterator.visit(el, post);
        return post;
    }

    public List<WebElement> getPostDivs() {
        return driver.findElements(By.xpath(POSTS_FROM_PAGE_QUERY));
    }

    private void showLikesOfPost(WebElement el) {
        String script = el.findElement(By.className("post_like")).getAttribute("onmouseover");
        ((FirefoxDriver) driver).executeScript(script);
        ((FirefoxDriver) driver).executeScript("arguments[0].querySelector('.like_tt').style.display='block'", el);
    }

    private void showRepostedOfPost(WebElement el) {
        String script = el.findElement(By.className("post_share")).getAttribute("onmouseover");
        ((FirefoxDriver) driver).executeScript(script);
        ((FirefoxDriver) driver).executeScript("arguments[0].querySelector('.like_tt').style.display='block'", el);
    }

//    public WebElement getRepostDivs(WebElement post) {
//        try {
//            return driver.findElement(By.xpath(RE_POST_QUERY));
//        } catch (NoSuchElementException e) {
//            return null;
//        }
//
//    }

}
