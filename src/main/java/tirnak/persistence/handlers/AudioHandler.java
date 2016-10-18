package tirnak.persistence.handlers;

import org.hibernate.SessionFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.AbstractHandler;
import tirnak.persistence.common.Handler;
import tirnak.persistence.model.Audio;
import tirnak.persistence.model.Post;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class AudioHandler extends AbstractHandler implements Handler {

    public AudioHandler(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean checkDom(WebElement el) {
        return wrapString(el.getAttribute("class")).contains(AUDIO_ROW_CLASS);
    }

    private static final String AUDIO_ROW_CLASS = "audio_row";
    private static final String AUDIO_PERFORMER_CLASS = "audio_performer";
    private static final String AUDIO_TITLE_CLASS = "audio_title";

    @Override
    public Post parse(WebElement el, Post currentPost) {
        Audio audio = new Audio();
        audio.setId(el.getAttribute("id").replace("audio_", ""));
        audio.setPerformer(el.findElement(By.className(AUDIO_PERFORMER_CLASS)).getText());
        audio.setTrack(el.findElement(By.className(AUDIO_TITLE_CLASS)).getText());
        currentPost.addAudio(audio);
        return currentPost;
    }
}
