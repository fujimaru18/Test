package App;

import View.Menu;
import view.DangNhapView;

public class MainApp {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new Menu().setVisible(true);
            //new DangNhapView().setVisible(true);
        });
    }
}
