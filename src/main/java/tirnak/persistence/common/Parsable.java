package tirnak.persistence.common;

import org.openqa.selenium.WebElement;
import tirnak.persistence.model.Post;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public interface Parsable {
    Predicate<WebElement> getPredicateIfAppropriateDom();
    BiConsumer<WebElement, Post> getFunctionForParsing();
}
