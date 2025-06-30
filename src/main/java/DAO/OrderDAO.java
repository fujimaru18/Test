/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import Model.OrderModel;
import db.DBConnection;
import util.Constants.DBConstants;

/**
 *
 * @author Tuong Hue
 */
public class OrderDAO {

    public int insert(int customerId) throws SQLException {
        String sql = "INSERT INTO orders (customerId, orderDate, status) VALUES (? , NOW(), 'pending')";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public OrderModel getById(int orderId) throws SQLException {
        OrderModel order = null;

        String sql = "SELECT * FROM orders WHERE orderId = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    order = new OrderModel();
                    order.setOrderId(rs.getInt("orderId"));
                    order.setCustomerId(rs.getInt("customerId"));
                    order.setOrderDate(rs.getTimestamp("orderDate"));
                    // Thêm các cột khác nếu có
                }
            }
        }

        return order;
    }

    public List<OrderModel> getAll() {
        List<OrderModel> list = new ArrayList<>();
        String sql = "SELECT o.orderId, o.orderDate, o.status, c.fullName AS customerName, c.phone AS CustomerPhone "
                + "FROM orders o "
                + "JOIN customers c ON o.customerId = c.customerId";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int orderId = rs.getInt("orderId");
                String customerName = rs.getString("customerName");
                String customerPhone = rs.getString("customerPhone");
                Timestamp orderDate = rs.getTimestamp("orderDate");
                String status = rs.getString("status");

                OrderModel order = new OrderModel(orderId, customerName, customerPhone, orderDate, status);
                list.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn đơn hàng: " + e.getMessage());
        }

        return list;
    }

    public boolean update(int orderId) {
        String sql = "UPDATE orders SET status = ? WHERE orderId = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "paid");
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;  // true nếu cập nhật thành công

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<OrderModel> search(String orderId, String phone, String nameCus, String startDate, String endDate, String status) {
        List<OrderModel> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT o.orderId, o.orderDate, o.status, "
                + "c.fullName AS customerName, c.phone AS customerPhone FROM orders o "
                + "JOIN customers c ON o.customerId = c.customerId WHERE 1=1");

        // Điều kiện lọc
        if (status != null && !status.equals("-----Chọn-----")) {
            sql.append(" AND o.status = ?");
        }
        if (startDate != null && endDate != null) {
            sql.append(" AND o.orderDate BETWEEN ? AND ?");
        } else if (startDate != null) {
            sql.append(" AND o.orderDate >= ?");
        }
        if (nameCus != null && !nameCus.trim().isEmpty()) {
            sql.append(" AND c.fullName LIKE ?");
        }
        if (orderId != null && !orderId.trim().isEmpty()) {
            sql.append(" AND o.orderId = ?");
        }
        if (phone != null && !phone.trim().isEmpty()) {
            sql.append(" AND c.phone = ?");
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (status != null && !status.equals("-----Chọn-----")) {
                stmt.setString(index++, status);
            }
            if (startDate != null && endDate != null) {
                stmt.setString(index++, startDate);
                stmt.setString(index++, endDate);
            } else if (startDate != null) {
                stmt.setString(index++, startDate);
            }
            if (nameCus != null && !nameCus.trim().isEmpty()) {
                stmt.setString(index++, "%" + nameCus.trim() + "%");
            }
            if (orderId != null && !orderId.trim().isEmpty()) {
                stmt.setInt(index++, Integer.parseInt(orderId.trim()));
            }
            if (phone != null && !phone.trim().isEmpty()) {
                stmt.setString(index++, phone.trim());
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int order_Id = rs.getInt("orderId");
                String customerName = rs.getString("customerName");
                String customerPhone = rs.getString("customerPhone");
                Timestamp orderDate = rs.getTimestamp("orderDate");
                String orderStatus = rs.getString("status");

                list.add(new OrderModel(order_Id, customerName, customerPhone, orderDate, orderStatus));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn đơn hàng: " + e.getMessage());
        }

        return list;
    }

    public boolean delete(int orderId) {
        String sql = "DELETE FROM orders WHERE orderId = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            int rowsAffected = ps.executeUpdate(); // đúng cho DELETE, INSERT, UPDATE
            return rowsAffected > 0; // trả về true nếu có ít nhất 1 dòng bị xóa

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
