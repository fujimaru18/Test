/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Tuong Hue
 */
public class OrderModel {
    private int OrderId;
    private int CustomerId;
    private Timestamp orderDate;
    private String status;
    private String customerName;
    private String phone;
    public OrderModel(int OrderId, String customerName, String phone, Timestamp  orderDate, String status) {
        this.OrderId = OrderId;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.status = status;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public OrderModel() {
    }

    public int getOrderId() {
        return OrderId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    

    public Timestamp  getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setOrderId(int OrderId) {
        this.OrderId = OrderId;
    }

    public void setCustomerId(int CustomerId) {
        this.CustomerId = CustomerId;
    }

    

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
    
    
}
