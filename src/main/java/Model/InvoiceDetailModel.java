/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Timestamp;

/**
 *
 * @author Tuong Hue
 */
public class InvoiceDetailModel {
    // invoice
    private int invoiceId;
    private int orderId;
    private Timestamp paymentDate;
    private int totalAmount;
    private String paymentMethod;
    //customers
    private int CustomerId;
    private String CustomerName;
    private String phone;
    private String address;
    private String note;
    //product
    private int productId;
    private String productName;
//    private double price;
    //order_detail
    private int quantity;
    private int unitPrice;
    private int total_items;
    private Timestamp orderDate;

    public InvoiceDetailModel(int invoiceId, int orderId, Timestamp paymentDate, Timestamp orderDate, int totalAmount, String paymentMethod, int CustomerId, String CustomerName, String phone, String address,  int productId, String productName, int quantity, int unitPrice, int total_items) {
        this.invoiceId = invoiceId;
        this.orderId = orderId;
        this.paymentDate = paymentDate;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.CustomerId = CustomerId;
        this.CustomerName = CustomerName;
        this.phone = phone;
        this.address = address;
//        this.note = note;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total_items = total_items;
        this.orderDate = orderDate;
    }

  

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    

    public int getInvoiceId() {
        return invoiceId;
    }

    public int getOrderId() {
        return orderId;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getNote() {
        return note;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

//    public double getPrice() {
//        return price;
//    }

    public int getTotal_items() {
        return total_items;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
    }

    
    
    public int getQuantity() {
        return quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setCustomerId(int CustomerId) {
        this.CustomerId = CustomerId;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

//    public void setPrice(double price) {
//        this.price = price;
//    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    
    
}
