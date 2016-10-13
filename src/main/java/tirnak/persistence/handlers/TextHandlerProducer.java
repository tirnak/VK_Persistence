package tirnak.persistence.handlers;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.HandlerProducer;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class TextHandlerProducer implements HandlerProducer {

    private static TextHandlerProducer instance = new TextHandlerProducer();
    public static TextHandlerProducer getInstance() {
        return instance;
    }
    private TextHandlerProducer() {}

    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> wrapString(el.getAttribute("class")).containsOneOf(TEXT_CLASS);
    }

    private static final String[] TEXT_CLASS = {"wall_post_text", "wall_reply_text"};

    @Override
    public BiFunction<WebElement, Post, Post> getFunctionForParsing() {
        return (el, post) -> {
            post.setText(el.getText());
            return post;
        };
    }
}
