package tirnak.persistence.common;

import org.openqa.selenium.WebDriver;

public abstract class VkSeleniumGeneric {
    protected String baseUrl = "http://vk.com";
    protected WebDriver driver;

    public VkSeleniumGeneric(WebDriver driver) {
        this.driver = driver;
    }
}
