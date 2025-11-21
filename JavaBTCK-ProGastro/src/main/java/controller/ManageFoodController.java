package controller;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import model.BackgroundImageManager;
import model.Food;
import model.FoodStorageJSON;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ManageFoodController {

    @FXML private AnchorPane root;

    // --- FORM FIELDS ---
    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private TextField priceField;
    @FXML private ComboBox<String> categoryCombo;   // ⭐ NEW CATEGORY

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
    @FXML private TableColumn<Food, String> categoryColumn; // ⭐ NEW COLUMN

    // --- DATA ---
    private ObservableList<Food> foods = FXCollections.observableArrayList();

    private boolean isAddingNew = true;
    private Food editingFood = null;

    private String selectedImagePath = null;


    /** ================= INITIALIZE ================== */
    public void initialize() throws IOException {

        // Load background
        String bg = BackgroundImageManager.loadBackgroundImageForStage("ManageFood");
        if (!bg.isEmpty()) {
            root.setStyle("-fx-background-image: url('" + bg + "'); -fx-background-size: cover;");
        }

        // ===== category dropdown =====
        categoryCombo.getItems().addAll(
                "Món Bò",
                "Món Gà",
                "Hải Sản",
                "Pizza",
                "Mì / Bún / Phở",
                "Salad",
                "Bánh - Bakery",
                "Đồ Uống",
                "Khác"
        );


        // ====== Table setup ======
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        refreshTable();

        foodTable.setOnMouseClicked(this::handleTableClick);

        switchToAddMode();
    }


    /** ================= TABLE CLICK (EDIT MODE) ================= */
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {

            editingFood = foodTable.getSelectionModel().getSelectedItem();
            if (editingFood == null) return;

            isAddingNew = false;

            nameField.setText(editingFood.getName());
            descriptionField.setText(editingFood.getDescription());
            priceField.setText(String.valueOf(editingFood.getPrice()));

            // ⭐ Load category
            categoryCombo.setValue(editingFood.getCategory());

            selectedImagePath = editingFood.getImagePath();
            if (selectedImagePath != null && !selectedImagePath.isBlank()) {
                foodImageView.setImage(new Image("file:" + selectedImagePath));
            } else {
                foodImageView.setImage(null);
            }

            deleteButton.setDisable(false);
        }
    }


    /** ================= SWITCH TO ADD MODE ================= */
    @FXML
    private void handleAddNewFood() {
        switchToAddMode();
    }

    private void switchToAddMode() {
        isAddingNew = true;
        editingFood = null;
        selectedImagePath = null;

        nameField.clear();
        descriptionField.clear();
        priceField.clear();
        categoryCombo.setValue(null);
        foodImageView.setImage(null);

        deleteButton.setDisable(true);
    }


    /** ================= SAVE FOOD ================= */
    @FXML
    public void handleSave() throws IOException {

        String name = nameField.getText();
        String desc = descriptionField.getText();
        String category = categoryCombo.getValue(); // ⭐ NEW
        double price;

        if (name.isBlank() || desc.isBlank() || category == null) {
            showAlert("Lỗi", "Tên, mô tả và loại món ăn không được để trống.");
            return;
        }

        try {
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Giá phải là số!");
            return;
        }

        if (isAddingNew) {

            // ⭐ Tạo ID duy nhất
            String id = "F-" + UUID.randomUUID().toString().substring(0, 6);

            Food newFood = new Food(id, name, desc, price, category, selectedImagePath);

            FoodStorageJSON.addFood(newFood);
            showAlert("Thành công", "Đã thêm món mới!");

        } else {

            editingFood.setName(name);
            editingFood.setDescription(desc);
            editingFood.setPrice(price);
            editingFood.setCategory(category);
            editingFood.setImagePath(selectedImagePath);

            FoodStorageJSON.updateFood(editingFood);

            showAlert("Thành công", "Đã lưu chỉnh sửa!");
        }

        refreshTable();
        switchToAddMode();
    }


    /** ================= DELETE FOOD ================= */
    @FXML
    public void handleDeleteFood() throws IOException {
        if (editingFood != null) {

            FoodStorageJSON.eraseFood(editingFood);
            showAlert("Đã xóa", "Món ăn đã được xóa.");

            refreshTable();
            switchToAddMode();
        }
    }


    /** ================= CHOOSE IMAGE ================= */
    @FXML
    private void handleChooseImage() {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fc.showOpenDialog(null);
        if (file == null) return;

        try {
            selectedImagePath = saveFoodImage(file);
            foodImageView.setImage(new Image("file:" + selectedImagePath));

        } catch (IOException e) {
            showAlert("Lỗi", "Không thể lưu ảnh!");
        }
    }

    private String saveFoodImage(File file) throws IOException {

        File dir = new File("JavaBTCK-ProGastro/images/food/");
        if (!dir.exists()) dir.mkdirs();

        File dest = new File(dir, file.getName());
        Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return dest.getAbsolutePath();
    }


    /** ================= DELETE IMAGE ONLY ================= */
    @FXML
    private void handleDeleteImage() throws IOException {

        if (selectedImagePath == null) {
            showAlert("Thông báo", "Món ăn không có ảnh để xóa.");
            return;
        }

        Files.deleteIfExists(Path.of(selectedImagePath));

        selectedImagePath = null;
        foodImageView.setImage(null);

        if (editingFood != null) {
            editingFood.setImagePath(null);
            FoodStorageJSON.updateFood(editingFood);
        }

        showAlert("Đã xóa ảnh", "Ảnh món ăn đã được xóa.");
    }


    /** ================= UTILS ================= */
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
