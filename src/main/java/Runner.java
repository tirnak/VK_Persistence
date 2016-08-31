import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.springframework.social.vkontakte.api.VKontakteProfile;
import org.springframework.social.vkontakte.api.impl.VKontakteTemplate;
import org.springframework.social.vkontakte.api.impl.json.VKArray;
import tirnak.persistence.VkOAuthorizer;

import java.util.ResourceBundle;

/**
 * Created by kise0116 on 31.08.2016.
 */
public class Runner {

    //Start to write our test method. It should ends with "Test"
    public static void main(String[] args) {
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


        VkOAuthorizer authorizer = new VkOAuthorizer();
        String toQueryInBrowser = authorizer.getQuery();

        do {
            driver.navigate().to(toQueryInBrowser);
            toQueryInBrowser = authorizer.getQuery();
        } while (!toQueryInBrowser.isEmpty());

        System.out.println(toQueryInBrowser);
        if (toQueryInBrowser.isEmpty()) {
            makeFriendsRequest();
        }
    }

    public static void makeFriendsRequest() {
        VkOAuthorizer authorizer = new VkOAuthorizer();

        VKontakteTemplate vKontakteTemplate = new VKontakteTemplate(authorizer.getAccessToken(), authorizer.getClientSecret());
        VKArray<VKontakteProfile> array = vKontakteTemplate.friendsOperations().get();
        for (VKontakteProfile profile : array.getItems()) {
            System.out.println(profile.getBirthDate().getDay());
        }
    }

}
