package Controller;

import Model.ChiTietHoaDon;
import Model.HoaDon;
import controller.MyConnection;
import model.SanPham;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BANHANGDAO {

    private final MyConnection conn;

    public BANHANGDAO() {
        conn = new MyConnection();
    }
    
    public List<HoaDon> getAllHoaDon() throws ClassNotFoundException, SQLException{
        List<HoaDon> lst = new ArrayList<>();
        String query = "SELECT * FROM HoaDon";
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()){
            HoaDon hd = new HoaDon();
            hd.setId(rs.getString("Id"));
            hd.setThoiGian(rs.getTimestamp("ThoiGian"));
            hd.setIdKhachHang(rs.getString("IdKhachHang"));
            hd.setIdNguoiDung(rs.getString("IdNguoiDung"));
            hd.setTongTienGoc(rs.getBigDecimal("TongTienGoc"));
            hd.setMucGiamGia(rs.getBigDecimal("MucGiamGia"));
            hd.setTongTienSauGiamGia(rs.getBigDecimal("TongTienSauGiamGia"));
            lst.add(hd);
        }
        return lst;
    }
    
    public List<String[]> getChiTietHoaDon(String idHoaDon) throws ClassNotFoundException, SQLException{
        String query = "SELECT cthd.Id, cthd.SoLuong, sp.Ten AS TenSanPham, cthd.DonGia "
                     + "FROM ChiTietHoaDon cthd "
                     + "INNER JOIN SanPham sp ON cthd.IdSanPham = sp.Id "
                     + "WHERE cthd.IdHoaDon = ?";
        Connection connect = conn.DBConnect();
        PreparedStatement stmt = connect.prepareStatement(query); 
        stmt.setString(1, idHoaDon);
        ResultSet rs = stmt.executeQuery();
        List<String[]> lst = new ArrayList<>();
        while(rs.next()){
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

    public List<ChiTietHoaDon> getAllChiTietHoaDon(String idHoaDon) throws ClassNotFoundException, SQLException{
        String query = "SELECT * FROM ChiTietHoaDon WHERE IdHoaDon = ?";
        Connection connect = conn.DBConnect();
        PreparedStatement stmt = connect.prepareStatement(query); 
        stmt.setString(1, idHoaDon);
        ResultSet rs = stmt.executeQuery();
        List<ChiTietHoaDon> lst = new ArrayList<>();
        while(rs.next()){
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
    
    public List<SanPham> getAllSanPham() throws ClassNotFoundException, SQLException{
        List<SanPham> lst = new ArrayList<>();
        String query = "SELECT * FROM SanPham";
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()){
            SanPham sp = new SanPham();
            sp.setId(rs.getString("Id"));
            sp.setTen(rs.getString("Ten"));
            sp.setMoTa(rs.getString("MoTa"));
            sp.setGia((float) rs.getDouble("Gia")); 
            sp.setIdLoaiSanPham(rs.getString("IdLoaiSanPham"));
            lst.add(sp);
        }
        return lst;
    }
    
    public boolean ThemHoaDon(HoaDon hd) throws ClassNotFoundException, SQLException{
        String query = "INSERT INTO HoaDon (Id, ThoiGian, KhachHangId, NguoiDungId, TongTienGoc, MucGiamGia, TongTienSauGiamGia) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connect = conn.DBConnect(); 
        PreparedStatement stmt = connect.prepareStatement(query); 

        String newId = UUID.randomUUID().toString();
        
        stmt.setString(1, newId);
        stmt.setTimestamp(2, hd.getThoiGian());
        stmt.setString(3, hd.getIdKhachHang());
        stmt.setString(4, hd.getIdNguoiDung());
        stmt.setBigDecimal(5, hd.getTongTienGoc());
        stmt.setBigDecimal(6, hd.getMucGiamGia());
        stmt.setBigDecimal(7, hd.getTongTienSauGiamGia());
        
        int rowAdded = stmt.executeUpdate();
        return rowAdded > 0;
    }
    
    public boolean ThemChiTietHD(ChiTietHoaDon cthd) throws ClassNotFoundException, SQLException{
        String query = "INSERT INTO ChiTietHoaDon (Id, HoaDonId, SanPhamId, SoLuong, DonGia) "
                     + "VALUES (?, ?, ?, ?, ?)";
        Connection connect = conn.DBConnect(); 
        PreparedStatement stmt = connect.prepareStatement(query); 

        String newId = UUID.randomUUID().toString();
        
        stmt.setString(1, newId);
        stmt.setString(2, cthd.getIdHoaDon());
        stmt.setString(3, cthd.getIdSanPham());
        stmt.setInt(4, cthd.getSoLuong());
        stmt.setDouble(5, cthd.getDonGia());
        
        int rowAdded = stmt.executeUpdate();
        return rowAdded > 0;
    }
        
    public boolean UpdateChiTietHD(int soluong, String idCTHD) throws ClassNotFoundException, SQLException{
        String query = "UPDATE ChiTietHoaDon SET SoLuong = ? WHERE Id = ?";
        Connection connect = conn.DBConnect(); 
        PreparedStatement stmt = connect.prepareStatement(query); 
        stmt.setInt(1, soluong);
        stmt.setString(2, idCTHD);
        int rowUpdated = stmt.executeUpdate();
        return rowUpdated > 0;
    }
    
    public boolean UpdateHoaDon(double tongtien, String idHoaDon) throws ClassNotFoundException, SQLException{
        String query = "UPDATE HoaDon SET TongTienGoc = ? WHERE Id = ?";
        Connection connect = conn.DBConnect(); 
        PreparedStatement stmt = connect.prepareStatement(query); 
        stmt.setDouble(1, tongtien);
        stmt.setString(2, idHoaDon);
        int rowUpdated = stmt.executeUpdate();
        return rowUpdated > 0;
    }
        
    public boolean XoaChiTietHD(String idCTHD) throws ClassNotFoundException, SQLException{
        String query = "DELETE FROM ChiTietHoaDon WHERE Id = ?";
        Connection connect = conn.DBConnect(); 
        PreparedStatement stmt = connect.prepareStatement(query);
        stmt.setString(1, idCTHD);
        int rowDeleted = stmt.executeUpdate();
        return rowDeleted > 0;
    }
    
    public boolean XoaHoaDon(HoaDon hd) throws ClassNotFoundException, SQLException{
        Connection connect = conn.DBConnect(); 
        String query1 = "DELETE FROM ChiTietHoaDon WHERE HOADONID = ?";
        PreparedStatement stmt1 = connect.prepareStatement(query1); 
        stmt1.setString(1, hd.getId());
        stmt1.executeUpdate();
        
        String query2 = "DELETE FROM HoaDon WHERE Id = ?";
        PreparedStatement stmt2 = connect.prepareStatement(query2); 
        stmt2.setString(1, hd.getId());
        int rowDeleted = stmt2.executeUpdate();
        return rowDeleted > 0;
    }
}