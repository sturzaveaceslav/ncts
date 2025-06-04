package md.ncts.util;

import com.google.gson.Gson;
import md.ncts.model.SavedFormData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SavedDataStorage {
    private static final String BASE_DIR = System.getenv("APPDATA") + File.separator + "NCTS";
    private static final String FORM_FILE_PATH = BASE_DIR + File.separator + "saved-form.json";
    private static final String ACTIVATION_FILE_PATH = BASE_DIR + File.separator + "activation.dat";

    public static void save(SavedFormData data) {
        try {
            new File(BASE_DIR).mkdirs(); // asigură existența folderului
            try (Writer writer = Files.newBufferedWriter(Paths.get(FORM_FILE_PATH))) {
                new Gson().toJson(data, writer);
                System.out.println("✅ Salvat în: " + FORM_FILE_PATH);
            }
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

    public static void saveActivation(String code) {
        try {
            new File(BASE_DIR).mkdirs();
            try (FileWriter fw = new FileWriter(ACTIVATION_FILE_PATH)) {
                fw.write(code);
            }
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
