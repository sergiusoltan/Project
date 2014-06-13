package main.java.util;

/**
 * User: Sergiu Soltan
 */
public enum UserProperties {
    NAME("name"),
    EMAIL("email"),
    DATE("date"),
    PASSWORD("password"),
    ID("id"),
    SESSION_TOKEN("sessionToken");

    private String key;

    private UserProperties(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
