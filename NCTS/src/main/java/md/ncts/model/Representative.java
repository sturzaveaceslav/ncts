package md.ncts.model;

public class Representative {
    private String name;
    private String cui;
    private String street;
    private String city;
    private String postcode;
    private String country;

    public Representative(String name, String cui, String street, String city, String postcode, String country) {
        this.name = name;
        this.cui = cui;
        this.street = street;
        this.city = city;
        this.postcode = postcode;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getCui() {
        return cui;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
