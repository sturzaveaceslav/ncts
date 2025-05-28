package md.ncts;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import md.ncts.model.*;
import md.ncts.service.ExcelService;
import md.ncts.service.JsonService;

import java.io.File;
import java.util.List;

public class MainApp extends Application {

    // Declarații
    private File selectedExcelFile = null;

    // Controale
    private TextField eoriField = new TextField();
    private TextField truckField = new TextField();
    private TextField depOfficeField = new TextField("MD203000");
    private TextField destOfficeField = new TextField("MD207000");
    private TextField grossMassField = new TextField();
    private TextField invoiceValueField = new TextField();
    private TextField packTypeField = new TextField("ZZ");
    private TextField shippingMarksField = new TextField("F/M");

    private TextField guaranteeNumberField = new TextField();
    private TextField guaranteeCodeField = new TextField();
    private TextField guaranteeAmountField = new TextField("10");
    private ComboBox<String> guaranteeTypeBox = new ComboBox<>(FXCollections.observableArrayList(
            "1 - Garanție globală", "0 - Niciuna", "2 - Individuală"
    ));

    private Label fileLabel = new Label("Niciun fișier selectat");

    private TextField expNameField = new TextField();
    private TextField expStreetField = new TextField();
    private TextField expCityField = new TextField();
    private TextField expPostcodeField = new TextField();

    @Override
    public void start(Stage stage) {
        Label title = new Label("NCTS - Generator JSON");
        title.setId("title");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);

        int r = 0;
        ComboBox<String> declarationTypeBox = new ComboBox<>(FXCollections.observableArrayList("T1", "T2", "C", "T2F"));
        declarationTypeBox.setValue("T1");

        ComboBox<String> addDeclTypeBox = new ComboBox<>(FXCollections.observableArrayList("A - Declarație standard", "D - Declaratie inainte de prezentare"));
        addDeclTypeBox.setValue("A - Declarație standard");

        grid.add(new Label("Tip Declarație"), 0, r); grid.add(declarationTypeBox, 1, r++);
        grid.add(new Label("Tip detaliere"), 0, r); grid.add(addDeclTypeBox, 1, r++);

        TextField contactNameField = new TextField();
        TextField contactPhoneField = new TextField();
        TextField contactEmailField = new TextField();
        var saved = md.ncts.util.ContactStorage.load();
        if (saved != null) {
            contactNameField.setText(saved.name);
            contactPhoneField.setText(saved.phone);
            contactEmailField.setText(saved.email);
        }


        GridPane contactGrid = new GridPane();
        contactGrid.setHgap(15);
        contactGrid.setVgap(10);
        contactGrid.setPadding(new Insets(20, 0, 0, 0));

        contactGrid.add(new Label("Nume Contact"), 0, 0); contactGrid.add(contactNameField, 1, 0);
        contactGrid.add(new Label("Telefon"), 0, 1); contactGrid.add(contactPhoneField, 1, 1);
        contactGrid.add(new Label("Email"), 0, 2); contactGrid.add(contactEmailField, 1, 2);



        grid.add(new Label("EORI"), 0, r); grid.add(eoriField, 1, r++);
        grid.add(new Label("Truck Number"), 0, r); grid.add(truckField, 1, r++);
        grid.add(new Label("Departure Office"), 0, r); grid.add(depOfficeField, 1, r++);
        grid.add(new Label("Destination Office"), 0, r); grid.add(destOfficeField, 1, r++);
        grid.add(new Label("Total Gross Mass"), 0, r); grid.add(grossMassField, 1, r++);
        grid.add(new Label("Total Invoice Value (EUR)"), 0, r); grid.add(invoiceValueField, 1, r++);
        grid.add(new Label("Package Type"), 0, r); grid.add(packTypeField, 1, r++);
        grid.add(new Label("Shipping Marks"), 0, r); grid.add(shippingMarksField, 1, r++);

        grid.add(new Label("Guarantee Number"), 2, 0); grid.add(guaranteeNumberField, 3, 0);
        grid.add(new Label("Guarantee Code"), 2, 1); grid.add(guaranteeCodeField, 3, 1);
        grid.add(new Label("Guarantee Amount (EUR)"), 2, 2); grid.add(guaranteeAmountField, 3, 2);
        grid.add(new Label("Tip Garanție"), 2, 3); grid.add(guaranteeTypeBox, 3, 3);

        // Secțiune Exportator
        GridPane exporterGrid = new GridPane();
        exporterGrid.setHgap(15);
        exporterGrid.setVgap(10);
        exporterGrid.setPadding(new Insets(20, 0, 0, 0));

        exporterGrid.add(new Label("Exporter"), 0, 0); exporterGrid.add(expNameField, 1, 0);
        exporterGrid.add(new Label("Street"), 0, 1); exporterGrid.add(expStreetField, 1, 1);
        exporterGrid.add(new Label("City"), 0, 2); exporterGrid.add(expCityField, 1, 2);
        exporterGrid.add(new Label("Postcode"), 0, 3); exporterGrid.add(expPostcodeField, 1, 3);

        // Butoane
        Button uploadBtn = new Button("Upload File...");
        uploadBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Selectează fișier Excel");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            selectedExcelFile = chooser.showOpenDialog(stage);

            if (selectedExcelFile != null) {
                fileLabel.setText(selectedExcelFile.getName());

                // ✅ Citește și calculează imediat
                List<HouseItem> items = ExcelService.readExcel(
                        selectedExcelFile,
                        packTypeField.getText(),
                        shippingMarksField.getText()
                );

                // ✅ Actualizează valorile în interfață
                grossMassField.setText(String.format("%.2f", items.stream().mapToDouble(HouseItem::getGrossMass).sum()));
                invoiceValueField.setText(String.format("%.2f", items.stream().mapToDouble(HouseItem::getStatisticalValue).sum()));
            }
        });


        Button genBtn = new Button("Generate JSON");
        genBtn.setOnAction(e -> {
            if (selectedExcelFile == null) {
                showAlert("Te rugăm să selectezi un fișier Excel!");
                return;
            }

            List<HouseItem> items = ExcelService.readExcel(
                    selectedExcelFile,
                    packTypeField.getText(),
                    shippingMarksField.getText()
            );

            grossMassField.setText(String.format("%.2f", items.stream().mapToDouble(HouseItem::getGrossMass).sum()));
            invoiceValueField.setText(String.format("%.2f", items.stream().mapToDouble(HouseItem::getStatisticalValue).sum()));

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Salvează JSON");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            File saveFile = chooser.showSaveDialog(stage);

            if (saveFile != null) {

                // 1. Applicant
                Applicant applicant = new Applicant(
                        expNameField.getText(),
                        eoriField.getText(),
                        expStreetField.getText(),
                        expCityField.getText(),
                        "MD"
                );

                // 2. Contact
                ApplicantContact contact = new ApplicantContact(
                        contactNameField.getText(),
                        contactPhoneField.getText(),
                        contactEmailField.getText()
                );


                // 3. Consignee
                // înlocuiește blocul:
                Consignee consignee = new Consignee(
                        expNameField.getText(),           // Numele firmei
                        eoriField.getText(),              // EORI același
                        expStreetField.getText(),         // strada
                        expCityField.getText(),           // oraș
                        "MD"                              // țară
                );


                // 4. Representative
                Representative rep = new Representative(
                        "Declarant SRL",
                        "EORI987654321",
                        "Str. Decebal 12",
                        "Chișinău",
                        "MD"
                );

                // 5. Guarantee + reference
                GuaranteeReference ref = new GuaranteeReference(
                        guaranteeNumberField.getText(),
                        guaranteeCodeField.getText(),
                        Double.parseDouble(guaranteeAmountField.getText()),
                        "EUR",
                        1
                );
                Guarantee guarantee = new Guarantee(1, guaranteeTypeBox.getValue().substring(0, 1), List.of(ref));
                List<Guarantee> guarantees = List.of(guarantee);

                // 6. HouseConsignment
                HouseConsignment cons = new HouseConsignment(1,
                        items.stream().mapToDouble(HouseItem::getGrossMass).sum(),
                        items
                );
                List<HouseConsignment> houseConsignments = List.of(cons);

                // 7. Apel JsonService complet
                // Creează idType (ex: 30 = "Registration Number of the Road Vehicle")
                IdType idType = new IdType(30, "Registration Number of the Road Vehicle", true);

// Creează custOffice (biroul vamal de plecare, numele poate fi hardcodat)
                CustOffice custOffice = new CustOffice(depOfficeField.getText(), "LEUSENI (PVFI, rutier)");

// Creează BorderTransport complet
                BorderTransport borderTransport = new BorderTransport(
                        1,                         // sequence
                        idType,
                        truckField.getText(),      // număr camion
                        "MD",                      // țară
                        custOffice
                );
                ;
                JsonService.generateJson(
                        applicant,
                        contact,
                        consignee,
                        rep,
                        guarantees,
                        houseConsignments,
                        borderTransport,                           // ✅ 7
                        declarationTypeBox.getValue(),             // ✅ 8
                        grossMassField.getText(),                  // ✅ 9
                        invoiceValueField.getText(),               // ✅ 10
                        depOfficeField.getText(),                  // ✅ 11
                        destOfficeField.getText(),                 // ✅ 12
                        saveFile                                   // ✅ 13
                );



                showAlert("✅ JSON salvat cu succes!");
            }
        });


        HBox buttons = new HBox(10, uploadBtn, genBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);
        HBox actorSection = new HBox(50, exporterGrid, contactGrid);
        actorSection.setAlignment(Pos.CENTER_LEFT);

        HBox dataBoxes = new HBox(50, exporterGrid, contactGrid);
        VBox root = new VBox(20, title, grid, fileLabel, buttons, dataBoxes);


        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");

// ✅ AICI setezi stilul
        fileLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Scene scene = new Scene(root, 950, 750);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("NCTS - Generator JSON");
        stage.show();
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.setHeaderText(null);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
