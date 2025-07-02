package Controller;

import DAO.RevenueDAO;
import Model.RevenueDTO;
import View.RevenuePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RevenueController {

    private final RevenuePanel view;
    private final RevenueDAO dao;

    public RevenueController(RevenuePanel view, RevenueDAO dao) {
        this.view = view;
        this.dao = dao;
        init();
    }

    private void init() {
        view.getBtnStat().addActionListener(this::onStat);
    }

    private void onStat(ActionEvent e) {
        try {
            String gran = (String) view.getCboGran().getSelectedItem();
            LocalDate from, to;

            switch (gran) {
                case "Tháng" -> {
                    // Sử dụng YearMonth để lấy ngày đầu và cuối tháng
                    DateTimeFormatter monthFmt = DateTimeFormatter.ofPattern("MM/yyyy");
                    YearMonth ym = YearMonth.parse(view.getTxtFrom().getText().trim(), monthFmt);
                    from = ym.atDay(1);               // ngày đầu tháng
                    to = ym.atEndOfMonth();           // ngày cuối tháng
                }
                case "Năm" -> {
                    int year = Integer.parseInt(view.getTxtFrom().getText().trim());
                    from = LocalDate.of(year, 1, 1);
                    to = LocalDate.of(year, 12, 31);
                }
                default -> {
                    from = LocalDate.parse(view.getTxtFrom().getText().trim());
                    to = LocalDate.parse(view.getTxtTo().getText().trim());
                }
            }

            // Gọi DAO
            List<RevenueDTO> list = switch (gran) {
                case "Tháng" ->
                    dao.getMonthlyRevenue(from, to);
                case "Năm" ->
                    dao.getYearlyRevenue(from, to);
                default ->
                    dao.getDailyRevenue(from, to);
            };

            // Hiển thị bảng
            DefaultTableModel model = (DefaultTableModel) view.getTblResult().getModel();
            model.setRowCount(0);
            double total = 0;

            for (RevenueDTO dto : list) {
                model.addRow(new Object[]{
                    dto.getLabel(),
                    dto.getInvoiceCount(),
                    String.format("%,.0f", dto.getTotal())
                });
                total += dto.getTotal();
            }

            view.getLblTotal().setText(String.format("Tổng doanh thu: %,.0f VND", total));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
