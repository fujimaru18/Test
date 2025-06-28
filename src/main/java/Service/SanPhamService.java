package Service;

import DAO.SanPhamDAO;
import Model.SanPham;
import java.util.List;
import javax.swing.JOptionPane;
import util.Constants.MessageConstants;
import util.Validator.SanPhamValidator;

public class SanPhamService {

    private final SanPhamDAO dao = new SanPhamDAO();

    public List<SanPham> getAll() {
        return dao.getAll();
    }

    public boolean addSanPham(SanPham sp) {
        if (!SanPhamValidator.isValid(sp.getName(), String.valueOf(sp.getImportPrice()), String.valueOf(sp.getSalePrice()),
                String.valueOf(sp.getStockQuantity()), sp.getUnit(), String.valueOf(sp.getCategoryId()),
                String.valueOf(sp.getSupplierId()), String.valueOf(sp.getStatus()), sp.getImage())) {
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

    public boolean updateSanPham(SanPham sp) {
        if (!SanPhamValidator.isValid(sp.getName(), String.valueOf(sp.getImportPrice()), String.valueOf(sp.getSalePrice()),
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
