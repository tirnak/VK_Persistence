package tirnak.persistence.handlers;

import org.hibernate.SessionFactory;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.AbstractHandler;
import tirnak.persistence.common.Handler;
import tirnak.persistence.model.Post;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class PostIdHandler extends AbstractHandler implements Handler {

    public PostIdHandler(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean checkDom(WebElement el) {
        return  wrapString(el.getAttribute("id")).matches(POST_ID_REGEX);
    }

    private static final String POST_ID_REGEX = "post\\d+_\\d+";

    @Override
    public Post parse(WebElement el, Post currentPost) {
        String id = el.getAttribute("id").replace("post","");
        currentPost.setId(id);
        return currentPost;
    }
}
