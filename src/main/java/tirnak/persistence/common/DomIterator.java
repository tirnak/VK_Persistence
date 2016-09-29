package tirnak.persistence.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tirnak.persistence.model.Post;
import tirnak.persistence.parser.ParserContainer;

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
        Optional<BiFunction<WebElement, Post, Post>> parsingFunciton = parserContainer.getFunctionForWebElement(el);
        if (parsingFunciton.isPresent()) {
            post = parsingFunciton.get().apply(el, post);
        }
        for (WebElement child : getChildren(el)) {
            visit(child, post);
        }
    }

    private List<WebElement> getChildren(WebElement el) {
        return el.findElements(By.xpath(GET_CHILDRER_XPATH));
    }
}
