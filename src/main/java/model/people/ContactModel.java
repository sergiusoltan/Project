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
    private Long recomendedById;
    private String date;
    private String age;
    private String height;
    private String weight;

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

    public Long getRecomendedById() {
        return recomendedById;
    }

    public void setRecomendedById(Long recomendedById) {
        this.recomendedById = recomendedById;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
