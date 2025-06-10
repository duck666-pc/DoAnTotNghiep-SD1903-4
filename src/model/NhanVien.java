package model;

import java.util.Date;

public class NhanVien {
    private String id;           
    private String matKhau;
    private String tenDayDu;
    private Date ngaySinh;
    private String gioiTinh;
    private String email;

    public NhanVien() {
    }

    public NhanVien(String id, String matKhau, String tenDayDu, Date ngaySinh, String gioiTinh, String email) {
        this.id = id;
        this.matKhau = matKhau;
        this.tenDayDu = tenDayDu;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getTenDayDu() {
        return tenDayDu;
    }

    public void setTenDayDu(String tenDayDu) {
        this.tenDayDu = tenDayDu;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "id=" + id +
                ", tenDayDu='" + tenDayDu + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", gioiTinh=" + gioiTinh +
                ", email='" + email + '\'' +
                '}';
    }
}
