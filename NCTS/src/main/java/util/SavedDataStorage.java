package md.ncts.util;

import com.google.gson.Gson;
import md.ncts.model.SavedFormData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SavedDataStorage {
    private static final String FORM_FILE_PATH = "saved-form.json";      // datele formularului
    private static final String ACTIVATION_FILE_PATH = "activation.dat"; // codul de activare

    // ✅ Pentru formular
    public static void save(SavedFormData data) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(FORM_FILE_PATH))) {
            new Gson().toJson(data, writer);
            System.out.println("✅ Salvat în: " + new File(FORM_FILE_PATH).getAbsolutePath());
        } catch (IOException e) {
            System.out.println("❌ Eroare la salvare:");
            e.printStackTrace();
        }
    }

    public static SavedFormData load() {
        try (Reader reader = Files.newBufferedReader(Paths.get(FORM_FILE_PATH))) {
            return new Gson().fromJson(reader, SavedFormData.class);
        } catch (IOException e) {
            System.out.println("⚠️ Nu există fișierul saved-form.json");
            return null;
        }
    }

    // ✅ Pentru codul de activare
    public static void saveActivation(String code) {
        try (FileWriter fw = new FileWriter(ACTIVATION_FILE_PATH)) {
            fw.write(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadActivation() {
        try (BufferedReader br = new BufferedReader(new FileReader(ACTIVATION_FILE_PATH))) {
            return br.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}
