package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Invoice;
import model.InvoiceStorageJSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RevenueController {

    @FXML private TableView<Invoice> tableRevenue;
    @FXML private TableColumn<Invoice, String> colDate;
    @FXML private TableColumn<Invoice, String> colId;
    @FXML private TableColumn<Invoice, Double> colAmount;
    @FXML private Label totalRevenueLabel;
    @FXML private Button backButton;

    private List<Invoice> allInvoices;

    @FXML
    public void initialize() throws IOException {
        colDate.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCreatedAt()));
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBill_id()));
        colAmount.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getTotalPrice()).asObject());

        allInvoices = InvoiceStorageJSON.loadInvoices();
        if (allInvoices == null) allInvoices = new ArrayList<>();

        tableRevenue.getItems().setAll(allInvoices);

        double total = allInvoices.stream().mapToDouble(Invoice::getTotalPrice).sum();
        totalRevenueLabel.setText(String.format("Tổng doanh thu: %,.0f VND", total));
    }

    @FXML
    private void switchToDashBoard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/progastro/Dashboard.fxml"));
        Parent dashboardView = fxmlLoader.load();
        Scene scene = new Scene(dashboardView, 900, 600);
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Dashboard - ProGastro");
        stage.show();
    }
    @FXML
    private void switchToRevenueChart() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/progastro/RevenueChart.fxml"));
        Parent chartView = fxmlLoader.load();
        Scene scene = new Scene(chartView, 900, 600);
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Revenue Chart - ProGastro");
        stage.show();
    }
}
