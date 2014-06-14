package main.java.model.people;

/**
 * User: Sergiu Soltan
 */
public class DashBoardModel {
    private String month;
    private Integer number;

    public DashBoardModel() {
    }

    public DashBoardModel(Integer number, String month) {
        this.number = number;
        this.month = month;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
