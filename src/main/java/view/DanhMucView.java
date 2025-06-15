package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DanhMucView extends JPanel {

    public DanhMucView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Lề cho toàn bộ JPanel

        Font font = new Font("Arial", Font.PLAIN, 14);

        // Tiêu đề
        JLabel lblTitle = new JLabel("Quản Lý Danh Mục", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(Color.BLUE);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setPreferredSize(new Dimension(800, 40));

        // Thanh tìm kiếm
        JTextField searchField = new JTextField();
        searchField.setFont(font);
        searchField.setPreferredSize(new Dimension(800, 30));

        // Bảng dữ liệu
        String[] columns = {"Tên Danh Mục", "Mô Tả"};
        String[][] data = {
            {"Đồ uống", "Các loại đồ uống"},
            {"Thực phẩm", "Các mặt hàng thực phẩm"},
            {"Đồ gia dụng", "Sản phẩm sử dụng trong gia đình"},
            {"Thể thao", "Đồ dùng, thiết bị thể thao"},
            {"Thời trang", "Các mặt hàng thời trang"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns)) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setFont(font);
        table.setRowHeight(24);
        table.getTableHeader().setFont(font);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(600, 100));

        // Nút chức năng
        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        for (JButton btn : new JButton[]{btnAdd, btnEdit, btnDelete}) {
            btn.setBackground(Color.BLUE);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(font);
            btn.setMaximumSize(new Dimension(100, 35));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // Panel chứa nút (theo chiều dọc)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Lề trong cho nút
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(btnAdd);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(btnEdit);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(btnDelete);
        buttonPanel.add(Box.createVerticalGlue());

        // Panel chính chứa tìm kiếm + bảng + nút
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Lề trong content
        contentPanel.add(searchField, BorderLayout.NORTH);
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.EAST);

        // Thêm các thành phần vào giao diện chính
        add(lblTitle, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}
