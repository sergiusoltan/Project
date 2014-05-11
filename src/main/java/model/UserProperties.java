package main.java.model;

/**
 * User: Sergiu Soltan
 */
public enum UserProperties {
    USER_URL("userUrl"),
    NAME("name"),
    EMAIL("email"),
    IS_LOGGED("isLogged"),
    USER_ID("id");

    private String key;

    private UserProperties(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
