package tirnak.persistence.handlers;

import org.hibernate.SessionFactory;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.AbstractHandler;
import tirnak.persistence.common.Handler;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class RepostHandler extends AbstractHandler implements Handler {

    public RepostHandler(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean checkDom(WebElement el) {
        return wrapString(el.getAttribute("class")).contains(REPOST_CLASS);
    }

    private static final String REPOST_CLASS = "copy_quote";

    @Override
    public Post parse(WebElement el, Post currentPost) {
        Post repost = new Post();
        currentPost.setRepostOf(repost);
        return repost;
    }
}
