package tirnak.persistence.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PostTest {
    private SessionFactory sessionFactory;

    {
    }

    @Before
    public void setUp() throws Exception {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Test
    public void testPicture() throws Exception {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Picture picture = new Picture();
        picture.setId(1);
        Post post = new Post();
        post.setId(1);
        picture.setPost(post);
        session.save(post);
        session.save(picture);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Picture").list();
        Picture picture1 = (Picture) result.get(0);
        assertTrue(picture1.getId() == 1);
        assertTrue(picture1.getPost().getId() == 1);
    }
}