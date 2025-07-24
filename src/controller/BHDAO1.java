/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.*;
import java.time.LocalDate;
/**
 *
 * @author Admin
 */
public class BHDAO1 {
    public ResultSet getHD(){
        MyConnection DBconnect = new MyConnection();
        try{
            Connection conn = DBconnect.DBConnect();
            String sql = "Select * from HoaDon";
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    public ResultSet getSP(){
        MyConnection DBconnect = new MyConnection();
        try{
            Connection conn = DBconnect.DBConnect();
            String sql = "select ID,Ten,Gia from SanPham";
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    public ResultSet getCTHD(String ID){
        MyConnection DBconnect = new MyConnection();
        try{
            Connection conn = DBconnect.DBConnect();
            String sql = "select MAX(ID),MAX(SanPhamID),SUM(SoSanPhamThanhToan) as soluong,GiaBanMoiSanPham,(SUM(SoSanPhamThanhToan)*GiaBanMoiSanPham) as ThanhTien from ChiTietHoaDon " +
                         "where HoaDonID = ? " +
                         "GROUP BY HoaDonID,GiaBanMoiSanPham";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ID);
            return stmt.executeQuery();
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    public boolean addCTHD(String ID, int soLuong, String idHoaDon, String idSanPham, double donGia){
        MyConnection DBconnect = new MyConnection();
        try{
            Connection conn = DBconnect.DBConnect();
            String sql = "INSERT INTO ChiTietHoaDon (ID, SoSanPhamThanhToan, HoaDonID, SanPhamID, GiaBanMoiSanPham)"
                    + " VALUES (?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ID);
            stmt.setInt(2, soLuong);
            stmt.setString(3, idHoaDon);
            stmt.setString(4, idSanPham);
            stmt.setDouble(5, donGia);
            return stmt.executeUpdate() > 0;
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteCTHD(String IDHD, String ID){
        MyConnection DBconnect = new MyConnection();
        try{
            Connection conn = DBconnect.DBConnect();
            String sql = "DELETE FROM ChiTietHoaDon where HoaDonID = ? AND SanPhamID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, IDHD);
            stmt.setString(2, ID);
            return stmt.executeUpdate() > 0;
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateCTHD(double ThanhTien,String ID){
        MyConnection DBconnect = new MyConnection();
        try{
            Connection conn = DBconnect.DBConnect();
            String sql = "UPDATE HoaDon"
                    + " SET TongTienGoc = ? where ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, ThanhTien);
            stmt.setString(2, ID);
            return stmt.executeUpdate() > 0;
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateCTHD1(String ID, int soLuong, String idSanPham, double donGia, String idhd){
        MyConnection DBconnect = new MyConnection();
        try{
            Connection conn = DBconnect.DBConnect();
            String sql = "UPDATE ChiTietHoaDon"
                    + " SET ID = ?, SoSanPhamThanhToan = ?, SanPhamID = ?, GiaBanMoiSanPham = ? where ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ID);
            stmt.setInt(2, soLuong);
            stmt.setString(3, idSanPham);
            stmt.setDouble(4, donGia);
            stmt.setString(5,idhd);
            return stmt.executeUpdate() > 0;
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace(); 
            return false;
        }
    }
    public boolean addHD(String id, String thoiGian, String idKhachHang, String idNguoiDung, double tongTienGoc, double mucGiamGia, double tongTienSauGiamGia){
        MyConnection DBconnect = new MyConnection();
        try{
            Connection conn = DBconnect.DBConnect();
            String query = "Insert Into  HoaDon (ID, ThoiGian, KhachHangID, NguoiDungID, TongTienGoc, MucGiamGia, TongTienSauGiamGia)"
                    + " VALUES(?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            stmt.setDate(2, Date.valueOf(thoiGian));
            stmt.setString(3, idKhachHang);
            stmt.setString(4, idNguoiDung);
            stmt.setDouble(5, tongTienGoc);
            stmt.setDouble(6, mucGiamGia);
            stmt.setDouble(7, tongTienSauGiamGia);
            return stmt.executeUpdate() > 0;
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateHD(String id, String thoiGian, String idKhachHang, String idNguoiDung, double tongTienGoc, double mucGiamGia, double tongTienSauGiamGia, String IDHD){
        MyConnection DBconnect = new MyConnection();
        try{
            Connection conn = DBconnect.DBConnect();
            String query = "Update HoaDon "
                         + "set ID = ?, ThoiGian = ?, KhachHangID = ?, NguoiDungID = ?, TongTienGoc = ?, MucGiamGia = ?, TongTienSauGiamGia = ? where ID = ? "
                         + "Update ChiTietHoaDon "
                         + "Set HoaDonId = ? where HoaDonId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            stmt.setDate(2, Date.valueOf(thoiGian));
            stmt.setString(3, idKhachHang);
            stmt.setString(4, idNguoiDung);
            stmt.setDouble(5, tongTienGoc);
            stmt.setDouble(6, mucGiamGia);
            stmt.setDouble(7, tongTienSauGiamGia);
            stmt.setString(8, IDHD);
            stmt.setString(9, id);
            stmt.setString(10, IDHD);
            return stmt.executeUpdate() >= 0;
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteHD(String IDHD){
        MyConnection DBconnect = new MyConnection();
        try{
            Connection conn = DBconnect.DBConnect();
            String query = "Delete from ChiTietHoaDon where HoaDonId = ?"
                         + "Delete from HoaDon where ID = ? ";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, IDHD);
            stmt.setString(2, IDHD);
            return stmt.executeUpdate() >= 0;
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
