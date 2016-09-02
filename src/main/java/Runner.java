import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.springframework.social.vkontakte.api.Post;
import org.springframework.social.vkontakte.api.VKontakteProfile;
import org.springframework.social.vkontakte.api.impl.VKontakteTemplate;
import org.springframework.social.vkontakte.api.impl.json.VKArray;
import org.springframework.social.vkontakte.api.impl.wall.CommentsQuery;
import org.springframework.social.vkontakte.api.impl.wall.UserWall;
import org.springframework.social.vkontakte.api.impl.wall.WallOwner;
import tirnak.persistence.VkAuthorizer;
import tirnak.persistence.VkOAuthorizer;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class Runner {

    static VkOAuthorizer oAuthorizer;
    //Start to write our test method. It should ends with "Test"
    public static void main(String[] args) throws InterruptedException, IOException {
                //Step 1- Driver Instantiation: Instantiate driver object as FirefoxDriver
        WebDriver driver = new FirefoxDriver();
//
//        VkAuthorizer authorizer = new VkAuthorizer(driver);
//        authorizer.authorize();
//
//        //Step 4- Close Driver
//        driver.close();
//
//        //Step 5- Quit Driver
//        driver.quit();


        oAuthorizer = new VkOAuthorizer();
        if (oAuthorizer.getAccessToken().isEmpty()) {
            getToken(driver);
        }

        getAlbums();
//            makeFriendsRequest();

    }

    public static void getToken(WebDriver driver) throws IOException {
        VkAuthorizer authorizer = new VkAuthorizer(driver);
        authorizer.authorize();
        String queryToGetCode = oAuthorizer.getQueryForCode();
        driver.navigate().to(queryToGetCode);
        String location = driver.getCurrentUrl();
        oAuthorizer.parseCode(location);

        String queryToGetToken = oAuthorizer.getQueryForToken();
        driver.navigate().to(queryToGetToken);
        String tokenResponse = driver.findElement(By.tagName("pre")).getText();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(tokenResponse);
        oAuthorizer.setAccessToken(actualObj.get("access_token").textValue());

    }

    public static void getAlbums() {

        VKontakteTemplate vKontakteTemplate = new VKontakteTemplate(oAuthorizer.getAccessToken(), oAuthorizer.getClientSecret());
        List<Post> posts = vKontakteTemplate.wallOperations().getPosts();
        for (Post post: posts) {
            post.getText();
        }
        //vKontakteTemplate.wallOperations().getPosts(0, 100);
        CommentsQuery query = new CommentsQuery.Builder(new UserWall(482616L), 6349).build();
        vKontakteTemplate.wallOperations().getComments(query);
//        VKArray<VKontakteProfile> array = vKontakteTemplate.friendsOperations().get();
//        for (VKontakteProfile profile : array.getItems()) {
//            System.out.println(profile.getBirthDate().getDay());
//        }
    }

    public static void makeFriendsRequest() {

        VKontakteTemplate vKontakteTemplate = new VKontakteTemplate(oAuthorizer.getAccessToken(), oAuthorizer.getClientSecret());
        VKArray<VKontakteProfile> array = vKontakteTemplate.friendsOperations().get();
        for (VKontakteProfile profile : array.getItems()) {
            System.out.println(profile.getBirthDate().getDay());
        }
    }

}
