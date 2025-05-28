package md.ncts.model;

import java.util.List;

public class HouseConsignment {
    private int sequenceNumber;
    private double grossMass;
    private List<HouseItem> houseItems;

    public HouseConsignment(int sequenceNumber, double grossMass, List<HouseItem> houseItems) {
        this.sequenceNumber = sequenceNumber;
        this.grossMass = grossMass;
        this.houseItems = houseItems;
    }

    public int getSequenceNumber() { return sequenceNumber; }
    public double getGrossMass() { return grossMass; }
    public List<HouseItem> getHouseItems() { return houseItems; }

    public void setSequenceNumber(int sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    public void setGrossMass(double grossMass) { this.grossMass = grossMass; }
    public void setHouseItems(List<HouseItem> houseItems) { this.houseItems = houseItems; }
}
