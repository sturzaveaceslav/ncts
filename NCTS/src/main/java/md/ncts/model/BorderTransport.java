package md.ncts.model;

public class BorderTransport {
    private int sequence;
    private String transportType = "BORDER"; // fix conform cerințelor
    private IdType idType;
    private String identificationNumber;
    private String nationality;
    private CustOffice custOffice;

    public BorderTransport(int sequence, IdType idType, String identificationNumber, String nationality, CustOffice custOffice) {
        this.sequence = sequence;
        this.idType = idType;
        this.identificationNumber = identificationNumber;
        this.nationality = nationality;
        this.custOffice = custOffice;
    }

    public int getSequence() { return sequence; }
    public String getTransportType() { return transportType; }
    public IdType getIdType() { return idType; }
    public String getIdentificationNumber() { return identificationNumber; }
    public String getNationality() { return nationality; }
    public CustOffice getCustOffice() { return custOffice; }

    public void setSequence(int sequence) { this.sequence = sequence; }
    public void setIdType(IdType idType) { this.idType = idType; }
    public void setIdentificationNumber(String identificationNumber) { this.identificationNumber = identificationNumber; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public void setCustOffice(CustOffice custOffice) { this.custOffice = custOffice; }
}
