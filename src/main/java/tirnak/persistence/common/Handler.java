package tirnak.persistence.common;

import org.openqa.selenium.WebElement;
import tirnak.persistence.model.Post;

public interface Handler {
    boolean checkDom(WebElement el);
    Post parse(WebElement element, Post currentPost);
}
