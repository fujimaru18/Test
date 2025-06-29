package Model;

public class Product {

    private int productId;
    private String productName;
    private int categoryId;
    private int stockQuantity;

    public Product() {
    }

    public Product(int productId, String productName, int categoryId, int stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.stockQuantity = stockQuantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return productName;
    }
}
