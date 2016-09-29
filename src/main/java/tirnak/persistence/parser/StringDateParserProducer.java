package tirnak.persistence.parser;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.ParserProducer;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.NullObjects.wrapString;

public class StringDateParserProducer implements ParserProducer {

    private static StringDateParserProducer instance = new StringDateParserProducer();
    public static StringDateParserProducer getInstance() {
        return instance;
    }
    private StringDateParserProducer() {}

    private final static String STRING_DATE_CLASS = "post_date";
    private final static String STRING_TIME_CLASS = "rel_date";

    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> {
            String cssClass = wrapString(el.getAttribute("class"));
            return cssClass.contains(STRING_DATE_CLASS) ||
                    cssClass.contains(STRING_TIME_CLASS);
        };
    }

    @Override
    public BiFunction<WebElement, Post, Post> getFunctionForParsing() {
        return (el, post) -> {
            post.setDate("adsfgasdf");
            return post;
        };
    }
}
