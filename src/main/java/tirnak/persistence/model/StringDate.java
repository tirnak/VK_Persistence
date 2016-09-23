package tirnak.persistence.model;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.Parsable;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class StringDate implements Parsable {
    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> el.getAttribute("class").contains("");
    }

    @Override
    public BiConsumer<WebElement, Post> getFunctionForParsing() {
        return null;
    }
}
