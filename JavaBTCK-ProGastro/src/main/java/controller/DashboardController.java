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
    private Label title;

    @FXML
    private AnchorPane contentArea;

    @FXML
    public Button orderButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button foodButton;

    @FXML
    private Button menuButton;
    @FXML
    private Button RevenueButton;
    @FXML
    private Button neworderButton;

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

    private Button currentSelectedButton;

    public void initialize() throws IOException {
//        updateSummaryStats();
        // Tải ảnh nền cho màn hình này (stageId = "editFoodStage") khi mở ứng dụng
        String imagePath = BackgroundImageManager.loadBackgroundImageForStage("dashBoard");
        if (!imagePath.isEmpty()) {
            root.setStyle("-fx-background-image: url('" + imagePath + "'); -fx-background-size: cover; -fx-background-position: center center;");
        }
        showHome();
        currentSelectedButton = homeButton;
        setActiveButton(homeButton);

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
        setActiveButton(foodButton);
    }
    @FXML
    public void handleorderButton(ActionEvent event) throws IOException{
        switchToManageOrderController();
        setActiveButton(orderButton);
    }

    @FXML
    public void handleMenuButoon(ActionEvent event) throws IOException {
        switchToMenuController();
        setActiveButton(menuButton);
    }

    @FXML
    public void handleHomeButton(ActionEvent event) throws IOException {
        showHome();
        setActiveButton(homeButton);
    }
    @FXML
    private void handleRevenueButton(ActionEvent event) throws IOException{
        switchToRevenueController();
        setActiveButton(RevenueButton);
    }
    @FXML
    private void handleNewOrderButton(ActionEvent event) throws IOException {
        switchToNewOrderController();
        setActiveButton(neworderButton);
    }
    private void showHome() throws IOException {
        Parent homeView = FXMLLoader.load(getClass().getResource("/org/example/progastro/Home.fxml"));
        contentArea.getChildren().setAll(homeView);
    }

    private void switchToMenuController() throws IOException {
        Parent menuView = FXMLLoader.load(getClass().getResource("/org/example/progastro/ListFood.fxml"));
        contentArea.getChildren().setAll(menuView);
    }
    private void switchToManageFoodController() throws IOException{
        Parent manageFoodView = FXMLLoader.load(getClass().getResource("/org/example/progastro/Managefood.fxml"));
        contentArea.getChildren().setAll(manageFoodView);
    }
    private void switchToManageOrderController() throws IOException{
        Parent manageOrder = FXMLLoader.load(getClass().getResource("/org/example/progastro/Manageorder.fxml"));
        contentArea.getChildren().setAll(manageOrder);
    }
    @FXML
    private void switchToLoginController(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/progastro/Login.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent,1500,750);
        Stage stage = (Stage) orderButton.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Login.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Login-ProGastro");
        stage.show();
    }
    private void switchToRevenueController() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/progastro/Revenue.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent,1500,750);
        Stage stage = (Stage) RevenueButton.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Login.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Revenue-ProGastro");
        stage.show();
    }
    private void switchToNewOrderController() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/progastro/newordercontroller.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent,1500,750);
        Stage stage = (Stage) neworderButton.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Login.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("order-ProGastro");
        stage.show();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setActiveButton(Button newSelectedButton) {
        if (currentSelectedButton != null) {
            // 1. Xóa class 'sidebar-button-selected' khỏi nút cũ
            currentSelectedButton.getStyleClass().remove("sidebar-button-selected");
        }

        // 2. Thêm class 'sidebar-button-selected' vào nút mới
        newSelectedButton.getStyleClass().add("sidebar-button-selected");

        title.setText(newSelectedButton.getText());
        // 3. Cập nhật nút đang chọn
        currentSelectedButton = newSelectedButton;
    }

}