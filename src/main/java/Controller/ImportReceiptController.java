package Controller;

import DAO.CategoryDAO;
import DAO.ImportReceiptDAO;
import DAO.ImportReceiptDetailDAO;
import DAO.ProductDAO;
import DAO.UserDAO;
import Model.Category;
import Model.ImportReceipt;
import Model.ImportReceiptDetail;
import Model.Product;

import java.sql.SQLException;
import java.util.List;

public class ImportReceiptController {

    private ImportReceiptDAO receiptDAO;
    private ImportReceiptDetailDAO detailDAO;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    public ImportReceiptController() {
        receiptDAO = new ImportReceiptDAO();
        detailDAO = new ImportReceiptDetailDAO();
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
    }

    // Thêm phiếu nhập cùng danh sách chi tiết
    public boolean addImportReceipt(ImportReceipt receipt, List<ImportReceiptDetail> details) throws SQLException {
        boolean added = receiptDAO.addImportReceipt(receipt);
        if (!added) {
            return false;
        }

        for (ImportReceiptDetail detail : details) {
            detail.setReceiptId(receipt.getReceiptId());
            boolean success = detailDAO.addImportReceiptDetail(detail);
            if (!success) {
                return false;
            }
            // Nếu có cập nhật tồn kho, đã xử lý bên DAO chi tiết
        }

        return true;
    }

    // Cập nhật phiếu nhập cùng chi tiết
    public boolean updateImportReceipt(ImportReceipt receipt, List<ImportReceiptDetail> details) throws SQLException {
        return receiptDAO.updateImportReceipt(receipt, details);
    }

    public List<ImportReceipt> searchImportReceipts(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllImportReceipts();
        }

        // Nếu keyword là số (toàn bộ ký tự là số), tìm theo mã phiếu
        if (keyword.matches("\\d+")) {
            return receiptDAO.searchImportReceiptsByReceiptId(keyword);
        }

        // Nếu không, tìm theo tất cả các trường
        return receiptDAO.searchImportReceiptsByKeyword(keyword);
    }

    public boolean deleteImportReceipt(int receiptId) throws SQLException {
        return receiptDAO.deleteImportReceipt(receiptId);
    }

    public List<ImportReceipt> getAllImportReceipts() throws SQLException {
        return receiptDAO.getAllImportReceipts();
    }

    public List<ImportReceiptDetail> getDetailsByReceiptId(int receiptId) throws SQLException {
        return detailDAO.getDetailsByReceiptId(receiptId);
    }

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.getAllCategories();
    }

    public List<Product> getProductsByCategoryId(int categoryId) throws SQLException {
        return productDAO.getProductsByCategoryId(categoryId);
    }

    public Product getProductById(int productId) throws SQLException {
        return productDAO.getProductById(productId);
    }

    public List<Product> getAllProducts() throws SQLException {
        return productDAO.getAllProducts();
    }

    public String getUserNameById(int userId) throws SQLException {
        return UserDAO.getUserNameById(userId);
    }

    public String getUserFullNameById(int userId) throws SQLException {
        return UserDAO.getUserFullNameById(userId);
    }

}
