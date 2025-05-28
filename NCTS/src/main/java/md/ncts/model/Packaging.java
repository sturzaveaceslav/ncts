package md.ncts.model;

public class Packaging {
    private String typeCode;
    private String numberOfPackages;
    private String shippingMarks;
    private int sequence;

    public Packaging(String typeCode, String numberOfPackages, String shippingMarks, int sequence) {
        this.typeCode = typeCode;
        this.numberOfPackages = numberOfPackages;
        this.shippingMarks = shippingMarks;
        this.sequence = sequence;
    }

    public String getTypeCode() { return typeCode; }
    public String getNumberOfPackages() { return numberOfPackages; }
    public String getShippingMarks() { return shippingMarks; }
    public int getSequence() { return sequence; }

    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }
    public void setNumberOfPackages(String numberOfPackages) { this.numberOfPackages = numberOfPackages; }
    public void setShippingMarks(String shippingMarks) { this.shippingMarks = shippingMarks; }
    public void setSequence(int sequence) { this.sequence = sequence; }
}
