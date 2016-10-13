package tirnak.persistence.handlers;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.HandlerProducer;
import tirnak.persistence.model.Picture;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class ImageHandlerProducer implements HandlerProducer {

    private static ImageHandlerProducer instance = new ImageHandlerProducer();
    public static ImageHandlerProducer getInstance() {
        return instance;
    }
    private ImageHandlerProducer() {}

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
