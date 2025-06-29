package DAO;

import Model.ProfitDTO;
import Model.RevenueDTO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    /* ---------- Thống kê theo NGÀY ---------- */
    public List<ProfitDTO> getDailyProfit(LocalDate from, LocalDate to) throws SQLException {
        String sql = """
            SELECT DATE(i.paymentDate) AS profitDate,
                   SUM(od.quantity * od.unitPrice) AS revenue,
                   SUM(od.quantity * p.importPrice) AS cost
            FROM invoices i
            JOIN orders o        ON i.orderId = o.orderId
            JOIN order_details od ON o.orderId = od.orderId
            JOIN products p      ON od.productId = p.productId
            WHERE DATE(i.paymentDate) BETWEEN ? AND ?
            GROUP BY DATE(i.paymentDate)
            ORDER BY profitDate
        """;
    return fetchProfit(sql, from, to, "Ngày");
    }

    /* ---------- Thống kê theo THÁNG ---------- */
    public List<ProfitDTO> getMonthlyProfit(LocalDate from, LocalDate to) throws SQLException {
        String sql = """
            SELECT CAST(DATE_FORMAT(i.paymentDate, '%Y-%m-01') AS DATE) AS profitDate,
                   SUM(od.quantity * od.unitPrice) AS revenue,
                   SUM(od.quantity * p.importPrice) AS cost
            FROM invoices i
            JOIN orders o        ON i.orderId = o.orderId
            JOIN order_details od ON o.orderId = od.orderId
            JOIN products p      ON od.productId = p.productId
            WHERE DATE(i.paymentDate) BETWEEN ? AND ?
            GROUP BY YEAR(i.paymentDate), MONTH(i.paymentDate)
            ORDER BY profitDate
        """;
    return fetchProfit(sql, from, to, "Tháng");
    }

    /* ---------- Thống kê theo NĂM ---------- */
    public List<ProfitDTO> getYearlyProfit(LocalDate from, LocalDate to) throws SQLException {
        String sql = """
            SELECT CAST(DATE_FORMAT(i.paymentDate, '%Y-01-01') AS DATE) AS profitDate,
                   SUM(od.quantity * od.unitPrice) AS revenue,
                   SUM(od.quantity * p.importPrice) AS cost
            FROM invoices i
            JOIN orders o        ON i.orderId = o.orderId
            JOIN order_details od ON o.orderId = od.orderId
            JOIN products p      ON od.productId = p.productId
            WHERE DATE(i.paymentDate) BETWEEN ? AND ?
            GROUP BY YEAR(i.paymentDate)
            ORDER BY profitDate
        """;
    return fetchProfit(sql, from, to, "Năm");
    }

    /* ---------- Helper chung ---------- */
private List<ProfitDTO> fetchProfit(String sql, LocalDate from, LocalDate to, String granularity) throws SQLException {
    List<ProfitDTO> list = new ArrayList<>();
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setDate(1, Date.valueOf(from));
        ps.setDate(2, Date.valueOf(to));
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String rawLabel = rs.getString("profitDate");
                String label;
                if (granularity.equals("Tháng")) {
                    label = "Tháng " + rawLabel.substring(5, 7) + "/" + rawLabel.substring(0, 4);  // yyyy-mm-dd
                } else if (granularity.equals("Năm")) {
                    label = "Năm " + rawLabel.substring(0, 4);
                } else {
                    label = rawLabel;
                }

                double revenue = rs.getDouble("revenue");
                double cost = rs.getDouble("cost");
                list.add(new ProfitDTO(label, revenue, cost));
            }
        }
    }
    return list;
}


public List<RevenueDTO> getRevenueStats(String granularity, LocalDate from, LocalDate to) throws SQLException {
    String timeFormat = switch (granularity) {
        case "Tháng" -> "%m/%Y";
        case "Năm"   -> "%Y";
        default      -> "%d/%m/%Y";
    };

    String groupBy = switch (granularity) {
        case "Tháng" -> "YEAR(paymentDate), MONTH(paymentDate)";
        case "Năm"   -> "YEAR(paymentDate)";
        default      -> "DATE(paymentDate)";
    };

    String sql = String.format("""
        SELECT DATE_FORMAT(paymentDate, '%s') AS timeLabel,
               COUNT(*) AS invoiceCount,
               SUM(totalAmount) AS totalAmount
        FROM invoices
        WHERE DATE(paymentDate) BETWEEN ? AND ?
        GROUP BY %s
        ORDER BY MIN(paymentDate)
    """, timeFormat, groupBy);

    List<RevenueDTO> list = new ArrayList<>();
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setDate(1, Date.valueOf(from));
        ps.setDate(2, Date.valueOf(to));
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new RevenueDTO(
                    rs.getString("timeLabel"),
                    rs.getInt("invoiceCount"),
                    rs.getDouble("totalAmount")
                ));
            }
        }
    }
    return list;
}



    




}
