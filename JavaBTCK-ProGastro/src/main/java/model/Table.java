package model;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String index;
    private String name;
    private String status;
    private String date;
    private List<OrderItem> orderItems;
    private int id;
    private double totalPrice;
    public Table(String index, String name, String status) {
        this.index = index;
        this.name = name;
        this.status = status;
        this.orderItems = new ArrayList<>();
        calculateTotalPrice();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItem() {
        return orderItems;
    }

    public double getTotalPrice() {
        return totalPrice ;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderFood(List<OrderItem> orderFood) {
        this.orderItems = orderFood;
    }
    public void calculateTotalPrice() {
        double sum = 0;
        for (OrderItem orderItem : orderItems) {
            sum += orderItem.getQuantity() * orderItem.getPrice();
        }
        // Làm tròn và ép kiểu về int
        setTotalPrice(sum);
    }

    public String toString(){
        return (name);
    }
}
