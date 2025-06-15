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
    // check số thực
//    public static boolean isPositiveDouble(String value, String fieldName) {
//        try {
//            double number = Double.parseDouble(value);
//            if (number < 0) {
//                showError(String.format(MessageConstants.ERROR_NEGATIVE_NUMBER, fieldName));
//                return false;
//            }
//            return true;
//        } catch (NumberFormatException e) {
//            showError(String.format(MessageConstants.ERROR_INVALID_NUMBER, fieldName));
//            return false;
//        }
//    }

    //check số tự nhiên
    public static boolean isPositiveInteger(String value, String fieldName) {
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

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, MessageConstants.TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
