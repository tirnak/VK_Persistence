package tirnak.persistence.common;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.HandlerProducer;
import tirnak.persistence.model.Post;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

abstract public class HandlerContainer {
    Map<Predicate<WebElement>, BiFunction<WebElement, Post, Post>> checkingFunctions;

    protected HandlerContainer(List<HandlerProducer> producers) {
        for (HandlerProducer producer : producers) {
            checkingFunctions.put(producer.getPredicateIfAppropriateDom(), producer.getFunctionForParsing());
        }
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
