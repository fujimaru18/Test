package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProfitPanel extends JPanel {
    private JLabel lblFrom, lblTo;
    private JTextField txtFrom, txtTo;
    private JComboBox<String> cboGran;
    private JButton btnStat;
    private JTable tblResult;
    private JLabel lblTotal;

    public ProfitPanel() {
        setLayout(new BorderLayout());

        // ===== Top Wrapper Panel with BoxLayout =====
        JPanel topWrapper = new JPanel();
        topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));

        // ===== Line 1: "Thống kê theo" =====
        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        line1.add(new JLabel("Thống kê theo:"));
        cboGran = new JComboBox<>(new String[]{"Ngày", "Tháng", "Năm"});
        line1.add(cboGran);
        topWrapper.add(line1);

        // ===== Line 2: From - To + Button =====
        JPanel line2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblFrom = new JLabel("Từ:");
        txtFrom = new JTextField(10);
        line2.add(lblFrom);
        line2.add(txtFrom);

        lblTo = new JLabel("Đến:");
        txtTo = new JTextField(10);
        line2.add(lblTo);
        line2.add(txtTo);

        btnStat = new JButton("Thống kê");
        line2.add(btnStat);

        topWrapper.add(line2);
        add(topWrapper, BorderLayout.NORTH);

        // ===== Table =====
        tblResult = new JTable(new DefaultTableModel(
                new Object[]{"Thời gian", "Doanh thu", "Giá vốn", "Lợi nhuận"}, 0
        ));
        add(new JScrollPane(tblResult), BorderLayout.CENTER);

        // ===== Total Label =====
        lblTotal = new JLabel("Tổng lợi nhuận: 0 VND");
        lblTotal.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(lblTotal, BorderLayout.SOUTH);

        // ===== Initial logic =====
        cboGran.addActionListener(e -> updateInputHint());
        updateInputHint(); // thiết lập mặc định
    }

    private void updateInputHint() {
        String gran = (String) cboGran.getSelectedItem();
        switch (gran) {
            case "Ngày" -> {
                lblFrom.setText("Từ (yyyy-MM-dd):");
                lblTo.setVisible(true);
                txtTo.setVisible(true);
                lblTo.setText("Đến:");
            }
            case "Tháng" -> {
                lblFrom.setText("Tháng/Năm (MM/yyyy):");
                lblTo.setVisible(false);
                txtTo.setVisible(false);
            }
            case "Năm" -> {
                lblFrom.setText("Năm (yyyy):");
                lblTo.setVisible(false);
                txtTo.setVisible(false);
            }
        }
    }

    // ===== Getters =====
    public JTextField getTxtFrom() { return txtFrom; }
    public JTextField getTxtTo() { return txtTo; }
    public JComboBox<String> getCboGran() { return cboGran; }
    public JButton getBtnStat() { return btnStat; }
    public JTable getTblResult() { return tblResult; }
    public JLabel getLblTotal() { return lblTotal; }
}
