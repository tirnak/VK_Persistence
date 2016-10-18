package tirnak.persistence.handlers;

import org.hibernate.SessionFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.AbstractHandler;
import tirnak.persistence.common.Handler;
import tirnak.persistence.model.Link;
import tirnak.persistence.model.Post;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class LinkHandler extends AbstractHandler implements Handler {

    public LinkHandler(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean checkDom(WebElement el) {
        return wrapString(el.getAttribute("class")).contains(LINK_WRAPER_CLASS);
    }

    private static final String LINK_WRAPER_CLASS = "page_media_link_desc_wrap";
    private static final String LINK_DESCRIPTION = "page_media_link_desc";
    private static final String LINK_HREF = "page_media_link_title";


    @Override
    public Post parse(WebElement el, Post currentPost) {
        Link link = new Link();
        link.setDescription(el.findElement(By.className(LINK_DESCRIPTION)).getText());
        String urlRaw = el.findElement(By.className(LINK_HREF)).getAttribute("href");
        try {
            link.setHref(cleanHref(urlRaw));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        link.setPost(currentPost);
        currentPost.addLink(link);
        return currentPost;
    }

    private String cleanHref(String urlRaw) throws UnsupportedEncodingException {
        String queryParams = urlRaw.split("\\?")[1];
        String[] params = queryParams.split("&");
        for (String param : params) {
            if (param.matches("to=.*")) {
                param = param.replace("to=", "");
                return URLDecoder.decode(param, "UTF-8");
            }
        }
        throw new IllegalArgumentException("some exception during parsing of href of page_media_link_title: " + urlRaw);
    }
}
