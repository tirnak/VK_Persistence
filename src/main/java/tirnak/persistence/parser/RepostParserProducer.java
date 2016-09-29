package tirnak.persistence.parser;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.ParserProducer;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.NullObjects.wrapString;

public class RepostParserProducer implements ParserProducer {
    private static RepostParserProducer instance = new RepostParserProducer();
    public static RepostParserProducer getInstance() {
        return instance;
    }
    private RepostParserProducer() {}

    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> wrapString(el.getAttribute("class")).contains(REPOST_CLASS);
    }

    private static final String REPOST_CLASS = "copy_quote";

    @Override
    public BiFunction<WebElement, Post, Post> getFunctionForParsing() {
        return (el, post) -> {
            Post repost = new Post();
            post.setRepostOf(repost);
            return repost;
        };
    }
}
