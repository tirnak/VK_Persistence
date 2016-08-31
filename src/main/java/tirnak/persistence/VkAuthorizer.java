package tirnak.persistence;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import tirnak.persistence.common.VkSeleniumGeneric;

import java.util.ResourceBundle;

/**
 * Created by kise0116 on 31.08.2016.
 */
public class VkAuthorizer extends VkSeleniumGeneric {

    public VkAuthorizer(WebDriver driver) {
        super(driver);
    }

    private String email;
    private String pass;

    {
        ResourceBundle myResources = ResourceBundle.getBundle("credentials");
        email = myResources.getString("email");
        pass = myResources.getString("pass");
    }

    public boolean authorize() {
        try {
            _authorize();
        } catch (Exception e) {
            System.out.println("Exception during log in" + e);
            return false;
        }
        return true;
    }

    private void _authorize() throws Exception {
        driver.navigate().to(baseUrl + "/login");
        RemoteWebElement emailElement = (RemoteWebElement) driver.findElement(By.id("email"));
        RemoteWebElement passElement = (RemoteWebElement) driver.findElement(By.id("pass"));
        emailElement.sendKeys(email);
        passElement.sendKeys(pass);
        driver.findElement(By.id("login_button")).click();

        Thread.sleep(10000);

        String name = driver.findElement(By.className("top_profile_name")).getText();
        driver.findElement(By.className("profile_online"));

        System.out.println("Logged in as " + name);
    }
}
