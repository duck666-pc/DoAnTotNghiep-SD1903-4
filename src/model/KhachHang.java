package model;

public class KhachHang {
    private String id;
    private String ten;
    private String dienThoai;
    private String diaChi;
    private String hangKhachHangid;

    public KhachHang() {
    }

    public KhachHang(String id, String ten, String dienThoai, String diaChi, String hangKhachHangid) {
        this.id = id;
        this.ten = ten;
        this.dienThoai = dienThoai;
        this.diaChi = diaChi;
        this.hangKhachHangid = hangKhachHangid;
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

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String gethangKhachHangId() {
        return hangKhachHangid;
    }

    public void sethangKhachHangId(String hangKhachHangid) {
        this.hangKhachHangid = hangKhachHangid;
    }
}