package Model;

public class DanhMuc {

    private int categoryId;
    private String name;
    private String des;

    public DanhMuc() {
    }

    public DanhMuc(String name, String des) {
        this.name = name;
        this.des = des;
    }

    public DanhMuc(int categoryId, String name, String des) {
        this.categoryId = categoryId;
        this.name = name;
        this.des = des;
    }

    public int getcategoryId() {
        return categoryId;
    }

    public void setcategoryId(int categoryId) {
        this.categoryId = categoryId;
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
