package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Food {

    private String name;
    private String description;
    private double price;
    private int index;
    private String imagePath;

    public Food(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imagePath="";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String toString(){
        return (name);
    }
}
