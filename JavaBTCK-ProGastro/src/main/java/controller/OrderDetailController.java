package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import model.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.FoodStorageJSON.getFoodList;

public class OrderDetailController {

    @FXML private Label orderIdLabel;
    @FXML private Label dateLabel;
    @FXML private Label customerLabel;
    @FXML private Label amountLabel;
    @FXML private Label statusLabel;
    @FXML private TextArea notesArea;

    @FXML private ChoiceBox<String> foodChoiceBox;
    @FXML private TextField quantityField;

    private Order currentOrder;
    private DashboardController dashboardController;

    // Menu đọc từ JSON
    private Map<String, Double> menu = new HashMap<>();

    private double totalAmount = 0.0;

    @FXML
    public void initialize() {

        // === Load menu from food.json ===
        List<Food> foodList = FoodStorageJSON.getFoodList();
        for (Food f : foodList) {
            menu.put(f.getName(), f.getPrice());
        }

        foodChoiceBox.getItems().addAll(menu.keySet());
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/progastro/ManagerOrder.fxml"));
            Parent orderList = loader.load();

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

            totalAmount = selectedOrder.getAmount();
        }
    }

    // =============== ADD FOOD ITEM ===============
    @FXML
    private void handleAddItem() {
        String food = foodChoiceBox.getValue();
        String qtyText = quantityField.getText();

        if (food == null || qtyText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please choose food and enter quantity!");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyText);
            if (qty <= 0) throw new Exception();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Quantity must be a valid number!");
            return;
        }

        double price = menu.get(food);
        double cost = price * qty;

        totalAmount += cost;
        amountLabel.setText(String.format("%.2f", totalAmount));

        currentOrder.setAmount(totalAmount);

        quantityField.clear();
        foodChoiceBox.getSelectionModel().clearSelection();
    }

    // =============== SAVE ORDER ===============
    @FXML
    public void handleSave(ActionEvent event) {
        if (currentOrder == null) return;

        String newNotes = notesArea.getText();
        currentOrder.setNote(newNotes);
        currentOrder.setAmount(totalAmount);

        OrderJSON.updateOrder(currentOrder);

        showAlert(Alert.AlertType.INFORMATION, "Updated successfully!");
    }

    // Helper: Show alert
    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
