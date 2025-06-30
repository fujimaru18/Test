/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import View.InvoiceDetail;
import View.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import javax.swing.JFileChooser;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

/**
 *
 * @author Tuong Hue
 */
public class InvoiceDetailController {

    private static InvoiceDetail form;
    private static Menu menu;

    public InvoiceDetailController(InvoiceDetail form, Menu menu) {
        this.form = form;
        this.menu = menu;

        this.form.getBtnExport().addActionListener(new ExportInvoice());
    }

    class ExportInvoice implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu hóa đơn");
            fileChooser.setCurrentDirectory(new File("D:\\hoadon"));
            fileChooser.setSelectedFile(new File("hoadon.txt"));

            int option = fileChooser.showSaveDialog(menu);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
                    // Thông tin hóa đơn
                    writer.println("===== HÓA ĐƠN THANH TOÁN =====");
                    writer.println("Mã hóa đơn       : " + form.getLbIdInvoice().getText());
                    writer.println("Mã đơn hàng      : " + form.getLbOrderId().getText());
                    writer.println("Ngày tạo đơn     : " + form.getLbOrderDate().getText());
                    writer.println("Ngày thanh toán  : " + form.getLbDate().getText());

                    // Thông tin khách hàng
                    writer.println("------------ Thông tin khách hàng ------------");
                    writer.println("Mã khách hàng    : " + form.getLbCustomerId().getText());
                    writer.println("Tên khách hàng   : " + form.getLbName().getText());
                    writer.println("Số điện thoại    : " + form.getLbPhone().getText());
                    writer.println("Địa chỉ          : " + form.getLbAdress().getText());
                    writer.println("Ghi chú          : ");

                    // Bảng sản phẩm
                    writer.println("------------ Thông tin sản phẩm --------------");
                    writer.println("+------------+----------------------+-----------+-----------+-----------+");
                    writer.printf("| %-10s | %-20s | %-9s | %-9s | %-9s |\n",
                            form.getTblItems().getColumnName(0),
                            form.getTblItems().getColumnName(1),
                            form.getTblItems().getColumnName(2),
                            form.getTblItems().getColumnName(3),
                            form.getTblItems().getColumnName(4) );
                    writer.println("+------------+----------------------+-----------+-----------+-----------+");

                    for (int row = 0; row < form.getTblItems().getRowCount(); row++) {
                        writer.printf("| %-10s | %-20s | %-9s | %-9s | %-9s |\n",
                                form.getTblItems().getValueAt(row, 0),
                                form.getTblItems().getValueAt(row, 1),
                                form.getTblItems().getValueAt(row, 2),
                                form.getTblItems().getValueAt(row, 3),
                                form.getTblItems().getValueAt(row, 4) + "VND");
                    }

                    writer.println("+------------+----------------------+-----------+-----------+-----------+");

                    // Tổng tiền và phương thức thanh toán
                    writer.println();
                    writer.println("Tổng cộng           : " + form.getLbTotal().getText() + " VND");
                    writer.println("Phương thức thanh toán: " + form.getLbPayment().getText());
                    writer.println("==============================================");

                    JOptionPane.showMessageDialog(menu, "Đã lưu hóa đơn!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(menu, "Lỗi khi ghi file: " + ex.getMessage());
                }

            }
        }
    }
}
