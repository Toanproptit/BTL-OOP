package model;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Invoice {
    private static Set<String> existingBillIds = new HashSet<>(); // lưu tất cả bill_id đã dùng
    private static Random random = new Random();

    private String tableName;
    private List<OrderItem> orderItems;
    private double totalPrice;
    private String createdAt;
    private String bill_id;

    // Constructor tự sinh bill_id nếu không truyền
    public Invoice(String tableName, List<OrderItem> orderItems, double totalPrice, String createdAt) {
        this.tableName = tableName;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.bill_id = generateUniqueBillId();
        existingBillIds.add(this.bill_id); // lưu vào set để tránh trùng
    }

    private String generateUniqueBillId() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        String id;

        do {
            StringBuilder sb = new StringBuilder("HD-");
            for (int i = 0; i < 4; i++) {
                sb.append(letters.charAt(random.nextInt(letters.length())));
            }
            for (int i = 0; i < 2; i++) {
                sb.append(numbers.charAt(random.nextInt(numbers.length())));
            }
            // shuffle ký tự
            char[] arr = sb.substring(3).toCharArray();
            for (int i = 0; i < arr.length; i++) {
                int j = random.nextInt(arr.length);
                char tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
            id = "HD-" + new String(arr);
        } while (existingBillIds.contains(id)); // lặp nếu trùng
        return id;
    }

    // Getter & Setter
    public String getTableName() {
        return tableName;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }
}
