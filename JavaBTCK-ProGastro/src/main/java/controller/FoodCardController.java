package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Food;

import java.io.File;

public class FoodCardController {

    @FXML
    private ImageView foodImage;

    @FXML
    private Label foodName;

    @FXML
    private Label foodDescription;

    @FXML
    private Label foodPrice;

    public void setData(Food food) {

        foodName.setText(food.getName());
        foodDescription.setText(food.getDescription());
        foodPrice.setText("$" + food.getPrice());

        String path = food.getImagePath();
        Image img = loadImage(path);

        foodImage.setImage(img);
    }
    private Image loadImage(String path) {

        // 1. Nếu rỗng hoặc null → dùng ảnh mặc định
        if (path == null || path.isBlank()) {
            return new Image("/org/example/progastro/images/default.png");
        }

        try {
            // 2. Nếu là URL: http:// hoặc file:
            if (path.startsWith("http") || path.startsWith("file:")) {
                return new Image(path, false);
            }

            // 3. Nếu là đường dẫn file local
            File file = new File(path);
            if (file.exists()) {
                return new Image(file.toURI().toString(), false);
            }

        } catch (Exception e) {
            // ignore — dùng ảnh mặc định
        }

        // 4. Fallback
        return new Image("/org/example/progastro/images/default.png");
    }


}
