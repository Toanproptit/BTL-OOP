package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.BackgroundImageManager;
import model.Food;
import model.FoodStorageJSON;
import javafx.geometry.Insets;
import javafx.geometry.Pos; // Cần thiết để căn chỉnh
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FoodController {
    @FXML
    private AnchorPane root;


    @FXML
    private Button backButton;

    @FXML
    private GridPane foodGrid;

    private int currentRow = 0;
    private int currentCol = 0;
    private final int MAX_COLUMNS = 3;

// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @FXML
    public void initialize() throws IOException {
        // --- 1. Load ảnh nền (GIỮ NGUYÊN) ---
        String imagePath = BackgroundImageManager.loadBackgroundImageForStage("ManageFood");


        // --- 2. Load dữ liệu món ăn từ JSON ---
        List<Food> foodList = FoodStorageJSON.loadFoods();

        // --- 3. Thêm từng món ăn vào GridPane ---
        // Xóa logic TableView: nameColumn.setCellValueFactory(...), foodTable.setItems(foods);

        for (Food food : foodList) {
            // Tạo card giao diện cho từng món ăn
            HBox foodCard = createFoodCard(food);

            // Thêm card vào GridPane
            foodGrid.add(foodCard, currentCol, currentRow);
            System.out.println("Da them vao luoi");
            // Cập nhật vị trí
            currentCol++;
            if (currentCol >= MAX_COLUMNS) {
                currentCol = 0;
                currentRow++;
            }
        }

        // --- 4. Xử lý sự kiện (Nếu muốn click vào card món ăn) ---
        // foodTable.setOnMouseClicked(this::handleTableViewClick); <-- PHẢI XÓA ĐI
        // Bạn sẽ cần xử lý click trên từng "card" (AnchorPane/HBox) trong createFoodCard()
    }

// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------





// ...

    @FXML
    public void handleSwitchToDashBoard(ActionEvent event)throws IOException{
        switchToDashBoard();
    }

    private void switchToDashBoard() throws IOException {
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/org/example/progastro/Dashboard.fxml"));
        Parent dashboard = fxmlLoader.load();
        Scene scene = new Scene(dashboard,1500,750  );
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Dashboard.css").toExternalForm());
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Dashboard - ProGastro");
        stage.show();
    }


    private HBox createFoodCard(Food food) {
        // 1. CONTAINER CHÍNH (Card)
        HBox card = new HBox(5);
        card.setPrefHeight(100);
        card.setPrefWidth(250); // Đặt chiều rộng ưu tiên
        card.setAlignment(Pos.CENTER_LEFT); // Căn nội dung ở giữa/bên trái
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #e5e5e5; -fx-border-width: 1; -fx-background-color: white;");

        // 2. ẢNH MÓN ĂN


// Đảm bảo ImageView đã được khai báo/khởi tạo trước đó, ví dụ:
        ImageView imageView = new ImageView();

        try {
            // 1. Tạo đối tượng File từ đường dẫn tuyệt đối
            File file = new File(food.getImagePath());

            // 2. Chuyển File sang URI (định dạng URL)
            String fileUrl = file.toURI().toString();

            // 3. Load ảnh từ URL (định dạng file:///)
            Image image = new Image(fileUrl);

            imageView.setImage(image);

            // 4. THIẾT LẬP KÍCH THƯỚC CỐ ĐỊNH (Quan trọng)
            final double FIT_SIZE = 80.0; // Kích thước mong muốn (ví dụ: 80x80)

            imageView.setFitWidth(FIT_SIZE);
            imageView.setFitHeight(FIT_SIZE);
            imageView.setPreserveRatio(true); // Giữ tỷ lệ khung hình, tránh làm méo ảnh

            System.out.println("Đã load ảnh thành công cho: " + food.getName());

        } catch (Exception e) {
            System.err.println("Lỗi load ảnh cho " + food.getName() + ": " + e.getMessage());

            // Thiết lập kích thước cho ImageView để placeholder/icon lỗi vẫn có vị trí
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
            // Có thể thêm ảnh/icon báo lỗi nếu muốn:
            // imageView.setImage(new Image(getClass().getResourceAsStream("/images/error_placeholder.png")));
        }


        // 3. THÔNG TIN (Tên và Giá) - Dùng VBox
        VBox infoBox = new VBox(5); // Khoảng cách 5px giữa Tên và Giá
        infoBox.setAlignment(Pos.TOP_LEFT);
        infoBox.setPadding(new Insets(0, 0, 0, 5)); // Đệm nhẹ cho thông tin

        Label nameLabel = new Label(food.getName());
        nameLabel.setFont(Font.font("Arial", 14));
        nameLabel.setStyle("-fx-font-weight: bold;");

        Label priceLabel = new Label(String.format("%,.0f", food.getPrice()));
        priceLabel.setStyle("-fx-text-fill: #E91E63; -fx-font-weight: bold;");

        infoBox.getChildren().addAll(nameLabel, priceLabel);

        // 4. SPACER (Quan trọng để đẩy nút '+' sang phải)
        Label spacer = new Label("   ");
        HBox.setHgrow(spacer, Priority.ALWAYS); // Spacer chiếm hết không gian còn lại

        // 5. NÚT THÊM (+) - Dùng Label cho đơn giản, căn giữa
        Label addButton = new Label("+");
        addButton.setFont(Font.font("Arial", 20));
        addButton.setPadding(new Insets(5, 10, 5, 10));
        addButton.setStyle(
                "-fx-background-color: #4CAF50; " + // Màu xanh lá
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-background-radius: 5;"
        );
        addButton.setOnMouseClicked(event -> handleFoodCardClick(food));

        // VBox để căn nút '+' theo chiều dọc (căn giữa)
        VBox buttonWrapper = new VBox(addButton);
        buttonWrapper.setAlignment(Pos.CENTER); // Căn nút '+' ở giữa chiều cao của card

        // 6. THÊM TẤT CẢ VÀO CARD CHÍNH
        card.getChildren().addAll(imageView, infoBox, spacer, buttonWrapper);

        // Thêm sự kiện click cho toàn bộ card (tùy chọn)
        card.setOnMouseClicked(event -> handleFoodCardClick(food));

        return card;
    }

    /**
     * Xử lý khi người dùng click vào một card món ăn
     */
    private void handleFoodCardClick(Food food) {
        System.out.println("Đã click vào món ăn: " + food.getName());
        // Chuyển sa
    }
}
