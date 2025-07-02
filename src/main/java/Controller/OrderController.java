/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import App.MainApp;
import Model.CartiItemModel;
import Model.CustomerModel;
import Model.Product;
import Service.InvoiceService;
import Service.OrderService;
import View.InvoiceView;
import View.Menu;
import View.OrderView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Tuong Hue
 */
public class OrderController {

    private OrderView form;
    private OrderService service;
    private Menu mainApp;
    private List<CartiItemModel> cartItems = new ArrayList<>();

    public OrderController(OrderView form, OrderService service, Menu mainApp) {
        this.form = form;
        this.service = new OrderService();
        this.mainApp = mainApp;
        // Gắn sự kiện cho nút "Tạo đơn hàng"
        this.form.getBtnAddCart().addActionListener(new AddCartListener());
        this.form.getBtnAddOrder().addActionListener(new CreateOrderListener());
        this.form.getCreateInvoice().addActionListener(new CreateInvoice());
        this.form.getBtnUpdate().addActionListener(new UpdateOrder());
    }

    class UpdateOrder implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int orderId = form.getOrderId();
                List<CartiItemModel> cartItems = form.getCartItems();
                
//                 int customerId = form.getSelectedCustomerId(); // Lấy ID từ dòng đang chọn
                String name = form.getCustomerName();
                String phone = form.getPhoneNumber();
                String address = form.getAddress();
                String note = form.getNote();
                
                 CustomerModel model = new CustomerModel(form.getCusId(),name,phone,address,note);
                service.updateOrderDetail(model,cartItems, orderId);
                
                
                
                JOptionPane.showMessageDialog(form, "Cập nhật đơn hàng thành công!");
                System.out.println("123");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(form, "Lỗi tạo đơn hàng: " + ex.getMessage());
            }
        }
    }

   class CreateInvoice implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String name = form.getCustomerName();
            String phone = form.getPhoneNumber();
            String address = form.getAddress();
            String note = form.getNote();
            List<CartiItemModel> cartItems = form.getCartItems();

            if (name.isEmpty() || phone.isEmpty() || cartItems.isEmpty()) {
                JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            CustomerModel customer = new CustomerModel(name, phone, address, note);

            int existingOrderId = service.findPendingOrder(customer);
            int orderId;
            if (existingOrderId != -1) {
                orderId = existingOrderId;
            } else {
                orderId = service.createOrderOnly(customer, cartItems);
            }

            // ✅ Gọi đúng hàm hiển thị hóa đơn từ Menu
            mainApp.showInvoiceView(orderId);

            System.out.println("Order ID: " + orderId);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Lỗi tạo đơn hàng: " + ex.getMessage());
        }
    }
}


    class AddCartListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Product selectProduct = (Product) form.getSelectedProduct();
                int productID = selectProduct.getproductId();

                String productName = selectProduct.getName(); // tạo phương thức trong View
                int quantity = form.getSelectedQuantity();          // tạo phương thức trong View
                int unitPrice = selectProduct.getSalePrice();
                int total = unitPrice * quantity;

                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(form, "Số lượng phải lớn hơn 0");
                    return;
                }

                CartiItemModel item = new CartiItemModel(productID, productName, quantity, unitPrice);
                cartItems.add(item);

                // Hiển thị lại bảng
                DefaultTableModel model = (DefaultTableModel) form.getCartTable().getModel();
                model.addRow(new Object[]{
                    model.getRowCount() + 1,
                    productID,
                    productName,
                    quantity,
                    unitPrice
                });

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(form, "Số lượng không hợp lệ");
            }
        }

    }

    class CreateOrderListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String name = form.getCustomerName();
                String phone = form.getPhoneNumber();
                String address = form.getAddress();
                String note = form.getNote();
                List<CartiItemModel> cartItems = form.getCartItems();

                if (name.isEmpty() || phone.isEmpty() || cartItems.isEmpty()) {
                    JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                if (service.hasPendingOrders(phone)) {
                    JOptionPane.showMessageDialog(form, "Khách hàng SĐT " + phone + " đã có đơn đang chờ xử lý!");
                    return;
                }

                CustomerModel customer = new CustomerModel(name, phone, address, note);

                service.createOrderOnly(customer, cartItems);

                JOptionPane.showMessageDialog(form, "Tạo đơn hàng thành công!");
                form.clearForm();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(form, "Lỗi tạo đơn hàng: " + ex.getMessage());
            }
        }
    }
}
