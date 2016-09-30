package tirnak.persistence.parser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.ParserProducer;
import tirnak.persistence.model.Audio;
import tirnak.persistence.model.Picture;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.NullObjects.wrapString;

public class ImageParserProducer implements ParserProducer {

    private static ImageParserProducer instance = new ImageParserProducer();
    public static ImageParserProducer getInstance() {
        return instance;
    }
    private ImageParserProducer() {}

    private static final String WALL_IMAGE_CLASS = "image_cover";
    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> wrapString(el.getAttribute("class")).contains(WALL_IMAGE_CLASS);
    }

    private static final String IMAGE_BACKGROUND_CSS = "background-image";

    @Override
    public BiFunction<WebElement, Post, Post> getFunctionForParsing() {
        return (el, post) -> {
            Picture image = new Picture();
            String href = el.getCssValue(IMAGE_BACKGROUND_CSS).split("\"")[1];
            String id = el.getAttribute("onclick").split("'")[1];
            image.setId(id);
            image.setHref(href);
            image.setPost(post);
            post.addImage(image);
//            image.setId();
//            audio.setId(el.getAttribute("id").replace("audio_", ""));
//            audio.setPerformer(el.findElement(By.className(AUDIO_PERFORMER_CLASS)).getText());
//            audio.setTrack(el.findElement(By.className(AUDIO_TITLE_CLASS)).getText());
//            post.addAudio(audio);
            return post;
        };
    }
}
