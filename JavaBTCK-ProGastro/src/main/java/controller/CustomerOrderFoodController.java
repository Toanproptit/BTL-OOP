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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javafx.scene.control.Alert;


public class CustomerOrderFoodController {
    @FXML
    private Button addFood;

    @FXML
    private Button backButton;

    @FXML
    private Button btnDeleteItem;

    @FXML
    private Button btnEdit;

    @FXML
    private Button invoice;

    @FXML
    private TableColumn<OrderItem, String> colFoodName;

    @FXML
    private TableColumn<OrderItem, Double> colPrice;

    @FXML
    private TableColumn<OrderItem, Integer> colQuantity;

    @FXML
    private ComboBox<Food> comboMenu;

    @FXML
    private ComboBox<Table> comboTable;

    @FXML
    private TableView<OrderItem> tableViewOrders;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField total;

    @FXML
    private Button DatBan;

    @FXML
    private AnchorPane root;

    private boolean tablechoosed = false;
    private Table table;
    private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();

    public void initialize() throws IOException {
        String imagePath = BackgroundImageManager.loadBackgroundImageForStage("editTable");
        if (!imagePath.isEmpty()) {
            root.setStyle("-fx-background-image: url('" + imagePath + "'); -fx-background-size: cover; -fx-background-position: center center;");
        }
        // Gán dữ liệu vào ComboBox (menu)
        comboMenu.getItems().addAll(FoodStorageJSON.loadFoods());
        // Gán dữ liệu vào ComboBox (table)
        List<Table> tables = TableJSON.loadTable();
        for (Table table : tables) {
            if (table.getStatus().equals("Trống"))
                comboTable.getItems().add(table);
        }
        comboTable.setPromptText("Chọn bàn");
        comboTable.setDisable(false);
        // Gán các cột trong TableView
        colFoodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableViewOrders.setItems(orderItems);

    }

    @FXML
    public void handleChangeBackgroundImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();

            root.setStyle("-fx-background-image: url('" + imagePath + "'); -fx-background-size: cover; -fx-background-position: center center;");
            try {
                BackgroundImageManager.saveBackgroundImage("editTable",imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể lưu ảnh nền");
            }
        }
    }

    public void setTable(Table table) {
        this.table = table;
        orderItems = FXCollections.observableArrayList(table.getOrderItem());
        tableViewOrders.setItems(orderItems);
        total.setText(String.valueOf(table.getTotalPrice()));
    }


    @FXML
    public void handleAddItem(ActionEvent event) throws IOException {
        Food selectedFood = comboMenu.getSelectionModel().getSelectedItem();
        String quantityText = txtQuantity.getText();

        if (tablechoosed){
            if (selectedFood != null && !quantityText.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(quantityText);

                    if (quantity > 0) {

                        OrderItem newOrderItem = new OrderItem(selectedFood, quantity);
                        orderItems.add(newOrderItem);
                        table.setOrderFood(orderItems);
                        table.calculateTotalPrice();
                        total.setText(String.valueOf(table.getTotalPrice()));
                        TableJSON.updateTable(table);
                        tableViewOrders.setItems(orderItems);
                        showAlert("Thành công", "Đã thêm món ăn vào đơn hàng!");

                    } else {
                        showAlert("Lỗi", "Số lượng phải lớn hơn 0.");
                    }
                } catch (NumberFormatException e) {
                    showAlert("Lỗi", "Vui lòng nhập số hợp lệ cho số lượng.");
                }
            } else {
                showAlert("Lỗi", "Vui lòng chọn món ăn và nhập số lượng.");
            }
        }
        else showAlert("Lỗi","Vui lòng chọn bàn.");
    }

    @FXML
    void switchToLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/org/example/progastro/Login.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent,800,600);
        Stage stage = (Stage) backButton.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Login.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    @FXML
    public void exportInvoiceToTXT(ActionEvent event) throws IOException {
        // ✅ Kiểm tra nếu đã xuất hóa đơn cho bàn này
        if (InvoiceStorageJSON.isInvoiceAlreadyExists(table.getName())) {
            showAlert("Cảnh báo", "Bàn này đã được xuất hóa đơn rồi!");
            return;
        }

        try {
            File file = new File("Invoice.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write("Hóa Đơn\n");
            writer.write("Ngày xuất hóa đơn: " +
                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            writer.write("====================================\n");

            for (OrderItem orderItem : tableViewOrders.getItems()) {
                writer.write("Tên Món: " + orderItem.getName() + "\n");
                writer.write("Số Lượng: " + orderItem.getQuantity() + "\n");
                writer.write("Giá: " + orderItem.getPrice() + "\n");
                writer.write("====================================\n");
            }

            writer.write("Tổng Tiền: " + table.getTotalPrice());
            writer.close();

            // ➕ Ghi vào file JSON
            saveInvoiceToJson(table.getName(), table.getTotalPrice());

            showAlert("Thông báo", "Hóa đơn đã được xuất thành công.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể xuất hóa đơn.");
        }
    }


    private void saveInvoiceToJson(String tableName, double totalPrice) throws IOException {
        String currentTime = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Invoice invoice = new Invoice(tableName, orderItems, totalPrice, currentTime);
        InvoiceStorageJSON.addInvoice(invoice);
    }

    @FXML
    private void handleDeleteItem(ActionEvent event) {
        OrderItem selectedItem = tableViewOrders.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            orderItems.remove(selectedItem); // Chỉ cần xóa ở đây
            table.setOrderFood(orderItems);
            table.calculateTotalPrice();
            tableViewOrders.refresh();
            TableJSON.updateTable(table);
            total.setText(String.valueOf(table.getTotalPrice()));
            showAlert("Thông báo", "Đã Xóa Món ăn");
        } else {
            showAlert("Lỗi", "Vui Lòng chọn món ăn cần xóa");
        }
    }

    @FXML
    void handleEditItem(ActionEvent event) {
        OrderItem selectedOrderItem = tableViewOrders.getSelectionModel().getSelectedItem();
        if (selectedOrderItem != null) {
            try {
                int newQuantity = Integer.parseInt(quantityField.getText());
                if (newQuantity > 0) {
                    selectedOrderItem.setQuantity(newQuantity);
                    int index = orderItems.indexOf(selectedOrderItem);
                    if (index != -1) {
                        orderItems.set(index, selectedOrderItem);
                        table.setOrderFood(orderItems);
                        table.calculateTotalPrice();
                        tableViewOrders.refresh();
                        TableJSON.updateTable(table);
                        total.setText(String.valueOf(table.getTotalPrice()));
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
    public void handleChooseTable(ActionEvent event) throws IOException {
        Table selectedTable = comboTable.getSelectionModel().getSelectedItem();

        if (selectedTable != null) {
            selectedTable.setStatus("Đang sử dụng");
            TableJSON.updateTable(selectedTable);
            setTable(selectedTable);
            tablechoosed = true;
            comboTable.setDisable(true);
            showAlert("Thông báo", "Đã chọn bàn: " + selectedTable.getName());
        } else {
            showAlert("Lỗi", "Vui lòng chọn bàn trước khi đặt.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}