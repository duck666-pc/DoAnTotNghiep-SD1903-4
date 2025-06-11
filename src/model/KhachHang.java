package Model;

public class KhachHang {
    private String id;
    private String ten;
    private String dienThoai;
    private String diaChi;
    private String idHangKhachHang;

    public KhachHang() {
    }

    public KhachHang(String id, String ten, String dienThoai, String diaChi, String idHangKhachHang) {
        this.id = id;
        this.ten = ten;
        this.dienThoai = dienThoai;
        this.diaChi = diaChi;
        this.idHangKhachHang = idHangKhachHang;
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

    public String getIdHangKhachHang() {
        return idHangKhachHang;
    }

    public void setIdHangKhachHang(String idHangKhachHang) {
        this.idHangKhachHang = idHangKhachHang;
    }
}