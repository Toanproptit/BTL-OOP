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

public class LoginController {

    @FXML
    public Button choosePhoto;

    @FXML
    private ImageView backgroundImage;


    @FXML
    private Label lable1;

    @FXML
    private Label lable2;

    @FXML
    private Button login;

    @FXML
    private Button orderButton;

    @FXML
    private Button registerButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField usernameField;

    public void initialize() {
        // Tải ảnh nền cho màn hình này (stageId = "loginStage") khi mở ứng dụng
        String imagePath = BackgroundImageManager.loadBackgroundImageForStage("loginStage");

        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image newImage = new Image(imagePath);
                if (newImage != null && newImage.getWidth() > 0) {
                    backgroundImage.setImage(newImage);

                    // ✅ Cấu hình ImageView hiển thị đẹp
                    backgroundImage.setPreserveRatio(true);
                    backgroundImage.setSmooth(true);
                    backgroundImage.setCache(true);

                    // Co giãn ảnh vừa khung
                    backgroundImage.fitWidthProperty().bind(root.widthProperty());
                    backgroundImage.fitHeightProperty().bind(root.heightProperty());

                    // ✅ Căn giữa ảnh trong khung
                    backgroundImage.setX((root.getWidth() - backgroundImage.getFitWidth()) / 2);
                    backgroundImage.setY((root.getHeight() - backgroundImage.getFitHeight()) / 2);

                    // Lắng nghe khi kích thước thay đổi để giữ căn giữa
                    root.widthProperty().addListener((obs, oldVal, newVal) -> {
                        backgroundImage.setX((newVal.doubleValue() - backgroundImage.getFitWidth()) / 2);
                    });
                    root.heightProperty().addListener((obs, oldVal, newVal) -> {
                        backgroundImage.setY((newVal.doubleValue() - backgroundImage.getFitHeight()) / 2);
                    });

                } else {
                    System.out.println("Ảnh không hợp lệ hoặc không thể tải: " + imagePath);
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi tải ảnh nền: " + e.getMessage());
            }
        } else {
            System.out.println("Không tìm thấy đường dẫn ảnh nền cho stage 'loginStage'");
        }
    }





    @FXML
    public void handleChangeBackgroundImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                // 1. Lấy đường dẫn URI
                String imagePath = selectedFile.toURI().toString();

                // 2. TẠO đối tượng Image từ đường dẫn
                Image newImage = new Image(imagePath);

                if (newImage.isError()) {
                    // Nếu có lỗi, bạn có thể nhận thông báo chi tiết hơn
                    System.err.println("Lỗi tải ảnh: " + newImage.getException());
                    showAlert("Lỗi", "Không thể tải hoặc hiển thị ảnh hhuhu");
                    return; // Dừng lại nếu có lỗi
                }

                // 3. SET ảnh cho ImageView
                backgroundImage.setImage(newImage);

                // 4. Lưu đường dẫn (tùy chọn, giống như bạn đã làm)
                BackgroundImageManager.saveBackgroundImage("loginStage", imagePath);

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể lưu ảnh nền");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể tải hoặc hiển thị ảnh");
            }
        }
    }

    public void handleLogin(ActionEvent event) throws IOException{
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(validateLogin(username,password)){
            switchToDashBoard();
        }
        else {
            showAlert("Đăng nhập thất bại", "Sai tài khoản hoặc mật khẩu!");
        }
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
        Scene scene = new Scene(dashboard,1500,750);

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
        Scene scene = new Scene(parent,1500,750);
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Register.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Register-ProGastro");
        stage.show();
    }

    public void handleOrderNow(ActionEvent event) throws IOException{
        switchToOrderNow();
    }
    public void switchToOrderNow() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/progastro/CustomerOrderFood.fxml"));
        Parent parent =fxmlLoader.load();
        Stage stage = (Stage) registerButton.getScene().getWindow();
        Scene scene = new Scene(parent,800,600);
//        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Register.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Order Ngay!");
        stage.show();
    }

}

