package main.java.util;

/**
 * User: Sergiu Soltan
 */
public enum EvaluationsProperties {
    ID("id"),
    CONTACT_ID("contactId"),
    DATE("date"),
    WEIGHT("weight"),
    MUSCLE_MASS("muscle"),
    FAT("fat"),
    IMC("imc"),
    MINERALIZATION("mineralization"),
    METABOLIC_AGE("metabolicage"),
    HYDRATION("hydration"),
    VISCERAL_FAT("visceralfat");
    private String id;

    private EvaluationsProperties(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
