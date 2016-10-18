package tirnak.persistence.handlers;

import org.hibernate.SessionFactory;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.AbstractHandler;
import tirnak.persistence.common.Handler;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class StringDateHandler extends AbstractHandler implements Handler {
    public StringDateHandler(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    private final static String[] STRING_DATE_TIME_CLASSES = {"post_date", "rel_date"};

    @Override
    public boolean checkDom(WebElement el) {
        return wrapString(el.getAttribute("class")).containsOneOf(STRING_DATE_TIME_CLASSES);
    }

    @Override
    public Post parse(WebElement el, Post currentPost) {
        currentPost.setDate(el.getText());
        return currentPost;
    }
}
