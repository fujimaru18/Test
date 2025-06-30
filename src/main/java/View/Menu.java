package View;

import Controller.CustomerController;
import Controller.DanhMucController;
import Controller.InvoiceController;
import Controller.ManageInvoiceController;
import Controller.ManageOrderController;
import Controller.OrderController;
import DAO.OrderDAO;
import Service.InvoiceService;
import Service.OrderService;

import javax.swing.*;
import java.awt.*;

import static util.Constants.UIConstants.APP_TITLE;

public class Menu extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Khai báo view & controller
    private DanhMucView danhMucView;
    private DanhMucController danhMucController;

    private OrderView orderView;
    private ManageOrderView manageOrderView;
    private ManageCustomerView customerView;
    private ManageInvoiceView manageInvoiceView;
    private InvoiceDetail invoiceDetailView;
    private InvoiceView invoiceView;

    private OrderController orderController;
    private ManageOrderController manageOrderController;
    private CustomerController customerController;
    private ManageInvoiceController manageInvoiceController;

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

        JMenuItem danhMucItem = new JMenuItem("Quản lý Danh Mục");
        danhMucItem.addActionListener(e -> showPanel("DanhMuc"));

        JMenuItem sanPhamItem = new JMenuItem("Đơn hàng");
        sanPhamItem.addActionListener(e -> showPanel("Order"));

        JMenuItem manageOrderItem = new JMenuItem("Danh sách đơn hàng");
        manageOrderItem.addActionListener(e -> {
            manageOrderView.loadOrderData(orderDAO.getAll());
            showPanel("ManageOrder");
        });

        JMenuItem customerItem = new JMenuItem("Danh sách khách hàng");
        customerItem.addActionListener(e ->{
            customerView.loadData();
            showPanel("Customer");
        });
        JMenuItem hoaDonItem = new JMenuItem("Lịch sử hóa đơn");
        hoaDonItem.addActionListener(e -> {
            manageInvoiceView.loadData();
            showPanel("HoaDon");
        });

        menu.add(danhMucItem);
        menu.add(sanPhamItem);
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
        danhMucView = new DanhMucView();
        danhMucController = new DanhMucController(danhMucView);
        mainPanel.add(danhMucView, "DanhMuc");

        orderView = new OrderView();
        manageOrderView = new ManageOrderView();
        customerView = new ManageCustomerView();
        manageInvoiceView = new ManageInvoiceView();
        invoiceDetailView = new InvoiceDetail();

        OrderService orderService = new OrderService();
        orderController = new OrderController(orderView, orderService, this);
        manageOrderController = new ManageOrderController(manageOrderView, orderView, this);
        customerController = new CustomerController(customerView, this);
        manageInvoiceController = new ManageInvoiceController(manageInvoiceView, invoiceDetailView, this);

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
