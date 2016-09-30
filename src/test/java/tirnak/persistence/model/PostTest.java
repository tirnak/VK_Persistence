package tirnak.persistence.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
        picture.setId("1");
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
        assertTrue(picture1.getId().equals("1"));
        assertTrue(picture1.getPost().getId() == 1);
    }

    @Test
    public void testLike() throws Exception {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Post post = new Post(); post.setId(10); session.save(post);
        Person person = new Person(); person.setHref("11"); session.save(person);
        post.addLikedBy(person); //person.addLike(post);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Post ").list();
        Post post1 = (Post) result.get(0);
        assertTrue(post1.getId() == 10);
        for (Person person1 : post1.getLikedBy()) {
            assertTrue(person1.getHref().equals("11"));
        }
    }

    @Test
    public void testPostHierarchy() throws Exception {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Post> posts = new ArrayList<Post>();
        for (int i = 0; i < 5; i++) {
            posts.add(new Post());
            posts.get(i).setId(i);
            if (i > 0) {
                posts.get(i).setParent(posts.get(i - 1));
            }
            if (i > 0) {
                posts.get(i).setRepostOf(posts.get(i - 1));
            }
            session.save(posts.get(i));
        }

        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        session.beginTransaction();
        Function<Post, Post> getParent = p -> p.getParent();
        Function<Post, Post> getRepostOf = p -> p.getRepostOf();
        Post post = session.get(Post.class, 4);
        _testHierarchy(getParent, post);
        // test repost
        post = session.get(Post.class, 4);
        _testHierarchy(getRepostOf, post);
    }

    private void _testHierarchy(Function<Post,Post> f, Post initial) {
        assertNotNull(f.apply(initial));
        for (int i = 0; i < 4; i++) {
            assertNotNull(f.apply(initial));
            initial = f.apply(initial); // 3, 2, 1, 0
        }
        assertNull(f.apply(initial));
    }
}