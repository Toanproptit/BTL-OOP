package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import model.Order; // IMPORT MỚI
import model.OrderJSON; // IMPORT MỚI

import java.io.IOException;
import java.time.LocalDate; // Cần thiết cho cột Date

public class ManagerOrderController {


    @FXML
    private Text tatalOrders;

    @FXML
    private Text canceled;

    @FXML
    private Text onProcess;

    @FXML
    private Text completed;



    // --- CÁC TRƯỜNG ĐÃ ĐỔI TỪ TABLE SANG ORDER ---
    @FXML private TableColumn<Order, String> orderIdCol; // Đã đổi tên và kiểu dữ liệu
    @FXML private TableColumn<Order, LocalDate> dateCol; // Cột mới cho Date
    @FXML private TableColumn<Order, String> customerCol; // Đổi kiểu dữ liệu
    @FXML private TableColumn<Order, Double> costCol; // Cột mới cho Amount (Double)
    @FXML private TableColumn<Order, String> statusCol; // Đã đổi tên
    @FXML private TableView<Order> ordersTable; // Đã đổi tên và kiểu dữ liệu



    // Đã đổi kiểu dữ liệu từ Table sang Order
    private ObservableList<Order> orderList = FXCollections.observableArrayList();

    // Đã đổi tên Button từ addTable thành buttonAdd trong FXML của bạn
    @FXML private Button addTable;

    private DashboardController dashboardController;

    @FXML
    public void initialize() {
        try {

            // 1. Tải dữ liệu từ JSON (sử dụng OrderJSON)
            orderList = FXCollections.observableArrayList(OrderJSON.loadOrders());

            // 2. Thiết lập CellValueFactory (Quan trọng: Tên thuộc tính phải khớp với Getters trong Order.java)
            orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
            customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
            statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date")); // Lấy từ LocalDate
            costCol.setCellValueFactory(new PropertyValueFactory<>("cost")); // Lấy từ double

            // 3. Thiết lập TableView
            ordersTable.setItems(orderList);
//            ordersTable.setOnMouseClicked(this::handleOrderDoubleClick); // Đổi tên handler

            refreshOrderTable();



        } catch (Exception e) { // Bắt Exception chung để đảm bảo khởi tạo không bị crash
            e.printStackTrace();
            showAlert("Lỗi Khởi Tạo", "Không thể tải dữ liệu đơn hàng hoặc thiết lập bảng.");
        }
    }

    public void addOrderToTable(Order newOrder) {
        orderList.add(0, newOrder); // Thêm vào đầu ObservableList để hiển thị ngay
        // Lưu ý: OrderJSON.addOrder(newOrder) đã được gọi trong AddOrderFormController
    }


    // --- LOGIC MỞ FORM THÊM ĐƠN HÀNG ---
    @FXML
    public void handleAddOrder(ActionEvent event) throws IOException { // Đã đổi tên phương thức
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/progastro/AddOrderForm.fxml"));
        Parent root = loader.load();

        // Lấy Controller của Form thêm đơn hàng
        AddOrderFormController controller = loader.getController();

        // Thiết lập tham chiếu ngược (RẤT QUAN TRỌNG)
        controller.setMainController(this);

        // Tạo Stage mới và mở cửa sổ
        Stage stage = new Stage();
        stage.setTitle("Thêm Đơn Hàng Mới");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác màn hình chính
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.showAndWait();
    }




    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }


    public void refreshOrderTable() {
        ordersTable.setItems(FXCollections.observableArrayList(OrderJSON.getOrderList()));
        updateOrderStats();
    }


    private void updateOrderStats() {
        int total = OrderJSON.getOrderList().size();
        int onProc = 0;
        int comp = 0;
        int canc = 0;

        for (Order order : OrderJSON.getOrderList()) {
            String status = order.getStatus();
            if (status == null) continue;

            switch (status.toLowerCase()) {
                case "on process":
                    onProc++;
                    break;
                case "completed":
                    comp++;
                    break;
                case "canceled":
                    canc++;
                    break;
            }
        }

        // Gán vào các Text trong giao diện
        tatalOrders.setText(String.valueOf(total));
        onProcess.setText(String.valueOf(onProc));
        completed.setText(String.valueOf(comp));
        canceled.setText(String.valueOf(canc));
    }

    // --- LOGIC DOUBLE CLICK (MỞ CHI TIẾT ĐƠN HÀNG) ---
    @FXML
    private void handleOrderDoubleClick(MouseEvent event) { // Đã đổi tên handler
        if (event.getClickCount() == 2) {
            Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                openOrderDetailView(selectedOrder);
            }
        }
    }

    private void openOrderDetailView(Order selectedOrder) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/progastro/DetailOrder.fxml"));
            Parent detailView = loader.load();

            // Lấy controller của DetailOrder.fxml
            OrderDetailController detailController = loader.getController();
            detailController.setOrder(selectedOrder);

            // Cho phép quay lại danh sách đơn hàng
            detailController.setDashboardController(dashboardController);

            // Gọi Dashboard để thay nội dung hiển thị
            if (dashboardController != null) {
                dashboardController.setContent(detailView);
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở chi tiết đơn hàng!");
        }
    }


    // --- CÁC PHƯƠNG THỨC KHÁC (Giữ nguyên hoặc đã sửa tên) ---

    public void showSuccessMessage(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Giữ nguyên logic đổi ảnh nền
    public void handleChangeBackgroundImage(MouseEvent event) {
        // ... (Logic giữ nguyên) ...
    }

    @FXML
    private void switchToDashBoard(ActionEvent event) throws IOException {
        // ... (Logic giữ nguyên) ...
    }

    private void showAlert(String title, String message) {
        // ... (Logic giữ nguyên) ...
    }
}