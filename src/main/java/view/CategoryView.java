package View;

import util.Constants.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CategoryView extends JPanel {

    private JTable tblDanhMuc;
    private DefaultTableModel tableModel;
    private JTextField txtTimKiem;
    private JButton btnTimKiem, btnThem, btnSua, btnXoa, btnClear;

    public CategoryView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(UIConstants.PANEL_PADDING);

        // Tiêu đề
        add(createTitleLabel(), BorderLayout.NORTH);

        // Nội dung chính
        add(createContentPanel(), BorderLayout.CENTER);
    }

    private JLabel createTitleLabel() {
        JLabel lblTitle = new JLabel("DANH MỤC", JLabel.CENTER);
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
        JPanel pnSearch = new JPanel();

        txtTimKiem = new JTextField("Nhập tên danh mục...");
        txtTimKiem.setFont(UIConstants.DEFAULT_FONT);
        txtTimKiem.setPreferredSize(UIConstants.TEXTFIELD_SIZE);

        btnTimKiem = new JButton("Tìm kiếm");
        styleButton(btnTimKiem);

        pnSearch.add(btnTimKiem);
        pnSearch.add(txtTimKiem);

        return pnSearch;
    }

    private JScrollPane createTableScrollPane() {
        String[] columns = {"Tên Danh Mục", "Mô Tả"};
        tableModel = new DefaultTableModel(columns,0);

        tblDanhMuc = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDanhMuc.getTableHeader().setReorderingAllowed(false);
        tblDanhMuc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDanhMuc.setFont(UIConstants.DEFAULT_FONT);
        tblDanhMuc.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        tblDanhMuc.getTableHeader().setFont(UIConstants.DEFAULT_FONT);
        tblDanhMuc.getTableHeader().setBackground(UIConstants.TABLE_HEADER_BG);

        JScrollPane scrollPane = new JScrollPane(tblDanhMuc);
        scrollPane.setMaximumSize(new Dimension(600, 100));

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

        for (JButton btn : new JButton[]{btnThem, btnSua, btnXoa,btnClear}) {
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

    // === Getter cho Controller ===
    public JTable getTblDanhMuc() {
        return tblDanhMuc;
    }

    public JTextField getTxtTimKiem() {
        return txtTimKiem;
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