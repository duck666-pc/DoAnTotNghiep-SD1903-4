package Model;

public class LoaiSanPham {
    private String id;
    private String ten;
    private String moTa;

    public LoaiSanPham() {
    }

    public LoaiSanPham(String id, String ten, String moTa) {
        this.id = id;
        this.ten = ten;
        this.moTa = moTa;
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}