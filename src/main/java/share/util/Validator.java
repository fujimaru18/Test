/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package share.util;

import javax.swing.JOptionPane;

/**
 *
 * @author LENOVO
 */
public class Validator {
    public static boolean isNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, fieldName + " không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean isValidPrice(String value) {
        try {
            double price = Double.parseDouble(value);
            if (price < 0) {
                JOptionPane.showMessageDialog(null, "Giá phải >= 0", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Giá không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
