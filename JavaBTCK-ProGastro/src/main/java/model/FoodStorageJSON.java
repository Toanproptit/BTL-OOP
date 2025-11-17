package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FoodStorageJSON {

    private static final String FILE_PATH = "food.json";
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

            // gán lại index đúng cho từng food
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setIndex(i);
                }
            }

            return (list != null) ? list : new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /** ======================= SAVE ======================= */
    public static void saveFoods() {
        // tự động cập nhật index trước khi lưu
        for (int i = 0; i < foodList.size(); i++) {
            foodList.get(i).setIndex(i);
        }

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(foodList, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** ======================= CRUD ======================= */

    public static void addFood(Food food) {
        // index = cuối danh sách
        food.setIndex(foodList.size());
        foodList.add(food);
        saveFoods();
    }

    public static void updateFood(Food food) {
        int i = food.getIndex();
        if (i >= 0 && i < foodList.size()) {
            foodList.set(i, food);
            saveFoods();
        }
    }

    public static void eraseFood(Food food) {
        int i = food.getIndex();
        if (i >= 0 && i < foodList.size()) {
            foodList.remove(i);
            saveFoods();
        }
    }

    /** ======================= GETTER ======================= */
    public static List<Food> getFoodList() {
        return foodList;
    }
}
