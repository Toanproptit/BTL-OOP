package controller;

import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.BackgroundImageManager;
import model.Food;
import model.FoodStorageJSON;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class EditFoodController {
    @FXML
    private ImageView foodImageView;

    @FXML
    private Button back;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private Button sign;

    @FXML
    private Button eraseButton;

    @FXML
    private Button imagechoose;

    @FXML
    private Button imageerase;

    private Food food;

    @FXML
    public void handleSign(ActionEvent event)throws IOException{
        saveFood();
    }
    @FXML
    public void handleErase(ActionEvent event)throws IOException{
        eraseFood();
    }

    public void initialize() {
        // Tải ảnh nền cho màn hình này (stageId = "editFoodStage") khi mở ứng dụng
        String imagePath = BackgroundImageManager.loadBackgroundImageForStage("editFoodStage");
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
                BackgroundImageManager.saveBackgroundImage("editFoodStage",imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể lưu ảnh nền");
            }
        }
    }

//    @FXML
//    public void handleEraseFood

    @FXML
    public void handleChangeImage(MouseEvent event){
        FileChooser fileChoose = new FileChooser();
        fileChoose.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File","*.png","*.jpg","*.jpeg","*.gif"));
        File selectedFile = fileChoose.showOpenDialog(null);
        if(selectedFile!=null){
            String imagePath = selectedFile.toURI().toString();
            Image image = new Image(imagePath);
            foodImageView.setImage(image);
            try {
                saveFoodImage(selectedFile);
            }
            catch (IOException e){
                e.printStackTrace();
                showAlert("Lỗi","Không thể lưu ảnh món ăn");
            }
        }
    }



    private void saveFoodImage(File selectedFile) throws IOException{
        String folderPath = "images/foodImages/";
        File directory = new File(folderPath);
        if(!directory.exists()){
            directory.mkdir();
        }
        File destinationFile = new File(folderPath + selectedFile.getName());
        Files.copy(selectedFile.toPath(),destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        food.setImagePath(destinationFile.getAbsolutePath());
        FoodStorageJSON.updateFood(food);
        showAlert("Thông báo","Lưu ảnh thành công");
    }

    @FXML
    private void switchToManagefood() throws IOException {
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/org/example/progastro/Managefood.fxml"));
        Parent dashboard = fxmlLoader.load();
        Scene scene = new Scene(dashboard,800,600);
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Managefood.css").toExternalForm());
        Stage stage = (Stage) back.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("ManageFood - ProGastro");
        stage.show();
    }

    public void setFood(Food food) {
        this.food = food;
        nameField.setText(food.getName());
        descriptionField.setText(food.getDescription());
        priceField.setText(String.valueOf(food.getPrice()));
        if (food.getImagePath() != null) {
            Image image = new Image("file:" + food.getImagePath());  // Đường dẫn ảnh
            foodImageView.setImage(image);
        }
    }

    public void eraseFood() throws IOException{
        FoodStorageJSON.eraseFood(food);
        showAlert("Thông báo","Xóa Thành Công");
    }
    public void saveFood() throws IOException {
        food.setName(nameField.getText());
        food.setDescription(descriptionField.getText());
        try {
            double price = Double.parseDouble(priceField.getText());
            food.setPrice(price);
            FoodStorageJSON.updateFood(food);
            showAlert("Lưu thành công", "Success");
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Vui lòng nhập giá trị hợp lệ cho giá món ăn.");
        }
    }

    @FXML
    public void removeFoodImage(ActionEvent event)throws  IOException  {
        try {
            String imagePath = food.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                Path imagePathToDelete = Path.of(imagePath.replace("file:", ""));
                Files.delete(imagePathToDelete);
                food.setImagePath(null);
                FoodStorageJSON.updateFood(food);
                foodImageView.setImage(null);
                showAlert("Thông báo","Ảnh món ăn đã được xóa");
            } else {
                showAlert("Thông báo","Món ăn không có ảnh để xóa");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Thông báo","Lỗi khi xóa ảnh món ăn");
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