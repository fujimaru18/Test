package Service;

import DAO.DanhMucDAO;
import Model.DanhMuc;
import util.Constants.MessageConstants;
import util.Validator.DanhMucValidator;

import javax.swing.*;
import java.util.List;

public class DanhMucService {

    private final DanhMucDAO dao = new DanhMucDAO();

    public List<DanhMuc> getAll() {
        return dao.getAll();
    }

    public boolean addDanhMuc(DanhMuc dm) {
        if (!DanhMucValidator.isValid(dm.getName(), dm.getDes())) {
            return false;
        }

        if (dao.findByName(dm.getName()) != null) {
            showError(MessageConstants.ERROR_DUPLICATE_DATA);
            return false;
        }

        boolean success = dao.insert(dm);
        if (success) {
            showSuccess(MessageConstants.SUCCESS_INSERT);
        } else {
            showError(MessageConstants.ERROR_DATABASE);
        }
        return success;
    }

    public boolean updateDanhMuc(DanhMuc dm) {
        if (!DanhMucValidator.isValid(dm.getName(), dm.getDes())) {
            return false;
        }

        boolean success = dao.update(dm);
        if (success) {
            showSuccess(MessageConstants.SUCCESS_UPDATE);
        } else {
            showError(MessageConstants.ERROR_DATABASE);
        }
        return success;
    }

    public boolean deleteDanhMuc(int id) {
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
