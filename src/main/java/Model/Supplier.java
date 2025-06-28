package Model;

public class Supplier {

    private int supplierId;
    private String name;
    private String phone_number;
    private String address;
    private String email;

    public Supplier() {
    }

    public Supplier(int supplierId) {
        this.supplierId = supplierId;
    }

    public Supplier(int supplierId, String name, String phone_number, String address, String email) {
        this.supplierId = supplierId;
        this.name = name;
        this.phone_number = phone_number;
        this.address = address;
        this.email = email;
    }

    public int getsupplierId() {
        return supplierId;
    }

    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void setsupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
