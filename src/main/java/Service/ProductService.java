package Service;

import Controller.SupplierController;
import DAO.CategoryDAO;
import DAO.ProductDAO;
import Model.Category;
import Model.Product;
import Model.Supplier;
import java.util.List;
import javax.swing.JOptionPane;
import util.Constants.MessageConstants;
import util.Validator.ProductValidator;

public class ProductService {

    private final ProductDAO dao = new ProductDAO();

    public List<Category> getAllCategories() {
        return new CategoryDAO().getAll(); // DAO riêng cho Category
    }
    
    public List<Supplier> getAllSuppliers() {
        return new SupplierController().getAll();
    }
    

    public List<Product> getAll() {
        return dao.getAll();
    }

    public boolean addSanPham(Product sp) {
        if (!ProductValidator.isValid(sp.getName(), String.valueOf(sp.getImportPrice()), String.valueOf(sp.getSalePrice()),
                String.valueOf(sp.getStockQuantity()), sp.getUnit(), String.valueOf(sp.getCategoryId().getcategoryId()),
                String.valueOf(sp.getSupplierId().getsupplierId()), String.valueOf(sp.getStatus()), sp.getImage())) {
            return false;
        }
        if (dao.findByName(sp.getName()) != null) {
            showError(MessageConstants.ERROR_DUPLICATE_DATA);
            return false;
        }

        boolean success = dao.insert(sp);
        if (success) {
            showSuccess(MessageConstants.SUCCESS_INSERT);
        } else {
            showError(MessageConstants.ERROR_DATABASE);
        }
        return success;
    }

    public boolean updateSanPham(Product sp) {
        if (!ProductValidator.isValid(sp.getName(), String.valueOf(sp.getImportPrice()), String.valueOf(sp.getSalePrice()),
                String.valueOf(sp.getStockQuantity()), sp.getUnit(), String.valueOf(sp.getCategoryId()),
                String.valueOf(sp.getSupplierId()), String.valueOf(sp.getStatus()), sp.getImage())) {
            return false;
        }

        boolean success = dao.update(sp);
        if (success) {
            showSuccess(MessageConstants.SUCCESS_UPDATE);
        } else {
            showError(MessageConstants.ERROR_DATABASE);
        }
        return success;
    }

    public boolean deleteSanPham(int id) {
        boolean success = dao.delete(id);
        if (success) {
            showSuccess(MessageConstants.SUCCESS_DELETE);
        } else {
            showError(MessageConstants.ERROR_DATABASE);
        }
        return success;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, MessageConstants.TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, MessageConstants.TITLE_WARNING, JOptionPane.INFORMATION_MESSAGE);
    }
}
