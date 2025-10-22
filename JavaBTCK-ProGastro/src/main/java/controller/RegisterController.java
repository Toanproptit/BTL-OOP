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
import java.util.List;

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

        if (name.isEmpty() || name.length() < 3) {
            showAlert("Lỗi nhập liệu", "Họ tên phải có ít nhất 3 ký tự!");
            return;
        }
        if (!phone.matches("^0\\d{9}$")) {
            showAlert("Lỗi nhập liệu", "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0!");
            return;
        }
        if (account.isEmpty() || account.length() < 4) {
            showAlert("Lỗi nhập liệu", "Tên tài khoản phải có ít nhất 4 ký tự!");
            return;
        }
        List<Account> accounts = AccountJSON.getAccountList();
        for (Account acc : accounts) {
            if (acc.getAccount().equalsIgnoreCase(account)) {
                showAlert("Lỗi đăng ký", "Tên tài khoản này đã tồn tại, vui lòng chọn tên khác!");
                return;
            }
        }
        if (password.length() < 6) {
            showAlert("Lỗi nhập liệu", "Mật khẩu phải có ít nhất 6 ký tự!");
            return;
        }

        if (!password.equals(password2)) {
            showAlert("Lỗi nhập liệu", "Mật khẩu xác nhận không khớp!");
            return;
        }
        Account newAccount = new Account(name, phone, account, password, Account.Role.STAFF);
        AccountJSON.addAccount(newAccount);

        showAlert("Thành công", "Đăng ký tài khoản thành công!\nBạn có thể đăng nhập ngay bây giờ.");
        switchToLogin();
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
