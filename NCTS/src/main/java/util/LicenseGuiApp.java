package md.ncts.util;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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

            long expiryMillis = Date.from(expiryDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime();

            JsonObject license = new JsonObject();
            license.addProperty("mac", mac);
            license.addProperty("company", company);
            license.addProperty("expiry", expiryMillis);

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Salvează fișierul license.json");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            chooser.setInitialFileName("license.json");

            var file = chooser.showSaveDialog(stage);
            if (file != null) {
                try (FileWriter writer = new FileWriter(file)) {
                    new Gson().toJson(license, writer);
                    showAlert("✅ Fișierul de licență a fost generat cu succes!");
                } catch (Exception ex) {
                    showAlert("❌ Eroare la salvare: " + ex.getMessage());
                }
            }
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
