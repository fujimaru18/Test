package DAO;

import Model.DanhMuc;
import db.DBConnection;
import util.Constants.DBConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DanhMucDAO {

    public List<DanhMuc> getAll() {
        List<DanhMuc> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBConstants.TABLE_CATEGORY;
        try (Connection conn = DBConnection.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                DanhMuc dm = new DanhMuc(
                        rs.getInt(DBConstants.CATEGORY_ID),
                        rs.getString(DBConstants.CATEGORY_NAME),
                        rs.getString(DBConstants.CATEGORY_DESCRIPTION)
                );
                list.add(dm);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn danh sách danh mục: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(DanhMuc dm) {
        String sql = "INSERT INTO " + DBConstants.TABLE_CATEGORY + "("
                + DBConstants.CATEGORY_NAME + "," + DBConstants.CATEGORY_DESCRIPTION + ") VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, dm.getName());
            ps.setString(2, dm.getDes());
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                System.err.println("Không có bản ghi nào được thêm.");
                return false;
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                dm.setcategoryId(rs.getInt(1));
            }
            return true;

        } catch (SQLException e) {
            String msg = e.getMessage().toLowerCase();
            if (msg.contains("unique") || msg.contains("duplicate") || msg.contains("constraint")) {
                System.err.println("Dữ liệu bị trùng, không thể thêm.");
            } else {
                System.err.println("Lỗi khi thêm danh mục: " + e.getMessage());
            }
            return false;
        }
    }

    public boolean update(DanhMuc dm) {
        String sql = "UPDATE " + DBConstants.TABLE_CATEGORY + " SET "
                + DBConstants.CATEGORY_NAME + " = ?, "
                + DBConstants.CATEGORY_DESCRIPTION + " = ? WHERE "
                + DBConstants.CATEGORY_ID + " = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dm.getName());
            ps.setString(2, dm.getDes());
            ps.setInt(3, dm.getcategoryId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật danh mục: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + DBConstants.TABLE_CATEGORY
                + " WHERE " + DBConstants.CATEGORY_ID + " = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa danh mục: " + e.getMessage());
            return false;
        }
    }

    public DanhMuc findByName(String name) {
        String sql = "SELECT * FROM " + DBConstants.TABLE_CATEGORY
                + " WHERE " + DBConstants.CATEGORY_NAME + " = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DanhMuc(
                        rs.getInt(DBConstants.CATEGORY_ID),
                        rs.getString(DBConstants.CATEGORY_NAME),
                        rs.getString(DBConstants.CATEGORY_DESCRIPTION)
                );
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm danh mục theo tên: " + e.getMessage());
        }
        return null;
    }
}
