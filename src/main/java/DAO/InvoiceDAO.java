/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import Model.InvoiceDetailModel;
import Model.InvoiceModel;

/**
 *
 * @author Tuong Hue
 */
public class InvoiceDAO {

    public void insert(int orderId, int totalAmount, String paymentMethod) throws SQLException {
        String sql = "INSERT INTO invoices (orderId, totalAmount, paymentDate,paymentMethod) VALUES (?, ?, NOW(),?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, totalAmount);
            stmt.setString(3, paymentMethod);

            stmt.executeUpdate();
        }
    }

    public List<InvoiceModel> getAll() {
        List<InvoiceModel> list = new ArrayList<>();
        String sql = "SELECT * From invoices";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int invoiceId = rs.getInt("invoiceId");
                int orderId = rs.getInt("orderId");
                Timestamp paymentDate = rs.getTimestamp("paymentDate");
                int totalAmount = rs.getInt("totalAmount");
                String paymentMethod = rs.getString("paymentMethod");

                InvoiceModel invoice = new InvoiceModel(invoiceId, orderId, paymentDate, totalAmount, paymentMethod);
                list.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn đơn hàng: " + e.getMessage());
        }
        return list;
    }

    public List<InvoiceDetailModel> getDataDetail(int invoiceId) {
        List<InvoiceDetailModel> list = new ArrayList<>();
        String sql = "SELECT "
                + "invoices.invoiceId, invoices.paymentDate, invoices.totalAmount, invoices.paymentMethod, "
                + "orders.orderId, orders.orderDate, "
                + "customers.customerId, customers.fullName, customers.address, customers.phone, customers.note, "
                + "products.productId, products.productName, "
                + "order_details.quantity, order_details.unitPrice, "
                + "(order_details.quantity * order_details.unitPrice) AS item_total "
                + "FROM invoices "
                + "JOIN orders ON invoices.orderId = orders.orderId "
                + "JOIN customers ON orders.customerId = customers.customerId "
                + "JOIN order_details ON orders.orderId = order_details.orderId "
                + "JOIN products ON order_details.productId = products.productId "
                + "WHERE invoices.invoiceId = ?";
        try (Connection conn = DBConnection.getConnection(); // tùy class bạn đặt tên
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
            System.out.println("Querying invoiceId = " + invoiceId);
            stmt.setInt(1, invoiceId);  // truyền invoiceId động
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int invId = rs.getInt("invoiceId");
                Timestamp paymentDate = rs.getTimestamp("paymentDate");
                int total = rs.getInt("totalAmount");
                String paymentMethod = rs.getString("paymentMethod");

                int orderId = rs.getInt("orderId");
                Timestamp orderDate = rs.getTimestamp("orderDate");

                int customerId = rs.getInt("customerId");
                String customerName = rs.getString("fullName");
                String customerAddress = rs.getString("address");
                String customerPhone = rs.getString("phone");
//                String customerNote = rs.getString("note");

                int productId = rs.getInt("productId");
                String productName = rs.getString("productName");
                int quantity = rs.getInt("quantity");
                int unitPrice = rs.getInt("unitPrice");
                int itemTotal = rs.getInt("item_total");

                // Sau đây bạn có thể đẩy dữ liệu lên JTable hoặc JLabel tùy bạn
                InvoiceDetailModel detail = new InvoiceDetailModel(
                        invId, orderId, paymentDate, orderDate, total, paymentMethod, customerId, customerName, customerPhone,
                        customerAddress, productId, productName, quantity, unitPrice, itemTotal);
                list.add(detail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<InvoiceModel> Search(String keyword, String searchType, String method, String start, String end) {
        List<InvoiceModel> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM invoices WHERE 1=1");

        // Tìm theo mã
        if (keyword != null && !keyword.trim().isEmpty()) {
            if ("invoiceId".equals(searchType)) {
                sql.append(" AND invoiceId = ?");
            } else if ("orderId".equals(searchType)) {
                sql.append(" AND orderId = ?");
            }
        }

        // Lọc theo phương thức thanh toán
        if (method != null && !method.equals("-----Chọn-----")) {
            sql.append(" AND paymentMethod = ?");
        }

        // Lọc theo khoảng ngày
        if (start != null && end != null) {
            sql.append(" AND paymentDate BETWEEN ? AND ?");
        } else if (start != null) {
            sql.append(" AND paymentDate >= ?");
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;

            // Gán giá trị cho keyword
            if (keyword != null && !keyword.trim().isEmpty()) {
                stmt.setInt(index++, Integer.parseInt(keyword.trim()));
            }

            // Gán phương thức thanh toán
            if (method != null && !method.equals("-----Chọn-----")) {
                stmt.setString(index++, method);
            }

            // Gán ngày
            if (start != null && end != null) {
                stmt.setString(index++, start);
                stmt.setString(index++, end);
            } else if (start != null) {
                stmt.setString(index++, start);
            }

            // Thực thi
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int invoiceId = rs.getInt("invoiceId");
                int orderId = rs.getInt("orderId");
                Timestamp paymentDate = rs.getTimestamp("paymentDate");
                int totalAmount = rs.getInt("totalAmount");
                String paymentMethod = rs.getString("paymentMethod");

                InvoiceModel invoice = new InvoiceModel(invoiceId, orderId, paymentDate, totalAmount, paymentMethod);
                list.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn đơn hàng: " + e.getMessage());
        }

        return list;
    }

}
