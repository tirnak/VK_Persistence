package tirnak.persistence.common;

import org.hibernate.SessionFactory;

/**
 * Handler is a type of classes that:
 * 1) check if they can parse some DomElement
 * 2) parse that element and modify currently processed post with content of element parsed
 * 3) return current post or create new, if DomElement's tree relates to another post (repost or comment)
 */
abstract public class AbstractHandler implements Handler {

    protected AbstractHandler(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected SessionFactory sessionFactory;
}
