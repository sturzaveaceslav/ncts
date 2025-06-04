package md.ncts.model;

public class Packaging {
    private String typeCode;
    private int packageNumber;
    private String shippingMarks;
    private int sequence;

    // Constructor complet
    public Packaging(String typeCode, int packageNumber, String shippingMarks, int sequence) {
        this.typeCode = typeCode;
        this.packageNumber = packageNumber;
        this.shippingMarks = shippingMarks;
        this.sequence = sequence;
    }

    // 🔥 Constructor gol — acesta e esențial pentru instanțiere fără parametri
    public Packaging() {
    }

    public String getTypeCode() { return typeCode; }
    public int getPackageNumber() { return packageNumber; }
    public String getShippingMarks() { return shippingMarks; }
    public int getSequence() { return sequence; }

    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }
    public void setPackageNumber(int packageNumber) { this.packageNumber = packageNumber; }
    public void setShippingMarks(String shippingMarks) { this.shippingMarks = shippingMarks; }
    public void setSequence(int sequence) { this.sequence = sequence; }
}
