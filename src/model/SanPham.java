package model;

public class SanPham {
    private String id;           
    private String ten;
    private String moTa;
    private float gia;
    private String loaiSanPham;    

    public SanPham() {
    }

    public SanPham(String id, String ten, String moTa, float gia, String loaiSanPham) {
        this.id = id;
        this.ten = ten;
        this.moTa = moTa;
        this.gia= gia;
        this.loaiSanPham = loaiSanPham;
    }

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

    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }    
    
    public String getLoaiSanPham() {
        return loaiSanPham;
    }
    
    public void setLoaiSanPham(String loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "id=" + id +
                ", ten='" + ten + '\'' +
                ", moTa=" + moTa +
                ", gia=" + gia +
                ", loaiSanPham='" + loaiSanPham + '\'' +
                '}';
    }
}
