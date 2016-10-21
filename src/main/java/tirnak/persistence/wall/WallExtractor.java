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
        PostDivWrapper postWrapper = new PostDivWrapper(el);
        return parsePost(postWrapper);
    }

    public Post parsePost(PostDivWrapper postWrapper) {
        postWrapper.iterateBy(baseDomIterator);
        if (postWrapper.hasLikes()) {
            Repeat.procedure(() -> {
                postWrapper.showLikesOfPost();
                postWrapper.iterateBy(likeDomIterator);})
            .during(Duration.ofSeconds(10)).orUntil(postWrapper::areLikesConsistent).run();
        }
        if (postWrapper.hasReposts()) {
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
