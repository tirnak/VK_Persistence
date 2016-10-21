import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import tirnak.persistence.VkAuthorizer;
import tirnak.persistence.VkOAuthorizer;
import tirnak.persistence.model.Like;
import tirnak.persistence.model.Post;
import tirnak.persistence.wall.PostDivWrapper;
import tirnak.persistence.wall.WallExtractor;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Runner {

    static VkOAuthorizer oAuthorizer;

    public static void main(String[] args) throws InterruptedException, IOException {

        Properties properties = new Properties();
        URL filePath = Runner.class.getResource("credentials.properties");
        oAuthorizer = new VkOAuthorizer(properties, filePath);

        System.setProperty("webdriver.gecko.driver", properties.getProperty("path_to_driver"));
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", true);
        WebDriver driver = new FirefoxDriver(capabilities);

        VkAuthorizer authorizer = new VkAuthorizer(driver);
        authorizer.authorize();

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        WallExtractor wallExtractor = new WallExtractor(driver, sessionFactory);
        wallExtractor.goToWall();

        PostDivWrapper[] postDivWrappers = PostDivWrapper.getPostDivs(driver);
        List<CompletableFuture<Void>> futurePosts = new LinkedList<>();
        for (PostDivWrapper postDivWrapper : postDivWrappers) {
            Post post = wallExtractor.parsePost(postDivWrapper);
            post.persistRecursive(sessionFactory);
        }

        List<Post> posts = getPosts(sessionFactory);


        Thread.sleep(1000);
        driver.close();

    }

    private static List<Post> getPosts(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Post> result = session.createQuery("from Post ").list();
        Like[] likes = result.stream().flatMap(l -> l.getLikes().stream()).toArray(Like[]::new);
        session.close();
        return result;
    }

}
