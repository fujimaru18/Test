package DAO;

import Model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getProductsByCategoryId(int categoryId) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT productId, productName, categoryId, stockQuantity "
                + "FROM products WHERE categoryId = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("productId"),
                        rs.getString("productName"),
                        rs.getInt("categoryId"),
                        rs.getInt("stockQuantity")
                );
                list.add(p);
            }
        }
        return list;
    }
//    public List<Product> getProductsByCategory(int categoryId) throws SQLException {
//        return getProductsByCategoryId(categoryId);
//    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT productId, productName, categoryId, stockQuantity FROM products";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("productId"),
                        rs.getString("productName"),
                        rs.getInt("categoryId"),
                        rs.getInt("stockQuantity")
                );
                list.add(p);
            }
        }
        return list;
    }

    public Product getProductById(int productId) throws SQLException {
        String sql = "SELECT productId, productName, categoryId, stockQuantity "
                + "FROM products WHERE productId = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getInt("productId"),
                        rs.getString("productName"),
                        rs.getInt("categoryId"),
                        rs.getInt("stockQuantity")
                );
            }
        }
        return null;
    }

    public boolean updateStockAfterImport(int productId, int addedQuantity) throws SQLException {
        String sql = "UPDATE products SET stockQuantity = stockQuantity + ? WHERE productId = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, addedQuantity);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Product> getLowStockProducts(int threshold) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT productId, productName, categoryId, stockQuantity "
                + "FROM products WHERE stockQuantity < ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("productId"),
                        rs.getString("productName"),
                        rs.getInt("categoryId"),
                        rs.getInt("stockQuantity")
                );
                list.add(p);
            }
        }
        return list;
    }

//    public boolean insertProduct(Product p) throws SQLException {
//        String sql = "INSERT INTO products (productName, categoryId, stockQuantity) "
//                + "VALUES (?, ?, ?)";
//        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            ps.setString(1, p.getProductName());
//            ps.setInt(2, p.getCategoryId());
//            ps.setInt(3, p.getStockQuantity());
//            int affected = ps.executeUpdate();
//            if (affected == 1) {
//                ResultSet rs = ps.getGeneratedKeys();
//                if (rs.next()) {
//                    p.setProductId(rs.getInt(1));
//                }
//            }
//            return affected == 1;
//        }
//    }
//
//    public boolean updateProduct(Product p) throws SQLException {
//        String sql = "UPDATE products SET productName = ?, categoryId = ?, stockQuantity = ? "
//                + "WHERE productId = ?";
//        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, p.getProductName());
//            ps.setInt(2, p.getCategoryId());
//            ps.setInt(3, p.getStockQuantity());
//            ps.setInt(4, p.getProductId());
//            return ps.executeUpdate() == 1;
//        }
//    }
//    public boolean deleteProduct(int productId) throws SQLException {
//        String sql = "DELETE FROM products WHERE productId = ?";
//        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, productId);
//            return ps.executeUpdate() == 1;
//        }
//    }
}
