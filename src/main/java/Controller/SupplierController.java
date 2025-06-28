package Controller;

import Model.Supplier;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import db.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierController {

    public List<Supplier> getAll() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Supplier s = new Supplier(
                        rs.getInt("supplierId"),
                        rs.getString("supplierName"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("email")
                );
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(Supplier s) {
        String sql = "INSERT INTO suppliers VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getPhone_number());
            ps.setString(4, s.getAddress());
            ps.setString(5, s.getEmail());

            return ps.executeUpdate() > 0;  // trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isDuplicateId(String id) {
        String sql = "SELECT COUNT(*) FROM suppliers WHERE supplierId = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu số lượng > 0 tức là đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Supplier s) {
        String sql = "UPDATE suppliers SET supplierName = ?, phone = ?, address = ?, email = ? WHERE supplierId = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getPhone_number());
            ps.setString(3, s.getAddress());
            ps.setString(4, s.getEmail());
            ps.setInt(5, s.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM suppliers WHERE supplierId = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Supplier> searchByName(String keyword) {
        List<Supplier> result = new ArrayList<>();
        String sql = "SELECT * FROM suppliers WHERE supplierName LIKE ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Supplier s = new Supplier(
                        rs.getInt("supplierId"),
                        rs.getString("supplierName"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("email")
                );
                result.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
