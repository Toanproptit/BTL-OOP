package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.BackgroundImageManager;
import model.FoodStorageJSON;
import model.Table;
import model.TableJSON;

import java.io.File;
import java.io.IOException;

public class ManageorderController {

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Button backButton;

    @FXML
    private Button buttonAdd;

    @FXML
    private AnchorPane root;

    @FXML
    private TableColumn<Table, Double> columnId;

    @FXML
    private TableColumn<Table, String> columnName;

    @FXML
    private TableColumn<Table, String> columnStatus;

    @FXML
    private TableView<Table> tableview;

    @FXML
    private TextField textField1;

    @FXML
    private TextField textField2;

    @FXML
    private TextField textField3;

    private ObservableList<Table> observableList = FXCollections.observableArrayList();

    @FXML
    public void initialize()throws IOException{
        String imagePath = BackgroundImageManager.loadBackgroundImageForStage("ManageOrder");
        if (!imagePath.isEmpty()) {
            root.setStyle("-fx-background-image: url('" + imagePath + "'); -fx-background-size: cover; -fx-background-position: center center;");
        }
        observableList = FXCollections.observableArrayList(TableJSON.loadTable());
        columnId.setCellValueFactory(new PropertyValueFactory<>("index"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableview.setItems(observableList);
        tableview.setOnMouseClicked(this::handleTableViewClick);
    }

    public void handleChangeBackgroundImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();

            root.setStyle("-fx-background-image: url('" + imagePath + "'); -fx-background-size: cover; -fx-background-position: center center;");
            try {
                BackgroundImageManager.saveBackgroundImage("ManageOrder",imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể lưu ảnh nền");
            }
        }
    }

    private void handleTableViewClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Table selectedTable = tableview.getSelectionModel().getSelectedItem();
            int rowIndex = tableview.getSelectionModel().getSelectedIndex();
            selectedTable.setId(rowIndex);
            openEditFood(selectedTable);
        }
    }
    private void openEditFood(Table selectedTable) {
        try {
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/org/example/progastro/EditTable.fxml"));
            Parent parent = fxmlLoader.load();
            EditTableController controller =fxmlLoader.getController();
            controller.setTable(selectedTable);
            Scene scene = new Scene(parent,800,600);
            scene.getStylesheets().add(getClass().getResource("/org/example/progastro/EditTable.css").toExternalForm());
            Stage stage = (Stage) tableview.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Bàn số "+ selectedTable.getIndex());
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở màn hình chỉnh sửa món ăn!");
        }
    }
    @FXML
    private void switchToDashBoard(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/org/example/progastro/Dashboard.fxml"));
        Parent dashboardview = fxmlLoader.load();
        Scene scene = new Scene(dashboardview,900,600);
        Stage stage = (Stage) backButton.getScene().getWindow();
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Dashboard.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Dashboard - ProGastro");
        stage.show();
    }

    @FXML
    public void handleAddTable(ActionEvent event) throws IOException {
        String status = statusComboBox.getValue();
        if (status == null || status.isEmpty()) {
            showAlert("Lỗi", "Vui lòng chọn trạng thái bàn.");
            return;
        }

        if (textField1.getText().isEmpty() || textField2.getText().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin bàn.");
            return;
        }

        int index = Integer.parseInt(textField1.getText());
        String name = textField2.getText();


        if (!name.matches("[\\p{L}\\s]+")) {
            showAlert("Lỗi", "Tên bàn chỉ được chứa chữ cái (không số / ký tự đặc biệt).");
            return;
        }

        // ===== CHỐNG TRÙNG =====
        for (Table t : observableList) {
            if (t.getIndex() == index) {
                showAlert("Lỗi", "Số bàn " + index + " đã tồn tại!");
                return;
            }
            if (t.getName().equalsIgnoreCase(name)) {
                showAlert("Lỗi", "Tên bàn '" + name + "' đã tồn tại!");
                return;
            }
        }

        // ===== THÊM BÀN =====
        Table newTable = new Table(index, name, status);
        TableJSON.addTable(newTable);

        observableList = FXCollections.observableArrayList(TableJSON.loadTable());
        tableview.setItems(observableList);

        showAlert("Thông báo", "Đã Thêm Bàn");

        // Clear input sau khi thêm
        textField1.clear();
        textField2.clear();
        statusComboBox.setValue(null);
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}