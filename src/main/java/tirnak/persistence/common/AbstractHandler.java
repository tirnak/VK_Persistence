package tirnak.persistence.common;

import org.hibernate.SessionFactory;

import java.lang.invoke.MethodHandles;

abstract public class AbstractHandler implements Handler {

    protected AbstractHandler(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected SessionFactory sessionFactory;

    public static Class getInstance(SessionFactory sessionFactory) {
        try {
            return MethodHandles.lookup().lookupClass().getClass().getConstructor(SessionFactory.class).newInstance(sessionFactory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
