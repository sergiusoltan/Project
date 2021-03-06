package main.java.model.auth;

import main.java.util.UserServiceUtil;
import main.java.util.Utils;

/**
 * User: Sergiu Soltan
 */
public class UserStatus {
    private String userUrl;
    private String name;
    private String email;
    private String sessionToken;

    public static UserStatus getUserStatus(String json){
        return Utils.getInstance().fromJson(json,UserStatus.class);
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
