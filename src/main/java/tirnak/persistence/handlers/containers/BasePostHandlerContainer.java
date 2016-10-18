package tirnak.persistence.handlers.containers;

import org.hibernate.SessionFactory;
import tirnak.persistence.common.HandlerContainer;
import tirnak.persistence.common.Handler;
import tirnak.persistence.handlers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BasePostHandlerContainer extends HandlerContainer {

    public BasePostHandlerContainer(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected List<Handler> getHandlers(SessionFactory sessionFactory) {
        return Arrays.asList(
            new RepostHandler(sessionFactory),
            new CommentHandler(sessionFactory),
            new AudioHandler(sessionFactory),
            new AuthorHandler(sessionFactory),
            new ImageHandler(sessionFactory),
            new LinkHandler(sessionFactory),
            new PostIdHandler(sessionFactory),
            new StringDateHandler(sessionFactory),
            new TextHandler(sessionFactory)
        );
    }
}
