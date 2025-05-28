package md.ncts.model;

public class ItemTaxes {
    private String currency;
    private double quantity1;
    private double quantity2;
    private double statisticalValue;

    public ItemTaxes(String currency, double quantity1, double quantity2, double statisticalValue) {
        this.currency = currency;
        this.quantity1 = quantity1;
        this.quantity2 = quantity2;
        this.statisticalValue = statisticalValue;
    }

    public String getCurrency() { return currency; }
    public double getQuantity1() { return quantity1; }
    public double getQuantity2() { return quantity2; }
    public double getStatisticalValue() { return statisticalValue; }

    public void setCurrency(String currency) { this.currency = currency; }
    public void setQuantity1(double quantity1) { this.quantity1 = quantity1; }
    public void setQuantity2(double quantity2) { this.quantity2 = quantity2; }
    public void setStatisticalValue(double statisticalValue) { this.statisticalValue = statisticalValue; }
}
