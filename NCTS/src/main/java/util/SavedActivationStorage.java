package md.ncts.util;

import java.io.*;

public class SavedActivationStorage {

    private static final File FILE = new File(System.getenv("APPDATA"), "NCTS/activation.txt");

    public static void save(String code) {
        try {
            FILE.getParentFile().mkdirs(); // creează folderul dacă lipsește
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
                writer.write(code);
            }

            md.ncts.util.LicenseValidator.saveLicenseJson(code);

        } catch (IOException e) {
            System.err.println("Eroare la salvarea codului de activare: " + e.getMessage());
        }
    }

    public static String load() {
        if (!FILE.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
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
