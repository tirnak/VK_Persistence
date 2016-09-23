package tirnak.persistence.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.Parsable;

import javax.persistence.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@Entity
@Table(name="audio")
public class Audio implements Parsable {

    @Id
    @Column(name = "audio_id")
    private String id;

    @Column(name = "performer")
    private String performer;

    @Column(name = "track")
    private String track;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> el.getAttribute("class").contains("audio_row");
    }

    private static final String AUDIO_PERFORMER_CLASS = "audio_performer";
    private static final String AUDIO_TITLE_CLASS = "audio_title";

    @Override
    public BiConsumer<WebElement, Post> getFunctionForParsing() {
        return (el, post) -> {
            Audio audio = new Audio();
            audio.setId(el.getAttribute("id").replace("audio_", ""));
            audio.setPerformer(el.findElement(By.className(AUDIO_PERFORMER_CLASS)).getText());
            audio.setTrack(el.findElement(By.className(AUDIO_TITLE_CLASS)).getText());
            post.addAudio(audio);
        };
    }
}
