package share.util;

import javax.swing.JOptionPane;

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
