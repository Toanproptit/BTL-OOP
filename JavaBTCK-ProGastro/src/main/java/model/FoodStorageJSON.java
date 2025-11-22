package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FoodStorageJSON {

    private static final String FILE_PATH =
            System.getProperty("user.dir") + "/JavaBTCK-ProGastro/food.json";

    private static List<Food> foodList;

    static {
        foodList = loadFoods();
    }

    /** ======================= LOAD ======================= */
    public static List<Food> loadFoods() {

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {

            List<Food> list = new Gson().fromJson(
                    reader,
                    new TypeToken<List<Food>>() {}.getType()
            );

            return (list != null) ? list : new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /** ======================= SAVE ======================= */
    private static void saveFoods() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(foodList, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** ======================= CRUD ======================= */

    /** Add new food */
    public static void addFood(Food food) {
        foodList.add(food);
        saveFoods();
    }

    /** Update food by ID */
    public static void updateFood(Food updatedFood) {

        for (int i = 0; i < foodList.size(); i++) {
            if (foodList.get(i).getId().equals(updatedFood.getId())) {
                foodList.set(i, updatedFood);
                saveFoods();
                return;
            }
        }
    }

    /** Delete food by ID */
    public static void eraseFood(Food food) {

        foodList.removeIf(f -> f.getId().equals(food.getId()));
        saveFoods();
    }

    /** Get all foods */
    public static List<Food> getFoodList() {
        return foodList;
    }
}