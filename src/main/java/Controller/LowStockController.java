package Controller;

import DAO.ProductDAO;
import Model.Product;
import View.LowStockPanel;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LowStockController {

    private final LowStockPanel view;
    private final ProductDAO dao;

    public LowStockController(LowStockPanel view, ProductDAO dao) throws SQLException {
        this.view = view;
        this.dao = dao;
        init();
    }

    private void init() throws SQLException {
        view.getBtnFilter().addActionListener(e -> {
            try {
                loadData();
            } catch (SQLException ex) {
                Logger.getLogger(LowStockController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        loadData(); 
    }

    private void loadData() throws SQLException {
        try {
            int threshold = Integer.parseInt(view.getTxtThreshold().getText().trim());
            if (threshold < 0) {
                throw new NumberFormatException();
            }

            List<Product> list = dao.getLowStockProducts(threshold);
            DefaultTableModel model = view.getTableModel();
            model.setRowCount(0);

            for (Product p : list) {
                model.addRow(new Object[]{
                    p.getProductId(),
                    p.getProductName(),
                    p.getStockQuantity()
                });
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Ngưỡng phải là số nguyên dương.");
        }
    }
}
