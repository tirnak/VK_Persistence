package tirnak.persistence.handlers;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.HandlerProducer;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class PostIdHandlerProducer implements HandlerProducer {

    private static PostIdHandlerProducer instance = new PostIdHandlerProducer();
    public static PostIdHandlerProducer getInstance() {
        return instance;
    }
    private PostIdHandlerProducer() {}

    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> wrapString(el.getAttribute("id")).matches(POST_ID_REGEX);
    }

    private static final String POST_ID_REGEX = "post\\d+_\\d+";

    @Override
    public BiFunction<WebElement, Post, Post> getFunctionForParsing() {
        return (el, post) -> {
            String id = el.getAttribute("id").replace("post","");
            post.setId(id);
            return post;
        };
    }
}
