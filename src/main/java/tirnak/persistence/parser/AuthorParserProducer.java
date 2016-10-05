package tirnak.persistence.parser;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.ParserProducer;
import tirnak.persistence.model.Person;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class AuthorParserProducer implements ParserProducer {

    private static AuthorParserProducer instance = new AuthorParserProducer();
    public static AuthorParserProducer getInstance() {
        return instance;
    }
    private AuthorParserProducer() {}

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
