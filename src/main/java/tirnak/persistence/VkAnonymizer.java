package tirnak.persistence;

import org.openqa.selenium.WebDriver;
import tirnak.persistence.common.VkSaver;
import tirnak.persistence.common.VkSeleniumGeneric;

public class VkAnonymizer extends VkSeleniumGeneric implements VkSaver {

    private VkSaverImpl saver;
    public VkAnonymizer(WebDriver driver) {
        super(driver);
    }
}
