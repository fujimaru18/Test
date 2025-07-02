package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LowStockPanel extends JPanel {

    private final JTable tblProducts;
    private final DefaultTableModel tableModel;
    private final JTextField txtThreshold;
    private final JButton btnFilter;

    public LowStockPanel() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.add(new JLabel("Ngưỡng tồn kho (<):"));
        txtThreshold = new JTextField("10", 5);
        btnFilter = new JButton("Lọc");
        top.add(txtThreshold);
        top.add(btnFilter);

        String[] cols = {"Mã", "Tên sản phẩm", "Số lượng"};
        tableModel = new DefaultTableModel(cols, 0);
        tblProducts = new JTable(tableModel);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(tblProducts), BorderLayout.CENTER);
    }

    public JTextField getTxtThreshold() {
        return txtThreshold;
    }

    public JButton getBtnFilter() {
        return btnFilter;
    }

    public JTable getTblProducts() {
        return tblProducts;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
