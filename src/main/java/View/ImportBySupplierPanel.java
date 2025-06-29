package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ImportBySupplierPanel extends JPanel {
    private JLabel lblFrom, lblTo;
    private JTextField txtFrom, txtTo;
    private JComboBox<String> cboGran;
    private JButton btnStat;
    private JTable tblResult;
    private JLabel lblTotal;

    public ImportBySupplierPanel() {
        setLayout(new BorderLayout());

        /* ----- TOP WRAPPER ----- */
        JPanel topWrapper = new JPanel();
        topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));

        /* Dòng 1: Combobox */
        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        line1.add(new JLabel("Thống kê theo:"));
        cboGran = new JComboBox<>(new String[]{"Ngày", "Tháng", "Năm"});
        line1.add(cboGran);
        topWrapper.add(line1);

        /* Dòng 2: Từ – Đến – Button */
        JPanel line2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblFrom = new JLabel("Từ:");
        txtFrom = new JTextField(10);
        line2.add(lblFrom); line2.add(txtFrom);
        lblTo = new JLabel("Đến:");
        txtTo = new JTextField(10);
        line2.add(lblTo);   line2.add(txtTo);
        btnStat = new JButton("Thống kê");
        line2.add(btnStat);
        topWrapper.add(line2);

        add(topWrapper, BorderLayout.NORTH);

        /* Bảng kết quả */
        tblResult = new JTable(new DefaultTableModel(
                new Object[]{"Nhà CC", "Danh mục", "Sản phẩm", "SL", "Đơn giá", "Thành tiền"}, 0));
        add(new JScrollPane(tblResult), BorderLayout.CENTER);

        /* Tổng tiền */
        lblTotal = new JLabel("Tổng tiền: 0 VND");
        lblTotal.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        add(lblTotal, BorderLayout.SOUTH);

        /* Ẩn/hiện ô nhập theo loại */
        cboGran.addActionListener(e -> updateHint());
        updateHint();
    }

    private void updateHint() {
        String g = (String) cboGran.getSelectedItem();
        switch (g) {
            case "Ngày" -> {                       // yyyy-MM-dd
                lblFrom.setText("Từ (yyyy-MM-dd):");
                lblTo.setVisible(true);  txtTo.setVisible(true);
                lblTo.setText("Đến:");
            }
            case "Tháng" -> {                      // MM/yyyy
                lblFrom.setText("Tháng/Năm (MM/yyyy):");
                lblTo.setVisible(false); txtTo.setVisible(false);
            }
            case "Năm" -> {                        // yyyy
                lblFrom.setText("Năm (yyyy):");
                lblTo.setVisible(false); txtTo.setVisible(false);
            }
        }
    }

    /* ---- Getters ---- */
    public JTextField getTxtFrom()          { return txtFrom;   }
    public JTextField getTxtTo()            { return txtTo;     }
    public JComboBox<String> getCboGran()   { return cboGran;   }
    public JButton    getBtnStat()          { return btnStat;   }
    public JTable     getTblResult()        { return tblResult; }
    public JLabel     getLblTotal()         { return lblTotal;  }
}
