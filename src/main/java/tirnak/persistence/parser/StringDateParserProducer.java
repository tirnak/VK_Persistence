package tirnak.persistence.parser;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.ParserProducer;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class StringDateParserProducer implements ParserProducer {

    private static StringDateParserProducer instance = new StringDateParserProducer();
    public static StringDateParserProducer getInstance() {
        return instance;
    }
    private StringDateParserProducer() {}

    private final static String[] STRING_DATE_TIME_CLASSES = {"post_date", "rel_date"};

    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> wrapString(el.getAttribute("class")).containsOneOf(STRING_DATE_TIME_CLASSES);
    }

    @Override
    public BiFunction<WebElement, Post, Post> getFunctionForParsing() {
        return (el, post) -> {
            post.setDate(el.getText());
            return post;
        };
    }
}
