package main.java.model.people;

import main.java.util.Utils;

/**
 * User: Sergiu Soltan
 */
public class EvaluationModel {
    private Long id;
    private Long contactId;
    private String date;
    private Integer dateYear;
    private Integer dateMonth;
    private Integer dateDay;
    private Long weight;
    private Long fat;
    private Long muscle;
    private Long imc;
    private Double mineralization;
    private Long metabolicage;
    private Long hydration;
    private Long visceralfat;

    public static EvaluationModel getFromString(String evaluation){
        return Utils.getInstance().fromJson(evaluation,EvaluationModel.class);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getFat() {
        return fat;
    }

    public void setFat(Long fat) {
        this.fat = fat;
    }

    public Long getMuscle() {
        return muscle;
    }

    public void setMuscle(Long muscle) {
        this.muscle = muscle;
    }

    public Long getImc() {
        return imc;
    }

    public void setImc(Long imc) {
        this.imc = imc;
    }

    public Double getMineralization() {
        return mineralization;
    }

    public void setMineralization(Double mineralization) {
        this.mineralization = mineralization;
    }

    public Long getMetabolicage() {
        return metabolicage;
    }

    public void setMetabolicage(Long metabolicage) {
        this.metabolicage = metabolicage;
    }

    public Long getHydration() {
        return hydration;
    }

    public void setHydration(Long hydration) {
        this.hydration = hydration;
    }

    public Long getVisceralfat() {
        return visceralfat;
    }

    public void setVisceralfat(Long visceralfat) {
        this.visceralfat = visceralfat;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Integer getDateYear() {
        return dateYear;
    }

    public void setDateYear(Integer dateYear) {
        this.dateYear = dateYear;
    }

    public Integer getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(Integer dateMonth) {
        this.dateMonth = dateMonth;
    }

    public Integer getDateDay() {
        return dateDay;
    }

    public void setDateDay(Integer dateDay) {
        this.dateDay = dateDay;
    }
}
