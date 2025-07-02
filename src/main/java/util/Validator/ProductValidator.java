package util.Validator;

public class ProductValidator {

    public static boolean isValid(String name, String importPrice, String salePrice, String stockQuantity,
                                  String unit, String categoryId, String supplierId, String status, byte[] productImage) {
        return isValidName(name)
                && isValidImportPrice(importPrice)
                && isValidSalePrice(salePrice)
                && isValidStock(stockQuantity)
                && isValidUnit(unit)
                && isValidCategoryId(categoryId)
                && isValidSupplierId(supplierId)
                && isValidStatus(status)
                && isValidProductImage(productImage);
    }

    public static boolean isValidName(String name) {
        return CommonValidator.isNotEmpty(name, "Tên sản phẩm")
                && CommonValidator.isMaxLength(name, 100, "Tên sản phẩm");
    }

    public static boolean isValidImportPrice(String importPrice) {
        return CommonValidator.isPositiveInteger(importPrice, "Giá nhập");
    }

    public static boolean isValidSalePrice(String salePrice) {
        return CommonValidator.isPositiveInteger(salePrice, "Giá bán");
    }

    public static boolean isValidStock(String stockQuantity) {
        return CommonValidator.isPositiveInteger(stockQuantity, "Số lượng tồn");
    }

    public static boolean isValidUnit(String unit) {
        return CommonValidator.isNotEmpty(unit, "Đơn vị")
                && CommonValidator.isMaxLength(unit, 20, "Đơn vị");
    }

    public static boolean isValidCategoryId(String categoryId) {
        return CommonValidator.isNotEmpty(categoryId, "Danh mục");
    }

    public static boolean isValidSupplierId(String supplierId) {
        return CommonValidator.isNotEmpty(supplierId, "Nhà cung cấp");
    }

    public static boolean isValidStatus(String status) {
        return CommonValidator.isNotEmpty(status, "Trạng thái")
                && (status.equals("0") || status.equals("1"));
    }

    public static boolean isValidProductImage(byte[] productImage) {
        return CommonValidator.isValidImage(productImage, "Ảnh");
    }
}
