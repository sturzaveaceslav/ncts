package md.ncts.model;

import java.util.List;

public class HouseItem {
    private int sequence;
    private String hsCode;
    private String description;
    private double grossMass;
    private double netMass;
    private double supplementaryUnits;
    private List<Packaging> packagings;
    private ItemTaxes itemTaxes;

    private String dispatchCountryCode; // Cod țară (ex: "RO")
    private String dispatchCountryName; // Nume țară (ex: "România")

    // Constructorul folosit de ExcelService
    public HouseItem(int sequence, String hsCode, String description, double grossMass,
                     double netMass, double supplementaryUnits,
                     List<Packaging> packagings, ItemTaxes itemTaxes) {
        this.sequence = sequence;
        this.hsCode = hsCode;
        this.description = description;
        this.grossMass = grossMass;
        this.netMass = netMass;
        this.supplementaryUnits = supplementaryUnits;
        this.packagings = packagings;
        this.itemTaxes = itemTaxes;
    }

    // Getteri
    public int getSequence() { return sequence; }
    public String getHsCode() { return hsCode; }
    public String getDescription() { return description; }
    public double getGrossMass() { return grossMass; }
    public double getNetMass() { return netMass; }
    public double getSupplementaryUnits() { return supplementaryUnits; }
    public List<Packaging> getPackagings() { return packagings; }
    public ItemTaxes getItemTaxes() { return itemTaxes; }
    public String getDispatchCountryCode() { return dispatchCountryCode; }
    public String getDispatchCountryName() { return dispatchCountryName; }

    // Setteri
    public void setSequence(int sequence) { this.sequence = sequence; }
    public void setHsCode(String hsCode) { this.hsCode = hsCode; }
    public void setDescription(String description) { this.description = description; }
    public void setGrossMass(double grossMass) { this.grossMass = grossMass; }
    public void setNetMass(double netMass) { this.netMass = netMass; }
    public void setSupplementaryUnits(double supplementaryUnits) { this.supplementaryUnits = supplementaryUnits; }
    public void setPackagings(List<Packaging> packagings) { this.packagings = packagings; }
    public void setItemTaxes(ItemTaxes itemTaxes) { this.itemTaxes = itemTaxes; }
    public void setDispatchCountryCode(String dispatchCountryCode) { this.dispatchCountryCode = dispatchCountryCode; }
    public void setDispatchCountryName(String dispatchCountryName) { this.dispatchCountryName = dispatchCountryName; }
}
