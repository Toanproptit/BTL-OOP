package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import model.Food;
import model.FoodStorageJSON;

import java.io.IOException;
import java.util.List;

public class FoodMenuController {

    @FXML
    private FlowPane foodFlow;

    @FXML
    private TextField searchField;

    @FXML
    private ChoiceBox<String> sortChoice;

    @FXML
    public void initialize() {
        setupSortOptions();
        loadFoodList();
    }

    /** ========================== SORTING OPTIONS ========================== */
    private void setupSortOptions() {
        sortChoice.getItems().addAll(
                "Default",
                "Price: Low to High",
                "Price: High to Low"
        );

        sortChoice.setValue("Default");

        sortChoice.setOnAction(e -> loadFoodList());
    }

    /** ========================== LOAD FOOD LIST ========================== */
    private void loadFoodList() {
        foodFlow.getChildren().clear();

        List<Food> listFoods = FoodStorageJSON.getFoodList();

        // --- Sorting ---
        switch (sortChoice.getValue()) {

            case "Price: Low to High":
                listFoods.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
                break;

            case "Price: High to Low":
                listFoods.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
                break;

            case "Default":
            default:
                // Không làm gì
                break;
        }

        // --- Rendering Cards ---
        for (Food food : listFoods) {
            addFoodCard(food);
        }
    }

    /** ========================== ADD CARD ========================== */
    private void addFoodCard(Food food) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/progastro/FoodCard.fxml"));
            Parent card = loader.load();

            FoodCardController cardController = loader.getController();
            cardController.setData(food);

            foodFlow.getChildren().add(card);

        } catch (IOException e) {
            System.out.println("Lỗi load card: " + e.getMessage());
        }
    }

    /** ========================== SEARCH ========================== */
    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase().trim();

        foodFlow.getChildren().clear();

        List<Food> listFoods = FoodStorageJSON.getFoodList();

        for (Food food : listFoods) {

            if (food.getName().toLowerCase().contains(keyword) ||
                    food.getDescription().toLowerCase().contains(keyword)) {

                addFoodCard(food);
            }
        }
    }
}
