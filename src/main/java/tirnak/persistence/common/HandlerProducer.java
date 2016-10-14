package tirnak.persistence.common;

import org.openqa.selenium.WebElement;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface HandlerProducer {
    Predicate<WebElement> getPredicateIfAppropriateDom();
    BiFunction<WebElement, Post, Post> getFunctionForParsing();
}
