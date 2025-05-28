package md.ncts.model;

import java.util.List;

public class HouseItem {
    private int sequenceNumber;
    private String hsCode;
    private String description;
    private double grossMass;
    private double quantity;
    private double statisticalValue;
    private List<Packaging> packagings;
    private ItemTaxes itemTaxes; // ✅ Adăugat

    public HouseItem(int sequenceNumber, String hsCode, String description,
                     double grossMass, double quantity, double statisticalValue,
                     List<Packaging> packagings, ItemTaxes itemTaxes) {
        this.sequenceNumber = sequenceNumber;
        this.hsCode = hsCode;
        this.description = description;
        this.grossMass = grossMass;
        this.quantity = quantity;
        this.statisticalValue = statisticalValue;
        this.packagings = packagings;
        this.itemTaxes = itemTaxes;
    }

    public int getSequenceNumber() { return sequenceNumber; }
    public String getHsCode() { return hsCode; }
    public String getDescription() { return description; }
    public double getGrossMass() { return grossMass; }
    public double getQuantity() { return quantity; }
    public double getStatisticalValue() { return statisticalValue; }
    public List<Packaging> getPackagings() { return packagings; }
    public ItemTaxes getItemTaxes() { return itemTaxes; }

    public void setSequenceNumber(int sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    public void setHsCode(String hsCode) { this.hsCode = hsCode; }
    public void setDescription(String description) { this.description = description; }
    public void setGrossMass(double grossMass) { this.grossMass = grossMass; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public void setStatisticalValue(double statisticalValue) { this.statisticalValue = statisticalValue; }
    public void setPackagings(List<Packaging> packagings) { this.packagings = packagings; }
    public void setItemTaxes(ItemTaxes itemTaxes) { this.itemTaxes = itemTaxes; }

    public void addToTotals(double qty, double val, double brutt) {
        this.quantity += qty;
        this.statisticalValue += val;
        this.grossMass += brutt;
    }
}
