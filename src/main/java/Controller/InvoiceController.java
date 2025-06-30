/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Service.InvoiceService;
import View.InvoiceView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;


/**
 *
 * @author Tuong Hue
 */
public class InvoiceController {

    private InvoiceView form;
    private InvoiceService service;
//    private Main mainApp;
    private int orderId;

    public InvoiceController(InvoiceView form, InvoiceService service, int orderId) {
        this.form = form;
        this.service = new InvoiceService();
        this.orderId = orderId;
        this.form.getBtnPay().addActionListener(new CreatePay());
    }

    class CreatePay implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int total = form.getTotalAmountValue();
                String paymentMethod = form.getPaymentMethod();
                if (paymentMethod == "-----Chọn-----") {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn phương thức thanh toán");
                    return;
                }
                service.Pay(orderId, total, paymentMethod);
                JOptionPane.showMessageDialog(form, "Tạo hóa đơn thành công! ");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(form, "Lỗi tạo hóa đơn: " + ex.getMessage());
            }
        }
    }
}
