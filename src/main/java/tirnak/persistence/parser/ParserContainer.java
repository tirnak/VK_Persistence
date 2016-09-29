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
        List<ParserProducer> producers = Arrays.asList(
            AudioParserProducer.getInstance(),
            LikeParserProducer.getInstance(),
            StringDateParserProducer.getInstance(),
            TextParserProducer.getInstance(),
            RepostParserProducer.getInstance());
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
