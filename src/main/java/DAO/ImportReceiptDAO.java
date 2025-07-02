package DAO;

import Model.ImportDetailBySupplierDTO;
import Model.ImportReceipt;
import Model.ImportReceiptDetail;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ImportReceiptDAO {

    // Thêm mới phiếu nhập
    public boolean addImportReceipt(ImportReceipt receipt) throws SQLException {
        String sql = "INSERT INTO import_receipts (userId, importDate, note) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, receipt.getUserId());
            ps.setTimestamp(2, receipt.getImportDate());
            ps.setString(3, receipt.getNote());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    receipt.setReceiptId(rs.getInt(1));  // gán ID tự tăng
                }
                return true;
            }
            return false;
        }
    }

    // Xóa phiếu nhập
    public boolean deleteImportReceipt(int receiptId) throws SQLException {
        String deleteDetailsSql = "DELETE FROM import_receipt_details WHERE receiptId = ?";
        String deleteReceiptSql = "DELETE FROM import_receipts WHERE receiptId = ?";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);          // mở transaction

            try (PreparedStatement psDetail = conn.prepareStatement(deleteDetailsSql); PreparedStatement psReceipt = conn.prepareStatement(deleteReceiptSql)) {

                // 1. xóa chi tiết
                psDetail.setInt(1, receiptId);
                psDetail.executeUpdate();

                // 2. xóa header
                psReceipt.setInt(1, receiptId);
                int rows = psReceipt.executeUpdate();

                conn.commit();                  // thành công -> commit
                return rows > 0;
            } catch (SQLException ex) {
                conn.rollback();                // lỗi -> rollback
                throw ex;
            } finally {
                conn.setAutoCommit(true);       // khôi phục chế độ tự commit
            }
        }
    }

    // Lấy toàn bộ danh sách phiếu nhập
    public List<ImportReceipt> getAllImportReceipts() throws SQLException {
        List<ImportReceipt> list = new ArrayList<>();
        String sql = "SELECT * FROM import_receipts";

        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ImportReceipt receipt = new ImportReceipt();
                receipt.setReceiptId(rs.getInt("receiptId"));
                receipt.setUserId(rs.getInt("userId"));
                receipt.setImportDate(rs.getTimestamp("importDate"));
                receipt.setNote(rs.getString("note"));
                list.add(receipt);
            }
        }
        return list;
    }

    // Thống kê chi tiết nhập hàng theo nhà cung cấp
    public List<ImportDetailBySupplierDTO> getDetailedImportBySupplier(LocalDate from, LocalDate to) throws SQLException {
        List<ImportDetailBySupplierDTO> list = new ArrayList<>();

        String sql = """
            SELECT s.supplierName,
                   c.categoryName,
                   p.productName,
                   d.quantity,
                   d.importPrice
            FROM import_receipts r
            JOIN import_receipt_details d ON r.receiptId = d.receiptId
            JOIN products p ON d.productId = p.productId
            JOIN suppliers s ON p.supplierId = s.supplierId
            JOIN categories c ON p.categoryId = c.categoryId
            WHERE DATE(r.importDate) BETWEEN ? AND ?
            ORDER BY s.supplierName, c.categoryName, p.productName
        """;

        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ImportDetailBySupplierDTO(
                            rs.getString("supplierName"),
                            rs.getString("categoryName"),
                            rs.getString("productName"),
                            rs.getInt("quantity"),
                            rs.getDouble("importPrice")
                    ));
                }
            }
        }

        return list;
    }

    public boolean updateImportReceipt(ImportReceipt receipt, List<ImportReceiptDetail> newDetails) throws SQLException {
        String updateHead = """
            UPDATE import_receipts
               SET userId   = ?,
                   importDate = ?,
                   note      = ?
             WHERE receiptId = ?
            """;

        String deleteDet = "DELETE FROM import_receipt_details WHERE receiptId = ?";

        String insertDet = """
            INSERT INTO import_receipt_details
                   (receiptId, productId, quantity, importPrice)
            VALUES (?,?,?,?)
            """;

        try (Connection c = Database.getConnection()) {
            c.setAutoCommit(false);

            try (PreparedStatement up = c.prepareStatement(updateHead); PreparedStatement del = c.prepareStatement(deleteDet); PreparedStatement ins = c.prepareStatement(insertDet)) {

                // 1. Cập nhật header
                up.setInt(1, receipt.getUserId());
                up.setTimestamp(2, receipt.getImportDate());
                up.setString(3, receipt.getNote());
                up.setInt(4, receipt.getReceiptId());

                int headRows = up.executeUpdate();
                if (headRows == 0) { // receiptId không tồn tại
                    c.rollback();
                    return false;
                }

                // 2. Xóa chi tiết cũ
                del.setInt(1, receipt.getReceiptId());
                del.executeUpdate();

                // 3. Thêm chi tiết mới
                int detailRows = 0;
                for (ImportReceiptDetail d : newDetails) {
                    ins.setInt(1, receipt.getReceiptId());
                    ins.setInt(2, d.getProductId());
                    ins.setInt(3, d.getQuantity());
                    ins.setDouble(4, d.getImportPrice());
                    ins.addBatch();
                }
                int[] batchResults = ins.executeBatch();
                for (int result : batchResults) {
                    detailRows += result;
                }

                // Kiểm tra xem có chi tiết nào được thêm không
                if (detailRows == 0 && !newDetails.isEmpty()) {
                    c.rollback();
                    return false;
                }

                c.commit();
                return true;
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<ImportReceipt> searchImportReceiptsByKeyword(String keyword) throws SQLException {
        List<ImportReceipt> list = new ArrayList<>();
        String sql = """
        SELECT r.*
        FROM import_receipts r
        LEFT JOIN users u ON r.userId = u.userId
        WHERE CAST(r.receiptId AS CHAR) LIKE ?
           OR u.fullName LIKE ?
           OR DATE_FORMAT(r.importDate, '%Y-%m-%d') LIKE ?
           OR r.note LIKE ?
        ORDER BY r.importDate DESC
    """;

        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            for (int i = 1; i <= 4; i++) {
                ps.setString(i, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImportReceipt r = new ImportReceipt();
                    r.setReceiptId(rs.getInt("receiptId"));
                    r.setUserId(rs.getInt("userId"));
                    r.setImportDate(rs.getTimestamp("importDate"));
                    r.setNote(rs.getString("note"));
                    list.add(r);
                }
            }
        }
        return list;
    }

    public List<ImportReceipt> searchImportReceiptsByReceiptId(String receiptIdKeyword) throws SQLException {
        List<ImportReceipt> list = new ArrayList<>();
        String sql = "SELECT * FROM import_receipts WHERE CAST(receiptId AS CHAR) LIKE ? ORDER BY importDate DESC";

        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + receiptIdKeyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImportReceipt r = new ImportReceipt();
                    r.setReceiptId(rs.getInt("receiptId"));
                    r.setUserId(rs.getInt("userId"));
                    r.setImportDate(rs.getTimestamp("importDate"));
                    r.setNote(rs.getString("note"));
                    list.add(r);
                }
            }
        }
        return list;
    }

}
