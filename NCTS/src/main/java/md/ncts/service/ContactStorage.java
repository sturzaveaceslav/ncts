package md.ncts.util;

import com.google.gson.Gson;
import java.io.*;

public class ContactStorage {

    private static final File FILE = new File("contact_last.json");

    public static void save(String name, String phone, String email) {
        try (Writer writer = new FileWriter(FILE)) {
            Gson gson = new Gson();
            ContactData data = new ContactData(name, phone, email);
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ContactData load() {
        if (!FILE.exists()) return null;
        try (Reader reader = new FileReader(FILE)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, ContactData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class ContactData {
        public String name;
        public String phone;
        public String email;

        public ContactData(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }
    }
}
