package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DashboardController {
    @FXML
    private AnchorPane root;

    @FXML
    public Button orderButton;

    @FXML
    private Button foodButton;

    @FXML
    private Button menu;

    @FXML
    private Label lable1;

    @FXML
    private Label lable2;

    @FXML
    private TableView<?> tableView;

    @FXML
    private TextField text1;

    @FXML
    private TextField text2;

    @FXML
    private Label revenueTodayLabel, activeTablesLabel, totalInvoicesLabel, totalDishesLabel;


    public void initialize() throws IOException {
        updateSummaryStats();
        // Tải ảnh nền cho màn hình này (stageId = "editFoodStage") khi mở ứng dụng
        String imagePath = BackgroundImageManager.loadBackgroundImageForStage("dashBoard");
        if (!imagePath.isEmpty()) {
            root.setStyle("-fx-background-image: url('" + imagePath + "'); -fx-background-size: cover; -fx-background-position: center center;");
        }
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
        List<Table> tables = TableJSON.loadTable();
        long activeTables = tables.stream()
                .filter(t -> "Đang sử dụng".equalsIgnoreCase(t.getStatus()))
                .count();
        activeTablesLabel.setText(activeTables + " bàn");

        // 4. Tổng món ăn
        try {
            List<Food> foods = FoodStorageJSON.loadFoods();
            totalDishesLabel.setText(foods.size() + " món");
        } catch (IOException e) {
            totalDishesLabel.setText("Lỗi đọc file");
        }
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
                BackgroundImageManager.saveBackgroundImage("dashBoard",imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể lưu ảnh nền");
            }
        }
    }

    @FXML
    public void handlefoodButton(ActionEvent event) throws IOException{
        switchToManageFoodController();
    }
    @FXML
    public void handleorderButton(ActionEvent event) throws IOException{
        switchToManageOrderController();
    }

    @FXML
    public void handleMenuButoon(ActionEvent event) throws IOException {
        switchToMenuController();
    }

    private void switchToMenuController() throws IOException {
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/org/example/progastro/ListFood.fxml"));
        Stage stage = (Stage) foodButton.getScene().getWindow();
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent,800,600);
//        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Managefood.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ListFood-ProGastro");
        stage.show();
    }
    private void switchToManageFoodController() throws IOException{
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/org/example/progastro/Managefood.fxml"));
        Stage stage = (Stage) foodButton.getScene().getWindow();
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent,800,600);
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Managefood.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Managefood-ProGastro");
        stage.show();
    }
    private void switchToManageOrderController() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/progastro/Manageorder.fxml"));
        System.out.println("FXML URL = " + fxmlLoader);
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent,800,600);
        Stage stage = (Stage) orderButton.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/manageorder.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Manageorder-ProGastro");
        stage.show();
    }
    @FXML
    private void switchToLoginController(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/progastro/Login.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent,800,600);
        Stage stage = (Stage) orderButton.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Login.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Login-ProGastro");
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