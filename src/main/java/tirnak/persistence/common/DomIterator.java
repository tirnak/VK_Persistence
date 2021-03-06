package tirnak.persistence.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tirnak.persistence.model.Post;

import java.util.List;
import java.util.Optional;

/**
 * Iterates DomElement and its child elements with handler, contained in container
 */
public class DomIterator {
    private static String GET_CHILDRER_XPATH = "./*";
    private HandlerContainer handlerContainer;

    public DomIterator(HandlerContainer handlerContainer) {
        this.handlerContainer = handlerContainer;
    }

    public void visit(WebElement el, Post post) {
        Optional<Handler> parsingFunction = handlerContainer.getParserForWebElement(el);
        if (parsingFunction.isPresent()) {
            post = parsingFunction.get().parse(el, post);
        }
        List<WebElement> children = getChildren(el);
        for (WebElement child : children) {
            visit(child, post);
        }
    }

    /**
     * Gets children but not descendants
     */
    private List<WebElement> getChildren(WebElement el) {
        return el.findElements(By.xpath(GET_CHILDRER_XPATH));
    }
}
