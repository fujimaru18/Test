/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;


import DAO.CustomerDAO;
import DAO.InvoiceDAO;
import DAO.OrderDAO;
import DAO.OrderDetailDAO;
import Model.CartiItemModel;
import Model.CustomerModel;
import db.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import util.Constants.DBConstants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Tuong Hue
 */
public class OrderService {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();

    public void updateOrderDetail(CustomerModel model,List<CartiItemModel> items, int orderId) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            //xóa order_details
            orderDetailDAO.delete(orderId);

            //thêm lại order_details
            orderDetailDAO.insertList(items, orderId);
            
            //cập nhật khách hàng
            customerDAO.update(model);
            conn.commit(); // Xác nhận giao dịch
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int createOrderOnly(CustomerModel customerInput, List<CartiItemModel> cartItems) throws SQLException {
//        int totalAmount = cartItems.stream()
//                .mapToInt(item -> item.getQuantity() * item.getUnitPrice())
//                .sum();

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction
            CustomerModel customer = customerDAO.findByPhone(customerInput.getPhone());

            // 1. Thêm khách hàng
            if (customer == null) {
                int customerId = customerDAO.insert(customerInput);
                customerInput.setId(customerId);
            } else {
                customerInput.setId(customer.getCustomerId());
            }
            // 2. Tạo đơn hàng
            int orderId = orderDAO.insert(customerInput.getCustomerId());
            // 3. Thêm chi tiết sản phẩm
            orderDetailDAO.insertList(cartItems, orderId);

            conn.commit(); // Xác nhận giao dịch
            return orderId;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // rollback sẽ tự động nếu đóng connection trong try-with-resources
        }
    }

    public void deleteOrder(int orderId) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction
            orderDAO.delete(orderId);
            orderDetailDAO.delete(orderId);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // rollback sẽ tự động nếu đóng connection trong try-with-resources
        }
    }

    public void createInvoice(int orderId, String paymentMethod) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            // Tính lại tổng từ order_detail (hoặc bạn truyền từ ngoài vào nếu đã biết)
            int totalAmount = orderDetailDAO.calculateTotalAmount(conn, orderId);
            invoiceDAO.insert(orderId, totalAmount, paymentMethod);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int findPendingOrder(CustomerModel customer) throws SQLException {
        String sql = "SELECT orderId FROM orders WHERE customerId = ? AND status = 'pending'";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            int customerId = new CustomerDAO().getCustomerIdByInfo(customer); // bạn viết hàm này
            if (customerId == -1) {
                return -1;
            }

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("orderId");
            }
        }
        return -1; // Không tồn tại đơn hàng
    }

    public boolean hasPendingOrders(String phone) throws SQLException {
        String sql = """
        SELECT COUNT(*) FROM orders o
        JOIN customers c ON o.customerId = c.customerId
        WHERE  c.phone = ? AND o.status = 'pending'
    """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}
