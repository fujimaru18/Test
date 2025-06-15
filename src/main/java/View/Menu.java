package View;

import Controller.DanhMucController;

import javax.swing.*;
import java.awt.*;

import static util.Constants.UIConstants.APP_TITLE;

public class Menu extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Khai báo view để truy cập từ nhiều nơi nếu cần
    private DanhMucView danhMucView;
    private DanhMucController danhMucController;

    public Menu() {
        setTitle(APP_TITLE);
        setSize(800, 600); // tăng kích thước cho phù hợp nội dung
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // căn giữa màn hình

        // Giao diện người dùng
        createMenuBar();   // Tạo menu
        createMainPanel(); // Tạo panel chính với các view

        // Hiển thị giao diện mặc định
        cardLayout.show(mainPanel, "welcome");
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Quản lý");

        JMenuItem danhMucItem = new JMenuItem("Quản lý Danh Mục");
        danhMucItem.addActionListener(e -> cardLayout.show(mainPanel, "DanhMuc"));

        menu.add(danhMucItem);
        menuBar.add(menu);

        setJMenuBar(menuBar);
    }

    private void createMainPanel() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Panel chào mừng mặc định
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Chào mừng đến với hệ thống quản lý sản phẩm", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

        // Tạo View & Controller cho Danh Mục
        danhMucView = new DanhMucView();
        danhMucController = new DanhMucController(danhMucView); 

        // Thêm các view vào CardLayout
        mainPanel.add(welcomePanel, "welcome");
        mainPanel.add(danhMucView, "DanhMuc");

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu frame = new Menu();
            frame.setVisible(true);
        });
    }
}
