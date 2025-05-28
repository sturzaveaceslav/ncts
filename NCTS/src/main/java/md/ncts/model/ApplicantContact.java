package md.ncts.model;

public class ApplicantContact {
    private String name;
    private String phone;
    private String email;
    private final String contactFor = "APPLICANT"; // câmp static pentru JSON

    public ApplicantContact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getContactFor() {
        return contactFor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
