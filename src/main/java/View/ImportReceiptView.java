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
import javax.swing.table.DefaultTableCellRenderer;

public class ImportReceiptView extends JFrame {

    public ImportReceiptController controller;
    public JTable receiptTable;
    public JTable detailTable;
    public DefaultTableModel receiptTableModel;
    public DefaultTableModel detailTableModel;
    public JComboBox<String> cboCategory;
    public JComboBox<Product> cboProduct;
    public JTextField txtQuantity, txtImportPrice, txtNote;
    public JButton btnAddProduct, btnSaveReceipt, btnClear;
    public JButton btnDeleteProduct;
    public Map<String, Integer> categoryMap = new HashMap<>();
    public List<Product> allProducts = new ArrayList<>();
    public List<ImportReceiptDetail> currentDetails = new ArrayList<>();
    public JButton btnEditReceipt, btnDeleteReceipt, btnSearch;
    public JTextField txtSearch;
    public final int quantityColumnIndex = 1;
    public final int importPriceColumnIndex = 2;

    public int selectedDetailRow = -1; // chỉ số dòng chi tiết đang chọn

    public ImportReceiptView() {
        controller = new ImportReceiptController(this);
        initUI();
        controller.loadCategoriesController();
        controller.loadAllProductsController();
        controller.loadReceiptsController();
    }

    private void initUI() {
        setTitle("Quản lý nhập hàng");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Phần trên cùng: inputPanel + detailTable với tìm kiếm
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBackground(new Color(245, 250, 255));

        JPanel inputPanel = createInputPanel();
        JPanel detailPanel = createDetailTableWithSearchPanel();

        // Set preferred size theo tỷ lệ 
        inputPanel.setPreferredSize(new Dimension(400, 400));   // 30%
        detailPanel.setPreferredSize(new Dimension(600, 400));  // 70%

        topPanel.add(inputPanel);
        topPanel.add(detailPanel);

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
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Thông tin nhập hàng"),
                BorderFactory.createEmptyBorder(0, 40, 0, 40) // top, left, bottom, right
        ));

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
        btnAddProduct = new JButton("+ Thêm SP");
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

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < detailTable.getColumnCount(); i++) {
            detailTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane detailScroll = new JScrollPane(detailTable);
        detailScroll.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));

        // Thanh tìm kiếm + nút xóa
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField txtDetailSearch = new JTextField(15);
        JButton btnDetailSearch = new JButton("🔍 Tìm chi tiết");
        JButton btnDeleteProduct = new JButton("❌ Xóa SP");

        searchPanel.add(txtDetailSearch);
        searchPanel.add(btnDetailSearch);
        searchPanel.add(btnDeleteProduct);

        btnDetailSearch.addActionListener(e -> controller.searchDetailsController(txtDetailSearch.getText().trim()));
        btnDeleteProduct.addActionListener(e -> controller.deleteSelectedProductController());

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(detailScroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReceiptTablePanel() {
        receiptTableModel = new DefaultTableModel(
                new Object[]{"Mã phiếu", "Người nhập (Tên / Tài khoản)", "Ngày nhập", "Ghi chú"}, 0);

        receiptTable = new JTable(receiptTableModel);

        // 👉 Căn giữa nội dung trong bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < receiptTable.getColumnCount(); i++) {
            receiptTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

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

    private void addEventListeners() {
        cboCategory.addActionListener(e -> controller.filterProductsByCategoryController());
        btnAddProduct.addActionListener(e -> controller.addProductToListController());
        btnSaveReceipt.addActionListener(e -> controller.saveReceiptController());
        btnEditReceipt.addActionListener(e -> controller.updateReceiptController());
        btnDeleteReceipt.addActionListener(e -> controller.deleteReceiptController());
        btnSearch.addActionListener(e -> controller.searchReceiptsController());
        btnClear.addActionListener(e -> controller.clearFormFieldsController());
        detailTable.getSelectionModel().addListSelectionListener(e -> controller.loadSelectedDetailRowController());
        receiptTable.getSelectionModel().addListSelectionListener(e -> controller.loadSelectedReceiptController());
    }

    public DefaultTableModel getDetailTableModel() {
        return detailTableModel;
    }

    public JComboBox<String> getCboCategory() {
        return cboCategory;
    }

    public JComboBox<Product> getCboProduct() {
        return cboProduct;
    }

    public JTextField getTxtQuantity() {
        return txtQuantity;
    }

    public JTextField getTxtImportPrice() {
        return txtImportPrice;
    }

    public JTextField getTxtNote() {
        return txtNote;
    }

    public List<ImportReceiptDetail> getCurrentDetails() {
        return currentDetails;
    }

    public List<Product> getAllProducts() {
        return allProducts;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImportReceiptView view = new ImportReceiptView();
        view.setVisible(true);

    }

}
