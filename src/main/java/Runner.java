import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import tirnak.persistence.VkAuthorizer;

/**
 * Created by kise0116 on 31.08.2016.
 */
public class Runner {

    //Start to write our test method. It should ends with "Test"
    public static void main(String[] args) {
                //Step 1- Driver Instantiation: Instantiate driver object as FirefoxDriver
        WebDriver driver = new FirefoxDriver();

        VkAuthorizer authorizer = new VkAuthorizer(driver);
        authorizer.authorize();

        //Step 4- Close Driver
        driver.close();

        //Step 5- Quit Driver
        driver.quit();
    }
}
