package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import model.Food;
import model.FoodStorageJSON;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FoodMenuController {

    // UI chính
    @FXML private FlowPane foodFlow;

    // Search + Sort
    @FXML private TextField       searchField;
    @FXML private Button          searchBtn;
    @FXML private ChoiceBox<String> sortChoice;

    // Category filter
    @FXML private CheckBox cbAll;
    @FXML private CheckBox cbChicken;
    @FXML private CheckBox cbBeef;
    @FXML private CheckBox cbNoodles;
    @FXML private CheckBox cbSeafood;
    @FXML private CheckBox cbPizza;
    @FXML private CheckBox cbSalad;
    @FXML private CheckBox cbBakery;
    @FXML private CheckBox cbBeverages;
    @FXML private CheckBox cbOther;

    // Price filter
    @FXML private CheckBox price5_10;
    @FXML private CheckBox price10_20;
    @FXML private CheckBox price20_30;
    @FXML private CheckBox priceAbove30;

    private List<Food> foodList;

    @FXML
    public void initialize() {
        // Load dữ liệu
        foodList = FoodStorageJSON.loadFoods();

        // Sort options đã khai trong FXML, chỉ cần chọn default
        sortChoice.getSelectionModel().selectFirst();

        // Lần đầu load
        refreshList();

        // Event realtime
        searchField.textProperty().addListener((obs, oldV, newV) -> refreshList());
        sortChoice.setOnAction(e -> refreshList());
        searchBtn.setOnAction(e -> handleSearchClick());

        // Check all
        cbAll.setOnAction(e -> {
            boolean val = cbAll.isSelected();
            cbChicken.setSelected(val);
            cbBeef.setSelected(val);
            cbNoodles.setSelected(val);
            cbSeafood.setSelected(val);
            cbPizza.setSelected(val);
            cbSalad.setSelected(val);
            cbBakery.setSelected(val);
            cbBeverages.setSelected(val);
            cbOther.setSelected(val);
            refreshList();
        });

        // Category listeners
        cbChicken.setOnAction(e -> refreshList());
        cbBeef.setOnAction(e -> refreshList());
        cbNoodles.setOnAction(e -> refreshList());
        cbSeafood.setOnAction(e -> refreshList());
        cbPizza.setOnAction(e -> refreshList());
        cbSalad.setOnAction(e -> refreshList());
        cbBakery.setOnAction(e -> refreshList());
        cbBeverages.setOnAction(e -> refreshList());
        cbOther.setOnAction(e -> refreshList());

        // Price listeners
        price5_10.setOnAction(e -> refreshList());
        price10_20.setOnAction(e -> refreshList());
        price20_30.setOnAction(e -> refreshList());
        priceAbove30.setOnAction(e -> refreshList());
    }

    /** Khi bấm nút Tìm – cho chắc là luôn refresh */
    @FXML
    private void handleSearchClick() {
        refreshList();
    }

    /** ===================== MAIN UPDATE ====================== */
    private void refreshList() {
        List<Food> filtered = new ArrayList<>(foodList);

        // 1) SEARCH
        String keyword = searchField.getText() != null
                ? searchField.getText().trim().toLowerCase()
                : "";
        if (!keyword.isEmpty()) {
            filtered = filtered.stream()
                    .filter(f -> f.getName().toLowerCase().contains(keyword)
                            || f.getDescription().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }

        // 2) CATEGORY FILTER
        List<String> selectedCategories = getSelectedCategories();
        if (!selectedCategories.isEmpty()) {
            filtered = filtered.stream()
                    .filter(f -> selectedCategories.contains(f.getCategory()))
                    .collect(Collectors.toList());
        }

        // 3) PRICE FILTER (VND)
        filtered = applyPriceFilter(filtered);

        // 4) SORT
        filtered = applySort(filtered);

        // 5) RENDER UI
        foodFlow.getChildren().clear();
        for (Food food : filtered) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/org/example/progastro/FoodCard.fxml"));
                Parent card = loader.load();

                FoodCardController ctrl = loader.getController();
                ctrl.setFoodData(food);

                foodFlow.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Lấy các category đang chọn. Nếu không chọn gì hoặc chọn Tất cả → không filter */
    private List<String> getSelectedCategories() {
        List<String> list = new ArrayList<>();

        if (cbChicken.isSelected())   list.add("Món Gà");
        if (cbBeef.isSelected())      list.add("Món Bò");
        if (cbNoodles.isSelected())   list.add("Mì / Bún / Phở");
        if (cbSeafood.isSelected())   list.add("Hải Sản");
        if (cbPizza.isSelected())     list.add("Pizza");
        if (cbSalad.isSelected())     list.add("Salad");
        if (cbBakery.isSelected())    list.add("Bánh - Bakery");
        if (cbBeverages.isSelected()) list.add("Đồ Uống");
        if (cbOther.isSelected())     list.add("Khác");

        // Nếu cbAll được chọn hoặc không có ô nào chọn → trả về list rỗng = không lọc theo category
        boolean noneSelected = list.isEmpty();
        if (cbAll.isSelected() || noneSelected) {
            return Collections.emptyList();
        }
        return list;
    }

    /** PRICE RANGE FILTER – dùng logic OR, không phải AND */
    private List<Food> applyPriceFilter(List<Food> list) {
        boolean any = price5_10.isSelected()
                || price10_20.isSelected()
                || price20_30.isSelected()
                || priceAbove30.isSelected();

        if (!any) return list; // không chọn khoảng giá nào → không lọc

        return list.stream().filter(f -> {
            double p = f.getPrice();
            boolean match = false;

            if (price5_10.isSelected())
                match |= (p >= 50000 && p <= 100000);

            if (price10_20.isSelected())
                match |= (p > 100000 && p <= 200000);

            if (price20_30.isSelected())
                match |= (p > 200000 && p <= 300000);

            if (priceAbove30.isSelected())
                match |= (p > 300000);

            return match;
        }).collect(Collectors.toList());
    }

    /** SORTING LOGIC – khớp đúng text trong FXML */
    private List<Food> applySort(List<Food> list) {
        String mode = sortChoice.getValue();
        if (mode == null) return list;

        switch (mode) {
            case "Giá: Thấp → Cao":
                return list.stream()
                        .sorted(Comparator.comparingDouble(Food::getPrice))
                        .collect(Collectors.toList());

            case "Giá: Cao → Thấp":
                return list.stream()
                        .sorted(Comparator.comparingDouble(Food::getPrice).reversed())
                        .collect(Collectors.toList());

            case "Tên A → Z":
                return list.stream()
                        .sorted(Comparator.comparing(Food::getName))
                        .collect(Collectors.toList());

            default: // "Mặc định"
                return list;
        }
    }

    //===================== RESET FILTERS =====================//
    @FXML
    private void handleResetFilters() {
        // Search
        searchField.clear();

        // Sort
        sortChoice.getSelectionModel().selectFirst();

        // Category
        cbAll.setSelected(true);
        cbChicken.setSelected(false);
        cbBeef.setSelected(false);
        cbNoodles.setSelected(false);
        cbSeafood.setSelected(false);
        cbPizza.setSelected(false);
        cbSalad.setSelected(false);
        cbBakery.setSelected(false);
        cbBeverages.setSelected(false);
        cbOther.setSelected(false);

        // Price
        price5_10.setSelected(false);
        price10_20.setSelected(false);
        price20_30.setSelected(false);
        priceAbove30.setSelected(false);

        refreshList();
    }
}
