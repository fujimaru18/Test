package DAO;

import Model.Category;
import Model.Product;
import Model.Supplier;
import db.DBConnection;
import util.Constants.DBConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBConstants.TABLE_PRODUCT;
        try (Connection conn = DBConnection.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Category categoryId = new Category(rs.getInt(DBConstants.CATEGORY_ID));
                Supplier supplierId = new Supplier(rs.getInt(DBConstants.SUPPLIER_ID));

                Product sp = new Product(
                        rs.getInt(DBConstants.PRODUCT_ID),
                        rs.getString(DBConstants.PRODUCT_NAME),
                        rs.getInt(DBConstants.PRODUCT_IMPORT_PRICE),
                        rs.getInt(DBConstants.PRODUCT_SALE_PRICE),
                        rs.getInt(DBConstants.PRODUCT_STOCK_QUANTITY),
                        rs.getString(DBConstants.PRODUCT_UNIT),
                        categoryId,
                        supplierId,
                        rs.getInt(DBConstants.PRODUCT_STATUS),
                        rs.getBytes(DBConstants.PRODUCT_IMAGE)
                );
                list.add(sp);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn danh sách sản phẩm: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(Product sp) {
        String sql = "INSERT INTO " + DBConstants.TABLE_PRODUCT + "("
                + DBConstants.PRODUCT_NAME + "," + DBConstants.PRODUCT_IMPORT_PRICE + ","
                + DBConstants.PRODUCT_SALE_PRICE + "," + DBConstants.PRODUCT_STOCK_QUANTITY + "," + DBConstants.PRODUCT_UNIT + ","
                + DBConstants.PRODUCT_CATEGORY_ID + "," + DBConstants.PRODUCT_SUPPLIER_ID + ","
                + DBConstants.PRODUCT_STATUS + "," + DBConstants.PRODUCT_IMAGE + ") VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, sp.getName());
            ps.setInt(2, sp.getImportPrice());
            ps.setInt(3, sp.getSalePrice());
            ps.setInt(4, sp.getStockQuantity());
            ps.setString(5, sp.getUnit());
            ps.setInt(6, sp.getCategoryId().getcategoryId());
            ps.setInt(7, sp.getSupplierId().getsupplierId());
            ps.setInt(8, sp.getStatus());
            ps.setBytes(9, sp.getImage());
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                System.err.println("Không có bản ghi nào được thêm.");
                return false;
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                sp.setproductId(rs.getInt(1));
            }
            return true;

        } catch (SQLException e) {
            String msg = e.getMessage().toLowerCase();
            if (msg.contains("unique") || msg.contains("duplicate") || msg.contains("constraint")) {
                System.err.println("Dữ liệu bị trùng, không thể thêm.");
            } else {
                System.err.println("Lỗi khi thêm sản phẩm: " + e.getMessage());
            }
            return false;
        }
    }

    public boolean update(Product sp) {
        String sql = "UPDATE " + DBConstants.TABLE_PRODUCT + " SET "
                + DBConstants.PRODUCT_NAME + "=?, "
                + DBConstants.PRODUCT_IMPORT_PRICE + "=?, "
                + DBConstants.PRODUCT_SALE_PRICE + "=?, "
                + DBConstants.PRODUCT_STOCK_QUANTITY + "=?, "
                + DBConstants.PRODUCT_UNIT + "=?, "
                + DBConstants.PRODUCT_CATEGORY_ID + "=?, "
                + DBConstants.PRODUCT_SUPPLIER_ID + "=?, "
                + DBConstants.PRODUCT_STATUS + "=?, "
                + DBConstants.PRODUCT_IMAGE + "=? "
                + "WHERE " + DBConstants.PRODUCT_ID + "=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sp.getName());
            ps.setInt(2, sp.getImportPrice());
            ps.setInt(3, sp.getSalePrice());
            ps.setInt(4, sp.getStockQuantity());
            ps.setString(5, sp.getUnit());
            ps.setInt(6, sp.getCategoryId().getcategoryId());
            ps.setInt(7, sp.getSupplierId().getsupplierId());
            ps.setInt(8, sp.getStatus());
            ps.setBytes(9, sp.getImage());
            ps.setInt(10, sp.getproductId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int productId) {
        String sql = "DELETE FROM " + DBConstants.TABLE_PRODUCT + " WHERE " + DBConstants.PRODUCT_ID + " = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa sản phẩm: " + e.getMessage());
            return false;
        }
    }

    public Product findByName(String name) {
        String sql = "SELECT * FROM " + DBConstants.TABLE_PRODUCT
                + " WHERE " + DBConstants.PRODUCT_NAME + " = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Category category = new Category(rs.getInt(DBConstants.CATEGORY_ID));
                Supplier supplier = new Supplier(rs.getInt(DBConstants.SUPPLIER_ID));

                return new Product(
                        rs.getInt(DBConstants.PRODUCT_ID),
                        rs.getString(DBConstants.PRODUCT_NAME),
                        rs.getInt(DBConstants.PRODUCT_IMPORT_PRICE),
                        rs.getInt(DBConstants.PRODUCT_SALE_PRICE),
                        rs.getInt(DBConstants.PRODUCT_STOCK_QUANTITY),
                        rs.getString(DBConstants.PRODUCT_UNIT),
                        category,
                        supplier,
                        rs.getInt(DBConstants.PRODUCT_STATUS),
                        rs.getBytes(DBConstants.PRODUCT_IMAGE)
                );
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm sản phẩm theo tên: " + e.getMessage());
        }

        return null;
    }

    public List<Product> getByCategoryId(int cateId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT productId, productName, salePrice, categoryId FROM products WHERE categoryId = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cateId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setproductId(rs.getInt("productId"));
                product.setName(rs.getString("productName"));
                product.setSalePrice(rs.getInt("salePrice")); 
                Category category = new Category();
                category.setcategoryId(rs.getInt("categoryId"));
                product.setCategoryId(category);

                list.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
