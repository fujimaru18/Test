package View;

import Controller.ProductController;
import Model.Category;
import Model.Product;
import Model.Supplier;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.*;
import util.Constants.UIConstants;
import java.util.List;

public class ProductView extends JPanel {

    private JTextField txtTimKiem;
    private JPanel pnProduct;
    private JButton btnTimKiem, btnThem, btnSua, btnXoa, btnClear;
    private ProductController controller;
    private Product selectedProduct;

    public void setSelectedProduct(Product product) {
        this.selectedProduct = product;
    }

    public void setController(ProductController controller) {
        this.controller = controller;
    }

    public ProductView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(UIConstants.PANEL_PADDING);

        // Tiêu đề
        add(createTitleLabel(), BorderLayout.NORTH);

        // Nội dung chính
        add(createContentPanel(), BorderLayout.CENTER);
    }

    private JLabel createTitleLabel() {
        JLabel lblTitle = new JLabel("SẢN PHẨM", JLabel.CENTER);
        lblTitle.setFont(UIConstants.TITLE_FONT);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(UIConstants.TITLE_COLOR_BG);
        lblTitle.setForeground(UIConstants.TEXT_COLOR);
        lblTitle.setPreferredSize(new Dimension(600, 40));
        return lblTitle;
    }

    private JPanel createContentPanel() {
        JPanel pnContents = new JPanel(new BorderLayout(10, 10));
        pnContents.setBorder(UIConstants.PANEL_PADDING);

        pnContents.add(createSearchPanel(), BorderLayout.NORTH);
        pnContents.add(createTableScrollPane(), BorderLayout.CENTER);
        pnContents.add(createButtonPanel(), BorderLayout.EAST);

        return pnContents;
    }

    private JPanel createSearchPanel() {
        JPanel pnButton = new JPanel();

        txtTimKiem = new JTextField("Nhập tên sản phẩm...");
        txtTimKiem.setFont(UIConstants.DEFAULT_FONT);
        txtTimKiem.setPreferredSize(UIConstants.TEXTFIELD_SIZE);

        btnTimKiem = new JButton("Tìm kiếm");
        styleButton(btnTimKiem);

        pnButton.add(btnTimKiem);
        pnButton.add(txtTimKiem);

        return pnButton;
    }

    private JScrollPane createTableScrollPane() {
        pnProduct = new JPanel(new GridLayout(0, 3, 20, 20));
        JScrollPane scrollPane = new JScrollPane(pnProduct);
        return scrollPane;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(UIConstants.PANEL_PADDING);

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnClear = new JButton("Xóa trắng");

        for (JButton btn : new JButton[]{btnThem, btnSua, btnXoa, btnClear}) {
            styleButton(btn);
            panel.add(Box.createVerticalStrut(UIConstants.DEFAULT_PADDING));
            panel.add(btn);
        }

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private void styleButton(JButton button) {
        button.setBackground(UIConstants.BUTTON_COLOR_BG);
        button.setForeground(UIConstants.TEXT_COLOR);
        button.setFocusPainted(false);
        button.setFont(UIConstants.DEFAULT_FONT);
        button.setMaximumSize(UIConstants.BUTTON_SIZE);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public JPanel createProductCard(Product info) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(160, 260));
        card.setMaximumSize(new Dimension(160, 260));
        card.setMinimumSize(new Dimension(160, 260));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        card.setBackground(Color.WHITE); // thêm nền trắng nếu cần

        // Hình ảnh
        JLabel lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(140, 140));
        lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (info.getImage() != null) {
            ImageIcon icon = new ImageIcon(info.getImage());
            Image img = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(img));
        }
        // Tên sản phẩm
        JLabel lblName = new JLabel(info.getName());
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblName.setFont(UIConstants.DEFAULT_FONT);
        lblName.setHorizontalAlignment(SwingConstants.CENTER);

        // Giá
        JLabel lblSalePrice = new JLabel(info.getSalePrice() + " VND");
        lblSalePrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSalePrice.setFont(UIConstants.DEFAULT_FONT);
        lblSalePrice.setForeground(Color.RED);
        lblSalePrice.setHorizontalAlignment(SwingConstants.CENTER);

        // Thêm các thành phần
        card.add(Box.createVerticalStrut(5));
        card.add(lblImage);
        card.add(Box.createVerticalStrut(10));
        card.add(lblName);
        card.add(lblSalePrice);
        card.add(Box.createVerticalStrut(10));

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.onSelectCard(card, info);
            }
        });

        return card;
    }

    public Product showInputDialog(JPanel parent, List<Category> categories, List<Supplier> suppliers) {
        JTextField txtName = new JTextField(15);
        JTextField txtImportPrice = new JTextField(15);
        JTextField txtSalePrice = new JTextField(15);
        JTextField txtStockQuantity = new JTextField(15);
        JTextField txtUnit = new JTextField(15);

        JComboBox<Category> cboCategory = new JComboBox<>(categories.toArray(new Category[0]));
        JComboBox<Supplier> cboSupplier = new JComboBox<>(suppliers.toArray(new Supplier[0]));
        JComboBox<String> cboStatus = new JComboBox<>(new String[]{"Đang bán", "Chưa bán"});
        cboStatus.setSelectedIndex(0); // mặc định là "Đang bán"
        cboStatus.setVisible(false);   // ẩn vì không cần thiết trong thêm mới

        JButton btnChooseImage = new JButton("Chọn ảnh");
        JLabel lblImagePath = new JLabel("Chưa chọn ảnh");

        final File[] selectedImageFile = new File[1]; // lưu file ảnh nếu có chọn

        btnChooseImage.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                selectedImageFile[0] = file;
                lblImagePath.setText(file.getName()); // hiển thị tên ảnh mới chọn
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Tên sản phẩm:"));
        panel.add(txtName);
        panel.add(new JLabel("Giá nhập:"));
        panel.add(txtImportPrice);
        panel.add(new JLabel("Giá bán:"));
        panel.add(txtSalePrice);
        panel.add(new JLabel("Tồn kho:"));
        panel.add(txtStockQuantity);
        panel.add(new JLabel("Đơn vị:"));
        panel.add(txtUnit);
        panel.add(new JLabel("Danh mục:"));
        panel.add(cboCategory);
        panel.add(new JLabel("Nhà cung cấp:"));
        panel.add(cboSupplier);
        panel.add(btnChooseImage);
        panel.add(lblImagePath);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Thêm sản phẩm", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int importPrice = Integer.parseInt(txtImportPrice.getText().trim());
                int salePrice = Integer.parseInt(txtSalePrice.getText().trim());
                int stockQty = Integer.parseInt(txtStockQuantity.getText().trim());

                byte[] imageBytes = null;
                if (selectedImageFile[0] != null) {
                    imageBytes = java.nio.file.Files.readAllBytes(selectedImageFile[0].toPath());
                }

                return new Product(
                        0,
                        txtName.getText().trim(),
                        importPrice,
                        salePrice,
                        stockQty,
                        txtUnit.getText().trim(),
                        (Category) cboCategory.getSelectedItem(),
                        (Supplier) cboSupplier.getSelectedItem(),
                        cboStatus.getSelectedIndex(), // mặc định 0
                        imageBytes
                );
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "Vui lòng nhập đúng định dạng số (Giá và tồn kho).");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Lỗi khi xử lý ảnh hoặc dữ liệu.");
                ex.printStackTrace();
            }
        }

        return null;
    }

    public Product showEditDialog(JPanel parent, List<Category> categories, List<Supplier> suppliers) {
        JTextField txtName = new JTextField(selectedProduct.getName(), 15);
        JTextField txtImportPrice = new JTextField(String.valueOf(selectedProduct.getImportPrice()), 15);
        JTextField txtSalePrice = new JTextField(String.valueOf(selectedProduct.getSalePrice()), 15);
        JTextField txtStockQuantity = new JTextField(String.valueOf(selectedProduct.getStockQuantity()), 15);
        JTextField txtUnit = new JTextField(selectedProduct.getUnit(), 15);

        JComboBox<Category> cboCategory = new JComboBox<>(categories.toArray(Category[]::new));
        for (Category cat : categories) {
            if (cat.getcategoryId() == selectedProduct.getCategoryId().getcategoryId()) {
                cboCategory.setSelectedItem(cat);
                break;
            }
        }

        JComboBox<Supplier> cboSupplier = new JComboBox<>(suppliers.toArray(Supplier[]::new));
        for (Supplier sup : suppliers) {
            if (sup.getsupplierId() == selectedProduct.getSupplierId().getsupplierId()) {
                cboSupplier.setSelectedItem(sup);
                break;
            }
        }

        JComboBox<String> cboStatus = new JComboBox<>(new String[]{"Đang bán", "Ngừng bán"});
        cboStatus.setSelectedIndex(selectedProduct.getStatus()); // 0 hoặc 1
        cboStatus.setVisible(true);

        JButton btnChooseImage = new JButton("Chọn ảnh");
        JLabel lblImagePath = new JLabel();
        final File[] selectedImageFile = new File[1];

        if (selectedProduct.getImage() != null) {
            lblImagePath.setText("[Đã có ảnh]");
        } else {
            lblImagePath.setText("Chưa chọn ảnh");
        }

        btnChooseImage.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                selectedImageFile[0] = file;
                lblImagePath.setText(file.getName());
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Tên sản phẩm:"));
        panel.add(txtName);
        panel.add(new JLabel("Giá nhập:"));
        panel.add(txtImportPrice);
        panel.add(new JLabel("Giá bán:"));
        panel.add(txtSalePrice);
        panel.add(new JLabel("Tồn kho:"));
        panel.add(txtStockQuantity);
        panel.add(new JLabel("Đơn vị:"));
        panel.add(txtUnit);
        panel.add(new JLabel("Danh mục:"));
        panel.add(cboCategory);
        panel.add(new JLabel("Nhà cung cấp:"));
        panel.add(cboSupplier);
        panel.add(new JLabel("Trạng thái:"));
        panel.add(cboStatus);
        panel.add(btnChooseImage);
        panel.add(lblImagePath);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Chỉnh sửa sản phẩm", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int importPrice = Integer.parseInt(txtImportPrice.getText().trim());
                int salePrice = Integer.parseInt(txtSalePrice.getText().trim());
                int stockQty = Integer.parseInt(txtStockQuantity.getText().trim());

                byte[] imageBytes = selectedProduct.getImage(); // giữ ảnh cũ nếu không chọn lại
                if (selectedImageFile[0] != null) {
                    imageBytes = java.nio.file.Files.readAllBytes(selectedImageFile[0].toPath());
                }

                return new Product(
                        selectedProduct.getproductId(),
                        txtName.getText().trim(),
                        importPrice,
                        salePrice,
                        stockQty,
                        txtUnit.getText().trim(),
                        (Category) cboCategory.getSelectedItem(),
                        (Supplier) cboSupplier.getSelectedItem(),
                        cboStatus.getSelectedIndex(),
                        imageBytes
                );
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "Vui lòng nhập đúng định dạng số (Giá và tồn kho).");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Lỗi khi xử lý ảnh hoặc dữ liệu.");
                ex.printStackTrace();
            }
        }

        return null;
    }

    // === Getter cho Controller ===
    public JTextField getTxtTimKiem() {
        return txtTimKiem;
    }

    public JPanel getProductPanel() {
        return pnProduct;
    }

    public JButton getBtnTimKiem() {
        return btnTimKiem;
    }

    public JButton getBtnThem() {
        return btnThem;
    }

    public JButton getBtnSua() {
        return btnSua;
    }

    public JButton getBtnXoa() {
        return btnXoa;
    }

    public JButton getBtnClear() {
        return btnClear;
    }
}
