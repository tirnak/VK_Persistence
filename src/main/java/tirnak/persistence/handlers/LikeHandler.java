package tirnak.persistence.handlers;

import org.hibernate.SessionFactory;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.AbstractHandler;
import tirnak.persistence.common.Handler;
import tirnak.persistence.model.Like;
import tirnak.persistence.model.Person;
import tirnak.persistence.model.Post;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class LikeHandler extends AbstractHandler implements Handler {

    public LikeHandler(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean checkDom(WebElement el) {
        return wrapString(el.getAttribute("class")).equalsAsString(LIKE_CLASS);
    }

    private static final String LIKE_CLASS = "like_tt_owner";

    @Override
    public Post parse(WebElement el, Post currentPost) {
        Person person = new Person();
        Like like = new Like();
        person.setHref(el.getAttribute("href"));
        person.setFullName(el.getAttribute("title"));
        person.addLike(like); like.setOwner(person);
        currentPost.addLike(like); like.setPost(currentPost);
        return currentPost;
    }
}
