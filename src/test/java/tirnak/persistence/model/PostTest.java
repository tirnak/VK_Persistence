package tirnak.persistence.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;

public class PostTest {
    private SessionFactory sessionFactory;

    @Before
    public void setUp() throws Exception {
        sessionFactory = new Configuration().configure()
                .setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver")
                .setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:mymemdb")
                .setProperty("hibernate.connection.username", "sa")
                .setProperty("hibernate.connection.password", "")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect")
                .setProperty("hbm2ddl.auto", "update").setProperty("show_sql", "true")
                .addAnnotatedClass(Person.class).addAnnotatedClass(Picture.class).addAnnotatedClass(Post.class).addAnnotatedClass(Video.class)
                .addAnnotatedClass(Audio.class).addAnnotatedClass(Like.class).addAnnotatedClass(Link.class)
                .buildSessionFactory();
    }

    @Test
    public void testPicture() throws Exception {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Picture picture = new Picture(); picture.setId("test_id");
        Person person = new Person(); person.setHref("test_id");
        Post post = new Post(); post.setId("test_id"); post.addImage(picture);
        picture.setPost(post); post.setAuthor(person); session.save(person); session.save(picture); session.save(post);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Picture").list();
        Picture picture1 = (Picture) result.get(0);
        assertTrue(picture1.getId().equals("test_id"));
        assertTrue(picture1.getPost().getId().equals("test_id"));
    }

    @Test
    public void testLike() throws Exception {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Person person = new Person(); person.setHref("test_id2"); session.save(person);
        Post post = new Post(); post.setId("test_id2"); session.save(post);
        Like like = new Like();
        person.addLike(like); post.addLike(like);
        like.setOwner(person); like.setPost(post);session.save(like);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Post p where p.id = 'test_id2'").list();
        Post post1 = (Post) result.get(0);
        assertTrue(post1.getId().equals("test_id2"));
        Person[] likedBy = post1.getLikes().stream().map(Like::getOwner).toArray(Person[]::new);
        for (Person person1 : likedBy) {
            assertTrue(person1.getHref().equals("test_id2"));
        }
        session.close();
    }

    @Test
    public void testPostHierarchy() throws Exception {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Post> posts = new ArrayList<Post>();
        for (int i = 0; i < 5; i++) {
            posts.add(new Post());
            posts.get(i).setId(i + "");
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
        Post post = session.get(Post.class, "4");
        _testHierarchy(getParent, post);
        // test repost
        post = session.get(Post.class, "4");
        _testHierarchy(getRepostOf, post);
        session.close();
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