package tirnak.persistence.wall.filter;

import org.openqa.selenium.JavascriptExecutor;

public class TextPostFilter implements PostFilter {
    @Override
    public void filter(JavascriptExecutor driver) {
        driver.executeScript(
                "var filters = ['.copy_quote', '.page_media_wrap', '.image_cover', \n" +
                "'.page_media_thumbed_link', '.lnk', '.wall_audio_rows', \n" +
                "'.page_media_poll_wrap', '.post_thumbed_media']; \n" +
                "var posts = document.querySelectorAll('.wall_posts>.post'); \n" +
                "next_post: for (var i = 0;i < posts.length; i++) { \n" +
                "for (var j = 0; j < filters.length; j++) { \n" +
                "if (posts[i].querySelector(filters[j]) != null) { \n" +
                "    posts[i].remove(); \n" +
                "    continue next_post; \n" +
                "}"
        );
    }
}
