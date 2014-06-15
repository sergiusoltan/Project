package main.java.model.people;

/**
 * User: Sergiu Soltan
 */
public class MemberModel extends ContactModel {

    private String email;
    private String position;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
