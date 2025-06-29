package Controller;

import DAO.ImportReceiptDAO;
import Model.ImportDetailBySupplierDTO;
import View.ImportBySupplierPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ImportBySupplierController {
    private final ImportBySupplierPanel view;
    private final ImportReceiptDAO dao;

    public ImportBySupplierController(ImportBySupplierPanel view, ImportReceiptDAO dao) {
        this.view = view;
        this.dao = dao;
        view.getBtnStat().addActionListener(this::handleStat);
    }

    private void handleStat(ActionEvent e) {
        try {
            String gran = view.getCboGran().getSelectedItem().toString();
            LocalDate from, to;

            switch (gran) {
                case "Tháng" -> {
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/yyyy");
                    from = LocalDate.parse("01/" + view.getTxtFrom().getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    to = from.plusMonths(1).minusDays(1);
                }
                case "Năm" -> {
                    int year = Integer.parseInt(view.getTxtFrom().getText().trim());
                    from = LocalDate.of(year, 1, 1);
                    to = LocalDate.of(year, 12, 31);
                }
                default -> { // Ngày
                    from = LocalDate.parse(view.getTxtFrom().getText().trim());
                    to = LocalDate.parse(view.getTxtTo().getText().trim());
                }
            }

            if (from.isAfter(to)) {
                JOptionPane.showMessageDialog(view, "'Từ' phải trước hoặc bằng 'Đến'");
                return;
            }

            List<ImportDetailBySupplierDTO> data = dao.getDetailedImportBySupplier(from, to);
            DefaultTableModel model = (DefaultTableModel) view.getTblResult().getModel();
            model.setRowCount(0);

            double total = 0;
            for (ImportDetailBySupplierDTO d : data) {
                double thanhTien = d.getTotal();
                total += thanhTien;
                model.addRow(new Object[]{
                        d.getSupplierName(),
                        d.getCategoryName(),
                        d.getProductName(),
                        d.getQuantity(),
                        String.format("%,.0f", d.getImportPrice()),
                        String.format("%,.0f", thanhTien)
                });
            }

            view.getLblTotal().setText("Tổng tiền: " + String.format("%,.0f", total) + " VND");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi dữ liệu: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
