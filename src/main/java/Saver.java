import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import tirnak.persistence.VkAuthorizer;
import tirnak.persistence.VkOAuthorizer;
import tirnak.persistence.model.Like;
import tirnak.persistence.model.Post;
import tirnak.persistence.wall.PostDivWrapper;
import tirnak.persistence.wall.WallExtractor;
import tirnak.persistence.wall.filter.TextPostFilter;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class Saver {

    static VkOAuthorizer oAuthorizer;

    public static void main(String[] args) throws InterruptedException, IOException {

        Properties properties = new Properties();
        URL filePath = Saver.class.getResource("credentials.properties");
        oAuthorizer = new VkOAuthorizer(properties, filePath);

        System.setProperty("webdriver.chrome.driver", properties.getProperty("path_to_driver"));
        WebDriver driver = new ChromeDriver();

        VkAuthorizer authorizer = new VkAuthorizer(driver);
        authorizer.authorize();

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        WallExtractor wallExtractor = new WallExtractor(driver, sessionFactory);
        wallExtractor.goToWall();
        wallExtractor.scrollDownNTimes(20);
//        System.out.println("scrolled down 20 times");
//        new TextPostFilter().filter((JavascriptExecutor) driver);
//        System.out.println("filtered +20 scrolls");
//        wallExtractor.scrollDownNTimes(20);
//        System.out.println("scrolled down 40 times");
//        new TextPostFilter().filter((JavascriptExecutor) driver);
//        System.out.println("filtered +40 scrolls");
//        wallExtractor.scrollDownNTimes(20);
        System.out.println("scrolled down 20 times");
        new TextPostFilter().filter((JavascriptExecutor) driver);
        System.out.println("filtered +20 scrolls");

//        wallExtractor.scrollToEnd();
//        System.out.println("scrolled to end");
//
//        new TextPostFilter().filter((JavascriptExecutor) driver);
//        System.out.println("filtered");

        Collection<PostDivWrapper> postDivWrappers = PostDivWrapper.getPostDivs(driver);
        System.out.println("start parsing");
        List<CompletableFuture<Void>> futurePosts = new LinkedList<>();
        for (PostDivWrapper postDivWrapper : postDivWrappers) {
            Post post = wallExtractor.parsePost(postDivWrapper);
            post.persistRecursive(sessionFactory);
        }

//        List<Post> posts = getPosts(sessionFactory);


//        Thread.sleep(1000);
        driver.close();

    }

    private static List<Post> getPosts(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        List<Post> result = session.createQuery("from Post ").list();
        Like[] likes = result.stream().flatMap(l -> l.getLikes().stream()).toArray(Like[]::new);
        session.close();
        return result;
    }

}
