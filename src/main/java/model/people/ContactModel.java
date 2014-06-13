package main.java.model.people;

import java.util.Date;

/**
 * User: Sergiu Soltan
 */
public class ContactModel {
    private Long id;
    private String name;
    private Long phone;
    private String type;
    private String recomendedBy;
    private String date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecomendedBy() {
        return recomendedBy;
    }

    public void setRecomendedBy(String recomendedBy) {
        this.recomendedBy = recomendedBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
