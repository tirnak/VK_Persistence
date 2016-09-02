package tirnak.persistence;

import org.springframework.social.vkontakte.api.impl.VKontakteTemplate;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by kirill on 01.09.16.
 */
public class VkOAuthorizer {
    private URL pathToFile;
    private Properties properties;
    private String clientId;
    private String clientSecret;
    private String accessToken;
    private String code;
    private String redirect_uri;
    private String scope;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public VkOAuthorizer(Properties properties, URL pathToFile) throws IOException {
        this.properties = properties;
        this.pathToFile = pathToFile;
        properties.load(pathToFile.openStream());
        clientId = properties.getProperty("client_id");
        clientSecret = properties.getProperty("client_secret");
        accessToken = properties.getProperty("access_token");
        code = properties.getProperty("code");
        redirect_uri = properties.getProperty("redirect_uri");
        scope = properties.getProperty("scope");
    }

    public String getQueryForCode() {
        return "https://oauth.vk.com/authorize?" +
                "client_id=" + clientId + "&" +
                "redirect_uri=" + redirect_uri + "&" +
                "scope=" + scope + "&" +
                "response_type=code";
    }

    public String getQueryForToken() {
        return "https://oauth.vk.com/access_token?" +
                "client_id=" + clientId + "&" +
                "client_secret=" + clientSecret + "&" +
                "redirect_uri=" + redirect_uri + "&" +
                "code=" + code;
    }

    public void parseCode(String location) {
        String[] questionSeparated = location.split("\\?");
        String[] keyValue = questionSeparated[1].split("=");
        if (!keyValue[0].equals("code")) {
            throw new RuntimeException("something wrong with returned code response");
        }
        code = keyValue[1];
    }

    public void persist() throws IOException {
        String path = pathToFile.getPath();

        properties.setProperty("client_id", clientId);
        properties.setProperty("client_secret", clientSecret);
        properties.setProperty("access_token", accessToken);
        properties.setProperty("code", code);
        properties.setProperty("redirect_uri", redirect_uri);
        properties.setProperty("scope", scope);
        properties.store(new FileOutputStream(path), "comment");
    }
}
