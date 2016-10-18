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

public class LikeHandler extends AbstractHandler implements Handler {

    public LikeHandler(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean checkDom(WebElement el) {
        return wrapString(el.getAttribute("class")).contains(LIKE_CLASS);
    }

    private static final String LIKE_CLASS = "like_tt_owner";

    @Override
    public Post parse(WebElement el, Post currentPost) {
        Person person = new Person();
        person.setHref(el.getAttribute("href"));
        person.setFullName(el.getAttribute("title"));
        person.addLike(currentPost);
        currentPost.addLikedBy(person);
        return currentPost;
    }
}
