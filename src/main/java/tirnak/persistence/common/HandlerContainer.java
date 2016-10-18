package tirnak.persistence.common;

import org.hibernate.SessionFactory;
import org.openqa.selenium.WebElement;
import tirnak.persistence.model.Post;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

abstract public class HandlerContainer {

    Set<Handler> handlers = new HashSet<>();
    public Optional<Handler> getParserForWebElement(WebElement el) {
        for (Handler handler : handlers) {
            if (handler.checkDom(el)) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }

    public HandlerContainer(SessionFactory sessionFactory) {
        List<Handler> handlers = getHandlers(sessionFactory);
        for (Handler handler : handlers) {
            this.handlers.add(handler);
        }
    }

    abstract protected List<Handler> getHandlers(SessionFactory sessionFactory);
}
