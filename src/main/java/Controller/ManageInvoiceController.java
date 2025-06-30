/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;


import App.MainApp;
import DAO.InvoiceDAO;
import Model.InvoiceDetailModel;
import Model.InvoiceModel;
import View.InvoiceDetail;
import View.ManageInvoiceView;
import View.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Tuong Hue
 */
public class ManageInvoiceController {

    private ManageInvoiceView form;
    private InvoiceDAO invoiceDAO;
    private InvoiceDetail invoiceDetail;
    private Menu main;

    public ManageInvoiceController(ManageInvoiceView manageInvoiceView, InvoiceDetail invoiceDetail, Menu main) {
        this.form = manageInvoiceView;
        this.invoiceDetail = invoiceDetail;
        this.main = main;
        this.form.getBtnCheck().addActionListener(new btnCheck());
        this.form.getBtnSearch().addActionListener(new SearchDate());
    }

    class SearchDate implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                invoiceDAO = new InvoiceDAO();
                String startDate = form.getStartDateString(form.getStartDate());
                String endDate = null;

                if (form.getEndDate().getDate() != null) {
                    endDate = form.getEndDateString(form.getEndDate());
                }
                String method = form.getPaymentMethod();
                String key = form.getTxtId();
                String type = form.getSearchType();
                List<InvoiceModel> items = invoiceDAO.Search(key, type,method,startDate, endDate);
                DefaultTableModel model = (DefaultTableModel) form.getTblInvoice().getModel();
                model.setRowCount(0);
                int stt = 1;
                for (InvoiceModel item : items) {
                    model.addRow(new Object[]{
                        stt++,
                        item.getInvoiceId(),
                        item.getOrderId(),
                        item.getPaymentDate(),
                        item.getTotalAmount(),
                        item.getPaymentMethod(),});
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(form, "Lỗi truy vấn dữ liệu: " + ex.getMessage());
            }
        }
    }

    class btnCheck implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                invoiceDAO = new InvoiceDAO();
                int selectRow = form.getTblInvoice().getSelectedRow();
                if (selectRow == -1) {
                    JOptionPane.showMessageDialog(form, "Vui lòng chọn một hóa đơn để xem chi tiết!");
                    return;
                }

                int invoiceId = (int) form.getTblInvoice().getValueAt(selectRow, 1);

                List<InvoiceDetailModel> details = invoiceDAO.getDataDetail(invoiceId);
//                if (details.isEmpty()) {
//                    JOptionPane.showMessageDialog(form, "Không tìm thấy chi tiết hóa đơn.");
//                    return;
//                }

                InvoiceDetailModel info = details.get(0);
                invoiceDetail.setInfo(info);
                invoiceDetail.setCartItems(details);
                main.showPanel("InvoiceDetail"); // Chuyển về form tạo đơn hàng

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(form, "Lỗi truy vấn dữ liệu: " + ex.getMessage());
            }

        }
    }

}
