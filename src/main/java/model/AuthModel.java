package main.java.model;

import main.java.util.Utils;

import java.io.Serializable;

/**
 * @author Serghei Soltan (soltan@spmsoftware.com)
 */
public class AuthModel implements Serializable {

    private Boolean login;
    private String name;
    private String email;
    private String password;
    private Boolean rememberme;

    public static AuthModel getAuthModel(String jsonRepresentation){
        return Utils.getInstance().fromJson(jsonRepresentation,AuthModel.class);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }

    public Boolean getRememberme() {
        return rememberme;
    }

    public void setRememberme(Boolean rememberme) {
        this.rememberme = rememberme;
    }
}
