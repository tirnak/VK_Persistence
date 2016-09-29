package tirnak.persistence.parser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.ParserProducer;
import tirnak.persistence.model.Audio;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.NullObjects.wrapString;

public class AudioParserProducer implements ParserProducer {

    private static AudioParserProducer instance = new AudioParserProducer();
    public static AudioParserProducer getInstance() {
        return instance;
    }
    private AudioParserProducer() {}

    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> wrapString(el.getAttribute("class")).contains(AUDIO_ROW_CLASS);
    }

    private static final String AUDIO_ROW_CLASS = "audio_row";
    private static final String AUDIO_PERFORMER_CLASS = "audio_performer";
    private static final String AUDIO_TITLE_CLASS = "audio_title";

    @Override
    public BiFunction<WebElement, Post, Post> getFunctionForParsing() {
        return (el, post) -> {
            Audio audio = new Audio();
            audio.setId(el.getAttribute("id").replace("audio_", ""));
            audio.setPerformer(el.findElement(By.className(AUDIO_PERFORMER_CLASS)).getText());
            audio.setTrack(el.findElement(By.className(AUDIO_TITLE_CLASS)).getText());
            post.addAudio(audio);
            return post;
        };
    }
}
