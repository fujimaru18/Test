package Controller;

import DAO.CategoryDAO;
import Model.Category;
import java.sql.SQLException;
import java.util.List;

public class CategoryController {

    private CategoryDAO categoryDAO;

    public CategoryController() {
        categoryDAO = new CategoryDAO();
    }

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.getAllCategories();
    }
}
