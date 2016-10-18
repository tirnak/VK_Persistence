package tirnak.persistence.handlers;

import org.hibernate.SessionFactory;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.AbstractHandler;
import tirnak.persistence.common.Handler;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class TextHandler extends AbstractHandler implements Handler {

    public TextHandler(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    private static final String[] TEXT_CLASS = {"wall_post_text", "wall_reply_text"};

    public boolean checkDom(WebElement el) {
        return wrapString(el.getAttribute("class")).containsOneOf(TEXT_CLASS);
    }

    @Override
    public Post parse(WebElement el, Post currentPost) {
        currentPost.setText(el.getText());
        return currentPost;
    }
}
