package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Order;
import model.OrderJSON;
import java.time.LocalDate;

public class AddOrderFormController {

    @FXML
    private TextField orderIdField;
    @FXML
    private TextField customerField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> statusComboBox;

    private ManagerOrderController mainController; // để callback về form chính

    public void setMainController(ManagerOrderController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void handleAddOrder(ActionEvent event) {
        String orderId = orderIdField.getText();
        String customer = customerField.getText();
        LocalDate date = datePicker.getValue();
        String status = statusComboBox.getValue();

        if (orderId.isEmpty() || customer.isEmpty() || date == null || status == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin!");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        // Tạo đơn hàng mới
        Order order = new Order(orderId, customer, date, status);
        OrderJSON.addOrder(order);

        // Gọi hàm trong form chính để cập nhật bảng
        if (mainController != null) {
            mainController.refreshOrderTable();
            mainController.showSuccessMessage(" Thêm đơn hàng thành công!");
        }

        // Đóng form
        Stage stage = (Stage) orderIdField.getScene().getWindow();
        stage.close();
    }
}
