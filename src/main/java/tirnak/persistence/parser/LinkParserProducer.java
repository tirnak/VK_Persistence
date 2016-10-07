package tirnak.persistence.parser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tirnak.persistence.common.ParserProducer;
import tirnak.persistence.model.Link;
import tirnak.persistence.model.Person;
import tirnak.persistence.model.Post;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static tirnak.persistence.common.StringEnhanced.wrapString;

public class LinkParserProducer implements ParserProducer {

    private static LinkParserProducer instance = new LinkParserProducer();
    public static LinkParserProducer getInstance() {
        return instance;
    }
    private LinkParserProducer() {}

    @Override
    public Predicate<WebElement> getPredicateIfAppropriateDom() {
        return el -> wrapString(el.getAttribute("class")).contains(LINK_WRAPER_CLASS);
    }

    private static final String LINK_WRAPER_CLASS = "page_media_link_desc_wrap";
    private static final String LINK_DESCRIPTION = "page_media_link_desc";
    private static final String LINK_HREF = "page_media_link_title";


    @Override
    public BiFunction<WebElement, Post, Post> getFunctionForParsing() {
        return (el, post) -> {
            Link link = new Link();
            link.setDescription(el.findElement(By.className(LINK_DESCRIPTION)).getText());
            String urlRaw = el.findElement(By.className(LINK_HREF)).getAttribute("href");
            try {
                link.setHref(cleanHref(urlRaw));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            link.setPost(post);
            post.addLink(link);
            return post;
        };
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
