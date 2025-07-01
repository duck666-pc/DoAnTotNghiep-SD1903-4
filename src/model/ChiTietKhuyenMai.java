/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author minhd
 */
public class ChiTietKhuyenMai {
    private String id;
    private String hinhThucGiam;
    private float soTienGiamGia;
    private String sanPhamid;
    private float mucGiamGia;
    private String quaTang;

    public ChiTietKhuyenMai() {
    }

    
    public ChiTietKhuyenMai(String id, String hinhThucGiam, float soTienGiamGia, String sanPhamid, float mucGiamGia, String quaTang) {
        this.id = id;
        this.hinhThucGiam = hinhThucGiam;
        this.soTienGiamGia = soTienGiamGia;
        this.sanPhamid = sanPhamid;
        this.mucGiamGia = mucGiamGia;
        this.quaTang = quaTang;
    }

    public String getId() {
        return id;
    }

    public String getHinhThucGiam() {
        return hinhThucGiam;
    }

    public float getSoTienGiamGia() {
        return soTienGiamGia;
    }

    public String getSanPhamid() {
        return sanPhamid;
    }

    public float getMucGiamGia() {
        return mucGiamGia;
    }

    public String getQuaTang() {
        return quaTang;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHinhThucGiam(String hinhThucGiam) {
        this.hinhThucGiam = hinhThucGiam;
    }

    public void setSoTienGiamGia(float soTienGiamGia) {
        this.soTienGiamGia = soTienGiamGia;
    }

    public void setSanPhamid(String sanPhamid) {
        this.sanPhamid = sanPhamid;
    }

    public void setMucGiamGia(float mucGiamGia) {
        this.mucGiamGia = mucGiamGia;
    }

    public void setQuaTang(String quaTang) {
        this.quaTang = quaTang;
    }

    @Override
    public String toString() {
        return "ChiTietKhuyenMai{" 
                + "id=" + id 
                + ", hinhThucGiam=" + hinhThucGiam 
                + ", soTienGiamGia=" + soTienGiamGia 
                + ", sanPhamid=" + sanPhamid 
                + ", mucGiamGia=" + mucGiamGia 
                + ", quaTang=" + quaTang 
                + '}';
    }
}
