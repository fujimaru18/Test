/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;



/**
 *
 * @author Tuong Hue
 */
public class OrderDetailModel {

    private int OrderId;
    private int productId;
    private int quantity;
    private int unitPrice;
    private String productName;

    public OrderDetailModel(int OrderId, int productId, int quantity, int unitPrice) {
        this.OrderId = OrderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public OrderDetailModel() {
    }

    public int getOrderId() {
        return OrderId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setOrderId(int OrderId) {
        this.OrderId = OrderId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

}
