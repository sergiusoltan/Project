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
    POSITION("position"),
    RECOMENDED_BY("recomendedBy"),
    RECOMENDED_BY_ID("recomendedById"),
    AGE("age"),
    HEIGHT("height"),
    WEIGHT("weight");

    private String key;

    private PeopleProperties(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
