package main.java.model.people;

import com.google.appengine.api.blobstore.BlobKey;

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
    private Long age;
    private Long height;
    private Long weight;
    private String contactImageUrl;
    private String imageBlobKey;

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

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getContactImageUrl() {
        return contactImageUrl;
    }

    public void setContactImageUrl(String contactImageUrl) {
        this.contactImageUrl = contactImageUrl;
    }

    public String getImageBlobKey() {
        return imageBlobKey;
    }

    public void setImageBlobKey(String imageBlobKey) {
        this.imageBlobKey = imageBlobKey;
    }
}
