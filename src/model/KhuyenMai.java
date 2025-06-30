/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


import java.util.Date;
/**
 *
 * @author minhd
 */
public class KhuyenMai {
    private String id;
    private String chiTietid;   
    private String khachHangid;  
    private String ten; 
    private String moTa;
    private int soLuong;
    private Date thoiGianApDung;
    private Date thoiGianKetThuc;

    public KhuyenMai(String id, String chiTietid, String khachHangid, String ten, String moTa, int soLuong, Date thoiGianApDung, Date thoiGianKetThuc) {
        this.id = id;
        this.chiTietid = chiTietid;
        this.khachHangid = khachHangid;
        this.ten = ten;
        this.moTa = moTa;
        this.soLuong = soLuong;
        this.thoiGianApDung = thoiGianApDung;
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public String getId() {
        return id;
    }

    public String getChiTietid() {
        return chiTietid;
    }

    public String getKhachHangid() {
        return khachHangid;
    }

    public String getTen() {
        return ten;
    }

    public String getMoTa() {
        return moTa;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public Date getThoiGianApDung() {
        return thoiGianApDung;
    }

    public Date getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setChiTietid(String chiTietid) {
        this.chiTietid = chiTietid;
    }

    public void setKhachHangid(String khachHangid) {
        this.khachHangid = khachHangid;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setThoiGianApDung(Date thoiGianApDung) {
        this.thoiGianApDung = thoiGianApDung;
    }

    public void setThoiGianKetThuc(Date thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    @Override
    public String toString() {
        return "KhuyenMai{" 
                + "id=" + id 
                + ", chiTietid=" + chiTietid 
                + ", khachHangid=" + khachHangid 
                + ", ten=" + ten 
                + ", moTa=" + moTa 
                + ", soLuong=" + soLuong 
                + ", thoiGianApDung=" + thoiGianApDung 
                + ", thoiGianKetThuc=" + thoiGianKetThuc 
                + '}';
    }
  
}
