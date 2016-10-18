package tirnak.persistence.handlers;

import org.hibernate.SessionFactory;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.AbstractHandler;
import tirnak.persistence.common.Handler;
import tirnak.persistence.model.Person;
import tirnak.persistence.model.Post;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class AuthorHandler extends AbstractHandler implements Handler {

    public AuthorHandler(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean checkDom(WebElement el) {
        return wrapString(el.getAttribute("class")).equalsOneOf(AUTHOR_CLASSES);
    }
    private static final String[] AUTHOR_CLASSES = {"author","copy_author"};

    @Override
    public Post parse(WebElement el, Post currentPost) {
        Person author = new Person();
        author.setHref(el.getAttribute("href"));
        author.setFullName(el.getText());
        currentPost.setAuthor(author);
        return currentPost;
    }
}
