package tirnak.persistence.handlers;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.HandlerProducer;
import tirnak.persistence.model.Person;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class AuthorHandlerProducer implements HandlerProducer {

    private static AuthorHandlerProducer instance = new AuthorHandlerProducer();
    public static AuthorHandlerProducer getInstance() {
        return instance;
    }
    private AuthorHandlerProducer() {}

    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> {
            return wrapString(el.getAttribute("class")).equalsOneOf(AUTHOR_CLASSES);
        };
    }
    private static final String[] AUTHOR_CLASSES = {"author","copy_author"};

    @Override
    public BiFunction<WebElement, Post, Post> getFunctionForParsing() {
        return (el, post) -> {
            Person author = new Person();
            author.setHref(el.getAttribute("href"));
            author.setFullName(el.getText());
            post.setAuthor(author);
            return post;
        };
    }
}
