package tirnak.persistence.handlers.containers;

import tirnak.persistence.common.HandlerContainer;
import tirnak.persistence.common.HandlerProducer;
import tirnak.persistence.handlers.LikeHandlerProducer;
import tirnak.persistence.handlers.RepostedHandlerProducer;

import java.util.Arrays;
import java.util.List;

public class RepostHandlerContainer extends HandlerContainer {
    private static List<HandlerProducer> handlers = Arrays.asList(
        RepostedHandlerProducer.getInstance()
    );

    public RepostHandlerContainer() {
        super(handlers);
    }
}
