package tirnak.persistence.handlers.containers;

import org.hibernate.SessionFactory;
import tirnak.persistence.common.HandlerContainer;
import tirnak.persistence.common.Handler;
import tirnak.persistence.handlers.RepostedHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RepostHandlerContainer extends HandlerContainer {

    public RepostHandlerContainer(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected List<Handler> getHandlers(SessionFactory sessionFactory) {
        return Collections.singletonList(new RepostedHandler(sessionFactory));
    }


}
