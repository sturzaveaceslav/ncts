package md.ncts.model;

public class Exporter {
    private String name;
    private String eori;
    private String street;
    private String city;
    private String country;
    private String postcode;

    public Exporter(String name, String eori, String street, String city, String country, String postcode) {
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

    public String getEori() {
        return eori;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEori(String eori) {
        this.eori = eori;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public String toString() {
        return "Exporter{" +
                "name='" + name + '\'' +
                ", eori='" + eori + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", postcode='" + postcode + '\'' +
                '}';
    }
}
