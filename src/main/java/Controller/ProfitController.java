package Controller;

import DAO.InvoiceDAO;
import Model.ProfitDTO;
import View.ProfitPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class ProfitController {

    private final ProfitPanel view;
    private final InvoiceDAO dao;

    public ProfitController(ProfitPanel view, InvoiceDAO dao) {
        this.view = view;
        this.dao = dao;
        init();
    }

    private void init() {
        view.getBtnStat().addActionListener(this::onStat);
    }

    private void onStat(ActionEvent e) {
        try {
            String gran = view.getCboGran().getSelectedItem().toString();
            LocalDate from, to;

            switch (gran) {
                case "Tháng" -> {
                    String input = view.getTxtFrom().getText().trim(); // MM/yyyy
                    YearMonth ym = YearMonth.parse(input, java.time.format.DateTimeFormatter.ofPattern("MM/yyyy"));
                    from = ym.atDay(1);
                    to = ym.atEndOfMonth();
                }
                case "Năm" -> {
                    int year = Integer.parseInt(view.getTxtFrom().getText().trim());
                    from = LocalDate.of(year, 1, 1);
                    to = LocalDate.of(year, 12, 31);
                }
                default -> { // Ngày
                    from = LocalDate.parse(view.getTxtFrom().getText().trim()); // yyyy-MM-dd
                    to = LocalDate.parse(view.getTxtTo().getText().trim());
                }
            }

            List<ProfitDTO> list = switch (gran) {
                case "Tháng" ->
                    dao.getMonthlyProfit(from, to);
                case "Năm" ->
                    dao.getYearlyProfit(from, to);
                default ->
                    dao.getDailyProfit(from, to);
            };

            DefaultTableModel model = (DefaultTableModel) view.getTblResult().getModel();
            model.setRowCount(0);
            double total = 0;
            for (ProfitDTO dto : list) {

                model.addRow(new Object[]{
                    dto.getTimeLabel(),
                    String.format("%,.0f", dto.getRevenue()),
                    String.format("%,.0f", dto.getCost()),
                    String.format("%,.0f", dto.getProfit())
                });

                total += dto.getProfit();
            }

            view.getLblTotal().setText(String.format("Tổng lợi nhuận: %,.0f VND", total));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
        }
    }
}
