package Model;

import java.sql.Timestamp;

public class GiaoDichKho {
    private String id;
    private String idNguyenVatLieu;
    private Timestamp thoiGian;
    private String loai;
    private int soLuong;
    private String ghiChu;

    public GiaoDichKho() {
    }

    public GiaoDichKho(String id, String idNguyenVatLieu, Timestamp thoiGian, 
                       String loai, int soLuong, String ghiChu) {
        this.id = id;
        this.idNguyenVatLieu = idNguyenVatLieu;
        this.thoiGian = thoiGian;
        this.loai = loai;
        this.soLuong = soLuong;
        this.ghiChu = ghiChu;
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdNguyenVatLieu() {
        return idNguyenVatLieu;
    }

    public void setIdNguyenVatLieu(String idNguyenVatLieu) {
        this.idNguyenVatLieu = idNguyenVatLieu;
    }

    public Timestamp getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Timestamp thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}