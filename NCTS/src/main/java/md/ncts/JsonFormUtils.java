package md.ncts.service;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import md.ncts.model.Consignee;
import md.ncts.model.Exporter;

public class JsonFormUtils {

    // Exporter fields
    private static TextField exporterNameField = new TextField();
    private static TextField exporterAddressField = new TextField();
    private static TextField exporterCityField = new TextField();        // nou
    private static TextField exporterCountryField = new TextField();
    private static TextField exporterPostcodeField = new TextField();    // nou
    private static TextField exporterEoriField = new TextField();
    private static TextField exporterVatField = new TextField();

    // Consignee fields
    private static TextField consigneeNameField = new TextField();
    private static TextField consigneeAddressField = new TextField();
    private static TextField consigneeCountryField = new TextField();
    private static TextField consigneeEoriField = new TextField();
    private static TextField consigneeVatField = new TextField();

    public static VBox createExporterForm() {
        VBox box = new VBox(5);
        box.getChildren().addAll(
                new Label("EXPORTER"),
                new Label("Name"), exporterNameField,
                new Label("Address"), exporterAddressField,
                new Label("City"), exporterCityField,
                new Label("Country"), exporterCountryField,
                new Label("Postcode"), exporterPostcodeField,
                new Label("EORI"), exporterEoriField,
                new Label("VAT Number"), exporterVatField
        );
        return box;
    }

    public static VBox createConsigneeForm() {
        VBox box = new VBox(5);
        box.getChildren().addAll(
                new Label("CONSIGNEE"),
                new Label("Name"), consigneeNameField,
                new Label("Address"), consigneeAddressField,
                new Label("Country"), consigneeCountryField,
                new Label("EORI"), consigneeEoriField,
                new Label("VAT Number"), consigneeVatField
        );
        return box;
    }

    public static Exporter getExporterFromForm() {
        return new Exporter(
                exporterNameField.getText(),
                exporterEoriField.getText(),
                exporterAddressField.getText(),
                exporterCityField.getText(),
                exporterCountryField.getText(),
                exporterPostcodeField.getText()
        );
    }

    public static Consignee getConsigneeFromForm() {
        return new Consignee(
                consigneeNameField.getText(),
                consigneeAddressField.getText(),
                consigneeCountryField.getText(),
                consigneeEoriField.getText(),
                consigneeVatField.getText()
        );
    }
}
