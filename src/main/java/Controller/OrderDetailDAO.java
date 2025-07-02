/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Model.CartiItemModel;
import Model.OrderDetailModel;
import db.DBConnection;

/**
 *
 * @author Tuong Hue
 */
public class OrderDetailDAO {

    public void insertList(List<CartiItemModel> items, int orderId) throws SQLException {
        String sql = "INSERT INTO order_details (orderId, productId, quantity, unitPrice) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (CartiItemModel item : items) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, item.getProductId());
                stmt.setInt(3, item.getQuantity());
                stmt.setInt(4, item.getUnitPrice());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

  

    public int calculateTotalAmount(Connection conn, int orderId) throws SQLException {
        String sql = "SELECT SUM(quantity * unitPrice) FROM order_details WHERE orderId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<OrderDetailModel> getByOrderId(int orderId) throws SQLException {
        List<OrderDetailModel> list = new ArrayList<>();

        String sql = "SELECT od.productId, od.quantity, od.unitPrice, p.productName AS productName "
                + "FROM order_details od "
                + "JOIN products p ON od.productId = p.productId "
                + "WHERE od.orderId = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderDetailModel item = new OrderDetailModel();
                    item.setProductId(rs.getInt("productId"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getInt("unitPrice"));
                    item.setProductName(rs.getString("productName")); // <== gán tên sản phẩm

                    list.add(item);
                }
            }
        }

        return list;
    }

    public boolean delete(int orderId) {
        String sql = "DELETE FROM order_details WHERE orderId = ?";
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
