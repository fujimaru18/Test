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
import View.ImportReceiptView;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ImportReceiptController {

    private ImportReceiptDAO receiptDAO;
    private ImportReceiptDetailDAO detailDAO;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private ImportReceiptView importReceiptView;

//    public ImportReceiptController() {
//       
//    }
    public ImportReceiptController(ImportReceiptView view) {
        this.importReceiptView = view;

        receiptDAO = new ImportReceiptDAO();
        detailDAO = new ImportReceiptDetailDAO();
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
    }

    public void clearFormFieldsController() {
        importReceiptView.cboCategory.setSelectedIndex(0);
        importReceiptView.cboProduct.removeAllItems();
        importReceiptView.txtQuantity.setText("");
        importReceiptView.txtImportPrice.setText("");
        importReceiptView.txtNote.setText("");

        importReceiptView.detailTableModel.setRowCount(0);
        importReceiptView.currentDetails.clear();
    }

    public void addProductToListController() {
        try {
            Product selectedProduct = (Product) importReceiptView.cboProduct.getSelectedItem();
            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(importReceiptView, "Chưa chọn sản phẩm");
                return;
            }

            int quantity = Integer.parseInt(importReceiptView.txtQuantity.getText().trim());
            double importPrice = Double.parseDouble(importReceiptView.txtImportPrice.getText().trim());

            ImportReceiptDetail detail = new ImportReceiptDetail();
            detail.setProductId(selectedProduct.getProductId());
            detail.setQuantity(quantity);
            detail.setImportPrice(importPrice);
            importReceiptView.currentDetails.add(detail);

            importReceiptView.detailTableModel.addRow(new Object[]{
                selectedProduct.getProductName(), quantity, importPrice
            });

            importReceiptView.txtQuantity.setText("");
            importReceiptView.txtImportPrice.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(importReceiptView, "Vui lòng nhập đầy đủ thông tin: ");
        }
    }

    public void filterProductsByCategoryController() {
        importReceiptView.cboProduct.removeAllItems();
        String selectedCategory = (String) importReceiptView.cboCategory.getSelectedItem();
        if (selectedCategory == null || selectedCategory.equals("-- Chọn danh mục --")) {
            return;
        }

        Integer categoryId = importReceiptView.categoryMap.get(selectedCategory);
        for (Product p : importReceiptView.allProducts) {
            if (p.getCategoryId() == categoryId) {
                importReceiptView.cboProduct.addItem(p);
            }
        }
    }

    public void loadCategoriesController() {
        try {
            List<Category> categories = getAllCategories();
            importReceiptView.cboCategory.addItem("-- Chọn danh mục --");
            for (Category c : categories) {
                importReceiptView.cboCategory.addItem(c.getCategoryName());
                importReceiptView.categoryMap.put(c.getCategoryName(), c.getCategoryId());
            }
            importReceiptView.cboCategory.setSelectedIndex(0);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(importReceiptView, "Lỗi tải danh mục: " + e.getMessage());
        }
    }

    public void searchDetailsController(String keyword) {
        importReceiptView.detailTableModel.setRowCount(0);

        try {
            if (keyword.isEmpty()) {
                // Hiển thị lại toàn bộ chi tiết
                for (ImportReceiptDetail d : importReceiptView.currentDetails) {
                    Product p = getProductById(d.getProductId());
                    if (p != null) {
                        importReceiptView.detailTableModel.addRow(new Object[]{
                            p.getProductName(), d.getQuantity(), d.getImportPrice()
                        });
                    }
                }
                return;
            }

            for (ImportReceiptDetail d : importReceiptView.currentDetails) {
                Product p = getProductById(d.getProductId());
                if (p != null && (p.getProductName().toLowerCase().contains(keyword.toLowerCase())
                        || String.valueOf(d.getQuantity()).contains(keyword)
                        || String.valueOf(d.getImportPrice()).contains(keyword))) {

                    importReceiptView.detailTableModel.addRow(new Object[]{
                        p.getProductName(), d.getQuantity(), d.getImportPrice()
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(importReceiptView, "Lỗi khi tìm kiếm chi tiết: " + ex.getMessage());
        }
    }

    public void searchReceiptsController() {
        String keyword = importReceiptView.txtSearch.getText().trim();
        try {
            List<ImportReceipt> results = searchImportReceipts(keyword);
            importReceiptView.receiptTableModel.setRowCount(0);

            for (ImportReceipt r : results) {
                String userName = getUserNameById(r.getUserId());
                String fullName = getUserFullNameById(r.getUserId());

                String displayName = (fullName != null && !fullName.trim().isEmpty())
                        ? fullName + " (" + userName + ")"
                        : userName;

                importReceiptView.receiptTableModel.addRow(new Object[]{
                    r.getReceiptId(),
                    displayName,
                    r.getImportDate(),
                    r.getNote() != null ? r.getNote() : ""
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(importReceiptView, "Lỗi tìm kiếm: " + ex.getMessage());
        }
    }

    public void deleteSelectedProductController() {
        int selectedRow = importReceiptView.detailTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(importReceiptView, "Vui lòng chọn sản phẩm cần xóa trong bảng chi tiết.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(importReceiptView,
                "Bạn có chắc muốn xóa sản phẩm này khỏi chi tiết phiếu nhập?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Xóa khỏi currentDetails (danh sách chi tiết đang quản lý)
            importReceiptView.currentDetails.remove(selectedRow);
            // Xóa khỏi table model để cập nhật UI
            importReceiptView.detailTableModel.removeRow(selectedRow);

            // Reset chọn dòng
            importReceiptView.selectedDetailRow = -1;

            importReceiptView.txtQuantity.setText("");
            importReceiptView.txtImportPrice.setText("");
            importReceiptView.txtNote.setText("");
        }
        updateReceiptController();
    }

    public void saveReceiptController() {
        if (importReceiptView.currentDetails.isEmpty()) {
            JOptionPane.showMessageDialog(importReceiptView, "Vui lòng thêm sản phẩm trước khi lưu");
            return;
        }

        try {
            ImportReceipt receipt = new ImportReceipt();
            receipt.setUserId(1);
            receipt.setImportDate(new Timestamp(System.currentTimeMillis()));
            receipt.setNote(importReceiptView.txtNote.getText().trim().isEmpty() ? null : importReceiptView.txtNote.getText().trim());

            boolean result = addImportReceipt(receipt, importReceiptView.currentDetails);
            if (result) {
                JOptionPane.showMessageDialog(importReceiptView, "Lưu phiếu nhập thành công");
                importReceiptView.currentDetails.clear();
                importReceiptView.detailTableModel.setRowCount(0);
                importReceiptView.txtNote.setText("");
                loadReceiptsController();
            } else {
                JOptionPane.showMessageDialog(importReceiptView, "Lỗi khi lưu phiếu nhập");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(importReceiptView, "Lỗi CSDL: " + ex.getMessage());
        }
    }

    public void deleteReceiptController() {
        int selectedRow = importReceiptView.receiptTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(importReceiptView, "Chọn phiếu cần xóa");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(importReceiptView, "Xác nhận xóa phiếu này?", "Xóa phiếu", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int receiptId = (int) importReceiptView.receiptTableModel.getValueAt(selectedRow, 0);
            boolean ok = deleteImportReceipt(receiptId);

            if (ok) {
                JOptionPane.showMessageDialog(importReceiptView, "Xóa thành công");
                loadReceiptsController();
                importReceiptView.detailTableModel.setRowCount(0);
                importReceiptView.currentDetails.clear();
                importReceiptView.selectedDetailRow = -1;
            } else {
                JOptionPane.showMessageDialog(importReceiptView, "Xóa thất bại");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(importReceiptView, "Lỗi khi xóa: " + ex.getMessage());
        }
    }

    public void loadReceiptsController() {
        importReceiptView.receiptTableModel.setRowCount(0);
        try {
            List<ImportReceipt> receipts = getAllImportReceipts();
            for (ImportReceipt r : receipts) {
                String userName = getUserNameById(r.getUserId());
                String fullName = getUserFullNameById(r.getUserId());

                String displayName = (fullName != null && !fullName.trim().isEmpty())
                        ? fullName + " (" + userName + ")"
                        : userName;

                importReceiptView.receiptTableModel.addRow(new Object[]{
                    r.getReceiptId(),
                    displayName,
                    r.getImportDate(),
                    r.getNote() != null ? r.getNote() : ""
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(importReceiptView, "Lỗi tải phiếu nhập: " + ex.getMessage());
        }
    }

    public void loadSelectedReceiptController() {
        int row = importReceiptView.receiptTable.getSelectedRow();
        if (row == -1) {
            return;
        }
        int receiptId = Integer.parseInt(importReceiptView.receiptTableModel.getValueAt(row, 0).toString());

        try {
            importReceiptView.currentDetails.clear(); // Xóa danh sách chi tiết hiện tại
            importReceiptView.currentDetails = getDetailsByReceiptId(receiptId);
            importReceiptView.detailTableModel.setRowCount(0);
            for (ImportReceiptDetail d : importReceiptView.currentDetails) {
                Product p = getProductById(d.getProductId());
                importReceiptView.detailTableModel.addRow(new Object[]{
                    p.getProductName(), d.getQuantity(), d.getImportPrice()
                });
            }

            importReceiptView.txtNote.setText("");
            importReceiptView.txtQuantity.setText("");
            importReceiptView.txtImportPrice.setText("");
            importReceiptView.cboCategory.setSelectedIndex(0);
            importReceiptView.cboProduct.removeAllItems();

            importReceiptView.selectedDetailRow = -1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(importReceiptView, "Lỗi tải chi tiết: " + ex.getMessage());
        }
    }

    public void updateReceiptController() {
        // Kết thúc cell editor nếu đang chỉnh sửa trên bảng chi tiết
        if (importReceiptView.detailTable.isEditing()) {
            importReceiptView.detailTable.getCellEditor().stopCellEditing();
        }


        // Lấy dòng đang chọn của bảng chi tiết
        int selectedDetailRow = importReceiptView.detailTable.getSelectedRow();
        if (selectedDetailRow != -1) {
            try {
                // Lấy dữ liệu người dùng nhập ở các JTextField
                int newQuantity = Integer.parseInt(importReceiptView.txtQuantity.getText().trim());
                double newImportPrice = Double.parseDouble(importReceiptView.txtImportPrice.getText().trim());
                String newNote = importReceiptView.txtNote.getText().trim();

                // Cập nhật trực tiếp lên bảng chi tiết (model)
                importReceiptView.detailTableModel.setValueAt(newQuantity, selectedDetailRow, importReceiptView.quantityColumnIndex);
                importReceiptView.detailTableModel.setValueAt(newImportPrice, selectedDetailRow, importReceiptView.importPriceColumnIndex);
                // Nếu có cột ghi chú:

                // Cập nhật luôn vào currentDetails List (nếu bạn có lưu chi tiết ở đây)
                ImportReceiptDetail detail = importReceiptView.currentDetails.get(selectedDetailRow);
                detail.setQuantity(newQuantity);
                detail.setImportPrice(newImportPrice);
                // Nếu ImportReceiptDetail có note thì set thêm

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(importReceiptView, "Số lượng hoặc giá nhập phải là số hợp lệ");
                return;
            }
        }

        int selectedRow = importReceiptView.receiptTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(importReceiptView, "Chọn phiếu cần sửa");
            return;
        }

        try {
            int receiptId = (int) importReceiptView.receiptTableModel.getValueAt(selectedRow, 0);
            ImportReceipt updated = new ImportReceipt();
            updated.setReceiptId(receiptId);
            updated.setUserId(1); // TODO: Lấy userId từ người dùng đăng nhập
            updated.setImportDate(new Timestamp(System.currentTimeMillis())); // Hoặc giữ ngày cũ nếu muốn
            updated.setNote(importReceiptView.txtNote.getText().trim().isEmpty() ? null : importReceiptView.txtNote.getText().trim());

//            if (importReceiptView.currentDetails.isEmpty()) {
//                JOptionPane.showMessageDialog(importReceiptView, "Chi tiết phiếu nhập trống!");
//                return;
//            }

            boolean ok = updateImportReceipt(updated, importReceiptView.currentDetails);
            if (ok) {
                JOptionPane.showMessageDialog(importReceiptView, "Cập nhật thành công");
                loadReceiptsController();
                loadSelectedReceiptController();
                // Xóa selection chi tiết hoặc reset input nếu cần
                importReceiptView.detailTable.clearSelection();
                importReceiptView.txtQuantity.setText("");
                importReceiptView.txtImportPrice.setText("");
                importReceiptView.txtNote.setText("");
            } else {
                JOptionPane.showMessageDialog(importReceiptView, "Cập nhật thất bại");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(importReceiptView, "Lỗi cập nhật: " + ex.getMessage());
        }
    }

    public void loadAllProductsController() {
        try {
            importReceiptView.allProducts = getAllProducts();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(importReceiptView, "Lỗi tải sản phẩm: " + e.getMessage());
        }
    }

    public void loadSelectedDetailRowController() {
        int selectedRow = importReceiptView.detailTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        String productName = (String) importReceiptView.detailTableModel.getValueAt(selectedRow, 0);
        int quantity = (int) importReceiptView.detailTableModel.getValueAt(selectedRow, 1);
        double price = (double) importReceiptView.detailTableModel.getValueAt(selectedRow, 2);

        importReceiptView.txtQuantity.setText(String.valueOf(quantity));
        importReceiptView.txtImportPrice.setText(String.valueOf(price));

        // 🔁 Ghi chú lấy từ dòng đang chọn bên receiptTable
        int receiptRow = importReceiptView.receiptTable.getSelectedRow();
        if (receiptRow != -1) {
            String note = (String) importReceiptView.receiptTableModel.getValueAt(receiptRow, 3);
            importReceiptView.txtNote.setText(note != null ? note : "");
        }

        // Tìm Product để đổ lại combo box
        for (Product p : importReceiptView.allProducts) {
            if (p.getProductName().equals(productName)) {
                for (Map.Entry<String, Integer> entry : importReceiptView.categoryMap.entrySet()) {
                    if (entry.getValue() == p.getCategoryId()) {
                        importReceiptView.cboCategory.setSelectedItem(entry.getKey());
                        break;
                    }
                }
                SwingUtilities.invokeLater(() -> importReceiptView.cboProduct.setSelectedItem(p));
                break;
            }
        }
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
