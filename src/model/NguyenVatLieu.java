package Model;

public class NguyenVatLieu {
    private String id;
    private String ten;
    private String idDonVi;
    private int soLuong;
    private int mucCanDatThemHang;

    public NguyenVatLieu() {
    }

    public NguyenVatLieu(String id, String ten, String idDonVi, int soLuong, int mucCanDatThemHang) {
        this.id = id;
        this.ten = ten;
        this.idDonVi = idDonVi;
        this.soLuong = soLuong;
        this.mucCanDatThemHang = mucCanDatThemHang;
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

    public String getIdDonVi() {
        return idDonVi;
    }

    public void setIdDonVi(String idDonVi) {
        this.idDonVi = idDonVi;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getMucCanDatThemHang() {
        return mucCanDatThemHang;
    }

    public void setMucCanDatThemHang(int mucCanDatThemHang) {
        this.mucCanDatThemHang = mucCanDatThemHang;
    }
}