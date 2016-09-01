package tirnak.persistence;

import org.springframework.social.vkontakte.api.impl.VKontakteTemplate;

import java.util.ResourceBundle;

/**
 * Created by kirill on 01.09.16.
 */
public class VkOAuthorizer {
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

    {
        ResourceBundle myResources = ResourceBundle.getBundle("credentials");
        clientId = myResources.getString("client_id");
        clientSecret = myResources.getString("client_secret");
        accessToken = myResources.getString("access_token");
        code = myResources.getString("code");
        redirect_uri = myResources.getString("redirect_uri");
        scope = myResources.getString("scope");
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
}
