package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Account;
import model.AccountJSON;
import model.BackgroundImageManager;

import java.io.File;
import java.io.IOException;

public class RegisterController {

    @FXML
    private AnchorPane root;

    @FXML
    private Button backToLoginButton;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private GridPane account;

    @FXML
    private TextField accountField;

    @FXML
    private Text fullName;

    @FXML
    private TextField fullNameField;

    @FXML
    private Text password;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text passwordagain;

    @FXML
    private TextField phoneField;

    @FXML
    private Text phonenumber;

    @FXML
    private Button registerButton;

    public void initialize() {

        String imagePath = BackgroundImageManager.loadBackgroundImageForStage("Register");
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
                BackgroundImageManager.saveBackgroundImage("Register",imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể lưu ảnh nền");
            }
        }
    }


    @FXML
    public void handleRegister(ActionEvent event)throws IOException{
        String name = fullNameField.getText();
        String phone=phoneField.getText();
        String account = accountField.getText();
        String password= passwordField.getText();
        String password2 = confirmPasswordField.getText();
        if(password.equals(password2)){
            Account newAccount = new Account(name,phone,account,password);
            AccountJSON.addAccount(newAccount);
            showAlert("Thông báo","Đăng kí thành công");
        }
        else {
            showAlert("Thông báo","Mật khẩu không trùng khớp");
        }
    }
    @FXML
    public void handleLogin(ActionEvent event)throws IOException{switchToLogin();}

    @FXML
    public void switchToLogin() throws IOException {
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/org/example/progastro/Login.fxml"));
        Parent parent = fxmlLoader.load();
        Stage stage = (Stage) backToLoginButton.getScene().getWindow();
        Scene scene = new Scene(parent,800,600);
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
