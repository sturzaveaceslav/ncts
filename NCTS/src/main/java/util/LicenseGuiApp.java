package md.ncts.util;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;

public class LicenseGuiApp extends Application {

    @Override
    public void start(Stage stage) {
        Label macLabel = new Label("MAC Adresă:");
        TextField macField = new TextField();

        Label companyLabel = new Label("Companie:");
        TextField companyField = new TextField();

        Label expiryLabel = new Label("Data expirării:");
        DatePicker expiryPicker = new DatePicker(LocalDate.now().plusYears(1));

        Button generateBtn = new Button("Generează Licență");

        generateBtn.setOnAction(e -> {
            String mac = macField.getText().trim();
            String company = companyField.getText().trim();
            LocalDate expiryDate = expiryPicker.getValue();

            if (mac.isEmpty() || expiryDate == null) {
                showAlert("Completează MAC-ul și data de expirare.");
                return;
            }

            String expiry = expiryDate.toString();
            String code = md.ncts.util.LicenseValidator.generateActivationCode(mac, company, expiry);

            // ✅ Salvare în Excel
            try {
                String userHome = System.getProperty("user.home");
                File excelFile = new File(userHome + "/generated-licenses.xlsx");

                Workbook workbook;
                Sheet sheet;

                if (excelFile.exists()) {
                    workbook = new XSSFWorkbook(new FileInputStream(excelFile));
                    sheet = workbook.getSheetAt(0);
                } else {
                    workbook = new XSSFWorkbook();
                    sheet = workbook.createSheet("Licenses");
                    Row header = sheet.createRow(0);
                    header.createCell(0).setCellValue("MAC");
                    header.createCell(1).setCellValue("Company");
                    header.createCell(2).setCellValue("Expiry");
                    header.createCell(3).setCellValue("Code");
                }

                int lastRow = sheet.getLastRowNum() + 1;
                Row row = sheet.createRow(lastRow);
                row.createCell(0).setCellValue(mac);
                row.createCell(1).setCellValue(company);
                row.createCell(2).setCellValue(expiry);
                row.createCell(3).setCellValue(code);

                FileOutputStream out = new FileOutputStream(excelFile);
                workbook.write(out);
                out.close();
                workbook.close();
            } catch (Exception ex) {
                showAlert("❌ Eroare la salvarea în Excel: " + ex.getMessage());
            }

            // ✅ Afișează codul
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cod Generat");
            alert.setHeaderText("Codul de activare este:");
            alert.setContentText(code);

            ButtonType copyBtn = new ButtonType("Copiază", ButtonBar.ButtonData.LEFT);
            ButtonType closeBtn = new ButtonType("Închide", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(copyBtn, closeBtn);

            alert.showAndWait().ifPresent(result -> {
                if (result == copyBtn) {
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(code), null);
                }
            });
        });

        VBox root = new VBox(10,
                new HBox(10, macLabel, macField),
                new HBox(10, companyLabel, companyField),
                new HBox(10, expiryLabel, expiryPicker),
                generateBtn
        );

        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 400, 200);
        stage.setTitle("Generator Licență");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
