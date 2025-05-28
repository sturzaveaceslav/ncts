package md.ncts.model;

public class GuaranteeReference {
    private String grn;
    private String accessCode;
    private double coveredAmount;
    private String currency;
    private int sequence;

    public GuaranteeReference(String grn, String accessCode, double coveredAmount, String currency, int sequence) {
        this.grn = grn;
        this.accessCode = accessCode;
        this.coveredAmount = coveredAmount;
        this.currency = currency;
        this.sequence = sequence;
    }

    public String getGrn() { return grn; }
    public String getAccessCode() { return accessCode; }
    public double getCoveredAmount() { return coveredAmount; }
    public String getCurrency() { return currency; }
    public int getSequence() { return sequence; }

    public void setGrn(String grn) { this.grn = grn; }
    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }
    public void setCoveredAmount(double coveredAmount) { this.coveredAmount = coveredAmount; }
    public void setCurrency(String currency) { this.currency = currency; }
    public void setSequence(int sequence) { this.sequence = sequence; }
}
