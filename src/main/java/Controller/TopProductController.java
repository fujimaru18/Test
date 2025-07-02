package Controller;

import DAO.TopProductDAO;
import Model.TopProductDTO;
import View.TopProductPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TopProductController {

    private final TopProductPanel view;
    private final TopProductDAO dao;

    public TopProductController(TopProductPanel view, TopProductDAO dao) {
        this.view = view;
        this.dao = dao;
        view.getBtnView().addActionListener(this::handleViewTopProducts);
    }

    private void handleViewTopProducts(ActionEvent e) {
        try {
            String gran = view.getCboGranularity().getSelectedItem().toString();
            LocalDate from, to;

            switch (gran) {
                case "Tháng" -> {
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/yyyy");
                    YearMonth ym = YearMonth.parse(view.getTxtFromDate().getText().trim(), fmt);
                    from = ym.atDay(1);
                    to = ym.atEndOfMonth();
                }
                case "Năm" -> {
                    int year = Integer.parseInt(view.getTxtFromDate().getText().trim());
                    from = LocalDate.of(year, 1, 1);
                    to = LocalDate.of(year, 12, 31);
                }
                default -> {
                    from = LocalDate.parse(view.getTxtFromDate().getText().trim());
                    to = LocalDate.parse(view.getTxtToDate().getText().trim());
                }
            }

            if (from.isAfter(to)) {
                JOptionPane.showMessageDialog(view, "'Từ' phải trước hoặc bằng 'Đến'");
                return;
            }

            List<TopProductDTO> data = switch (gran) {
                case "Tháng" ->
                    dao.getTopProductsByMonth(from, to, 10);
                case "Năm" ->
                    dao.getTopProductsByYear(from, to, 10);
                default ->
                    dao.getTopProducts(from, to, 10);
            };

            DefaultTableModel model = (DefaultTableModel) view.getTblResult().getModel();
            model.setRowCount(0);
            for (TopProductDTO dto : data) {
                model.addRow(new Object[]{
                    dto.getProductName(),
                    dto.getQuantitySold(),
                    String.format("%,.0f", dto.getTotalRevenue())
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi dữ liệu: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
