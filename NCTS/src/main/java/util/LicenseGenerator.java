package md.ncts.util;

import com.google.gson.JsonObject;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LicenseGenerator {

    private static final String SECRET_KEY = "slavon1234-secret-key"; // 🔒 cheia ta secretă

    public static void main(String[] args) {
        String macAddress = "D85ED31AAD7C";
        String company = "Slavon";
        String expiryDate = "2026-05-29";

        generateLicense(macAddress, company, expiryDate);
    }

    public static void generateLicense(String mac, String company, String expiryDate) {
        try {
            JsonObject license = new JsonObject();
            license.addProperty("mac", mac);
            license.addProperty("company", company);
            license.addProperty("expiry", expiryDate);

            // 🔐 Generăm semnătura digitală
            String data = mac + company + expiryDate;
            String signature = hashWithSecret(data);
            license.addProperty("signature", signature);

            try (FileWriter writer = new FileWriter("license.json")) {
                new Gson().toJson(license, writer);
            }

            System.out.println("✅ Fișierul license.json a fost generat.");

            // Log CSV
            try (FileWriter logWriter = new FileWriter("generated-licenses.csv", true)) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                logWriter.write(mac + "," + company + "," + expiryDate + "," + timestamp + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String hashWithSecret(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest((input + SECRET_KEY).getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
