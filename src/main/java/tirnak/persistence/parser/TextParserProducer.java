package tirnak.persistence.parser;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.ParserProducer;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class TextParserProducer implements ParserProducer {

    private static TextParserProducer instance = new TextParserProducer();
    public static TextParserProducer getInstance() {
        return instance;
    }
    private TextParserProducer() {}

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
