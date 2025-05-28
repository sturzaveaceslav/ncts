package md.ncts.service;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import md.ncts.model.Consignee;
import md.ncts.model.Exporter;

public class JsonFormUtils {

    // Exporter fields
    private static TextField exporterNameField = new TextField();
    private static TextField exporterAddressField = new TextField();
    private static TextField exporterCountryField = new TextField();
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
                new javafx.scene.control.Label("EXPORTER"),
                new javafx.scene.control.Label("Name"), exporterNameField,
                new javafx.scene.control.Label("Address"), exporterAddressField,
                new javafx.scene.control.Label("Country"), exporterCountryField,
                new javafx.scene.control.Label("EORI"), exporterEoriField,
                new javafx.scene.control.Label("VAT Number"), exporterVatField
        );
        return box;
    }

    public static VBox createConsigneeForm() {
        VBox box = new VBox(5);
        box.getChildren().addAll(
                new javafx.scene.control.Label("CONSIGNEE"),
                new javafx.scene.control.Label("Name"), consigneeNameField,
                new javafx.scene.control.Label("Address"), consigneeAddressField,
                new javafx.scene.control.Label("Country"), consigneeCountryField,
                new javafx.scene.control.Label("EORI"), consigneeEoriField,
                new javafx.scene.control.Label("VAT Number"), consigneeVatField
        );
        return box;
    }

    public static Exporter getExporterFromForm() {
        return new Exporter(
                exporterNameField.getText(),
                exporterAddressField.getText(),
                exporterCountryField.getText(),
                exporterEoriField.getText(),
                exporterVatField.getText()
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
