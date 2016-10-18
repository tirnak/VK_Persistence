package tirnak.persistence.handlers;

import org.hibernate.SessionFactory;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.AbstractHandler;
import tirnak.persistence.common.Handler;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class CommentHandler extends AbstractHandler implements Handler {

    public CommentHandler(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean checkDom(WebElement el) {
        return wrapString(el.getAttribute("class")).contains(COMMENT_CLASS);
    }
    private static final String COMMENT_CLASS = "reply";

    @Override
    public Post parse(WebElement el, Post currentPost) {
        Post comment = new Post();
        comment.setParent(currentPost);
        currentPost.addComment(comment);
        return comment;
    }
}
