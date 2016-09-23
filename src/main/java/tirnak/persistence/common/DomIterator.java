package tirnak.persistence.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tirnak.persistence.model.Post;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class DomIterator {
    private static String GET_CHILDRER_XPATH = "./*";
    Map<Predicate<WebElement>, BiConsumer<WebElement, Post>> checkingFunctions;

    public DomIterator(Map<Predicate<WebElement>, BiConsumer<WebElement, Post>> checkingFunctions) {
        this.checkingFunctions = checkingFunctions;
    }

    public void visit(WebElement el, Post post) {
        for (Predicate<WebElement> predicate : checkingFunctions.keySet()) {
            if (predicate.test(el)) {
                checkingFunctions.get(predicate).accept(el, post);
            }
        }
        for (WebElement child : getChildren(el)) {
            visit(child, post);
        }
    }

    private List<WebElement> getChildren(WebElement el) {
        return el.findElements(By.xpath(GET_CHILDRER_XPATH));
    }
}
