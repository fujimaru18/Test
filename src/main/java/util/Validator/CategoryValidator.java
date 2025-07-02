package util.Validator;

public class CategoryValidator {

    public static boolean isValid(String name, String description) {
        return isValidName(name) && isValidDescription(description);
    }

    public static boolean isValidName(String name) {
        return CommonValidator.isNotEmpty(name, "Tên danh mục")
                && CommonValidator.isMaxLength(name, 100, "Tên danh mục");
    }

    public static boolean isValidDescription(String description) {
        return CommonValidator.isNotEmpty(description, "Mô tả")
                && CommonValidator.isMaxLength(description, 255, "Mô tả");
    }
}
