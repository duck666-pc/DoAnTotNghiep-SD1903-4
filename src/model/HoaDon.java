package Model;

import java.sql.Timestamp;
import java.math.*;

public class HoaDon {
    private String id;
    private Timestamp thoiGian;
    private String idKhachHang;
    private String idNguoiDung;
    private BigDecimal tongTienGoc;
    private BigDecimal mucGiamGia;
    private BigDecimal tongTienSauGiamGia;

    public HoaDon() {
    }

    public HoaDon(String id, Timestamp thoiGian, String idKhachHang, String idNguoiDung, 
                  BigDecimal tongTienGoc, BigDecimal mucGiamGia, BigDecimal tongTienSauGiamGia) {
        this.id = id;
        this.thoiGian = thoiGian;
        this.idKhachHang = idKhachHang;
        this.idNguoiDung = idNguoiDung;
        this.tongTienGoc = tongTienGoc;
        this.mucGiamGia = mucGiamGia;
        this.tongTienSauGiamGia = tongTienSauGiamGia;
    }

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

    public BigDecimal getTongTienGoc() {
        return tongTienGoc;
    }

    public void setTongTienGoc(BigDecimal tongTienGoc) {
        this.tongTienGoc = tongTienGoc;
    }

    public BigDecimal getMucGiamGia() {
        return mucGiamGia;
    }

    public void setMucGiamGia(BigDecimal mucGiamGia) {
        this.mucGiamGia = mucGiamGia;
    }

    public BigDecimal getTongTienSauGiamGia() {
        return tongTienSauGiamGia;
    }

    public void setTongTienSauGiamGia(BigDecimal tongTienSauGiamGia) {
        this.tongTienSauGiamGia = tongTienSauGiamGia;
    }
}