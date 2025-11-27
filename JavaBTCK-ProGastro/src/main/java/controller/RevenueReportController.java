package controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.Order;
import model.OrderItem;
import model.OrderJSON;
import model.OrderRow;

import java.io.IOException;
import java.util.*;


public class RevenueReportController {

    @FXML
    private Button backButton;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label totalOrdersLabel;

    @FXML
    private Label avgRevenueLabel;

    @FXML
    private Label topCategoryLabel;  // dùng cho tên món + phần trăm

    @FXML Label topCategoryPercentLabel;

    @FXML private LineChart<String, Number> revenueLineChart;

    @FXML private TableView<OrderRow> revenueTable;

    @FXML private TableColumn<OrderRow, String> colId;

    @FXML private TableColumn<OrderRow, String> colCategory;

    @FXML private TableColumn<OrderRow, String> colDate;

    @FXML private TableColumn<OrderRow, Double> colRevenue;

    @FXML private TableColumn<OrderRow, Double> colProfit;

    private Button activeButton;


    List<Order> orders = new ArrayList<>();

    @FXML
    public void initialize() throws IOException {
        loadData();
        updateDashboardStats();
        updateLineChart();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colRevenue.setCellValueFactory(new PropertyValueFactory<>("revenue"));
        colProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));
        loadTable();
    }

    private void loadData() throws IOException {
        orders = OrderJSON.loadOrders();
        if (orders == null) orders = new ArrayList<>();
    }

    private void updateDashboardStats() {

        // ----- Total Revenue -----
        double totalRevenue = orders.stream()
                .mapToDouble(Order::getCost)
                .sum();
        totalRevenueLabel.setText(String.format("%,.0f VND", totalRevenue));

        // ----- Total Orders -----
        int totalOrders = orders.size();
        totalOrdersLabel.setText(String.valueOf(totalOrders));

        // ----- Average Revenue per Month -----
        Map<Integer, Double> monthlyRevenue = new HashMap<>();
        for (Order o : orders) {
            int month = o.getDate().getMonthValue();
            monthlyRevenue.put(month, monthlyRevenue.getOrDefault(month, 0.0) + o.getCost());
        }
        double avgPerMonth = monthlyRevenue.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
        avgRevenueLabel.setText(String.format("%,.0f VND", avgPerMonth));

        // ----- Top Category (top-selling food) + % orders -----
        Map<String, Integer> foodCount = new HashMap<>();
        orders.forEach(o -> {
            o.getItems().forEach(item -> {
                String foodName = item.getName();
                foodCount.put(foodName, foodCount.getOrDefault(foodName, 0) + item.getQuantity());
            });
        });

        String topFood = foodCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Data");

        // tính tỉ lệ order có món Top Category
        long ordersWithTopFood = orders.stream()
                .filter(o -> o.getItems().stream()
                        .anyMatch(item -> item.getName().equalsIgnoreCase(topFood)))
                .count();
        double percentTopFoodOrders = totalOrders == 0 ? 0 : ((double) ordersWithTopFood / totalOrders) * 100;

        // hiển thị
        topCategoryLabel.setText("Top Category: " + topFood);
        topCategoryPercentLabel.setText("Top Category Percentage: " + String.format("%.2f", percentTopFoodOrders) + "%");
    }
    private void updateLineChart() {

        // chuẩn bị map cho 12 tháng
        Map<Integer, Double> monthlyRevenue = new HashMap<>();
        for (int i = 1; i <= 12; i++) monthlyRevenue.put(i, 0.0);

        // cộng doanh thu từng tháng
        for (Order o : orders) {
            int month = o.getDate().getMonthValue();
            monthlyRevenue.put(month, monthlyRevenue.get(month) + o.getCost());
        }

        // tạo series cho biểu đồ
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Revenue");

        // đưa vào biểu đồ theo thứ tự 1–12
        for (int month = 1; month <= 12; month++) {
            double value = monthlyRevenue.get(month);
            series.getData().add(new XYChart.Data<>(String.valueOf(month), value));
        }

        revenueLineChart.getData().clear();
        revenueLineChart.getData().add(series);
    }
    private void loadTable() {
        List<OrderRow> tableData = new ArrayList<>();

        for (Order order : orders) {

            // 1. Lấy thông tin cơ bản từ Order
            String id = order.getOrderId();
            String date = order.getDate().toString();
            double revenue = order.getCost();      // đã đọc từ JSON
            double profit = revenue * 0.4;

            // 2. Lấy CATEGORY từ OrderItem (name của món ăn)
            List<String> categories = order.getItems().stream()
                    .map(OrderItem::getName)        // <-- TÊN MÓN ĂN CHUẨN
                    .distinct()
                    .toList();

            // Nếu không có món thì đặt N/A
            String category = categories.isEmpty()
                    ? "N/A"
                    : String.join(", ", categories);

            // 3. Tạo dòng hoàn chỉnh đưa vào bảng
            tableData.add(new OrderRow(id, category, date, revenue, (double)Math.round(profit)));
        }

        // 4. Đổ dữ liệu ra TableView
        revenueTable.getItems().setAll(tableData);
    }
}