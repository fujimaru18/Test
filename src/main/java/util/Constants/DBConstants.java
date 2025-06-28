package util.Constants;

public class DBConstants {

    // Bảng Danh mục
    public static final String TABLE_CATEGORY = "categories";
    public static final String CATEGORY_ID = "categoryId";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String CATEGORY_DESCRIPTION = "description";

    // Bảng Sản phẩm
    public static final String TABLE_PRODUCT = "products";
    public static final String PRODUCT_ID = "productId";
    public static final String PRODUCT_NAME = "productName";
    public static final String PRODUCT_IMPORT_PRICE = "importPrice";
    public static final String PRODUCT_SALE_PRICE = "salePrice";
    public static final String PRODUCT_STOCK_QUANTITY = "stockQuantity";
    public static final String PRODUCT_UNIT = "unit";
    public static final String PRODUCT_CATEGORY_ID = "categoryId"; //FK bảng Category
    public static final String PRODUCT_SUPPLIER_ID = "supplierId";
    public static final String PRODUCT_STATUS = "status";
    public static final String PRODUCT_IMAGE = "productImage";

    //Bảng Nhà cung cấp
    public static final String TABLE_SUPPLIER = "suppliers";
    public static final String SUPPLIER_ID = "supplierId";
    public static final String SUPPLIER_NAME = "supplierName";
    public static final String SUPPLIER_PHONE_NUMBER = "phone";
    public static final String SUPPLIER_ADDRESS = "address";
    public static final String SUPPLIER_EMAIL = "email";
}
