package tirnak.persistence.parser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.ParserProducer;
import tirnak.persistence.model.Person;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.NullObjects.wrapString;

public class LikeParserProducer implements ParserProducer {

    private static LikeParserProducer instance = new LikeParserProducer();
    public static LikeParserProducer getInstance() {
        return instance;
    }
    private LikeParserProducer() {}

    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> wrapString(el.getAttribute("class")).contains(LIKE_CLASS);
    }

    private static final String LIKE_CLASS = "like_tt_owner";

    @Override
    public BiFunction<WebElement, Post, Post> getFunctionForParsing() {
        return (el, post) -> {
            el.findElements(By.className(LIKE_CLASS)).stream().forEach(subEl -> {
                Person person = new Person();
                person.setHref(subEl.getAttribute("href"));
                person.setFullName(subEl.getAttribute("title"));
                person.addLike(post);
                post.addLikedBy(person);
            });
            return post;
        };
    }
}
