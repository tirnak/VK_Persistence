package tirnak.persistence.handlers;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.HandlerProducer;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class CommentHandlerProducer implements HandlerProducer {
    private static CommentHandlerProducer instance = new CommentHandlerProducer();
    public static CommentHandlerProducer getInstance() {
        return instance;
    }
    private CommentHandlerProducer() {}

    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> wrapString(el.getAttribute("class")).contains(COMMENT_CLASS);
    }

    private static final String COMMENT_CLASS = "reply";

    @Override
    public BiFunction<WebElement, Post, Post> getFunctionForParsing() {
        return (el, post) -> {
            Post comment = new Post();
            comment.setParent(post);
            post.addComment(comment);
            return comment;
        };
    }
}
