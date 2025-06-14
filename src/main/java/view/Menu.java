package view;

import javax.swing.*;
import static util.Constants.APP_TITLE;
import java.awt.CardLayout;

public class Menu extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    public Menu() {
        setTitle(APP_TITLE);
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo thanh menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Quản lý");
        JMenuItem lopMenuItem = new JMenuItem("Quản lý Lớp học");
        menu.add(lopMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Tạo panel chính dùng CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Panel chào mừng mặc định
        JPanel welcomePanel = new JPanel();
        welcomePanel.add(new JLabel("Welcome to Product Management App"));

        // Tạo LopPanel
        DanhMucView danhMuc = new DanhMucView();

        // Thêm các panel vào card layout với tên khóa
        mainPanel.add(welcomePanel, "welcome");
        mainPanel.add(danhMuc, "DanhMuc"); // thêm panel quản lý lớp học

        add(mainPanel);

        // Xử lý sự kiện menu item chuyển sang panel LopPanel
        lopMenuItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "DanhMuc");
        });

        // Mặc định hiển thị panel welcome
        cardLayout.show(mainPanel, "welcome");
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Menu frame = new Menu();
            frame.setVisible(true);
        });
    }
}
