package DAO;

import Model.RevenueDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RevenueDAO {

    /** Thống kê theo NGÀY (yyyy-MM-dd) */
    public List<RevenueDTO> getDailyRevenue(LocalDate from, LocalDate to) {
        String sql = """
            SELECT DATE(i.paymentDate)            AS revenueDate,
                   COUNT(*)                       AS invoiceCount,
                   SUM(i.totalAmount)             AS totalAmount
            FROM invoices i
            WHERE DATE(i.paymentDate) BETWEEN ? AND ?
            GROUP BY DATE(i.paymentDate)
            ORDER BY revenueDate
        """;
        return fetchRevenue(sql, from, to);
    }

    /** Thống kê theo THÁNG (MM/yyyy) */
    public List<RevenueDTO> getMonthlyRevenue(LocalDate from, LocalDate to) {
        String sql = """
            SELECT DATE_FORMAT(i.paymentDate, '%m/%Y') AS revenueDate,
                   COUNT(*)                            AS invoiceCount,
                   SUM(i.totalAmount)                  AS totalAmount
            FROM invoices i
            WHERE DATE(i.paymentDate) BETWEEN ? AND ?
            GROUP BY YEAR(i.paymentDate), MONTH(i.paymentDate)
            ORDER BY YEAR(i.paymentDate), MONTH(i.paymentDate)
        """;
        return fetchRevenue(sql, from, to);
    }

    /** Thống kê theo NĂM (yyyy) */
    public List<RevenueDTO> getYearlyRevenue(LocalDate from, LocalDate to) {
        String sql = """
            SELECT DATE_FORMAT(i.paymentDate, '%Y') AS revenueDate,
                   COUNT(*)                         AS invoiceCount,
                   SUM(i.totalAmount)               AS totalAmount
            FROM invoices i
            WHERE DATE(i.paymentDate) BETWEEN ? AND ?
            GROUP BY YEAR(i.paymentDate)
            ORDER BY revenueDate
        """;
        return fetchRevenue(sql, from, to);
    }

    /* ---------- Helper chung ---------- */
    private List<RevenueDTO> fetchRevenue(String sql, LocalDate from, LocalDate to) {
        List<RevenueDTO> list = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String label      = rs.getString("revenueDate");
                    int    invCount   = rs.getInt("invoiceCount");
                    double total      = rs.getDouble("totalAmount");
                    list.add(new RevenueDTO(label, invCount, total));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // production code: log file
        }
        return list;
    }
}
