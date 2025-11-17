package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private String orderId;
    private String customer;
    private LocalDate date;
    private String status;
    private String note;
    private double amount; // Tính toán từ các OrderItem
    // Danh sách các món ăn trong đơn hàng
    private List<OrderItem> items;

    // Constructor chính (dùng khi tạo Order mới)
    public Order(String orderId, String customer, LocalDate date, String status) {
        this.orderId = orderId;
        this.customer = customer;
        this.date = date;
        this.status = status;
        this.amount = 0.0; // Khởi tạo Amount bằng 0
        this.note = "";
        this.items = new ArrayList<>(); // Khởi tạo danh sách Items rỗng
    }

    // Constructor mặc định (cần thiết cho Gson/Jackson)
    public Order() {
        this.items = new ArrayList<>();
    }

    /**
     * Phương thức tính toán lại tổng tiền dựa trên các món ăn trong đơn hàng.
     */
//    public void calculateAmount() {
//        this.amount = this.items.stream()
//                .mapToDouble(item -> item.getQuantity() * item.getPricePerUnit())
//                .sum();
//    }

    // --- Getters và Setters ---

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    // ... (Thêm các Setters cho các trường khác nếu cần chỉnh sửa)

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        // Tốt hơn nên gọi calculateAmount() trước khi lấy giá trị
        return amount;
    }

    // Setter chỉ được sử dụng khi đọc từ JSON
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getNotes() {
        return note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}