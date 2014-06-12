package main.java.util;

/**
 * User: Sergiu Soltan
 */
public enum PeopleProperties {
    ID("id"),
    NAME("name"),
    EMAIL("email"),
    DATE("date"),
    PHONE("phone"),
    TYPE("type"),
    RECOMENDED_BY("recomendedBy");

    private String key;

    private PeopleProperties(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
