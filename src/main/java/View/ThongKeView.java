
package View;
import Controller.ImportBySupplierController;
import Controller.LowStockController;
import Controller.ProfitController;
import javax.swing.JTabbedPane;
import View.RevenuePanel;
import Controller.RevenueController;
import DAO.RevenueDAO;

import View.TopProductPanel;
import Controller.TopProductController;
import DAO.ImportReceiptDAO;
import DAO.InvoiceDAO;
import DAO.ProductDAO;
import DAO.TopProductDAO;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;


public class ThongKeView extends JFrame {
    private JTabbedPane tabbedPane;

    public ThongKeView() {
        setTitle("Thống kê tổng hợp");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
    }

private void initUI() {
    tabbedPane = new JTabbedPane();

    try {
        // S-01: Doanh thu
        RevenuePanel revenuePanel = new RevenuePanel();
        new RevenueController(revenuePanel, new RevenueDAO());
        tabbedPane.addTab("Doanh thu", revenuePanel);

        // S-02: Top bán chạy
        TopProductPanel topPanel = new TopProductPanel();
        new TopProductController(topPanel, new TopProductDAO());
        tabbedPane.addTab("Top bán chạy", topPanel);

        // S-03: Tồn kho thấp
        LowStockPanel lowStockPanel = new LowStockPanel();
        new LowStockController(lowStockPanel, new ProductDAO());
        tabbedPane.addTab("Tồn kho thấp", lowStockPanel);
        
        
                // S-04: Nhập theo nhà cung cấp
        ImportBySupplierPanel importPanel = new ImportBySupplierPanel();
        new ImportBySupplierController(importPanel, new ImportReceiptDAO());
        tabbedPane.addTab("Nhập theo NCC", importPanel);

        // S-05: Lợi nhuận
        ProfitPanel profitPanel = new ProfitPanel();
        new ProfitController(profitPanel, new InvoiceDAO());
        tabbedPane.addTab("Lợi nhuận", profitPanel);

        // Các tab S-04, S-05 bạn thêm sau...

    } catch (SQLException e) {
        e.printStackTrace(); // hoặc hiển thị JOptionPane nếu cần
        JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage());
    }

    setContentPane(tabbedPane);
}


    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // KHÔNG dùng invokeLater
        ThongKeView view = new ThongKeView();
        view.setVisible(true);
    }

}
