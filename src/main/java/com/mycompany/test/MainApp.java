/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.test;
import share.db.DBConnection;
import view.MainFrame;

/**
 *
 * @author LENOVO
 */

public class MainApp {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);  // ✅ Đúng
        });
    }
}