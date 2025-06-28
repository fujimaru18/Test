package Model;

public class SanPham {

    private int id;
    private String name;
    private int importPrice;
    private int salePrice;
    private int stockQuantity;
    private String unit;
    private DanhMuc categoryId;
    private String supplierId;
    private String status;
    private byte[] image;

    public SanPham() {
    }

    public SanPham(int id, String name, int importPrice, int salePrice, int stockQuantity, String unit, DanhMuc categoryId, String supplierId, String status, byte[] image) {
        this.id = id;
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

    public int getId() {
        return id;
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

    public DanhMuc getCategoryId() {
        return categoryId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getStatus() {
        return status;
    }

    public byte[] getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setCategoryId(DanhMuc categoryId) {
        this.categoryId = categoryId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return id + name + importPrice + salePrice + stockQuantity + unit + categoryId + supplierId + status + image.length;
    }

}
