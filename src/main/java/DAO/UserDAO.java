package DAO;

import java.sql.*;

public class UserDAO {
    public static String getUserNameById(int userId) throws SQLException {
        String sql = "SELECT username FROM users WHERE userId = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        }
        return "Unknown";
    }
}
