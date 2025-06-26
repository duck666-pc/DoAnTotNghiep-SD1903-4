package model;

public class NguyenVatLieu {

    private String id;
    private String ten;
    private String donVi;
    private int soLuong;
    private int mucCanDatThem;

    public NguyenVatLieu() {
    }

    public NguyenVatLieu(String id, String ten, String donVi, int soLuong, int mucCanDatThem) {
        this.id = id;
        this.ten = ten;
        this.donVi = donVi;
        this.soLuong = soLuong;
        this.mucCanDatThem = mucCanDatThem;
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

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getMucCanDatThem() {
        return mucCanDatThem;
    }

    public void setMucCanDatThem(int mucCanDatThem) {
        this.mucCanDatThem = mucCanDatThem;
    }

    @Override
    public String toString() {
        return "NguyenVatLieu{"
                + "id=" + id
                + ", ten='" + ten + '\''
                + ", donVi=" + donVi
                + ", soLuong=" + soLuong
                + ", mucCanDatThem='" + mucCanDatThem + '\''
                + '}';
    }

}
