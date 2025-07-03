package Controller;

import Model.ChiTietHoaDon;
import Model.HoaDon;
import controller.MyConnection;
import model.SanPham;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BANHANGDAO {

    private final MyConnection conn;

    public BANHANGDAO() {
        conn = new MyConnection();
    }

    public List<HoaDon> getAllHoaDon() throws ClassNotFoundException, SQLException {
        List<HoaDon> lst = new ArrayList<>();
        String query = "SELECT * FROM HOADON";
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            HoaDon hd = new HoaDon();
            hd.setId(rs.getString("ID"));
            hd.setThoiGian(rs.getTimestamp("THOIGIAN"));
            hd.setIdKhachHang(rs.getString("KHACHHANGID"));
            hd.setIdNguoiDung(rs.getString("NGUOIDUNGID"));
            hd.setTongTienGoc(rs.getBigDecimal("TONGTIENGOC"));
            hd.setMucGiamGia(rs.getBigDecimal("MUCGIAMGIA"));
            hd.setTongTienSauGiamGia(rs.getBigDecimal("TONGTIENSAUGIAMGIA"));
            lst.add(hd);
        }
        return lst;
    }

    public int getcountHoaDon() throws ClassNotFoundException, SQLException {
        int count = 0;
        String query = "SELECT COUNT(*) AS COUNT FROM HOADON";
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            count = rs.getInt("count");
        }
        return count;
    }

    public int gettotalHoaDon() throws ClassNotFoundException, SQLException {
        int count = 0;
        String query = "SELECT SUM AS COUNT FROM HOADON";
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            count = rs.getInt("count");
        }
        return count;
    }

    public List<HoaDon> getAllHoaDondate(String bday, String bmonth, String byear, String eday, String emonth, String eyear) throws ClassNotFoundException, SQLException {
        List<HoaDon> lst = new ArrayList<>();
        String query = "SELECT * FROM HOADON WHERE THOIGIAN BETWEEN '%s-%s-%s' and '%s-%s-%s'";
        query = String.format(query, byear, bmonth, bday, eyear, emonth, eday);
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            HoaDon hd = new HoaDon();
            hd.setId(rs.getString("Id"));
            hd.setThoiGian(rs.getTimestamp("ThoiGian"));
            hd.setIdKhachHang(rs.getString("KHACHHANGID"));
            hd.setIdNguoiDung(rs.getString("NguoiDungId"));
            hd.setTongTienGoc(rs.getBigDecimal("TongTienGoc"));
            hd.setMucGiamGia(rs.getBigDecimal("MucGiamGia"));
            hd.setTongTienSauGiamGia(rs.getBigDecimal("TongTienSauGiamGia"));
            lst.add(hd);
        }
        return lst;
    }

    public int getcountHoaDondate(String bday, String bmonth, String byear, String eday, String emonth, String eyear) throws ClassNotFoundException, SQLException {
        int count = 0;
        String query = "SELECT count(*) as count FROM HoaDon where ThoiGian between '%s-%s-%s' and '%s-%s-%s'";
        query = String.format(query, byear, bmonth, bday, eyear, emonth, eday);
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            count = rs.getInt("count");
        }
        return count;
    }

    public int gettotalHoaDondate(String bday, String bmonth, String byear, String eday, String emonth, String eyear) throws ClassNotFoundException, SQLException {
        int count = 0;
        String query = "SELECT SUM(TongTienSauGiamGia) AS count FROM HoaDon "
                + "WHERE ThoiGian BETWEEN '%s-%s-%s' AND '%s-%s-%s'";
        query = String.format(query, byear, bmonth, bday, eyear, emonth, eday);
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            count = rs.getInt("count");
        }
        return count;
    }

    public List<String[]> getChiTietHoaDon(String idHoaDon) throws ClassNotFoundException, SQLException {
        String query = "SELECT cthd.Id, cthd.SoLuong, sp.Ten AS TenSanPham, hd.TongTienSauGiamGia "
                + "FROM ChiTietHoaDon cthd "
                + "INNER JOIN SanPham sp ON cthd.SanPhamId = sp.Id "
                + "INNER JOIN HoaDon hd ON cthd.HoaDonId = hd.Id "
                + "WHERE cthd.HoaDonId = ?";
        Connection connect = conn.DBConnect();
        PreparedStatement stmt = connect.prepareStatement(query);
        stmt.setString(1, idHoaDon);
        ResultSet rs = stmt.executeQuery();
        List<String[]> lst = new ArrayList<>();
        while (rs.next()) {
            String[] object = new String[]{
                rs.getString("Id"),
                rs.getString("SoLuong"),
                rs.getString("TenSanPham"),
                rs.getString("DonGia")
            };
            lst.add(object);
        }
        return lst;
    }

    public List<ChiTietHoaDon> getAllChiTietHoaDon(String idHoaDon) throws ClassNotFoundException, SQLException {
        String query = "SELECT * FROM ChiTietHoaDon WHERE HoaDonId = ?";
        Connection connect = conn.DBConnect();
        PreparedStatement stmt = connect.prepareStatement(query);
        stmt.setString(1, idHoaDon);
        ResultSet rs = stmt.executeQuery();
        List<ChiTietHoaDon> lst = new ArrayList<>();
        while (rs.next()) {
            ChiTietHoaDon ct = new ChiTietHoaDon();
            ct.setId(rs.getString("Id"));
            ct.setIdHoaDon(rs.getString("IdHoaDon"));
            ct.setIdSanPham(rs.getString("IdSanPham"));
            ct.setSoLuong(rs.getInt("SoLuong"));
            ct.setDonGia(rs.getDouble("DonGia"));
            lst.add(ct);
        }
        return lst;
    }

    public List<SanPham> getAllSanPham() throws ClassNotFoundException, SQLException {
        List<SanPham> lst = new ArrayList<>();
        String query = "SELECT * FROM SanPham";
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            SanPham sp = new SanPham();
            sp.setId(rs.getString("Id"));
            sp.setTen(rs.getString("Ten"));
            sp.setMoTa(rs.getString("MoTa"));
            sp.setGia((float) rs.getDouble("Gia"));
            sp.setLoaiSanPham(rs.getString("LoaiSanPhamId"));
            lst.add(sp);
        }
        return lst;
    }
}
