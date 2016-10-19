package tirnak.persistence.handlers;

import org.hibernate.SessionFactory;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.AbstractHandler;
import tirnak.persistence.common.Handler;
import tirnak.persistence.model.Like;
import tirnak.persistence.model.Post;

import java.util.List;
import java.util.Optional;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class RepostedHandler extends AbstractHandler implements Handler {

    public RepostedHandler(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean checkDom(WebElement el) {
        return wrapString(el.getAttribute("class")).contains(LIKE_CLASS);
    }

    private static final String LIKE_CLASS = "like_tt_owner";

    @Override
    public Post parse(WebElement el, Post currentPost) {
        String person_href = el.getAttribute("href");
        List<Like> likes = currentPost.getLikes();
        Optional<Like> maybeLike = likes.stream().filter(
            l -> l.getOwner().getHref().equals(person_href)
        ).findFirst();

        if (!maybeLike.isPresent()) {
            throw new RuntimeException("for post " + currentPost + " there is a reposted by " + person_href + ", but no like is found");
        }
        maybeLike.get().setIsReposted(true);
        return currentPost;
    }
}
