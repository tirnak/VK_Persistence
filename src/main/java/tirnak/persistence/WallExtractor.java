package tirnak.persistence;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import tirnak.persistence.common.VkSeleniumGeneric;

public class WallExtractor extends VkSeleniumGeneric {

    long userId;

    public WallExtractor(WebDriver driver, long userId) {
        super(driver);
        this.userId = userId;
    }

    public void goToWall() {
        driver.navigate().to(baseUrl + "/id" + userId);
    }
    /**
     * A method to scroll down the wall aimed to cope with lazy initialization of wall
     */
    public void scrollToEnd() throws InterruptedException {
        try {
            while (needsToBeLoaded()) {
                driver.findElement(By.id("wall_more_link")).click();
            }
        } catch (Exception ignored) {}

    }

    private boolean needsToBeLoaded() {
        try {
            driver.findElement(By.id("wall_more_link"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }

    }
}
