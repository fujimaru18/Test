/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import App.MainApp;
import DAO.CustomerDAO;
import Model.CustomerModel;
import View.ManageCustomerView;
import View.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Tuong Hue
 */
public class CustomerController {

    private ManageCustomerView form;
    private CustomerDAO cusDAO = new CustomerDAO();
    private Menu main;

    public CustomerController(ManageCustomerView form, Menu main) {
        this.form = form;
        this.main = main;
        this.form.getBtnAdd().addActionListener(new AddCustomer());
        this.form.getBtnUpdate().addActionListener(new UpdateCustomer());
        this.form.getBtnDelete().addActionListener(new DeleteCustomer());
        this.form.getBtnSearch().addActionListener(new SearchCustomer());
    }

    class SearchCustomer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String keyword = form.getSearch();
                List<CustomerModel> items = cusDAO.search(keyword);
                DefaultTableModel model = (DefaultTableModel) form.getTblCustomer().getModel();
                model.setRowCount(0);
                int stt = 1;

                for (CustomerModel item : items) {
                    model.addRow(new Object[]{
                        stt++,
                        item.getCustomerId(),
                        item.getName(),
                        item.getPhone(),
                        item.getAddress(),
                        item.getNote(),});
                }
            }catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(form, "Lỗi truy vấn dữ liệu: " + ex.getMessage());
            }
        }
    }

    class DeleteCustomer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {

                int selectRow = form.getTblCustomer().getSelectedRow();
                if (selectRow == -1) {
                    JOptionPane.showMessageDialog(form, "Vui lòng chọn một đơn hàng để xóa!");
                    return;
                }

                int cusId = (int) form.getTblCustomer().getValueAt(selectRow, 1);

                int confirm = JOptionPane.showConfirmDialog(form, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    cusDAO.delete(cusId);
                    form.loadData();
                }

                JOptionPane.showMessageDialog(form, "Xóa thành công!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(form, "Lỗi truy vấn dữ liệu: " + ex.getMessage());
            }
        }
    }

    class AddCustomer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String name = form.getCustomerName();
                String phone = form.getPhoneNumber();
                String address = form.getAddress();
                String note = form.getNote();
                CustomerModel model = new CustomerModel(name, phone, address, note);
                cusDAO.insert(model);
                form.loadData();
                JOptionPane.showMessageDialog(form, "Thêm thành công!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(form, "Lỗi tạo khách hàng: " + ex.getMessage());
            }

        }
    }

    class UpdateCustomer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int customerId = form.getSelectedCustomerId(); // Lấy ID từ dòng đang chọn
                String name = form.getCustomerName();
                String phone = form.getPhoneNumber();
                String address = form.getAddress();
                String note = form.getNote();

                if (name.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ tên và số điện thoại!");
                    return;
                }
                
                CustomerModel model = new CustomerModel(customerId,name,phone,address,note);
                
                cusDAO.update(model); // Gọi DAO
                form.loadData(); // Tải lại bảng
                System.out.println("Cập nhật: " + customerId + " - " + name + " - " + phone);

                JOptionPane.showMessageDialog(form, "Cập nhật thành công!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(form, "Lỗi cập nhật khách hàng: " + ex.getMessage());
            }
        }
    }
}
