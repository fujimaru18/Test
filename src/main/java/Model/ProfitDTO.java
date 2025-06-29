package Model;

public class ProfitDTO {
    private String timeLabel;
    private double revenue;
    private double cost;
    private double profit;

    public ProfitDTO(String timeLabel, double revenue, double cost) {
        this.timeLabel = timeLabel;
        this.revenue = revenue;
        this.cost = cost;
        this.profit = revenue - cost;
    }

    public String getTimeLabel() { return timeLabel; }
    public double getRevenue() { return revenue; }
    public double getCost() { return cost; }
    public double getProfit() { return profit; }
}
