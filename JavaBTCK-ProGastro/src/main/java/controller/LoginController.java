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
import model.Account;
import model.AccountJSON;
import model.BackgroundImageManager;

import java.io.File;
import java.io.IOException;

public class LoginController {

    @FXML
    public Button choosePhoto;

    @FXML
    private Label lable1;

    @FXML
    private Label lable2;

    @FXML
    private Button login;

    @FXML
    private Button registerButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField usernameField;

    public void initialize() {
        AccountJSON.getAccountList();
        // Tải ảnh nền cho màn hình này (stageId = "editFoodStage") khi mở ứng dụng
        String imagePath = BackgroundImageManager.loadBackgroundImageForStage("loginStage");
        if (!imagePath.isEmpty()) {
            root.setStyle("-fx-background-image: url('" + imagePath + "'); -fx-background-size: cover; -fx-background-position: center center;");
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
                BackgroundImageManager.saveBackgroundImage("loginStage",imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể lưu ảnh nền");
            }
        }
    }

    public void handleLogin(ActionEvent event) throws IOException{
        String username = usernameField.getText();
        String password = passwordField.getText();


        Account acc = AccountJSON.findAccount(username, password);
        if (acc == null) {
            showAlert("Đăng nhập thất bại", "Sai tài khoản hoặc mật khẩu!");
            return;
        }
        model.SessionManager.setCurrentUser(acc);
        switchToDashBoard();
    }

    public boolean validateLogin(String username, String password) {

        for(int i=0;i< AccountJSON.getAccountList().size();++i){
            if (username.equals(AccountJSON.getAccountList().get(i).getAccount()) &&
                    password.equals(AccountJSON.getAccountList().get(i).getPassword())) {
                return true;
            }
        }
        return false;
    }
    public void switchToDashBoard() throws IOException{
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/org/example/progastro/Dashboard.fxml"));
        Parent dashboard = fxmlLoader.load();
        Scene scene = new Scene(dashboard,900,600);
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Dashboard.css").toExternalForm());
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Dashboard - ProGastro");
        stage.show();
    }
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void handleRegister(ActionEvent event) throws IOException{
        switchToRegister();
    }
    public void switchToRegister() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/progastro/Register.fxml"));
        Parent parent =fxmlLoader.load();
        Stage stage = (Stage) registerButton.getScene().getWindow();
        Scene scene = new Scene(parent,800,600);
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Register.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Register-ProGastro");
        stage.show();
    }
}

