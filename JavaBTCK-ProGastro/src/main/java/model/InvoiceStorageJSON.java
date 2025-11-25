package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceStorageJSON {

    private static final String FILE_PATH = "invoices.json";

    public static List<Invoice> loadInvoices() {
        File file = new File(FILE_PATH);

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Invoice> loaded = gson.fromJson(reader, new TypeToken<List<Invoice>>() {}.getType());
            return loaded != null ? loaded : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đọc file invoices.json:");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void addInvoice(Invoice invoice) {
        if (invoice != null) {
            List<Invoice> current = loadInvoices();
            current.add(invoice);

            try {
                saveInvoices(current);
            } catch (IOException e) {
                System.err.println("❌ Không thể lưu hóa đơn mới.");
                e.printStackTrace();
            }
        }
    }

    public static void saveInvoices(List<Invoice> invoices) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(invoices, writer);
        }
    }

}
