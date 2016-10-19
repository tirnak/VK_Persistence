package tirnak.persistence.common;

import org.hibernate.SessionFactory;

abstract public class AbstractHandler implements Handler {

    protected AbstractHandler(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected SessionFactory sessionFactory;
}
