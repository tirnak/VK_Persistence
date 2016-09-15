package tirnak.persistence;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.VkSaver;
import tirnak.persistence.common.VkSeleniumGeneric;

/**
 * Created by kise0116 on 15.09.2016.
 */
public class VkImageSaver extends VkSeleniumGeneric implements VkSaver {

    private ImageSaver imageSaver;
    private String baseImageUrl = baseUrl + "/photo";
    private String photosWithMe = baseUrl + "/tag";

    public VkImageSaver(WebDriver driver, String dirString, long userId) {
        super(driver);
        imageSaver = new ImageSaver(dirString);
        photosWithMe = baseUrl + "/tag" + userId;
    }

    public void saveImagesWithMe() {
        driver.navigate().to(photosWithMe);
        for (WebElement link : driver.findElements(By.className("photos_row "))) {
            link.click();
            String imgSrc = driver.findElement(By.cssSelector("#pv_photo img")).getAttribute("src");
            String name = driver.findElement(By.className("rel_date")).getText().replace(" ", "_") + "_" + getNameFromSrc(imgSrc);
            imageSaver.save(imgSrc, name);
            driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
        }
    }

    private String getNameFromSrc(String imgSrc) {
        String[] split = imgSrc.split("/");
        return split[split.length - 1];
    }

}
