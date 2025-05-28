package md.ncts.util;

import com.google.gson.Gson;
import md.ncts.model.SavedFormData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SavedDataStorage {
    private static final String FILE_PATH = "saved-form.json";

    public static void save(SavedFormData data) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            new Gson().toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SavedFormData load() {
        try (Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH))) {
            return new Gson().fromJson(reader, SavedFormData.class);
        } catch (IOException e) {
            return null;
        }
    }
}
