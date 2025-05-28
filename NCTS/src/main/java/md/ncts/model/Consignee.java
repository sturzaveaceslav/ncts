package md.ncts.model;

public class Consignee {
    private String name;
    private String eori;
    private String street;
    private String city;
    private String postcode;
    private String country;

    public Consignee(String name, String eori, String street, String city, String country, String postcode) {
        this.name = name;
        this.eori = eori;
        this.street = street;
        this.city = city;
        this.country = country;
        this.postcode = postcode;
    }


    public String getName() {
        return name;
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
}
