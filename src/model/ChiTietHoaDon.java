package Model;

public class ChiTietHoaDon {
    private String id;
    private int soLuong;
    private String idHoaDon;
    private String idSanPham;
    private double donGia;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(String id, int soLuong, String idHoaDon, String idSanPham, double donGia) {
        this.id = id;
        this.soLuong = soLuong;
        this.idHoaDon = idHoaDon;
        this.idSanPham = idSanPham;
        this.donGia = donGia;
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getIdHoaDon() {
        return idHoaDon;
    }

    public void setIdHoaDon(String idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public String getIdSanPham() {
        return idSanPham;
    }

    public void setIdSanPham(String idSanPham) {
        this.idSanPham = idSanPham;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }
}