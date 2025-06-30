package Controller;

import java.util.List;
import Model.User;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserAccountController {

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User(
                        rs.getInt("userId"),
                        rs.getString("fullName"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("status")
                );
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<User> searchByName(String keyword) {
        List<User> result = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE fullName LIKE ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("userId"),
                        rs.getString("fullName"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("status")
                );
                result.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean insert(User u) {
        String sql = "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, u.getUserId());
            ps.setString(2, u.getFullName());
            ps.setString(3, u.getUserName());
            ps.setString(4, u.getPassword());
            ps.setString(5, u.getRole());
            ps.setInt(6, u.getStatus());

            return ps.executeUpdate() > 0;  // trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(User u) {
        String sql = "UPDATE users SET fullName = ?, username = ?, password = ?, role = ?, status = ? WHERE userId = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getUserName());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getRole());
            ps.setInt(5, u.getStatus());
            ps.setInt(6, u.getUserId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE userId = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
