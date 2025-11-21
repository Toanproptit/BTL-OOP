package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Food;

import java.io.File;

public class FoodCardController {

    @FXML private ImageView foodImage;
    @FXML private Label foodName;
    @FXML private Label foodDescription;
    @FXML private Label foodPrice;

    public void setFoodData(Food food) {

        foodName.setText(food.getName());
        foodDescription.setText(food.getDescription());

        foodPrice.setText(String.format("%,.0f VND", food.getPrice()));

        try {
            File f = new File(food.getImagePath());
            if (f.exists()) {
                foodImage.setImage(new Image(f.toURI().toString()));
            }
        } catch (Exception ignored) {}
    }
}
