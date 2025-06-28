package App;

import view.DangNhapView;
import View.Menu;



public class MainApp {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new Menu().setVisible(true);
        });
    }
}
