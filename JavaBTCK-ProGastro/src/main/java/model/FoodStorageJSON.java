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
        try {
            foodList = loadFoods();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Food> loadFoods() throws IOException {
        File file = new File(FILE_PATH);
        try (FileReader reader = new FileReader(FILE_PATH)) {
//            foodList = new Gson().fromJson(reader, new TypeToken<List<Food>>() {}.getType());
            return new Gson().fromJson(reader, new TypeToken<List<Food>>() {}.getType());
        } catch (IOException e) {
            foodList = new ArrayList<>();
        }
        return List.of();
    }
    public static void saveFoods() throws IOException {
        File file = new File(FILE_PATH);
        try (FileWriter writer = new FileWriter(file)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(foodList, writer);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void addFood(Food food) {
        foodList.add(food);
        try {
            saveFoods();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void updateFood(Food food)throws IOException{
        int i= food.getIndex();
        foodList.set(i,food);
        saveFoods();
    }

    public static void eraseFood(Food food) throws IOException {
        int i=food.getIndex();
        foodList.remove(i);
        saveFoods();
    }

    public List<Food> getFoodList() {
        return foodList;
    }
}
