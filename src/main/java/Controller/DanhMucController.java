package Controller;

import Model.DanhMuc;
import Service.DanhMucService;
import View.DanhMucView;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import util.Constants.MessageConstants;
import util.Validator.CommonValidator;

public class DanhMucController {

    private final DanhMucView view;
    private final DanhMucService service;

    public DanhMucController(DanhMucView view) {
        this.view = view;
        this.service = new DanhMucService();

        view.getTxtTimKiem().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                view.getTxtTimKiem().selectAll();
            }
        });
        initEvents();
        loadTableData();
    }

    private void initEvents() {
        view.getBtnTimKiem().addActionListener(e -> search());
        view.getBtnThem().addActionListener(e -> add());
        view.getBtnSua().addActionListener(e -> edit());
        view.getBtnXoa().addActionListener(e -> delete());
        view.getBtnClear().addActionListener(e -> clear());

    }

    private void loadTableData() {
        List<DanhMuc> list = service.getAll();
        DefaultTableModel model = (DefaultTableModel) view.getTblDanhMuc().getModel();
        model.setRowCount(0); // clear old rows

        for (DanhMuc dm : list) {
            model.addRow(new Object[]{dm.getName(), dm.getDes()});
        }
    }

    private void search() {
        String keyword = view.getTxtTimKiem().getText().trim();
        if (keyword.equals("Nhập tên danh mục...") || keyword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập tên cần tìm!");
            return;
        }
        List<DanhMuc> list = service.getAll();
        DefaultTableModel model = (DefaultTableModel) view.getTblDanhMuc().getModel();
        model.setRowCount(0);

        for (DanhMuc dm : list) {
            if (dm.getName().toLowerCase().contains(keyword.toLowerCase()) //                    || dm.getDes().toLowerCase().contains(keyword)
                    ) {
                model.addRow(new Object[]{dm.getName(), dm.getDes()});
            }
        }
    }

    private void add() {
        JTextField nameField = new JTextField();
        JTextArea desField = new JTextArea(5, 20);

        Object[] message = {
            "Tên danh mục:", nameField,
            "Mô tả:", desField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Thêm danh mục", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String des = desField.getText().trim();
            if (!CommonValidator.isNotEmpty(name, "Tên danh mục")
                    || !CommonValidator.isNotEmpty(des, "Mô tả")) {
                JOptionPane.showMessageDialog(null, MessageConstants.ERROR_INSERT);
                return;
            }
            DanhMuc dm = new DanhMuc(0, name, des);
            if (service.addDanhMuc(dm)) {
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(null, MessageConstants.ERROR_INSERT + "(trùng tên?)");
            }
        }
    }

    private void edit() {
        int selectedRow = view.getTblDanhMuc().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng cần sửa!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) view.getTblDanhMuc().getModel();
        String oldName = model.getValueAt(selectedRow, 0).toString();
        String oldDes = model.getValueAt(selectedRow, 1).toString();

        JTextField nameField = new JTextField(oldName);
        JTextArea desField = new JTextArea(oldDes, 5, 20);

        Object[] message = {
            "Tên danh mục:", nameField,
            "Mô tả:", desField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Sửa danh mục", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            DanhMuc dm = service.getAll().stream()
                    .filter(d -> d.getName().equals(oldName) && d.getDes().equals(oldDes))
                    .findFirst()
                    .orElse(null);

            if (dm != null) {
                dm.setName(nameField.getText().trim());
                dm.setDes(desField.getText().trim());

                if (service.updateDanhMuc(dm)) {
                    loadTableData();
                } else {
                    JOptionPane.showMessageDialog(null, MessageConstants.ERROR_UPDATE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy danh mục để cập nhật.");
            }
        }
    }

    private void delete() {
        int selectedRow = view.getTblDanhMuc().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng cần xóa!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) view.getTblDanhMuc().getModel();
        String name = model.getValueAt(selectedRow, 0).toString();
        String des = model.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(null,
                "Bạn có chắc muốn xóa danh mục này?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DanhMuc dm = service.getAll().stream()
                    .filter(d -> d.getName().equals(name) && d.getDes().equals(des))
                    .findFirst()
                    .orElse(null);

            if (dm != null && service.deleteDanhMuc(dm.getcategoryId())) {
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(null, MessageConstants.ERROR_DELETE);
            }
        }
    }

    private void clear() {
        view.getTxtTimKiem().setText("Nhập tên danh mục...");
        loadTableData();
    }

}
