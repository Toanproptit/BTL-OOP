package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javafx.stage.Stage;
import model.Invoice; // model của bạn có: bill_id, tableName, totalPrice, createdAt

public class RevenueChartController {

    @FXML private LineChart<String, Number> revenueChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    @FXML private Label totalRevenueLabel;
    @FXML private Label errorLabelDay;
    @FXML private Label errorLabelMonth;
    @FXML private Label errorLabelYear;


    @FXML private DatePicker dateFromPicker;
    @FXML private DatePicker dateToPicker;

    @FXML private ComboBox<Integer> monthFromCombo;
    @FXML private ComboBox<Integer> monthToCombo;
    @FXML private ComboBox<Integer> monthYearCombo;

    @FXML private ComboBox<Integer> yearFromCombo;
    @FXML private ComboBox<Integer> yearToCombo;

    @FXML private Button btnDay, btnMonth, btnYear;
    @FXML private Button backButton;

    @FXML private HBox dayInputs, monthInputs, yearInputs;

    private ObservableList<Invoice> invoices = FXCollections.observableArrayList();

    private enum Mode { DAY, MONTH, YEAR }
    private Mode currentMode = Mode.DAY;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        loadInvoices();
        setupComboBoxes();
        switchToDayMode();
    }

    private void loadInvoices() {
        try (FileReader reader = new FileReader("invoices.json")) {
            Type listType = new TypeToken<List<Invoice>>() {}.getType();
            List<Invoice> list = new Gson().fromJson(reader, listType);
            if (list != null) invoices.addAll(list);
        } catch (IOException e) {
            System.out.println("Không thể đọc file invoices.json: " + e.getMessage());
        }
    }

    private void setupComboBoxes() {
        List<Integer> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) months.add(i);
        monthFromCombo.setItems(FXCollections.observableArrayList(months));
        monthToCombo.setItems(FXCollections.observableArrayList(months));

        List<Integer> years = invoices.stream()
                .map(inv -> LocalDate.parse(inv.getCreatedAt(), formatter).getYear())
                .distinct().sorted().toList();

        monthYearCombo.setItems(FXCollections.observableArrayList(years));
        yearFromCombo.setItems(FXCollections.observableArrayList(years));
        yearToCombo.setItems(FXCollections.observableArrayList(years));
    }

    // ==== NÚT CHUYỂN CHẾ ĐỘ ====

    @FXML
    private void onModeDay(ActionEvent e) { switchToDayMode(); }

    @FXML
    private void onModeMonth(ActionEvent e) { switchToMonthMode(); }

    @FXML
    private void onModeYear(ActionEvent e) { switchToYearMode(); }

    @FXML
    private void switchToRevenueController() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/progastro/revenue.fxml"));

        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent, 800, 600);

        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Revenue-ProGastro");
        stage.show();
    }
    private void switchToDayMode() {
        currentMode = Mode.DAY;
        dayInputs.setVisible(true); dayInputs.setManaged(true);
        monthInputs.setVisible(false); monthInputs.setManaged(false);
        yearInputs.setVisible(false); yearInputs.setManaged(false);
    }

    private void switchToMonthMode() {
        currentMode = Mode.MONTH;
        dayInputs.setVisible(false); dayInputs.setManaged(false);
        monthInputs.setVisible(true); monthInputs.setManaged(true);
        yearInputs.setVisible(false); yearInputs.setManaged(false);
    }

    private void switchToYearMode() {
        currentMode = Mode.YEAR;
        dayInputs.setVisible(false); dayInputs.setManaged(false);
        monthInputs.setVisible(false); monthInputs.setManaged(false);
        yearInputs.setVisible(true); yearInputs.setManaged(true);
    }

    // ==== LỌC ====

    @FXML
    private void onFilter(ActionEvent e) {
        switch (currentMode) {
            case DAY -> filterByDay();
            case MONTH -> filterByMonth();
            case YEAR -> filterByYear();
        }
    }

    private void filterByDay() {
        errorLabelDay.setText("");
        LocalDate from = dateFromPicker.getValue();
        LocalDate to = dateToPicker.getValue();

        if (from == null || to == null) {
            errorLabelDay.setText("Vui lòng chọn ngày bắt đầu và kết thúc.");
            return;
        }
        if (to.isBefore(from)) {
            errorLabelDay.setText("Ngày kết thúc phải sau ngày bắt đầu.");
            return;
        }

        List<Invoice> filtered = invoices.stream()
                .filter(inv -> {
                    LocalDate date = LocalDate.parse(inv.getCreatedAt(), formatter);
                    return !date.isBefore(from) && !date.isAfter(to);
                }).toList();

        updateChart(filtered, "Theo ngày");
    }

    private void filterByMonth() {
        errorLabelMonth.setText("");
        Integer year = monthYearCombo.getValue();
        Integer from = monthFromCombo.getValue();
        Integer to = monthToCombo.getValue();

        if (year == null || from == null || to == null) {
            errorLabelMonth.setText("Chọn đủ năm và khoảng tháng.");
            return;
        }
        if (to < from) {
            errorLabelMonth.setText("Tháng kết thúc phải >= tháng bắt đầu.");
            return;
        }

        List<Invoice> filtered = invoices.stream()
                .filter(inv -> {
                    LocalDate date = LocalDate.parse(inv.getCreatedAt(), formatter);
                    return date.getYear() == year && date.getMonthValue() >= from && date.getMonthValue() <= to;
                }).toList();

        updateChart(filtered, "Theo tháng");
    }

    private void filterByYear() {
        errorLabelYear.setText("");
        Integer from = yearFromCombo.getValue();
        Integer to = yearToCombo.getValue();

        if (from == null || to == null) {
            errorLabelYear.setText("Chọn khoảng năm.");
            return;
        }
        if (to < from) {
            errorLabelYear.setText("Năm kết thúc phải >= năm bắt đầu.");
            return;
        }

        List<Invoice> filtered = invoices.stream()
                .filter(inv -> {
                    LocalDate date = LocalDate.parse(inv.getCreatedAt(), formatter);
                    return date.getYear() >= from && date.getYear() <= to;
                }).toList();

        updateChart(filtered, "Theo năm");
    }

    // ==== CẬP NHẬT BIỂU ĐỒ ====

    private void updateChart(List<Invoice> data, String label) {
        revenueChart.getData().clear();
        Map<String, Double> grouped = new TreeMap<>();

        for (Invoice inv : data) {
            LocalDate date = LocalDate.parse(inv.getCreatedAt(), formatter);
            String key = switch (currentMode) {
                case DAY -> date.toString();
                case MONTH -> date.getMonthValue() + "/" + date.getYear();
                case YEAR -> String.valueOf(date.getYear());
            };
            grouped.put(key, grouped.getOrDefault(key, 0.0) + inv.getTotalPrice());
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(label);

        double total = 0;
        for (Map.Entry<String, Double> entry : grouped.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            total += entry.getValue();
        }

        revenueChart.getData().add(series);
        totalRevenueLabel.setText(String.format("Tổng doanh thu: %.0f VND", total));
    }

    // ==== CHUYỂN LẠI DASHBOARD ====
    @FXML
    private void switchToDashBoard(ActionEvent e) {
        // TODO: thêm đoạn chuyển scene nếu bạn muốn quay lại màn khác
        System.out.println("Quay lại dashboard");
    }
}
