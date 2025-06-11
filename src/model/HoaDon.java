package Model;

import java.sql.Timestamp;

public class HoaDon {
    private String id;
    private Timestamp thoiGian;
    private String idKhachHang;
    private String idNguoiDung;
    private double tongTienGoc;
    private double mucGiamGia;
    private double tongTienSauGiamGia;

    public HoaDon() {
    }

    public HoaDon(String id, Timestamp thoiGian, String idKhachHang, String idNguoiDung, 
                  double tongTienGoc, double mucGiamGia, double tongTienSauGiamGia) {
        this.id = id;
        this.thoiGian = thoiGian;
        this.idKhachHang = idKhachHang;
        this.idNguoiDung = idNguoiDung;
        this.tongTienGoc = tongTienGoc;
        this.mucGiamGia = mucGiamGia;
        this.tongTienSauGiamGia = tongTienSauGiamGia;
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Timestamp thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public String getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(String idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public double getTongTienGoc() {
        return tongTienGoc;
    }

    public void setTongTienGoc(double tongTienGoc) {
        this.tongTienGoc = tongTienGoc;
    }

    public double getMucGiamGia() {
        return mucGiamGia;
    }

    public void setMucGiamGia(double mucGiamGia) {
        this.mucGiamGia = mucGiamGia;
    }

    public double getTongTienSauGiamGia() {
        return tongTienSauGiamGia;
    }

    public void setTongTienSauGiamGia(double tongTienSauGiamGia) {
        this.tongTienSauGiamGia = tongTienSauGiamGia;
    }
}