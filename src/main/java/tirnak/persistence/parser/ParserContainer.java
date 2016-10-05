package tirnak.persistence.parser;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.ParserProducer;
import tirnak.persistence.model.Post;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class ParserContainer {
    Map<Predicate<WebElement>, BiFunction<WebElement, Post, Post>> checkingFunctions;

    public ParserContainer() {
        checkingFunctions = new HashMap<>();
        /**
         * Order is quite important here. First we should check if new post is to be introduced.
         * And only then - parse webElement for its attributes (like id="post\d+_\d+")
         */
        List<ParserProducer> producers = Arrays.asList(
            RepostParserProducer.getInstance(),
            CommentParserProducer.getInstance(),
            AudioParserProducer.getInstance(),
            LikeParserProducer.getInstance(),
            LinkParserProducer.getInstance(),
            StringDateParserProducer.getInstance(),
            TextParserProducer.getInstance(),
            ImageParserProducer.getInstance(),
            PostIdParserProducer.getInstance(),
            AuthorParserProducer.getInstance());
        for (ParserProducer producer : producers) {
            checkingFunctions.put(producer.getPredicateIfAppropriateDom(), producer.getFunctionForParsing());
        }
    }

    public ParserContainer(Map<Predicate<WebElement>, BiFunction<WebElement, Post, Post>> checkingFunctions) {
        this.checkingFunctions = checkingFunctions;
    }

    public Optional<BiFunction<WebElement, Post, Post>> getFunctionForWebElement(WebElement el) {
        for (Predicate<WebElement> predicate : checkingFunctions.keySet()) {
            if (predicate.test(el)) {
                return Optional.of(checkingFunctions.get(predicate));
            }
        }
        return Optional.empty();
    }
}
