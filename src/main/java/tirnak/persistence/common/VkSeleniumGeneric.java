package tirnak.persistence.common;

import org.openqa.selenium.WebDriver;

/**
 * Created by kise0116 on 31.08.2016.
 */
public abstract class VkSeleniumGeneric {
    protected String baseUrl = "http://vk.com";
    protected WebDriver driver;

    public VkSeleniumGeneric(WebDriver driver) {
        this.driver = driver;
    }
}
