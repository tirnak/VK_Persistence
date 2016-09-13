package tirnak.persistence.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PostTest {
    private SessionFactory sessionFactory;

    {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            System.out.println(sessionFactory);
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    @Before
    public void setUp() throws Exception {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }

    }

    @Test
    public void testPicture() throws Exception {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Picture picture = new Picture();
        picture.setId(1);
        picture.setPost(new Post());
        session.save(picture);
        session.getTransaction().commit();
        session.close();

        Picture picture1;
        session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery( "from picture" ).list();
        for ( Picture pic : (List<Picture>) result ) {
            System.out.println(pic.getId());
            System.out.println(pic.getPost());
        }
        session.getTransaction().commit();



    }
}