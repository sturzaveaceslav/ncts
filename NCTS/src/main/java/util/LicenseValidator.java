package md.ncts.util;

import com.google.gson.JsonObject;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Enumeration;

public class LicenseValidator {

    private static final String SECRET_KEY = "slavon1234-secret-key"; // trebuie să fie IDENTICĂ cu cea din generator

    public static boolean isValid() {
        try {
            File file = new File("license.json");
            if (!file.exists()) return false;

            JsonObject json = new Gson().fromJson(new FileReader(file), JsonObject.class);
            String mac = json.get("mac").getAsString();
            String company = json.get("company").getAsString();
            String expiry = json.get("expiry").getAsString();
            String expectedSignature = json.get("signature").getAsString();

            String actualMac = getMacAddress();
            if (!actualMac.equalsIgnoreCase(mac)) return false;

            String data = mac + company + expiry;
            String calculatedSignature = hashWithSecret(data);
            if (!calculatedSignature.equals(expectedSignature)) return false;

            if (!expiry.equals("NEEXPIRABIL")) {
                long expiryMillis = new SimpleDateFormat("yyyy-MM-dd").parse(expiry).getTime();
                if (System.currentTimeMillis() > expiryMillis) return false;
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public static String getMacAddress() throws Exception {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface ni : Collections.list(interfaces)) {
            byte[] mac = ni.getHardwareAddress();
            if (mac == null) continue;

            StringBuilder sb = new StringBuilder();
            for (byte b : mac) sb.append(String.format("%02X", b));
            if (sb.length() > 0) return sb.toString();
        }
        return "";
    }

    private static String hashWithSecret(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest((input + SECRET_KEY).getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
