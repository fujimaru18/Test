package Model;

public class TopProductDTO {

    private String productName;
    private int quantitySold;
    private double totalRevenue;

    public TopProductDTO(String productName, int quantitySold, double totalRevenue) {
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.totalRevenue = totalRevenue;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}
