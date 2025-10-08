package model;

import java.util.List;

public class Invoice {
    private String tableName;
    private List<OrderItem> orderItems;
    private double totalPrice;
    private String createdAt;

    public Invoice(String tableName, List<OrderItem> orderItems, double totalPrice, String createdAt) {
        this.tableName = tableName;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public String getTableName() {
        return tableName;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
