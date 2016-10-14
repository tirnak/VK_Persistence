package tirnak.persistence.handlers.containers;

import tirnak.persistence.common.HandlerContainer;
import tirnak.persistence.common.HandlerProducer;
import tirnak.persistence.handlers.*;

import java.util.Arrays;
import java.util.List;

public class BasePostHandlerContainer extends HandlerContainer {
    private static List<HandlerProducer> handlers = Arrays.asList(
            RepostHandlerProducer.getInstance(),
            CommentHandlerProducer.getInstance(),
            AudioHandlerProducer.getInstance(),
            AuthorHandlerProducer.getInstance(),
            ImageHandlerProducer.getInstance(),
            LinkHandlerProducer.getInstance(),
            PostIdHandlerProducer.getInstance(),
            StringDateHandlerProducer.getInstance(),
            TextHandlerProducer.getInstance()
    );

    public BasePostHandlerContainer() {
        super(handlers);
    }
}
