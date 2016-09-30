package tirnak.persistence.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tirnak.persistence.model.Post;
import tirnak.persistence.parser.ParserContainer;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class DomIterator {
    private static String GET_CHILDRER_XPATH = "./*";
    private ParserContainer parserContainer;

    public DomIterator(ParserContainer parserContainer) {
        this.parserContainer = parserContainer;
    }

    public void visit(WebElement el, Post post) {
        Optional<BiFunction<WebElement, Post, Post>> parsingFunction = parserContainer.getFunctionForWebElement(el);
        if (parsingFunction.isPresent()) {
            post = parsingFunction.get().apply(el, post);
        }
        List<WebElement> children = getChildren(el);
        Collections.reverse(children);
        for (WebElement child : children) {
            visit(child, post);
        }
    }

    private List<WebElement> getChildren(WebElement el) {
        return el.findElements(By.xpath(GET_CHILDRER_XPATH));
    }
}
