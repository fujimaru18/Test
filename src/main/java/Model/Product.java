package Model;

public class Product {

    private int productId;
    private String name;
    private int importPrice;
    private int salePrice;
    private int stockQuantity;
    private String unit;
    private Category categoryId;
    private Supplier supplierId;
    private int status;
    private byte[] image;

    public Product() {
    }

    public Product(int productId, String name, int importPrice, int salePrice, int stockQuantity, String unit, Category categoryId, Supplier supplierId, int status, byte[] image) {
        this.productId = productId;
        this.name = name;
        this.importPrice = importPrice;
        this.salePrice = salePrice;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.status = status;
        this.image = image;
    }

    public int getproductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getImportPrice() {
        return importPrice;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public Supplier getSupplierId() {
        return supplierId;
    }

    public int getStatus() {
        return status;
    }

    public byte[] getImage() {
        return image;
    }

    public void setproductId(int productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImportPrice(int importPrice) {
        this.importPrice = importPrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    public void setSupplierId(Supplier supplierId) {
        this.supplierId = supplierId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return  name;
    }

}
