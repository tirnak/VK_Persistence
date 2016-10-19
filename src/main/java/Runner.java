import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.social.vkontakte.api.impl.VKontakteTemplate;
import tirnak.persistence.VkAuthorizer;
import tirnak.persistence.VkOAuthorizer;
import tirnak.persistence.model.Post;
import tirnak.persistence.wall.PostDivWrapper;
import tirnak.persistence.wall.WallExtractor;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Runner {

    static VkOAuthorizer oAuthorizer;
    static VKontakteTemplate vKontakteTemplate;

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

        if (oAuthorizer.getAccessToken().isEmpty()) {
            getToken(driver);
            oAuthorizer.persist();
        }

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        vKontakteTemplate = new VKontakteTemplate(oAuthorizer.getAccessToken(), oAuthorizer.getClientSecret());
        long userId = vKontakteTemplate.usersOperations().getUser().getId();
        WallExtractor wallExtractor = new WallExtractor(driver, userId, sessionFactory);
        wallExtractor.goToWall();

//        wallExtractor.scrollToEnd();
//        new TextPostFilter().filter((JavascriptExecutor) driver);
//        VkImageSaver imageSaver = new VkImageSaver(driver, ".", userId);
//        List<Post> posts = wallExtractor.getPostDivs().stream()
//                .map(wallExtractor::parsePost).collect(Collectors.toList());
        List<Post> posts = Collections.singletonList(
                wallExtractor.parsePost(PostDivWrapper.getPostDivs(driver)[0]));

        posts.forEach(p -> p.persistRecursive(sessionFactory));

        posts = getPosts(sessionFactory);


        Thread.sleep(1000);
//            makeFriendsRequest();
        driver.close();

    }

    private static List<Post> getPosts(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Post> result = session.createQuery("from Post ").list();
        session.close();
        return result;
    }

    public static void getToken(WebDriver driver) throws IOException {

        String queryToGetCode = oAuthorizer.getQueryForCode();
        try {
            driver.navigate().to(queryToGetCode);
        } catch (WebDriverException e) {
            System.out.println("page is a lie. Exception is quite normal");
        }
        String location = driver.getCurrentUrl();
        oAuthorizer.parseCode(location);

        String queryToGetToken = oAuthorizer.getQueryForToken();
        driver.navigate().to(queryToGetToken);
        String tokenResponse = driver.findElement(By.tagName("pre")).getText();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(tokenResponse);
        oAuthorizer.setAccessToken(actualObj.get("access_token").textValue());
    }




}
