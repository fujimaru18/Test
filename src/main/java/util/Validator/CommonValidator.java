package util.Validator;

import util.Constants.MessageConstants;

import javax.swing.JOptionPane;

public class CommonValidator {

    //check trống
    public static boolean isNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            showError(String.format(MessageConstants.ERROR_EMPTY_FIELD, fieldName));
            return false;
        }
        return true;
    }

    //check độ dài
    public static boolean isMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            showError(String.format(MessageConstants.ERROR_MAX_LENGTH, fieldName, maxLength));
            return false;
        }
        return true;
    }

    //check số tự nhiên
    public static boolean isPositiveInteger(String value, String fieldName) {
        // Kiểm tra không được để trống trước
        if (!isNotEmpty(value, fieldName)) {
            return false;
        }

        try {
            int number = Integer.parseInt(value);
            if (number < 0) {
                showError(String.format(MessageConstants.ERROR_NEGATIVE_NUMBER, fieldName));
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showError(String.format(MessageConstants.ERROR_INVALID_NUMBER, fieldName));
            return false;
        }
    }

    public static boolean isValidImage(byte[] image, String fieldName) {
    // Kiểm tra null
    if (image == null || image.length == 0) {
        showError(String.format("Trường %s không được để trống.", fieldName));
        return false;
    }

    // Kiểm tra kích thước ảnh (ví dụ: không quá 5MB)
    final int MAX_SIZE = 5 * 1024 * 1024; // 5 MB
    if (image.length > MAX_SIZE) {
        showError(String.format("Kích thước %s vượt quá giới hạn cho phép (tối đa 5MB).", fieldName));
        return false;
    }

    return true;
}

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, MessageConstants.TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
