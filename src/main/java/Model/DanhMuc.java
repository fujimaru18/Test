package Model;

public class DanhMuc {

    private int id;
    private String name;
    private String des;

    public DanhMuc() {
    }

    public DanhMuc(String name, String des) {
        this.name = name;
        this.des = des;
    }

    public DanhMuc(int id, String name, String des) {
        this.id = id;
        this.name = name;
        this.des = des;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public String toString() {
        return name;
    }

}
