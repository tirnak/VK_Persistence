package tirnak.persistence.handlers;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.HandlerProducer;
import tirnak.persistence.model.Person;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class LikeHandlerProducer implements HandlerProducer {

    private static LikeHandlerProducer instance = new LikeHandlerProducer();
    public static LikeHandlerProducer getInstance() {
        return instance;
    }
    private LikeHandlerProducer() {}

    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> wrapString(el.getAttribute("class")).contains(LIKE_CLASS);
    }

    private static final String LIKE_CLASS = "like_tt_owner";

    @Override
    public BiFunction<WebElement, Post, Post> getFunctionForParsing() {
        return (el, post) -> {
            Person person = new Person();
            person.setHref(el.getAttribute("href"));
            person.setFullName(el.getAttribute("title"));
            person.addLike(post);
            post.addLikedBy(person);
            return post;
        };
    }
}
