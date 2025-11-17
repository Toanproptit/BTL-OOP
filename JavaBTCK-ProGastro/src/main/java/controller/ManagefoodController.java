package controller;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.BackgroundImageManager;
import model.Food;
import model.FoodStorageJSON;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ManageFoodController {

    @FXML private AnchorPane root;

    // --- FORM FIELDS ---
    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private TextField priceField;

    @FXML private ImageView foodImageView;

    @FXML private Button addNewButton;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;
    @FXML private Button chooseImageButton;
    @FXML private Button deleteImageButton;

    // --- TABLE ---
    @FXML private TableView<Food> foodTable;
    @FXML private TableColumn<Food, String> nameColumn;
    @FXML private TableColumn<Food, String> descriptionColumn;
    @FXML private TableColumn<Food, Double> priceColumn;

    // --- DATA ---
    private ObservableList<Food> foods = FXCollections.observableArrayList();
    private boolean isAddingNew = true;
    private Food editingFood = null;

    public void initialize() throws IOException {

        // Load background if exists
        String bg = BackgroundImageManager.loadBackgroundImageForStage("ManageFood");
        if (!bg.isEmpty()) {
            root.setStyle("-fx-background-image: url('" + bg + "'); -fx-background-size: cover;");
        }

        // Table setup
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        refreshTable();

        // click 1 lần chọn món để edit
        foodTable.setOnMouseClicked(this::handleTableClick);

        // ban đầu là thêm mới
        switchToAddMode();
    }

    /** ========================= TABLE CLICK ========================== */
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            editingFood = foodTable.getSelectionModel().getSelectedItem();
            if (editingFood == null) return;

            isAddingNew = false;

            // fill data
            nameField.setText(editingFood.getName());
            descriptionField.setText(editingFood.getDescription());
            priceField.setText(String.valueOf(editingFood.getPrice()));

            if (editingFood.getImagePath() != null && !editingFood.getImagePath().isBlank()) {
                foodImageView.setImage(new Image("file:" + editingFood.getImagePath()));
            } else {
                foodImageView.setImage(null);
            }

            deleteButton.setDisable(false);
        }
    }

    /** ========================= ADD NEW MODE ========================== */
    @FXML
    private void handleAddNewFood() {
        switchToAddMode();
    }

    private void switchToAddMode() {
        isAddingNew = true;
        editingFood = null;

        nameField.clear();
        descriptionField.clear();
        priceField.clear();
        foodImageView.setImage(null);

        deleteButton.setDisable(true);
    }

    /** ========================= SAVE FOOD ========================== */
    @FXML
    public void handleSave() throws IOException {

        String name = nameField.getText();
        String desc = descriptionField.getText();
        double price;

        if (name.isBlank() || desc.isBlank()) {
            showAlert("Lỗi", "Tên và mô tả không được để trống.");
            return;
        }

        try {
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Giá phải là số!");
            return;
        }

        if (isAddingNew) {
            Food newFood = new Food(name, desc, price);

            FoodStorageJSON.addFood(newFood);
            showAlert("Thành công", "Đã thêm món mới!");

        } else if (editingFood != null) {
            editingFood.setName(name);
            editingFood.setDescription(desc);
            editingFood.setPrice(price);

            FoodStorageJSON.updateFood(editingFood);
            showAlert("Thành công", "Đã lưu chỉnh sửa!");
        }

        refreshTable();
        switchToAddMode();
    }

    /** ========================= DELETE FOOD ========================== */
    @FXML
    public void handleDeleteFood() throws IOException {
        if (editingFood != null) {
            FoodStorageJSON.eraseFood(editingFood);
            showAlert("Đã xóa", "Món ăn đã được xóa.");
            refreshTable();
            switchToAddMode();
        }
    }

    /** ========================= IMAGE BUTTONS ========================== */
    @FXML
    private void handleChooseImage() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File file = fc.showOpenDialog(null);
        if (file == null) return;

        foodImageView.setImage(new Image(file.toURI().toString()));

        try {
            saveFoodImage(file);
        } catch (IOException e) {
            showAlert("Lỗi", "Không thể lưu ảnh!");
        }
    }

    private void saveFoodImage(File file) throws IOException {
        String folder = "images/foodImages/";
        File dir = new File(folder);
        if (!dir.exists()) dir.mkdirs();

        File dest = new File(folder + file.getName());
        Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

        if (editingFood != null) {
            editingFood.setImagePath(dest.getAbsolutePath());
            FoodStorageJSON.updateFood(editingFood);
        }
    }

    @FXML
    private void handleDeleteImage() throws IOException {
        if (editingFood == null || editingFood.getImagePath() == null) {
            showAlert("Thông báo", "Món ăn không có ảnh để xóa");
            return;
        }

        Path p = Path.of(editingFood.getImagePath());
        Files.deleteIfExists(p);

        editingFood.setImagePath(null);
        FoodStorageJSON.updateFood(editingFood);

        foodImageView.setImage(null);
        showAlert("Đã xóa ảnh", "Ảnh món ăn đã được xóa.");
    }

    /** ========================= UTILS ========================== */
    private void refreshTable() throws IOException {
        foods = FXCollections.observableArrayList(FoodStorageJSON.loadFoods());
        foodTable.setItems(foods);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
