package main.java.model.auth;

/**
 * User: Sergiu Soltan
 */
public class Authorization {
    private String email;
    private String sessionToken;

    public Authorization(String authorization) {
        if(authorization != null){
            authorization = authorization.substring(6,authorization.length());
            String[] params = authorization.split(":");
            this.email = params[0];
            this.sessionToken = params[1];
        }
    }

    public String getEmail() {
        return email;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
