package tirnak.persistence.model;

import org.openqa.selenium.WebElement;
import tirnak.persistence.common.Parsable;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class Text implements Parsable {
    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> el.getAttribute("class").contains("wall_post_text");
    }

    @Override
    public BiConsumer<WebElement, Post> getFunctionForParsing() {
        return (el, post) -> {
            post.setText(el.getText());
        };
    }
}
