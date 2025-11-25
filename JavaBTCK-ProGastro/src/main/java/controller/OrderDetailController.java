package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.*;

import java.io.IOException;

public class OrderDetailController {


    @FXML
    private Label orderIdLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label customerLabel;
    @FXML
    private Label costLabel;
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
            costLabel.setText(String.format("%.2f", selectedOrder.getCost()));
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

    @FXML
    public void handleInvoice(ActionEvent event) {
        double cost = 0;
        for(OrderItem orderItem : currentOrder.getItems()){
            cost+= orderItem.getPrice() * orderItem.getQuantity();
        }
        Invoice invoice = new Invoice(currentOrder.getOrderId(),currentOrder.getCustomer(),currentOrder.getDate().toString(),currentOrder.getStatus(),currentOrder.getNote(),currentOrder.getItems(),cost);

        System.out.println(invoice.generateInvoiceText());

        InvoiceStorageJSON.addInvoice(invoice);
        invoice.saveToTXT();
        invoice.saveToExcel();
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Đã xuất excel và txt  và thành công");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}
