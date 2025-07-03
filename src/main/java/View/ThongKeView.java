package View;

import Controller.LowStockController;
import Controller.ProfitController;
import javax.swing.JTabbedPane;

import DAO.ImportReceiptDAO;
import DAO.ProfitDAO;
import DAO.ProductDAO;
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

            LowStockPanel lowStockPanel = new LowStockPanel();
            new LowStockController(lowStockPanel, new ProductDAO());
            tabbedPane.addTab("Tồn kho thấp", lowStockPanel);

            ProfitPanel profitPanel = new ProfitPanel();
            new ProfitController(profitPanel, new ProfitDAO());
            tabbedPane.addTab("Lợi nhuận", profitPanel);

        } catch (SQLException e) {
            e.printStackTrace();
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

        ThongKeView view = new ThongKeView();
        view.setVisible(true);
    }

}
