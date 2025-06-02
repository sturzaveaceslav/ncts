package md.ncts.util;

import java.io.*;

public class SavedActivationStorage {

    private static final String FILE_NAME = "activation.txt";

    public static void save(String code) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(code);
        } catch (IOException e) {
            System.err.println("Eroare la salvarea codului de activare: " + e.getMessage());
        }
    }

    public static String load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine();
        } catch (IOException e) {
            System.err.println("Eroare la citirea codului de activare: " + e.getMessage());
            return null;
        }
    }

    public static boolean isSavedLicenseValid() {
        String code = load();
        return code != null && md.ncts.util.LicenseValidator.isCodeValid(code);
    }
}
