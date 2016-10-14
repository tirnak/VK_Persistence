package tirnak.persistence.handlers.containers;

import tirnak.persistence.common.HandlerContainer;
import tirnak.persistence.common.HandlerProducer;
import tirnak.persistence.handlers.*;

import java.util.Arrays;
import java.util.List;

public class LikeHandlerContainer extends HandlerContainer {
    private static List<HandlerProducer> handlers = Arrays.asList(
        LikeHandlerProducer.getInstance()
    );

    public LikeHandlerContainer() {
        super(handlers);
    }
}
