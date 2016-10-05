package tirnak.persistence.parser;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.ParserProducer;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class CommentParserProducer implements ParserProducer {
    private static CommentParserProducer instance = new CommentParserProducer();
    public static CommentParserProducer getInstance() {
        return instance;
    }
    private CommentParserProducer() {}

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
