package main.java.util;

/**
 * @author Serghei Soltan (soltan@spmsoftware.com)
 */
public enum Entities {
    USER("USER"),
    ORDERS("ORDERS"),
    EVENTS("EVENTS"),
    CONTACT("CONTACT"),
    CLIENT("CLIENT"),
    DISTRIBUTOR("DISTRIBUTOR");

    private String id;

    private Entities(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
