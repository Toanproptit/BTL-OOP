package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Order;
import model.OrderJSON;

import java.io.IOException;

public class OrderDetailController {


    @FXML
    private Label orderIdLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label customerLabel;
    @FXML
    private Label amountLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private TextArea notesArea;

    private Order currentOrder;

    private DashboardController dashboardController;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/progastro/ManagerOrder.fxml"));
            Parent orderList = loader.load();

            // Truyền lại dashboardController vào ManageOrder khi quay lại
            ManagerOrderController manageorderController = loader.getController();
            manageorderController.setDashboardController(dashboardController);

            dashboardController.setContent(orderList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOrder(Order selectedOrder) {
        this.currentOrder = selectedOrder;

        if (selectedOrder != null) {
            orderIdLabel.setText(selectedOrder.getOrderId());
            dateLabel.setText(selectedOrder.getDate().toString());
            customerLabel.setText(selectedOrder.getCustomer());
            amountLabel.setText(String.format("%.2f", selectedOrder.getAmount()));
            statusLabel.setText(selectedOrder.getStatus());
            notesArea.setText(selectedOrder.getNotes() != null ? selectedOrder.getNotes() : "");
        }
    }

    public void handleSave(ActionEvent event) {
        if (currentOrder == null) return;
        String newNotes = notesArea.getText();
        currentOrder.setNote(newNotes);
        OrderJSON.updateOrder(currentOrder);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Updated note successfully!");
        alert.showAndWait();
    }
}
