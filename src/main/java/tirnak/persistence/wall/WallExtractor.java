package tirnak.persistence.wall;

import org.hibernate.SessionFactory;
import org.openqa.selenium.*;
import tirnak.persistence.common.DomIterator;
import tirnak.persistence.common.Repeat;
import tirnak.persistence.common.VkSeleniumGeneric;
import tirnak.persistence.handlers.containers.BasePostHandlerContainer;
import tirnak.persistence.handlers.containers.LikeHandlerContainer;
import tirnak.persistence.handlers.containers.RepostHandlerContainer;
import tirnak.persistence.model.Post;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Primary class to
 */
public class WallExtractor extends VkSeleniumGeneric {

    public static final String SCROLL_WALL_BUTTON_QUERY = "wall_more_link";
    public static final String EXPAND_CLASS = "wall_post_more";

    private String userId;
    private DomIterator baseDomIterator;
    private DomIterator likeDomIterator;
    private DomIterator repostDomIterator;

    public WallExtractor(WebDriver driver, SessionFactory sessionFactory) {
        super(driver);
        this.userId = driver.findElement(By.id("top_profile_link")).getAttribute("href");
        baseDomIterator = new DomIterator(new BasePostHandlerContainer(sessionFactory));
        likeDomIterator = new DomIterator(new LikeHandlerContainer(sessionFactory));
        repostDomIterator = new DomIterator(new RepostHandlerContainer(sessionFactory));
    }

    public void goToWall() {
        driver.navigate().to(
            userId.contains("http") ? userId : baseUrl + userId);
    }

    /**
     * A method to scroll down the wall aimed to cope with lazy initialization of wall
     */
    public void scrollToEnd() throws InterruptedException {
        Repeat.procedure(this::scrollDown)
        .during(Duration.ofMinutes(10))
        .orUntil(() -> !needsToBeLoaded()).run();
        for (WebElement link : driver.findElements(By.className(EXPAND_CLASS))) {
            link.click();
        }
    }

    public void scrollDownNTimes(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            scrollDown();
        }
        for (WebElement link : driver.findElements(By.className(EXPAND_CLASS))) {
            try {
                link.click();
            } catch (WebDriverException ignored) {}
        }
    }

    private void scrollDown() {
        try {

            driver.findElement(By.id(SCROLL_WALL_BUTTON_QUERY)).click();
        } catch (Exception ignored) {}

    }

    private boolean needsToBeLoaded() {
        try {
            WebElement el = driver.findElement(By.id(SCROLL_WALL_BUTTON_QUERY));
            if (el.isDisplayed()) {
                return true;
            }
        } catch (NoSuchElementException e) {
        } finally {
            return false;
        }
    }

    public Post parsePost(Supplier<WebElement> el) {
        PostDivWrapper postWrapper = new PostDivWrapper(el);
        return parsePost(postWrapper);
    }

    public Post parsePost(PostDivWrapper postWrapper) {
        postWrapper.iterateBy(baseDomIterator);
        if (postWrapper.hasLikes()) {
            long timeout = System.currentTimeMillis() + 10 * 1000;
//            while (!postWrapper.areLikesConsistent() && System.currentTimeMillis() < timeout ) {
//                postWrapper.showLikesOfPost();
//                postWrapper.iterateBy(likeDomIterator);
//            }
            Repeat.procedure(() -> {
                postWrapper.showLikesOfPost();
                postWrapper.iterateBy(likeDomIterator);})
            .during(Duration.ofSeconds(10)).orUntil(() -> postWrapper.areLikesConsistent()).run();
        }
        if (postWrapper.hasReposts()) {
            long timeout = System.currentTimeMillis() + 10 * 1000;
//            while (!postWrapper.areLikesConsistent() && System.currentTimeMillis() < timeout ) {
//                postWrapper.showRepostedOfPost();
//                postWrapper.iterateBy(repostDomIterator);
//            }
            Repeat.procedure(() -> {
                postWrapper.showRepostedOfPost();
                postWrapper.iterateBy(repostDomIterator);})
            .during(Duration.ofSeconds(10)).orUntil(postWrapper::areRepostedsConsistent).run();
        }
        return postWrapper.getPost();
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
