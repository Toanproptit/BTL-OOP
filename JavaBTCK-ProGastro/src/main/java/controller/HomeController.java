package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import model.*;

import java.io.IOException;
import java.util.List;

public class HomeController {
    @FXML
    private Label activeTablesLabel;

    @FXML
    private AnchorPane contentArea;

    @FXML
    private HBox hbox;

    @FXML
    private Label header;

    @FXML
    private Label revenueTodayLabel;

    @FXML
    private Label totalDishesLabel;

    @FXML
    private Label totalInvoicesLabel;

    public void initialize() throws IOException {
        updateSummaryStats();
    }

    private void updateSummaryStats() throws IOException {
        // 1. Tổng doanh thu hôm nay
        List<Invoice> invoices = InvoiceStorageJSON.loadInvoices();
        String today = java.time.LocalDate.now().toString(); // định dạng yyyy-MM-dd

        double revenueToday = invoices.stream()
                .filter(inv -> inv.getCreatedAt().startsWith(today))
                .mapToDouble(Invoice::getTotalPrice)
                .sum();
        revenueTodayLabel.setText(String.format("%,.0f VND", revenueToday));

        // 2. Tổng số hóa đơn
        totalInvoicesLabel.setText(invoices.size() + " hóa đơn");

        // 3. Bàn đang sử dụng
        List<Order> tables = OrderJSON.loadOrders();
        long activeTables = tables.stream()
                .filter(t -> "Đang sử dụng".equalsIgnoreCase(t.getStatus()))
                .count();
        activeTablesLabel.setText(activeTables + " bàn");

        // 4. Tổng món ăn
        List<Food> foods = FoodStorageJSON.loadFoods();
        totalDishesLabel.setText(foods.size() + " món");
    }

}
