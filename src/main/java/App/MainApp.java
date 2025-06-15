package App;

import View.Menu;

/**
 *
 * @author LENOVO
 */
public class MainApp {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new Menu().setVisible(true);
        });
    }
}
