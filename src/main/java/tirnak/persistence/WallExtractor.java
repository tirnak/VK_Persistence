package tirnak.persistence;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import tirnak.persistence.common.DomIterator;
import tirnak.persistence.common.VkSeleniumGeneric;
import tirnak.persistence.model.Post;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WallExtractor extends VkSeleniumGeneric {

    public static final String XPATH_QUERY_CLASS_EXCEPT = "(self::div|.//*[@class!=\"copy_quote\"])/*[@class=\"{}\"]";
    public static String getXpathQueryClassExcept(String className) {
        return XPATH_QUERY_CLASS_EXCEPT.replace("{}", className);
    }
    public static final String POSTS_FROM_PAGE_QUERY = ".//*[@id=\"page_wall_posts\"]/*[starts-with(@id,\"post\")]";
    public static final String SCROLL_WALL_BUTTON_QUERY = getXpathQueryClassExcept("wall_more_link");

    long userId;
    private DomIterator domIterator;

    public WallExtractor(DomIterator domIterator, WebDriver driver, long userId) {
        super(driver);
        this.userId = userId;
        this.domIterator = domIterator;
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

    public Post parsePost(WebElement el) {
        Post post = new Post();
        String script =  el.findElement(By.className("post_like")).getAttribute("onmouseover");
        ((JavascriptExecutor) driver).executeScript(script);
        domIterator.visit(el, post);
        return post;
    }

    public List<WebElement> getPostDivs() {
        return driver.findElements(By.xpath(POSTS_FROM_PAGE_QUERY));
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
