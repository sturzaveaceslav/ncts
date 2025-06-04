package md.ncts.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Enumeration;

public class LicenseValidator {

    private static final String SECRET_KEY = "slavon1234-secret-key"; // trebuie să fie IDENTICĂ cu cea din generator
    private static final File LICENSE_FILE = new File(System.getenv("APPDATA"), "NCTS/license.json");

    public static boolean isValid() {
        try {
            if (!LICENSE_FILE.exists()) return false;

            JsonObject json = new Gson().fromJson(new FileReader(LICENSE_FILE), JsonObject.class);
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

    public static void saveLicenseJson(String code) {
        try {
            String[] parts = code.split("\\|");
            if (parts.length != 4) return;

            JsonObject json = new JsonObject();
            json.addProperty("mac", parts[0]);
            json.addProperty("company", parts[1]);
            json.addProperty("expiry", parts[2]);
            json.addProperty("signature", parts[3]);

            // Creează folderul dacă nu există
            LICENSE_FILE.getParentFile().mkdirs();

            try (Writer writer = new FileWriter(LICENSE_FILE)) {
                new Gson().toJson(json, writer);
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    public static String generateActivationCode(String mac, String company, String expiryDate) {
        try {
            String data = mac + company + expiryDate;
            String signature = hashWithSecret(data);
            return mac + "|" + company + "|" + expiryDate + "|" + signature;
        } catch (Exception e) {
            return "INVALID";
        }
    }

    public static boolean isCodeValid(String code) {
        try {
            String[] parts = code.split("\\|");
            if (parts.length != 4) return false;

            String mac = parts[0];
            String company = parts[1];
            String expiry = parts[2];
            String signature = parts[3];

            String currentMac = getMacAddress();
            if (!mac.equalsIgnoreCase(currentMac)) return false;

            String data = mac + company + expiry;
            String expectedSignature = hashWithSecret(data);
            if (!expectedSignature.equalsIgnoreCase(signature)) return false;

            if (!expiry.equals("NEEXPIRABIL")) {
                long expiryMillis = new SimpleDateFormat("yyyy-MM-dd").parse(expiry).getTime();
                if (System.currentTimeMillis() > expiryMillis) return false;
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
