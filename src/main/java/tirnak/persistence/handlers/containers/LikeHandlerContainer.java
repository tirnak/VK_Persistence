package tirnak.persistence.handlers.containers;

import org.hibernate.SessionFactory;
import tirnak.persistence.common.Handler;
import tirnak.persistence.common.HandlerContainer;
import tirnak.persistence.handlers.LikeHandler;

import java.util.Collections;
import java.util.List;

public class LikeHandlerContainer extends HandlerContainer {

    public LikeHandlerContainer(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected List<Handler> getHandlers(SessionFactory sessionFactory) {
        return Collections.singletonList(new LikeHandler(sessionFactory));
    }
}
