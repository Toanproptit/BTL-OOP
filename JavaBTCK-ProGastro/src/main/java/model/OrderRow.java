package model;
public class OrderRow {
    private String id;
    private String category;
    private String date;
    private Double revenue;
    private Double profit;

    public OrderRow(String id, String category, String date, Double revenue, Double profit) {
        this.id = id;
        this.category = category;
        this.date = date;
        this.revenue = revenue;
        this.profit = profit;
    }

    public String getId() { return id; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public Double getRevenue() { return revenue; }
    public Double getProfit() { return profit; }
}
