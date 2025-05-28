package md.ncts.model;

import java.util.List;

public class Guarantee {
    private int sequence;
    private String typeCode;
    private List<GuaranteeReference> references;

    public Guarantee(int sequence, String typeCode, List<GuaranteeReference> references) {
        this.sequence = sequence;
        this.typeCode = typeCode;
        this.references = references;
    }

    public int getSequence() { return sequence; }
    public String getTypeCode() { return typeCode; }
    public List<GuaranteeReference> getReferences() { return references; }

    public void setSequence(int sequence) { this.sequence = sequence; }
    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }
    public void setReferences(List<GuaranteeReference> references) { this.references = references; }
}
