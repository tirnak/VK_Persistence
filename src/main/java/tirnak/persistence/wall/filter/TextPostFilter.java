package tirnak.persistence.wall.filter;

import org.openqa.selenium.JavascriptExecutor;

public class TextPostFilter implements PostFilter {
    @Override
    public void filter(JavascriptExecutor driver) {
        driver.executeScript(
                "var filters = ['.copy_quote' , '.page_media_wrap' , '.image_cover' , " +
                " '.page_media_thumbed_link' , '.lnk' , '.wall_audio_rows' , " +
                " '.page_media_poll_wrap' , '.post_thumbed_media' ]; " +
                "var posts = document.querySelectorAll('.wall_posts>.post'); \n" +
                "next_post: for (var i = 0;i < posts.length; i++) { " +
                    "for (var j = 0; j < filters.length; j++) { " +
                        "if (posts[i].querySelector(filters[j]) != null) { " +
                        "    posts[i].remove(); " +
                        "    continue next_post; " +
                        "} " +
                    "} " +
                "}"
        );
    }
}
