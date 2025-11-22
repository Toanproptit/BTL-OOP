package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderJSON {

    private static final String FILE_PATH = "C:\\Users\\LENOVO\\OneDrive\\Desktop\\OOP_BTL\\BTL-OOP\\JavaBTCK-ProGastro\\orders.json";
    private static List<Order> orderList;

    // 🔹 Khi chương trình chạy, tự động load dữ liệu nếu có file JSON
    static {
        try {
            orderList = loadOrders();
        } catch (IOException e) {
            e.printStackTrace();
            orderList = new ArrayList<>();
        }
    }

    /**
     * 🔸 Lưu danh sách đơn hàng xuống file JSON
     */
    public static void saveOrders() {
        File file = new File(FILE_PATH);
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonSerializer<LocalDate>)
                            (date, type, context) -> new com.google.gson.JsonPrimitive(date.toString()))
                    .setPrettyPrinting()
                    .create();

            gson.toJson(orderList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 🔸 Đọc danh sách đơn hàng từ file JSON
     */
    public static List<Order> loadOrders() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonDeserializer<LocalDate>)
                            (json, type, context) -> LocalDate.parse(json.getAsString()))
                    .create();

            List<Order> list = gson.fromJson(reader, new TypeToken<List<Order>>() {}.getType());
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 🔸 Thêm đơn hàng mới vào danh sách
     */
    public static void addOrder(Order order) {
        orderList.add(order);
        saveOrders();
    }

    /**
     * 🔸 Cập nhật đơn hàng theo orderId
     */
    public static void updateOrder(Order order) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getOrderId().equals(order.getOrderId())) {
                orderList.set(i, order);
                break;
            }
        }
        saveOrders();
    }

    /**
     * 🔸 Xóa đơn hàng theo orderId
     */
    public static void deleteOrder(Order order) {
        orderList.removeIf(o -> o.getOrderId().equals(order.getOrderId()));
        saveOrders();
    }

    /**
     * 🔸 Lấy danh sách toàn bộ đơn hàng hiện tại
     */
    public static List<Order> getOrderList() {
        return orderList;
    }
}
