package tirnak.persistence.parser;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.ParserProducer;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.NullObjects.wrapString;

public class PostIdParserProducer implements ParserProducer {

    private static PostIdParserProducer instance = new PostIdParserProducer();
    public static PostIdParserProducer getInstance() {
        return instance;
    }
    private PostIdParserProducer() {}

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
