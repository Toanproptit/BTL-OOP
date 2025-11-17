package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    private ImageView backgroundImage;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField accountField;

    @FXML
    private TextField fullNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneField;

    @FXML
    private Button registerButton;

    @FXML
    private Button choosePhoto;

    // ✅ Khi mở form đăng ký
    public void initialize() {
        // Tải ảnh nền cho stage này (registerStage)
        String imagePath = BackgroundImageManager.loadBackgroundImageForStage("registerStage");

        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image newImage = new Image(imagePath);
                if (!newImage.isError()) {
                    backgroundImage.setImage(newImage);
                } else {
                    System.out.println("Ảnh không hợp lệ hoặc không thể tải: " + imagePath);
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi tải ảnh nền: " + e.getMessage());
            }
        } else {
            System.out.println("Không tìm thấy đường dẫn ảnh nền cho stage 'registerStage'");
        }
    }

    // ✅ Chọn ảnh nền mới
    @FXML
    public void handleChangeBackgroundImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String imagePath = selectedFile.toURI().toString();
                Image newImage = new Image(imagePath);

                if (newImage.isError()) {
                    System.err.println("Lỗi tải ảnh: " + newImage.getException());
                    showAlert("Lỗi", "Không thể tải hoặc hiển thị ảnh.");
                    return;
                }

                backgroundImage.setImage(newImage);
                BackgroundImageManager.saveBackgroundImage("registerStage", imagePath);

            } catch (IOException e) {
                showAlert("Lỗi", "Không thể lưu ảnh nền.");
            } catch (Exception e) {
                showAlert("Lỗi", "Không thể tải hoặc hiển thị ảnh.");
            }
        }
    }

    // ✅ Xử lý đăng ký tài khoản
    @FXML
    public void handleRegister(ActionEvent event) throws IOException {
        String name = fullNameField.getText();
        String phone = phoneField.getText();
        String account = accountField.getText();
        String password = passwordField.getText();
        String password2 = confirmPasswordField.getText();

        if (name.isEmpty() || phone.isEmpty() || account.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            showAlert("Thông báo", "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!password.equals(password2)) {
            showAlert("Thông báo", "Mật khẩu không trùng khớp!");
            return;
        }

        Account newAccount = new Account(name, phone, account, password);
        AccountJSON.addAccount(newAccount);

        showAlert("Thông báo", "Đăng ký thành công!");
        switchToLogin();
    }

    // ✅ Quay về màn hình đăng nhập
    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        switchToLogin();
    }

    public void switchToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/progastro/Login.fxml"));
        Parent parent = fxmlLoader.load();
        Stage stage = (Stage) backToLoginButton.getScene().getWindow();
        Scene scene = new Scene(parent, 1500, 750);
        stage.setScene(scene);
        stage.setTitle("Login - ProGastro");
        stage.show();
    }

    // ✅ Hộp thoại thông báo tiện lợi
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
