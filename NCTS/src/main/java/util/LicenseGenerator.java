package md.ncts.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LicenseGenerator {

    private static final String SECRET_KEY = "slavon1234-secret-key"; // 🔐 cheia ta secretă

    public static void main(String[] args) {
        String macAddress = "D85ED31AAD7C";
        String company = "FirmaX";
        String expiry = "2026-06-01";

        String code = generateActivationCode(macAddress, company, expiry);
        System.out.println("🔑 Cod de activare:\n" + code);
    }

    public static String generateActivationCode(String mac, String company, String expiryDate) {
        try {
            String data = mac + company + expiryDate;
            String signature = hashWithSecret(data);
            return mac + "|" + company + "|" + expiryDate + "|" + signature;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
