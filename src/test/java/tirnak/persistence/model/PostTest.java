package tirnak.persistence.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PostTest {
    private SessionFactory factory;

    {
        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.addAnnotatedClass(Post.class);
        configuration.addAnnotatedClass(Picture.class);
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(Video.class);
        ServiceRegistryBuilder srBuilder = new ServiceRegistryBuilder();
        srBuilder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = srBuilder.buildServiceRegistry();
        factory = configuration.buildSessionFactory(serviceRegistry);
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testPicture() throws Exception {

        Session session = factory.openSession();
        session.beginTransaction();
        Picture picture = new Picture();
        picture.setId(1);
        picture.setPost(new Post());
        session.save(picture);
        session.getTransaction().commit();
        session.close();

        Picture picture1;
        session = factory.openSession();
        session.beginTransaction();
        List result = session.createQuery( "from picture" ).list();
        for ( Picture pic : (List<Picture>) result ) {
            System.out.println(pic.getId());
            System.out.println(pic.getPost());
        }
        session.getTransaction().commit();



    }
}