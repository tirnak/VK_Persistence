package tirnak.persistence.handlers;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.HandlerProducer;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class RepostHandlerProducer implements HandlerProducer {
    private static RepostHandlerProducer instance = new RepostHandlerProducer();
    public static RepostHandlerProducer getInstance() {
        return instance;
    }
    private RepostHandlerProducer() {}

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
