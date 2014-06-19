package main.java.util;

/**
 * @author Serghei Soltan (soltan@spmsoftware.com)
 */
public enum Entities {
    USER("USER"),
    EVALUATIONS("EVALUATIONS"),
    EVENTS("EVENTS"),
    PRODUCTS("PRODUCTS"),
    CONTACT("CONTACT"),;

    private String id;

    private Entities(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
