package Controller;

import Model.Product;
import Service.ProductService;
import View.ProductView;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import util.Constants.MessageConstants;

public class ProductController {

    private final ProductView view;
    private final ProductService service;
    private Product selectedProduct = null;
    private JPanel selectedCard = null;

    public ProductController(ProductView view) {
        this.view = view;
        this.service = new ProductService();
        this.view.setController(this);

        view.getTxtTimKiem().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                view.getTxtTimKiem().selectAll();
            }
        });
        initEvents();
        loadAllProducts();
    }

    private void initEvents() {
        view.getBtnTimKiem().addActionListener(e -> search());
        view.getBtnThem().addActionListener(e -> add());
        view.getBtnSua().addActionListener(e -> edit());
        view.getBtnXoa().addActionListener(e -> delete());
        view.getBtnClear().addActionListener(e -> clear());
    }

    private void loadAllProducts() {
        List<Product> list = service.getAll();
        JPanel panel = view.getProductPanel();
        panel.removeAll();

        for (Product info : list) {
            JPanel card = view.createProductCard(info);
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selectedProduct = info;
                    selectedCard = card;
                }
            });
            panel.add(card);
        }

        panel.revalidate();
        panel.repaint();
    }

    private void search() {
        String keyword = view.getTxtTimKiem().getText().trim();
        if (keyword.isEmpty() || keyword.equalsIgnoreCase("Nhập tên sản phẩm...")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập tên sản phẩm cần tìm!");
            return;
        }
        List<Product> list = service.getAll();
        JPanel panel = view.getProductPanel();
        panel.removeAll();

        for (Product info : list) {
            if (info.getName().toLowerCase().contains(keyword.toLowerCase())) {
                JPanel card = view.createProductCard(info);
                // Gắn sự kiện chọn card trong controller
                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        selectedProduct = info;
                        selectedCard = card;
                    }
                });
                panel.add(card);
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    private void add() {
        Product info = view.showInputDialog(null, service.getAllCategories(), service.getAllSuppliers());

        if (info != null) {
            boolean success = service.addSanPham(info);
            if (success) {
                JPanel panel = view.getProductPanel();
                JPanel card = view.createProductCard(info);
                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        selectedProduct = info;
                        selectedCard = card;
                    }
                });
                panel.add(card);
                panel.revalidate();
                panel.repaint();
            }
        }
    }

    private void edit() {
        if (selectedProduct == null || selectedCard == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm để sửa!");
            return;
        }

        // Mở dialog để chỉnh sửa, truyền dữ liệu cũ vào
        view.setSelectedProduct(selectedProduct);
        Product updatedInfo = view.showEditDialog(view, service.getAllCategories(), service.getAllSuppliers());

        if (updatedInfo != null) {
            updatedInfo.setproductId(selectedProduct.getproductId()); // Giữ nguyên ID để update

            boolean success = service.updateSanPham(updatedInfo);

            if (success) {
                // Cập nhật card giao diện
                JPanel panel = view.getProductPanel();
                panel.remove(selectedCard);

                JPanel newCard = view.createProductCard(updatedInfo);

                panel.add(newCard);
                panel.revalidate();
                panel.repaint();

                // Cập nhật biến selected
                selectedProduct = updatedInfo;
                selectedCard = newCard;
            }
        }
    }

    private void delete() {
        if (selectedProduct == null || selectedCard == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null,
                "Bạn có chắc muốn xóa sản phẩm này?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (service.deleteSanPham(selectedProduct.getproductId())) {
                JPanel panel = view.getProductPanel();
                panel.remove(selectedCard);
                panel.revalidate();
                panel.repaint();

                selectedProduct = null;
                selectedCard = null;
            }
        }
    }

    public void clear() {
        selectedProduct = null;
        selectedCard = null;
        loadAllProducts();
    }

    public void onSelectCard(JPanel card, Product product) {
        if (selectedCard != null) {
            selectedCard.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            selectedCard.setBackground(null);
        }

        selectedCard = card;
        selectedProduct = product;

        card.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        card.setBackground(new Color(220, 240, 255));
    }

}
