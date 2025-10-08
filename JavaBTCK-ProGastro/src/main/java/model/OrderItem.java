package model;

public class OrderItem {
    private String name;
    private Double price;
//    private Food food;
    private int quantity;

    public OrderItem(Food food, int quantity) {
        this.name=food.getName();
        this.price= food.getPrice();
//        this.food = food;
        this.quantity = quantity;
    }

//    public Food getFood() { return food; }
    public int getQuantity() { return quantity; }
    public String getName() {
        return name; // getter cho tên món ăn
    }

    public double getPrice() {
        return price; // getter cho giá món ăn
    }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
