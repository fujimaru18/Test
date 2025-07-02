package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TopProductPanel extends JPanel {

    private JLabel lblFrom, lblTo;
    private JTextField txtFromDate, txtToDate;
    private JComboBox<String> cboGranularity;
    private JButton btnView;
    private JTable tblResult;

    public TopProductPanel() {
        setLayout(new BorderLayout());

        JPanel topWrapper = new JPanel();
        topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));

        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        line1.add(new JLabel("Thống kê theo:"));
        cboGranularity = new JComboBox<>(new String[]{"Ngày", "Tháng", "Năm"});
        line1.add(cboGranularity);
        topWrapper.add(line1);

        JPanel line2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblFrom = new JLabel("Từ:");
        txtFromDate = new JTextField(10);
        line2.add(lblFrom);
        line2.add(txtFromDate);

        lblTo = new JLabel("Đến:");
        txtToDate = new JTextField(10);
        line2.add(lblTo);
        line2.add(txtToDate);

        btnView = new JButton("Xem");
        line2.add(btnView);

        topWrapper.add(line2);
        add(topWrapper, BorderLayout.NORTH);

        tblResult = new JTable(new DefaultTableModel(
                new Object[]{"Sản phẩm", "Số lượng bán", "Doanh thu (VND)"}, 0
        ));
        add(new JScrollPane(tblResult), BorderLayout.CENTER);

        cboGranularity.addActionListener(e -> updateHint());
        updateHint();
    }

    private void updateHint() {
        String gran = (String) cboGranularity.getSelectedItem();
        switch (gran) {
            case "Ngày" -> {
                lblFrom.setText("Từ (yyyy-MM-dd):");
                lblTo.setVisible(true);
                txtToDate.setVisible(true);
                lblTo.setText("Đến:");
            }
            case "Tháng" -> {
                lblFrom.setText("Tháng/Năm (MM/yyyy):");
                lblTo.setVisible(false);
                txtToDate.setVisible(false);
            }
            case "Năm" -> {
                lblFrom.setText("Năm (yyyy):");
                lblTo.setVisible(false);
                txtToDate.setVisible(false);
            }
        }
    }

    public JTextField getTxtFromDate() {
        return txtFromDate;
    }

    public JTextField getTxtToDate() {
        return txtToDate;
    }

    public JComboBox<String> getCboGranularity() {
        return cboGranularity;
    }

    public JButton getBtnView() {
        return btnView;
    }

    public JTable getTblResult() {
        return tblResult;
    }

    public JLabel getLblFrom() {
        return lblFrom;
    }

    public JLabel getLblTo() {
        return lblTo;
    }
}
