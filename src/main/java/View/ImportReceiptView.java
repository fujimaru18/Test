package View;

import Controller.ImportReceiptController;
import Model.Category;
import Model.ImportReceipt;
import Model.ImportReceiptDetail;
import Model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;
import javax.swing.event.TableModelEvent;

public class ImportReceiptView extends JFrame {

    private ImportReceiptController controller;
    private JTable receiptTable;
    private JTable detailTable;
    private DefaultTableModel receiptTableModel;
    private DefaultTableModel detailTableModel;
    private JComboBox<String> cboCategory;
    private JComboBox<Product> cboProduct;
    private JTextField txtQuantity, txtImportPrice, txtNote;
    private JButton btnAddProduct, btnSaveReceipt, btnClear;
    private JButton btnDeleteProduct;
    private Map<String, Integer> categoryMap = new HashMap<>();
    private List<Product> allProducts = new ArrayList<>();
    private List<ImportReceiptDetail> currentDetails = new ArrayList<>();
    private JButton btnEditReceipt, btnDeleteReceipt, btnSearch;
    private JTextField txtSearch;
    private final int quantityColumnIndex = 1;
    private final int importPriceColumnIndex = 2;

    private int selectedDetailRow = -1; // chỉ số dòng chi tiết đang chọn

    public ImportReceiptView() {
        controller = new ImportReceiptController();
        initUI();
        loadCategories();
        loadAllProducts();
        loadReceipts();
    }

    private void initUI() {
        setTitle("Quản lý nhập hàng");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Phần trên cùng: inputPanel + detailTable với tìm kiếm (song song)
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        topPanel.add(createInputPanel());
        topPanel.add(createDetailTableWithSearchPanel());

        mainPanel.add(topPanel, BorderLayout.CENTER);

        // Phần dưới: actionPanel + bảng danh sách phiếu nhập
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(createActionPanel(), BorderLayout.NORTH);
        bottomPanel.add(createReceiptTablePanel(), BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        addEventListeners();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin nhập hàng"));

        panel.add(new JLabel("Danh mục:"));
        cboCategory = new JComboBox<>();
        panel.add(cboCategory);

        panel.add(new JLabel("Sản phẩm:"));
        cboProduct = new JComboBox<>();
        panel.add(cboProduct);

        panel.add(new JLabel("Số lượng:"));
        txtQuantity = new JTextField();
        panel.add(txtQuantity);

        panel.add(new JLabel("Giá nhập:"));
        txtImportPrice = new JTextField();
        panel.add(txtImportPrice);

        panel.add(new JLabel("Ghi chú:"));
        txtNote = new JTextField();
        panel.add(txtNote);

        // Ô trống bên trái dòng cuối
        panel.add(new JLabel());

        // Panel bọc nút, căn phải với padding
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        btnAddProduct = new JButton("➕ Thêm SP");
        btnAddProduct.setPreferredSize(new Dimension(100, 25));
        btnAddProduct.setFont(new Font(btnAddProduct.getFont().getName(), Font.PLAIN, 12));
        buttonPanel.add(btnAddProduct);

        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createDetailTableWithSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Bảng chi tiết
        detailTableModel = new DefaultTableModel(
                new Object[]{"Tên SP", "Số lượng", "Giá nhập"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 1 || col == 2;
            }

            @Override
            public Class<?> getColumnClass(int col) {
                return switch (col) {
                    case 1 ->
                        Integer.class;
                    case 2 ->
                        Double.class;
                    default ->
                        String.class;
                };
            }
        };

        detailTable = new JTable(detailTableModel);
        detailTableModel.addTableModelListener(this::handleDetailTableChange);

        JScrollPane detailScroll = new JScrollPane(detailTable);
        detailScroll.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));

        // Thanh tìm kiếm + nút xóa
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField txtDetailSearch = new JTextField(15);
        JButton btnDetailSearch = new JButton("🔍 Tìm chi tiết");
        JButton btnDeleteProduct = new JButton("❌ Xóa SP");

        searchPanel.add(txtDetailSearch);
        searchPanel.add(btnDetailSearch);
        searchPanel.add(btnDeleteProduct);

        btnDetailSearch.addActionListener(e -> searchDetails(txtDetailSearch.getText().trim()));
        btnDeleteProduct.addActionListener(e -> deleteSelectedProduct());

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(detailScroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReceiptTablePanel() {
        receiptTableModel = new DefaultTableModel(
                new Object[]{"Mã phiếu", "Người nhập (Tên / Tài khoản)", "Ngày nhập", "Ghi chú"}, 0);

        receiptTable = new JTable(receiptTableModel);
        JScrollPane receiptScroll = new JScrollPane(receiptTable);
        receiptScroll.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu nhập"));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(receiptScroll, BorderLayout.CENTER);

        panel.setPreferredSize(new Dimension(0, 200)); // chiều cao cố định cho bảng dưới

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // căn giữa
        panel.setBorder(BorderFactory.createTitledBorder("Chức năng"));

        btnSaveReceipt = new JButton("💾 Lưu phiếu");
        btnEditReceipt = new JButton("✏️ Sửa phiếu");
        btnDeleteReceipt = new JButton("🗑 Xóa phiếu");
        btnClear = new JButton("Clear");
        btnSearch = new JButton("🔍 Tìm");
        txtSearch = new JTextField(20);

        panel.add(btnSaveReceipt);
        panel.add(btnEditReceipt);
        panel.add(btnDeleteReceipt);
        panel.add(btnClear);
        panel.add(txtSearch);
        panel.add(btnSearch);

        return panel;
    }

    private void handleDetailTableChange(TableModelEvent e) {
        int row = e.getFirstRow();
        int col = e.getColumn();

        if (row >= 0 && col >= 0 && currentDetails != null && row < currentDetails.size()) {
            selectedDetailRow = row;
            try {
                ImportReceiptDetail detail = currentDetails.get(row);
                if (col == 1) {
                    detail.setQuantity(Integer.parseInt(detailTableModel.getValueAt(row, 1).toString()));
                } else if (col == 2) {
                    detail.setImportPrice(Double.parseDouble(detailTableModel.getValueAt(row, 2).toString()));
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ ở dòng " + (row + 1));
            }
        }
    }

    private void addEventListeners() {
        cboCategory.addActionListener(e -> filterProductsByCategory());
        btnAddProduct.addActionListener(e -> addProductToList());
        btnSaveReceipt.addActionListener(e -> saveReceipt());
        btnEditReceipt.addActionListener(e -> updateReceipt());
        btnDeleteReceipt.addActionListener(e -> deleteReceipt());
        btnSearch.addActionListener(e -> searchReceipts());
        btnClear.addActionListener(e -> clearFormFields());
        detailTable.getSelectionModel().addListSelectionListener(e -> loadSelectedDetailRow());
        receiptTable.getSelectionModel().addListSelectionListener(e -> loadSelectedReceipt());
    }

//    -----------------------------------------------------------
    private List<ImportReceiptDetail> getDetailsFromTable(int receiptId) {
        List<ImportReceiptDetail> list = new ArrayList<>();
        for (int i = 0; i < detailTableModel.getRowCount(); i++) {
            ImportReceiptDetail detail = new ImportReceiptDetail();
            detail.setReceiptId(receiptId);
            detail.setProductId(Integer.parseInt(detailTableModel.getValueAt(i, 0).toString()));
            detail.setQuantity(Integer.parseInt(detailTableModel.getValueAt(i, 1).toString()));
            detail.setImportPrice(Double.parseDouble(detailTableModel.getValueAt(i, 2).toString()));
            list.add(detail);
        }
        return list;
    }

    private void clearFormFields() {
        cboCategory.setSelectedIndex(0);
        cboProduct.removeAllItems();
        txtQuantity.setText("");
        txtImportPrice.setText("");
        txtNote.setText("");

        detailTableModel.setRowCount(0);
        currentDetails.clear();
    }

    private void loadSelectedDetailRow() {
        int selectedRow = detailTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        String productName = (String) detailTableModel.getValueAt(selectedRow, 0);
        int quantity = (int) detailTableModel.getValueAt(selectedRow, 1);
        double price = (double) detailTableModel.getValueAt(selectedRow, 2);

        txtQuantity.setText(String.valueOf(quantity));
        txtImportPrice.setText(String.valueOf(price));

        // 🔁 Ghi chú lấy từ dòng đang chọn bên receiptTable
        int receiptRow = receiptTable.getSelectedRow();
        if (receiptRow != -1) {
            String note = (String) receiptTableModel.getValueAt(receiptRow, 3);
            txtNote.setText(note != null ? note : "");
        }

        // Tìm Product để đổ lại combo box
        for (Product p : allProducts) {
            if (p.getProductName().equals(productName)) {
                for (Map.Entry<String, Integer> entry : categoryMap.entrySet()) {
                    if (entry.getValue() == p.getCategoryId()) {
                        cboCategory.setSelectedItem(entry.getKey());
                        break;
                    }
                }
                SwingUtilities.invokeLater(() -> cboProduct.setSelectedItem(p));
                break;
            }
        }
    }

    private boolean syncAllDetailsFromTable() {
        currentDetails.clear();
        try {
            for (int i = 0; i < detailTableModel.getRowCount(); i++) {
                String productName = (String) detailTableModel.getValueAt(i, 0);
                int quantity = Integer.parseInt(detailTableModel.getValueAt(i, 1).toString());
                double price = Double.parseDouble(detailTableModel.getValueAt(i, 2).toString());

                Product matched = allProducts.stream()
                        .filter(p -> p.getProductName().equals(productName))
                        .findFirst()
                        .orElse(null);

                if (matched == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm: " + productName);
                    return false;
                }

                ImportReceiptDetail detail = new ImportReceiptDetail();
                detail.setProductId(matched.getProductId());
                detail.setQuantity(quantity);
                detail.setImportPrice(price);

                currentDetails.add(detail);
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng hoặc giá nhập không hợp lệ");
            return false;
        }
    }

    private void updateReceipt() {
        // Kết thúc cell editor nếu đang chỉnh sửa trên bảng chi tiết
        if (detailTable.isEditing()) {
            detailTable.getCellEditor().stopCellEditing();
        }

        // Đồng bộ toàn bộ dữ liệu từ bảng chi tiết vào currentDetails
        if (!syncAllDetailsFromTable()) {
            return; // Nếu lỗi thì thoát
        }

        // Lấy dòng đang chọn của bảng chi tiết
        int selectedDetailRow = detailTable.getSelectedRow();
        if (selectedDetailRow != -1) {
            try {
                // Lấy dữ liệu người dùng nhập ở các JTextField
                int newQuantity = Integer.parseInt(txtQuantity.getText().trim());
                double newImportPrice = Double.parseDouble(txtImportPrice.getText().trim());
                String newNote = txtNote.getText().trim();

                // Cập nhật trực tiếp lên bảng chi tiết (model)
                detailTableModel.setValueAt(newQuantity, selectedDetailRow, quantityColumnIndex);
                detailTableModel.setValueAt(newImportPrice, selectedDetailRow, importPriceColumnIndex);
                // Nếu có cột ghi chú:

                // Cập nhật luôn vào currentDetails List (nếu bạn có lưu chi tiết ở đây)
                ImportReceiptDetail detail = currentDetails.get(selectedDetailRow);
                detail.setQuantity(newQuantity);
                detail.setImportPrice(newImportPrice);
                // Nếu ImportReceiptDetail có note thì set thêm

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số lượng hoặc giá nhập phải là số hợp lệ");
                return;
            }
        }

        int selectedRow = receiptTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn phiếu cần sửa");
            return;
        }

        try {
            int receiptId = (int) receiptTableModel.getValueAt(selectedRow, 0);
            ImportReceipt updated = new ImportReceipt();
            updated.setReceiptId(receiptId);
            updated.setUserId(1); // TODO: Lấy userId từ người dùng đăng nhập
            updated.setImportDate(new Timestamp(System.currentTimeMillis())); // Hoặc giữ ngày cũ nếu muốn
            updated.setNote(txtNote.getText().trim().isEmpty() ? null : txtNote.getText().trim());

            if (currentDetails.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chi tiết phiếu nhập trống!");
                return;
            }

            boolean ok = controller.updateImportReceipt(updated, currentDetails);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                loadReceipts();
                loadSelectedReceipt();
                // Xóa selection chi tiết hoặc reset input nếu cần
                detailTable.clearSelection();
                txtQuantity.setText("");
                txtImportPrice.setText("");
                txtNote.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage());
        }
    }

    private void loadSelectedReceipt() {
        int row = receiptTable.getSelectedRow();
        if (row == -1) {
            return;
        }
        int receiptId = Integer.parseInt(receiptTableModel.getValueAt(row, 0).toString());

        try {
            currentDetails.clear(); // Xóa danh sách chi tiết hiện tại
            currentDetails = controller.getDetailsByReceiptId(receiptId);
            detailTableModel.setRowCount(0);
            for (ImportReceiptDetail d : currentDetails) {
                Product p = controller.getProductById(d.getProductId());
                detailTableModel.addRow(new Object[]{
                    p.getProductName(), d.getQuantity(), d.getImportPrice()
                });
            }

            txtNote.setText("");
            txtQuantity.setText("");
            txtImportPrice.setText("");
            cboCategory.setSelectedIndex(0);
            cboProduct.removeAllItems();

            selectedDetailRow = -1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải chi tiết: " + ex.getMessage());
        }
    }

    private void deleteReceipt() {
        int selectedRow = receiptTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn phiếu cần xóa");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa phiếu này?", "Xóa phiếu", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int receiptId = (int) receiptTableModel.getValueAt(selectedRow, 0);
            boolean ok = controller.deleteImportReceipt(receiptId);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                loadReceipts();
                detailTableModel.setRowCount(0);
                currentDetails.clear();
                selectedDetailRow = -1;
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage());
        }
    }

    private void deleteSelectedProduct() {
        int selectedRow = detailTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa trong bảng chi tiết.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa sản phẩm này khỏi chi tiết phiếu nhập?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Xóa khỏi currentDetails (danh sách chi tiết đang quản lý)
            currentDetails.remove(selectedRow);
            // Xóa khỏi table model để cập nhật UI
            detailTableModel.removeRow(selectedRow);

            // Reset chọn dòng
            selectedDetailRow = -1;

            // Có thể reset luôn các input nếu muốn
            txtQuantity.setText("");
            txtImportPrice.setText("");
            txtNote.setText("");
        }
    }

    private void searchReceipts() {
        String keyword = txtSearch.getText().trim();
        try {
            List<ImportReceipt> results = controller.searchImportReceipts(keyword);
            receiptTableModel.setRowCount(0);

            for (ImportReceipt r : results) {
                String userName = controller.getUserNameById(r.getUserId());
                String fullName = controller.getUserFullNameById(r.getUserId());

                String displayName = (fullName != null && !fullName.trim().isEmpty())
                        ? fullName + " (" + userName + ")"
                        : userName;

                receiptTableModel.addRow(new Object[]{
                    r.getReceiptId(),
                    displayName,
                    r.getImportDate(),
                    r.getNote() != null ? r.getNote() : ""
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage());
        }
    }

    private void searchDetails(String keyword) {
        detailTableModel.setRowCount(0);

        try {
            if (keyword.isEmpty()) {
                // Hiển thị lại toàn bộ chi tiết
                for (ImportReceiptDetail d : currentDetails) {
                    Product p = controller.getProductById(d.getProductId());
                    if (p != null) {
                        detailTableModel.addRow(new Object[]{
                            p.getProductName(), d.getQuantity(), d.getImportPrice()
                        });
                    }
                }
                return;
            }

            for (ImportReceiptDetail d : currentDetails) {
                Product p = controller.getProductById(d.getProductId());
                if (p != null && (p.getProductName().toLowerCase().contains(keyword.toLowerCase())
                        || String.valueOf(d.getQuantity()).contains(keyword)
                        || String.valueOf(d.getImportPrice()).contains(keyword))) {

                    detailTableModel.addRow(new Object[]{
                        p.getProductName(), d.getQuantity(), d.getImportPrice()
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm chi tiết: " + ex.getMessage());
        }
    }

    private void loadCategories() {
        try {
            List<Category> categories = controller.getAllCategories();
            cboCategory.addItem("-- Chọn danh mục --");
            for (Category c : categories) {
                cboCategory.addItem(c.getCategoryName());
                categoryMap.put(c.getCategoryName(), c.getCategoryId());
            }
            cboCategory.setSelectedIndex(0);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh mục: " + e.getMessage());
        }
    }

    private void loadAllProducts() {
        try {
            allProducts = controller.getAllProducts();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải sản phẩm: " + e.getMessage());
        }
    }

    private void filterProductsByCategory() {
        cboProduct.removeAllItems();
        String selectedCategory = (String) cboCategory.getSelectedItem();
        if (selectedCategory == null || selectedCategory.equals("-- Chọn danh mục --")) {
            return;
        }

        Integer categoryId = categoryMap.get(selectedCategory);
        for (Product p : allProducts) {
            if (p.getCategoryId() == categoryId) {
                cboProduct.addItem(p);
            }
        }
    }

    private void addProductToList() {
        try {
            Product selectedProduct = (Product) cboProduct.getSelectedItem();
            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(this, "Chưa chọn sản phẩm");
                return;
            }

            int quantity = Integer.parseInt(txtQuantity.getText().trim());
            double importPrice = Double.parseDouble(txtImportPrice.getText().trim());

            ImportReceiptDetail detail = new ImportReceiptDetail();
            detail.setProductId(selectedProduct.getProductId());
            detail.setQuantity(quantity);
            detail.setImportPrice(importPrice);
            currentDetails.add(detail);

            detailTableModel.addRow(new Object[]{
                selectedProduct.getProductName(), quantity, importPrice
            });

            txtQuantity.setText("");
            txtImportPrice.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin: ");
        }
    }

    private void saveReceipt() {
        if (currentDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng thêm sản phẩm trước khi lưu");
            return;
        }

        try {
            ImportReceipt receipt = new ImportReceipt();
            receipt.setUserId(1);
            receipt.setImportDate(new Timestamp(System.currentTimeMillis()));
            receipt.setNote(txtNote.getText().trim().isEmpty() ? null : txtNote.getText().trim());

            boolean result = controller.addImportReceipt(receipt, currentDetails);
            if (result) {
                JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thành công");
                currentDetails.clear();
                detailTableModel.setRowCount(0);
                txtNote.setText("");
                loadReceipts();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu nhập");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi CSDL: " + ex.getMessage());
        }
    }

    private void loadReceipts() {
        receiptTableModel.setRowCount(0);
        try {
            List<ImportReceipt> receipts = controller.getAllImportReceipts();
            for (ImportReceipt r : receipts) {
                String userName = controller.getUserNameById(r.getUserId());
                String fullName = controller.getUserFullNameById(r.getUserId());

                String displayName = (fullName != null && !fullName.trim().isEmpty())
                        ? fullName + " (" + userName + ")"
                        : userName;

                receiptTableModel.addRow(new Object[]{
                    r.getReceiptId(),
                    displayName,
                    r.getImportDate(),
                    r.getNote() != null ? r.getNote() : ""
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải phiếu nhập: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImportReceiptView().setVisible(true));
    }
}
