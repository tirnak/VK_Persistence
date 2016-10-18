package tirnak.persistence.handlers;

import org.hibernate.SessionFactory;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.AbstractHandler;
import tirnak.persistence.common.Handler;
import tirnak.persistence.model.Picture;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class ImageHandler extends AbstractHandler implements Handler {
    public ImageHandler(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    private static final String WALL_IMAGE_CLASS = "image_cover";
    @Override
    public boolean checkDom(WebElement el) {
        return wrapString(el.getAttribute("class")).contains(WALL_IMAGE_CLASS);
    }

    private static final String IMAGE_BACKGROUND_CSS = "background-image";

    @Override
    public Post parse(WebElement el, Post currentPost) {
        Picture image = new Picture();
        String href = el.getCssValue(IMAGE_BACKGROUND_CSS).split("\"")[1];
        String id = el.getAttribute("onclick").split("'")[1];
        image.setId(id);
        image.setHref(href);
        image.setPost(currentPost);
        currentPost.addImage(image);
        return currentPost;
    }
}
