package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OrderRowJSON {

    private static final String FILE_PATH = "OrderRow.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // ---------------------------------------------
    // READ JSON → LIST<OrderRow>
    // ---------------------------------------------
    public static List<OrderRow> loadOrderRows() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println("⚠ Không tìm thấy OrderRow.json → tạo file mới.");
            return new ArrayList<>();
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<List<OrderRow>>() {}.getType();
            List<OrderRow> data = gson.fromJson(reader, listType);

            return data != null ? data : new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ---------------------------------------------
    // WRITE LIST<OrderRow> → JSON
    // ---------------------------------------------
    public static void saveOrderRows(List<OrderRow> rows) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(FILE_PATH), StandardCharsets.UTF_8)) {
            gson.toJson(rows, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
