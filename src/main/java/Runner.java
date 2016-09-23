import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.social.vkontakte.api.VKontakteProfile;
import org.springframework.social.vkontakte.api.impl.VKontakteTemplate;
import org.springframework.social.vkontakte.api.impl.json.VKArray;
import org.springframework.social.vkontakte.api.impl.wall.CommentsQuery;
import org.springframework.social.vkontakte.api.impl.wall.UserWall;
import tirnak.persistence.VkAuthorizer;
import tirnak.persistence.VkImageSaver;
import tirnak.persistence.VkOAuthorizer;
import tirnak.persistence.WallExtractor;
import tirnak.persistence.model.Post;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class Runner {

    static VkOAuthorizer oAuthorizer;
    static VKontakteTemplate vKontakteTemplate;

    public static void main(String[] args) throws InterruptedException, IOException {

        Properties properties = new Properties();
        URL filePath = Runner.class.getResource("credentials.properties");
        oAuthorizer = new VkOAuthorizer(properties, filePath);

        System.setProperty("webdriver.gecko.driver","D:\\utils\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();

        VkAuthorizer authorizer = new VkAuthorizer(driver);
        authorizer.authorize();

        if (oAuthorizer.getAccessToken().isEmpty()) {
            getToken(driver);
            oAuthorizer.persist();
        }

        vKontakteTemplate = new VKontakteTemplate(oAuthorizer.getAccessToken(), oAuthorizer.getClientSecret());
        long userId = vKontakteTemplate.usersOperations().getUser().getId();
        WallExtractor wallExtractor = new WallExtractor(driver, userId);
        wallExtractor.goToWall();
        VkImageSaver imageSaver = new VkImageSaver(driver, ".", userId);
        WebElement postDiv1 = wallExtractor.getPostDivs().get(0);
        WebElement repostDiv1 = wallExtractor.getRepostDivs(postDiv1);
        WebElement postDiv2 = wallExtractor.getPostDivs().get(1);
        Post post = wallExtractor.parsePost(postDiv1);
//        wallExtractor.parsePost();
//        imageSaver.saveImagesWithMe();
        Thread.sleep(1000);
//            makeFriendsRequest();

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

    public static void getPostsByApi() {

//        VKontakteTemplate vKontakteTemplate = new VKontakteTemplate(oAuthorizer.getAccessToken(), oAuthorizer.getClientSecret());
//        List<Post> posts = vKontakteTemplate.wallOperations().getPosts();
//        for (Post post: posts) {
//            post.getText();
//            post.getComments();
//        }
//        CommentsQuery query = new CommentsQuery.Builder(new UserWall(482616L), 6349).build();
//        vKontakteTemplate.wallOperations().getComments(query);
    }

    public static void makeFriendsRequest() {

        VKontakteTemplate vKontakteTemplate = new VKontakteTemplate(oAuthorizer.getAccessToken(), oAuthorizer.getClientSecret());
        VKArray<VKontakteProfile> array = vKontakteTemplate.friendsOperations().get();
        for (VKontakteProfile profile : array.getItems()) {
            System.out.println(profile.getBirthDate().getDay());
        }
    }

}
