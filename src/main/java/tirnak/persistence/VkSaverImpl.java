package tirnak.persistence;

import org.openqa.selenium.WebDriver;
import tirnak.persistence.common.VkSaver;
import tirnak.persistence.common.VkSeleniumGeneric;

public class VkSaverImpl extends VkSeleniumGeneric implements VkSaver {
    public VkSaverImpl(WebDriver driver) {
        super(driver);
    }
}
