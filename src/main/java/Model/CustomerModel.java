/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;



/**
 *
 * @author Tuong Hue
 */
public class CustomerModel {
    private int CustomerId;
    private String name;
    private String phone;
    private String address;
    private String note;

    public CustomerModel( String name, String phone, String address, String note) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.note = note;
    }
    
    public CustomerModel( int id ,String name, String phone, String address, String note) {
        this.CustomerId = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.note = note;
    }
    public CustomerModel() {
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public String getName() {
        return name;
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

    public void setId(int CustomerId) {
        this.CustomerId = CustomerId;
    }

    public void setName(String name) {
        this.name = name;
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
    
    
}
