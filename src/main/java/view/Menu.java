package View;

import Controller.CustomerController;
import Controller.CategoryController;
import Controller.InvoiceController;
import Controller.InvoiceDetailController;
import Controller.ManageInvoiceController;
import Controller.ManageOrderController;
import Controller.OrderController;
import Controller.ProductController;
import Controller.SupplierController;
import DAO.OrderDAO;
import Service.InvoiceService;
import Service.OrderService;

import javax.swing.*;
import java.awt.*;
import view.SupplierView;
import static util.Constants.UIConstants.APP_TITLE;

public class Menu extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Khai báo view & controller
    private CategoryView categoryView;
    private ProductView productView;
    private SupplierView supplierView;
    private OrderView orderView;
    private ManageOrderView manageOrderView;
    private ManageCustomerView customerView;
    private ManageInvoiceView manageInvoiceView;
    private InvoiceDetail invoiceDetailView;
    private InvoiceView invoiceView;

    private CategoryController categoryController;
    private ProductController productController;
    private SupplierController supplierController;
    private OrderController orderController;
    private ManageOrderController manageOrderController;
    private CustomerController customerController;
    private ManageInvoiceController manageInvoiceController;
    private InvoiceDetailController invoiceDetailController;
    private InvoiceController invoiceController;

    private OrderDAO orderDAO;

    public Menu() {
        setTitle(APP_TITLE);
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        orderDAO = new OrderDAO();
        createMenuBar();
        createMainPanel();
        showPanel("Order"); // Mặc định hiển thị panel Đơn hàng
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Quản lý");

        JMenuItem danhMucItem = new JMenuItem("Danh mục");
        danhMucItem.addActionListener(e -> showPanel("DanhMuc"));

        JMenuItem sanPhamItem = new JMenuItem("Sản phẩm");
        sanPhamItem.addActionListener(e -> showPanel("SanPham"));

        JMenuItem donHangItem = new JMenuItem("Đơn hàng");
        donHangItem.addActionListener(e -> showPanel("Order"));

        JMenuItem manageOrderItem = new JMenuItem("Danh sách đơn hàng");
        manageOrderItem.addActionListener(e -> {
            manageOrderView.loadOrderData(orderDAO.getAll());
            showPanel("ManageOrder");
        });

        JMenuItem customerItem = new JMenuItem("Danh sách khách hàng");
        customerItem.addActionListener(e -> {
            customerView.loadData();
            showPanel("Customer");
        });
        JMenuItem hoaDonItem = new JMenuItem("Lịch sử hóa đơn");
        hoaDonItem.addActionListener(e -> {
            manageInvoiceView.loadData();
            showPanel("HoaDon");
        });

        JMenuItem nhaCungCapItem = new JMenuItem("Quản lý Nhà Cung Cấp");
        nhaCungCapItem.addActionListener(e -> cardLayout.show(mainPanel, "NhaCungCap"));

        menu.add(danhMucItem);
        menu.add(sanPhamItem);
        menu.add(nhaCungCapItem);
        menu.add(donHangItem);
        menu.add(manageOrderItem);
        menu.add(customerItem);
        menu.add(hoaDonItem);
        
        menuBar.add(menu);

        setJMenuBar(menuBar);
    }

    private void createMainPanel() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Panel chào mừng mặc định
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Chào mừng đến với hệ thống quản lý", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        mainPanel.add(welcomePanel, "welcome");

        // Tạo & thêm các view và controller
        categoryView = new CategoryView();
        supplierView = new SupplierView();
        productView = new ProductView();
        orderView = new OrderView();
        manageOrderView = new ManageOrderView();
        customerView = new ManageCustomerView();
        manageInvoiceView = new ManageInvoiceView();
        invoiceDetailView = new InvoiceDetail();

        categoryController = new CategoryController(categoryView);
        productController = new ProductController(productView);
        categoryController.setProductController(productController);
        OrderService orderService = new OrderService();
        orderController = new OrderController(orderView, orderService, this);
        manageOrderController = new ManageOrderController(manageOrderView, orderView, this);
        customerController = new CustomerController(customerView, this);
        manageInvoiceController = new ManageInvoiceController(manageInvoiceView, invoiceDetailView, this);
        invoiceDetailController = new InvoiceDetailController(invoiceDetailView, this);

        mainPanel.add(categoryView, "DanhMuc");
        mainPanel.add(productView,"SanPham");
        mainPanel.add(supplierView, "NhaCungCap");
        mainPanel.add(orderView, "Order");
        mainPanel.add(manageOrderView, "ManageOrder");
        mainPanel.add(customerView, "Customer");
        mainPanel.add(manageInvoiceView, "HoaDon");
        mainPanel.add(invoiceDetailView, "InvoiceDetail");

        add(mainPanel);
    }

    // Cho controller gọi để đổi panel
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void showInvoiceView(int orderId) {
        InvoiceView invoiceView = new InvoiceView(orderId);
        InvoiceService invoiceService = new InvoiceService();
        InvoiceController invoiceController = new InvoiceController(invoiceView, invoiceService, orderId);

        String panelKey = "Invoice_" + orderId;

        mainPanel.add(invoiceView, panelKey);
        cardLayout.show(mainPanel, panelKey);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu();
            menu.setVisible(true);
        });
    }
}
