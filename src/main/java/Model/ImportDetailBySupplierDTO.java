package Model;

public class ImportDetailBySupplierDTO {

    private final String supplierName;
    private final String categoryName;
    private final String productName;
    private final int quantity;
    private final double importPrice;   // đơn giá nhập

    public ImportDetailBySupplierDTO(String supplierName, String categoryName,
            String productName, int quantity, double importPrice) {
        this.supplierName = supplierName;
        this.categoryName = categoryName;
        this.productName = productName;
        this.quantity = quantity;
        this.importPrice = importPrice;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getImportPrice() {
        return importPrice;
    }

    public double getTotal() {
        return quantity * importPrice;
    }
}
