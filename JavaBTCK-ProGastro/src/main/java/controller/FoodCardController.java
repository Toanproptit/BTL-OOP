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

        try {
            // 1. URL hoặc file:
            if (path != null && (path.startsWith("http") || path.startsWith("file:"))) {
                return new Image(path, false);
            }

            // 2. File local:
            if (path != null) {
                File file = new File(path);
                if (file.exists()) {
                    return new Image(file.toURI().toString(), false);
                }
            }

        } catch (Exception ignored) {}

        // 3. Ảnh mặc định từ resources
        return new Image(new File("images/foodImages/default.jpg").toURI().toString());
    }


}
