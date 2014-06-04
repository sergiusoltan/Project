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
    private String id;
    private Boolean isLogged;

    public static UserStatus getUserStatus(String json){
        return Utils.getInstance().fromJson(json,UserStatus.class);
    }

    public UserStatus(Boolean logged) {
        isLogged = logged;
        userUrl = UserServiceUtil.getUserUrl(logged);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getLogged() {
        return isLogged;
    }

    public void setLogged(Boolean logged) {
        isLogged = logged;
    }
}
