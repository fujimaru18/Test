/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;


import DAO.InvoiceDAO;
import DAO.OrderDAO;
import DAO.OrderDetailDAO;
import Model.OrderDetailModel;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

import util.Constants.DBConstants;

/**
 *
 * @author Tuong Hue
 */
public class InvoiceService {

    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

    public void Pay(int orderId, int total, String paymentMethod) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction
            // Ghi hóa đơn
            invoiceDAO.insert(orderId, total, paymentMethod);
            List<OrderDetailModel> orderDetails = orderDetailDAO.getByOrderId(orderId);
            
            for (OrderDetailModel item : orderDetails) {
                checkStock(conn, item.getProductId() , (int)item.getQuantity() );
                // Trừ số lượng sản phẩm
                String updateSql = "UPDATE products SET stockQuantity = stockQuantity - ? WHERE productId = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, (int) item.getQuantity());
                    ps.setInt(2, item.getProductId());
                    ps.executeUpdate();
                }
            }
            orderDAO.update(orderId);
            conn.commit(); // Xác nhận lưu thay đổi
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void checkStock(Connection conn, int productId, int requiredQty) throws SQLException {
        String checkSql = "SELECT stockQuantity FROM products WHERE productId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int currentQty = rs.getInt("stockQuantity");
                    if (currentQty < requiredQty) {
                        throw new SQLException("Sản phẩm ID " + productId + " không đủ hàng. Hiện còn: " + currentQty);
                    }
                } else {
                    throw new SQLException("Không tìm thấy sản phẩm ID " + productId);
                }
            }
        }
    }
}
