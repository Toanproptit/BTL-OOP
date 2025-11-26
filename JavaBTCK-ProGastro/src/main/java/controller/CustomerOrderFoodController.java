package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;


public class CustomerOrderFoodController {
    @FXML
    private Button backButton;

    @FXML
    private Button placeorderButton;

    @FXML
    private TableView<OrderItem> tableViewOrders;

    @FXML
    private TextField orderIdField;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField cusNoteField;

    @FXML
    private TextField quantityField;

    @FXML
    private TableColumn<OrderItem, String> colFoodName;

    @FXML
    private TableColumn<OrderItem, Double> colPrice;

    @FXML
    private TableColumn<OrderItem, Integer> colQuantity;

    @FXML
    private AnchorPane root;

    @FXML
    private FlowPane foodFlow;

    @FXML
    private TextField searchField;

    @FXML
    private ChoiceBox<String> sortChoice;

    @FXML
    private Label lblTotalCost;

    private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();

    private List<Order> orderList = new ArrayList<>();

    @FXML
    public void initialize() {
        setupSortOptions();
        loadFoodList();
        loadOrder();
        tableViewOrders.setPlaceholder(new Label("Chưa có món nào trong đơn hàng"));
        colFoodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableViewOrders.setItems(orderItems);
    }

    private void setupSortOptions() {
        sortChoice.getItems().addAll(
                "Default",
                "Price: Low to High",
                "Price: High to Low"
        );

        sortChoice.setValue("Default");

        sortChoice.setOnAction(e -> loadFoodList());
    }


    private void loadOrder(){
        orderList = OrderJSON.getOrderList();
    }

    private void loadFoodList() {
        foodFlow.getChildren().clear();

        List<Food> listFoods = FoodStorageJSON.getFoodList();

        // --- Sorting ---
        switch (sortChoice.getValue()) {

            case "Price: Low to High":
                listFoods.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
                break;

            case "Price: High to Low":
                listFoods.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
                break;

            case "Default":
            default:
                // Không làm gì
                break;
        }

        // --- Rendering Cards ---
        for (Food food : listFoods) {
            addFoodCard(food);
        }
    }

    /** ========================== ADD CARD ========================== */
    private void addFoodCard(Food food) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/progastro/FoodCard.fxml"));
            Parent card = loader.load();

            FoodCardController cardController = loader.getController();
            cardController.setFoodData(food);
            // Truyền controller cha vào card con
            cardController.setParentController(this);

            foodFlow.getChildren().add(card);

        } catch (IOException e) {
            System.out.println("Lỗi load card: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** ========================== SEARCH ========================== */
    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase().trim();

        foodFlow.getChildren().clear();

        List<Food> listFoods = FoodStorageJSON.getFoodList();

        for (Food food : listFoods) {

            if (food.getName().toLowerCase().contains(keyword) ||
                    food.getDescription().toLowerCase().contains(keyword)) {

                addFoodCard(food);
            }
        }
    }

    private void updateTotalAmount() {
        double total = 0.0;
        for (OrderItem item : orderItems) {
            total += item.getPrice() * item.getQuantity();
        }

        if (lblTotalCost != null) {
            lblTotalCost.setText(String.format("%.1fVND", total));
        }
    }

    @FXML
    public void handleAddOrder(ActionEvent event) {

        String orderId = orderIdField.getText();
        String customer = customerNameField.getText();
        String note = cusNoteField.getText();
        double cost = Double.parseDouble(lblTotalCost.getText().replace("VND", "").trim());
        LocalDate date = LocalDate.now();
        String status = "On Process";

        if (orderId.isEmpty() || customer.isEmpty() || date == null || status == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin!");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        if (orderItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn món!");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }



        Order order = new Order(orderId, customer, date, status);

        for(Order order1 : orderList){
            if(order.getOrderId() .equals(order1.getOrderId()) ){
                Alert alert = new Alert(Alert.AlertType.ERROR,"Đã tồn tại Order , vui lòng nhập Id khác");
                alert.setHeaderText(null);
                alert.showAndWait();
                return;
            }
        }
        order.setItems(orderItems);
        order.setCost(cost);

        if (note != null){
            order.setNote(note);
        }


        OrderJSON.addOrder(order);
        showAlert("Thông báo", "Đơn đã được đặt!");

    }

    public void addFoodToOrder(Food food) {
        // Nếu món đã có → tăng quantity
        for (OrderItem item : orderItems) {
            if (item.getName().equals(food.getName())) {
                item.setQuantity(item.getQuantity() + 1);
                tableViewOrders.refresh(); // cập nhật lại bảng
                updateTotalAmount();
                return;
            }
        }

        // Nếu món chưa có → thêm mới
        OrderItem newItem = new OrderItem(food, 1);
        orderItems.add(newItem);
        updateTotalAmount();
    }

    @FXML
    private void handleDeleteItem(ActionEvent event) {
        OrderItem selectedItem = tableViewOrders.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            orderItems.remove(selectedItem); // Chỉ cần xóa ở đây
            tableViewOrders.refresh();
            updateTotalAmount();
            showAlert("Thông báo", "Đã Xóa Món ăn");
        } else {
            showAlert("Lỗi", "Vui Lòng chọn món ăn cần xóa");
        }
    }

    @FXML
    void handleEditQuantity(ActionEvent event) {
        OrderItem selectedOrderItem = tableViewOrders.getSelectionModel().getSelectedItem();
        if (selectedOrderItem != null) {
            try {
                int newQuantity = Integer.parseInt(quantityField.getText());
                if (newQuantity > 0) {
                    selectedOrderItem.setQuantity(newQuantity);
                    int index = orderItems.indexOf(selectedOrderItem);
                    if (index != -1) {
                        orderItems.set(index, selectedOrderItem);
                        tableViewOrders.refresh();
                        updateTotalAmount();
                        showAlert("Thông báo", "Số lượng món ăn đã được cập nhật.");
                    }
                } else {
                    showAlert("Lỗi", "Số lượng phải lớn hơn 0.");
                }
            } catch (NumberFormatException e) {
                showAlert("Lỗi", "Số lượng phải là một số hợp lệ.");
            }
        } else {
            showAlert("Lỗi", "Vui lòng chọn món ăn cần sửa.");
        }
    }

    @FXML
    void switchToLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/org/example/progastro/Login.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent,1500,750);
        Stage stage = (Stage) backButton.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Login.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}