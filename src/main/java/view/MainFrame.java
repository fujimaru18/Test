package view;

import javax.swing.*;
import static share.util.Constants.APP_TITLE;
import java.awt.CardLayout;

public class MainFrame extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MainFrame() {
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



        // Thêm các panel vào card layout với tên khóa
        mainPanel.add(welcomePanel, "welcome");
        add(mainPanel);

        // Xử lý sự kiện menu item chuyển sang panel LopPanel
        lopMenuItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "lop");
        });

        // Mặc định hiển thị panel welcome
        cardLayout.show(mainPanel, "welcome");
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
