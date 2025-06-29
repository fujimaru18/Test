/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author admin
 */
import Model.ImportReceiptDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImportReceiptDetailDAO {

    public boolean addImportReceiptDetail(ImportReceiptDetail detail) throws SQLException {
        String sql = "INSERT INTO import_receipt_details (receiptId, productId, quantity, importPrice) VALUES (?, ?, ?, ?)";
        String updateStock = "UPDATE products SET stockQuantity = stockQuantity + ? WHERE productId = ?";
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Thêm chi tiết nhập
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, detail.getReceiptId());
                ps.setInt(2, detail.getProductId());
                ps.setInt(3, detail.getQuantity());
                ps.setDouble(4, detail.getImportPrice());
                ps.executeUpdate();
            }

            // Cập nhật tồn kho
            try (PreparedStatement ps = conn.prepareStatement(updateStock)) {
                ps.setInt(1, detail.getQuantity());
                ps.setInt(2, detail.getProductId());
                ps.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback nếu lỗi
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<ImportReceiptDetail> getDetailsByReceiptId(int receiptId) throws SQLException {
        List<ImportReceiptDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM import_receipt_details WHERE receiptId = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, receiptId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ImportReceiptDetail detail = new ImportReceiptDetail();
                detail.setReceiptId(rs.getInt("receiptId"));
                detail.setProductId(rs.getInt("productId"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setImportPrice(rs.getDouble("importPrice"));
                list.add(detail);
            }
        }
        return list;
    }
}
