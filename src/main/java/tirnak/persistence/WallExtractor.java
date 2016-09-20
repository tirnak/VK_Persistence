package tirnak.persistence;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.VkSeleniumGeneric;
import tirnak.persistence.model.Audio;
import tirnak.persistence.model.Person;
import tirnak.persistence.model.Post;

import java.util.Set;
import java.util.stream.Collectors;

public class WallExtractor extends VkSeleniumGeneric {

    private static final String POST_CLASS = "_post";
    private static final String RE_POST_CLASS = "post_copy";
    private static final String POST_TEXT_CLASS = "wall_post_text";
    private static final String POST_AUDIO_RECORD_CLASS = "audio_row";
    private static final String SCROLL_WALL_BUTTON_CLASS = "wall_more_link";

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
                driver.findElement(By.id(SCROLL_WALL_BUTTON_CLASS)).click();
            }
        } catch (Exception ignored) {}

    }

    private boolean needsToBeLoaded() {
        try {
            driver.findElement(By.id(SCROLL_WALL_BUTTON_CLASS));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }

    }

    public Post parsePost(WebElement postDiv) {
        return new PostParser(postDiv).parse();
    }

    private class PostParser {
        private static final String AUDIO_PERFORMER_CLASS = "audio_performer";
        private static final String AUDIO_TITLE_CLASS = "audio_title";
        private static final String LIKE_CLASS = "like_tt_owner";
        private static final String DATE_CLASS = "published_by_date";
        private Post post;
        private WebElement postDom;

        public PostParser(WebElement postEl) {
            this.postDom = postEl;
        }

        public Post parse() {
            post = new Post();
            post.setText(getText());
            for (Audio audio : getAudios()) {
                audio.setPost(post); post.addAudio(audio);
            }
            for (Person person : getLikedBy()) {
                person.addLike(post); post.addLikedBy(person);
            }
            return post;
        }

        private String getText() {
            return postDom.findElement(By.className(POST_TEXT_CLASS)).getText();
        }
        private String getDate() {
            return postDom.findElement(By.className(DATE_CLASS)).getText();
        }

        private Audio[] getAudios() {
            return postDom.findElements(By.className(POST_AUDIO_RECORD_CLASS)).stream().map(el -> {
                Audio audio = new Audio();
                audio.setId(el.getAttribute("id").replace("audio_", ""));
                audio.setPerformer(el.findElement(By.className(AUDIO_PERFORMER_CLASS)).getText());
                audio.setTrack(el.findElement(By.className(AUDIO_TITLE_CLASS)).getText());
                return audio;
            }).toArray(Audio[]::new);
        }

        private Set<Person> getLikedBy() {
            return postDom.findElements(By.className(LIKE_CLASS)).stream().map(el -> {
                Person person = new Person();
                person.setHref(el.getAttribute("href"));
                person.setFullName(el.getAttribute("title"));
                return person;
            }).collect(Collectors.toSet());
        }
    }
}
