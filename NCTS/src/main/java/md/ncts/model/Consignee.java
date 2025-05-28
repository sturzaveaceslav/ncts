package md.ncts.model;

public class Consignee {
    private String name;
    private String eoriNumber;
    private String street;
    private String city;
    private String country;

    public Consignee(String name, String eoriNumber, String street, String city, String country) {
        this.name = name;
        this.eoriNumber = eoriNumber;
        this.street = street;
        this.city = city;
        this.country = country;
    }

    public String getName() { return name; }
    public String getEoriNumber() { return eoriNumber; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getCountry() { return country; }

    public void setName(String name) { this.name = name; }
    public void setEoriNumber(String eoriNumber) { this.eoriNumber = eoriNumber; }
    public void setStreet(String street) { this.street = street; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }
}
