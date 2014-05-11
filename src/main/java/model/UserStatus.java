package main.java.model;

/**
 * User: Sergiu Soltan
 */
public class UserStatus {
    private String userUrl;
    private String name;
    private String email;
    private String id;
    private Boolean isLogged;

    public UserStatus(Boolean logged) {
        isLogged = logged;
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
