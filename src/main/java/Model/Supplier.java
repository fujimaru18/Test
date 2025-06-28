package Model;

public class Supplier {

    private int id;
    private String name;
    private String phone_number;
    private String address;
    private String email;

    public Supplier() {
    }

    public Supplier(int id) {
        this.id = id;
    }
    
    public Supplier(int id, String name, String phone_number, String address, String email) {
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
        this.address = address;
        this.email = email;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
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
