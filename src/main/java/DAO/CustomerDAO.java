/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import Model.CustomerModel;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Tuong Hue
 */
public class CustomerDAO {

    public int insert(CustomerModel c) throws SQLException {
        String sql = "INSERT INTO customers (fullname, phone, address) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, c.getName());
            stmt.setString(2, c.getPhone());
            stmt.setString(3, c.getAddress());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

//    public void update(int id, String name, String phone, String address, String note) {
//        String sql = "UPDATE customers SET fullname = ?, phone = ?, address =? , note= ?  WHERE customerId = ?";
//        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, name);
//            ps.setString(2, phone);
//            ps.setString(3, address);
//            ps.setString(4, note);
//            ps.setInt(5, id);
//
//            ps.executeUpdate();  // true nếu cập nhật thành công
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    
    
    public void update(CustomerModel item) {
        String sql = "UPDATE customers SET fullname = ?, phone = ?, address =? , note= ?  WHERE customerId = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, item.getName());
            ps.setString(2, item.getPhone());
            ps.setString(3, item.getAddress());
            ps.setString(4, item.getNote());
            ps.setInt(5,item.getCustomerId());

            ps.executeUpdate();  // true nếu cập nhật thành công

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM customers WHERE customerId = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate(); // đúng cho DELETE, INSERT, UPDATE
            return rowsAffected > 0; // trả về true nếu có ít nhất 1 dòng bị xóa

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public CustomerModel findByPhone(String phone) throws SQLException {
        String sql = "SELECT * FROM customers WHERE phone = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CustomerModel customer = new CustomerModel();
                    customer.setId(rs.getInt("CustomerId")); // Hoặc tên cột trong DB
                    customer.setName(rs.getString("fullName"));
                    customer.setPhone(rs.getString("phone"));
                    customer.setAddress(rs.getString("address"));
                    customer.setNote(rs.getString("note"));
                    return customer;
                }
            }
        }
        return null;
    }

    public List<CustomerModel> getAll() {
        List<CustomerModel> list = new ArrayList<>();
        String sql = "SELECT *FROM customers";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("customerId");
                String name = rs.getString("fullName");
                String phone = rs.getString("Phone");
                String address = rs.getString("address");
                String note = rs.getString("note");

                CustomerModel customer = new CustomerModel(id, name, phone, address, note);
                list.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public CustomerModel getById(int customerId) throws SQLException {
        CustomerModel customer = null;

        String sql = "SELECT * FROM customers WHERE customerId = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    customer = new CustomerModel();
                    customer.setId(rs.getInt("customerId"));
                    customer.setName(rs.getString("fullName"));
                    customer.setPhone(rs.getString("phone"));
                    customer.setAddress(rs.getString("address"));
                    // Thêm các cột khác nếu có
                }
            }
        }

        return customer;
    }

    public List<CustomerModel> search(String keyword) throws SQLException {
        List<CustomerModel> list = new ArrayList<>();

        String sql = "SELECT * FROM customers WHERE "
                + "CAST(customerId AS CHAR) LIKE ? OR "
                + // tìm gần đúng ID
                "fullName LIKE ? OR "
                + "phone LIKE ? OR "
                + "address LIKE ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchKey = "%" + keyword + "%";

            stmt.setString(1, searchKey);
            stmt.setString(2, searchKey);
            stmt.setString(3, searchKey);
            stmt.setString(4, searchKey);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CustomerModel customer = new CustomerModel();
                    customer.setId(rs.getInt("customerId"));
                    customer.setName(rs.getString("fullName"));
                    customer.setPhone(rs.getString("phone"));
                    customer.setAddress(rs.getString("address"));
                    customer.setNote(rs.getString("note"));
                    list.add(customer);
                }
            }
        }

        return list;
    }

    public int getCustomerIdByInfo(CustomerModel customer) throws SQLException {
        String sql = "SELECT customerId FROM customers WHERE fullName = ? AND phone = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("customerId");
            }
        }
        return -1; // Không tìm thấy
    }

}
