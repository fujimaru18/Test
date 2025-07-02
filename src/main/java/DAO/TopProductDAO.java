package DAO;

import Model.TopProductDTO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TopProductDAO {

    public List<TopProductDTO> getTopProducts(LocalDate from, LocalDate to, int limit) throws SQLException {
        String sql = """
            SELECT p.productName, SUM(od.quantity) AS quantitySold, SUM(od.quantity * od.unitPrice) AS totalRevenue
            FROM orders o
            JOIN order_details od ON o.orderId = od.orderId
            JOIN products p ON od.productId = p.productId
            WHERE DATE(o.orderDate) BETWEEN ? AND ?
            GROUP BY p.productId
            ORDER BY quantitySold DESC
            LIMIT ?
        """;
        return queryTopProducts(sql, from, to, limit);
    }

    public List<TopProductDTO> getTopProductsByMonth(LocalDate from, LocalDate to, int limit) throws SQLException {
        String sql = """
            SELECT p.productName, SUM(od.quantity) AS quantitySold, SUM(od.quantity * od.unitPrice) AS totalRevenue
            FROM orders o
            JOIN order_details od ON o.orderId = od.orderId
            JOIN products p ON od.productId = p.productId
            WHERE DATE_FORMAT(o.orderDate, '%Y-%m') BETWEEN DATE_FORMAT(?, '%Y-%m') AND DATE_FORMAT(?, '%Y-%m')
            GROUP BY p.productId
            ORDER BY quantitySold DESC
            LIMIT ?
        """;
        return queryTopProducts(sql, from, to, limit);
    }

    public List<TopProductDTO> getTopProductsByYear(LocalDate from, LocalDate to, int limit) throws SQLException {
        String sql = """
            SELECT p.productName, SUM(od.quantity) AS quantitySold, SUM(od.quantity * od.unitPrice) AS totalRevenue
            FROM orders o
            JOIN order_details od ON o.orderId = od.orderId
            JOIN products p ON od.productId = p.productId
            WHERE YEAR(o.orderDate) BETWEEN YEAR(?) AND YEAR(?)
            GROUP BY p.productId
            ORDER BY quantitySold DESC
            LIMIT ?
        """;
        return queryTopProducts(sql, from, to, limit);
    }

    private List<TopProductDTO> queryTopProducts(String sql, LocalDate from, LocalDate to, int limit) throws SQLException {
        List<TopProductDTO> list = new ArrayList<>();
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setInt(3, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TopProductDTO dto = new TopProductDTO(
                        rs.getString("productName"),
                        rs.getInt("quantitySold"),
                        rs.getDouble("totalRevenue")
                );
                list.add(dto);
            }
        }
        return list;
    }
}
