package Controller;


import App.MainApp;
import DAO.CustomerDAO;
import DAO.OrderDAO;
import Model.CartiItemModel;
import Model.CustomerModel;
import Model.OrderDetailModel;
import Model.OrderModel;
import Service.OrderService;
import View.ManageOrderView;
import View.Menu;
import View.OrderView;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ManageOrderController {
    
    private ManageOrderView form;
    private OrderView orderView;
    private Menu main;
    private List<CartiItemModel> cartItems = new ArrayList<>();
    private OrderService service;
    private OrderDAO orderDAO;
    private CustomerDAO customerDAO;
    private OrderDetailDAO orderDetailDAO;
    
    public ManageOrderController(ManageOrderView form, OrderView orderView, Menu main) {
        this.form = form;
        this.orderView = orderView;
        this.main = main;

        // Khởi tạo DAO và Service
        this.orderDAO = new OrderDAO();
        this.customerDAO = new CustomerDAO();
        this.orderDetailDAO = new OrderDetailDAO();
        this.service = new OrderService();

        // Gắn sự kiện cho nút "Xử lý"
        this.form.getBtnHandle().addActionListener(new HandleOrder());
//        this.form.getRdPending().addActionListener(new SearchPending());
        this.form.getBtnDelete().addActionListener(new Delete());
        form.getTblOrders().getSelectionModel().addListSelectionListener(new HideBtn());
        this.form.getBtnSearch().addActionListener(new Search());
    }
    
    class Search implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String startDate = form.getStartDateString(form.getStartDate());
                String endDate = null;
                if (form.getEndDate().getDate() != null) {
                    endDate = form.getEndDateString(form.getEndDate());
                }
                String phone = form.getTxtPhone();
                String name = form.getTxtNameCus();
                String id = form.getTxtId();
                String statusUI = form.getCbStatus().getSelectedItem().toString();
                String status = form.convertStatusToCode(statusUI);

//                orderDAO.search(id, phone, name, startDate, endDate, status);
//                loadOrderData(id, phone, name, startDate, endDate, status);
                form.loadOrderData(orderDAO.search(id, phone, name, startDate, endDate, status));
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(form, "Lỗi truy vấn dữ liệu: " + ex.getMessage());
            }
        }
    }
    
   
    
    class HideBtn implements ListSelectionListener {
        
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = form.getTblOrders().getSelectedRow();
            if (selectedRow != -1) {
                String status = form.getTblOrders().getValueAt(selectedRow, 5).toString(); // Cột 4 là status
                if ("Đã thanh toán".equals(status)) {
                    form.getBtnDelete().setEnabled(false);
                    form.getBtnHandle().setEnabled(false);
                } else {
                    form.getBtnDelete().setEnabled(true);
                    form.getBtnHandle().setEnabled(true);
                }
            }
        }
    }
    
    class Delete implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int selectRow = form.getTblOrders().getSelectedRow();
                if (selectRow == -1) {
                    JOptionPane.showMessageDialog(form, "Vui lòng chọn một đơn hàng để xóa!");
                    return;
                }
                
                int orderId = (int) form.getTblOrders().getValueAt(selectRow, 1);
                String status = (String) form.getTblOrders().getValueAt(selectRow, 5);
                if (!status.equals("Đang chờ xử lý...")) {
                    JOptionPane.showMessageDialog(form, "Không thể xóa đơn đã thanh toán");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(form, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    service.deleteOrder(orderId);
                    form.loadOrderData(orderDAO.getAll());
                }
                
                JOptionPane.showMessageDialog(form, "Xóa thành công!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(form, "Lỗi truy vấn dữ liệu: " + ex.getMessage());
            }
        }
    }
    
    class HandleOrder implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int selectRow = form.getTblOrders().getSelectedRow();
                if (selectRow == -1) {
                    JOptionPane.showMessageDialog(form, "Vui lòng chọn một đơn hàng để xử lý!");
                    return;
                }
                String status = form.getTblOrders().getValueAt(selectRow, 5).toString();
                if (!status.equals("Đang chờ xử lý...")) {
                    JOptionPane.showMessageDialog(form, "Đơn hàng đã được thanh toán!");
                    return;
                }
                int orderId = (int) form.getTblOrders().getValueAt(selectRow, 1);
//                System.out.println(orderId);
                
                OrderModel order = orderDAO.getById(orderId);
                CustomerModel customer = customerDAO.getById(order.getCustomerId());
                List<OrderDetailModel> details = orderDetailDAO.getByOrderId(orderId);
                
                orderView.setCustomerInfo(customer);
                orderView.setCartItems(details);
                orderView.setCurrentOrderId(orderId);
                orderView.setCusId(order.getCustomerId());
                orderView.loadData(orderId);
                System.out.println("Đã set orderId: " + orderView.getOrderId());
                main.showPanel("Order"); // Chuyển về form tạo đơn hàng
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(form, "Lỗi truy vấn dữ liệu: " + ex.getMessage());
            }
        }
    }
}
