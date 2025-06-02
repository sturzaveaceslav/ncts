package md.ncts.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class ActivationWindow {

    private static String lastEnteredCode = "";

    public boolean showAndWait() {
        final boolean[] activated = {false};

        Stage stage = new Stage();
        stage.setTitle("Activare Licență");

        Label msgLabel = new Label("Introduceți codul de activare:");
        TextField codeField = new TextField();
        codeField.setPromptText("MAC|Firma|Data|Semnătură");

        Button checkBtn = new Button("Verifică");
        Button copyBtn = new Button("Copiază MAC");
        Label statusLabel = new Label();

        checkBtn.setOnAction(e -> {
            String code = codeField.getText().trim();
            if (md.ncts.util.LicenseValidator.isCodeValid(code)) {
                lastEnteredCode = code; // ✅ salvăm codul valid
                statusLabel.setText("✅ Cod valid. Licența este activată.");
                activated[0] = true;
                stage.close();
            } else {
                statusLabel.setText("❌ Cod invalid sau licență expirată.");
            }
        });

        copyBtn.setOnAction(e -> {
            try {
                String mac = md.ncts.util.LicenseValidator.getMacAddress();
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                        new StringSelection(mac), null
                );
                statusLabel.setText("📋 MAC-ul a fost copiat: " + mac);
            } catch (Exception ex) {
                statusLabel.setText("❌ Eroare la obținerea MAC-ului.");
            }
        });

        VBox box = new VBox(10, msgLabel, codeField, new HBox(10, checkBtn, copyBtn), statusLabel);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(box, 450, 180));
        stage.showAndWait();

        return activated[0];
    }

    public static String getLastEnteredCode() {
        return lastEnteredCode;
    }
}
