package md.ncts.model;

public class Packaging {
    private String typeCode;
    private int packageNumber; // ← corect
    private String shippingMarks;
    private int sequence;

    public Packaging(String typeCode, int packageNumber, String shippingMarks, int sequence) {
        this.typeCode = typeCode;
        this.packageNumber = packageNumber;
        this.shippingMarks = shippingMarks;
        this.sequence = sequence;
    }

    public String getTypeCode() { return typeCode; }
    public int getPackageNumber() { return packageNumber; } // ← corect
    public String getShippingMarks() { return shippingMarks; }
    public int getSequence() { return sequence; }

    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }
    public void setPackageNumber(int packageNumber) { this.packageNumber = packageNumber; } // ← corect
    public void setShippingMarks(String shippingMarks) { this.shippingMarks = shippingMarks; }
    public void setSequence(int sequence) { this.sequence = sequence; }
}
