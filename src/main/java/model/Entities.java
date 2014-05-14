package main.java.model;

/**
 * @author Serghei Soltan (soltan@spmsoftware.com)
 */
public enum Entities {
    USER("USER");

    private String id;

    private Entities(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
