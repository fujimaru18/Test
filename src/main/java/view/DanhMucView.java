package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DanhMucView extends JPanel {

    public DanhMucView() {
        setLayout(new BorderLayout(10, 10));

        Font font = new Font("SansSerif", Font.PLAIN, 14);
        Color headerColor = new Color(51, 153, 255);
        Color buttonColor = new Color(30, 144, 255);

        // Tiêu đề
        JLabel titleLabel = new JLabel("Quản Lý Danh Mục", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(headerColor);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(600, 40));

        // Thanh tìm kiếm
        JTextField searchField = new JTextField("Tìm kiếm...");
        searchField.setFont(font);
        searchField.setPreferredSize(new Dimension(400, 30));

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

        // Nút chức năng
        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        for (JButton btn : new JButton[]{btnAdd, btnEdit, btnDelete}) {
            btn.setBackground(buttonColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(font);
            btn.setPreferredSize(new Dimension(100, 35));
        }

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.add(searchField, BorderLayout.NORTH);
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
    }
}
