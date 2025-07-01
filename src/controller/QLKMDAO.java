/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.KhuyenMai;
import model.ChiTietKhuyenMai;

/**
 *
 * @author minhd
 */
public class QLKMDAO {

    private final MyConnection conn;

    public QLKMDAO() {
        conn = new MyConnection();
    }

    public List<KhuyenMai> getAllKM() throws SQLException, ClassNotFoundException {
        List<KhuyenMai> lstKM = new ArrayList<>();
        String query = "SELECT * FROM KHUYENMAI";
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            KhuyenMai km = new KhuyenMai();
            km.setId(rs.getString("ID"));
            km.setChiTietid(rs.getString("CHITIETID"));
            km.setKhachHangid(rs.getString("KHACHHANGID"));
            km.setTen(rs.getString("TEN"));
            km.setMoTa(rs.getString("MOTA"));
            km.setSoLuong(rs.getInt("SOLUONG"));
            km.setThoiGianApDung(rs.getDate("THOIGIANAPDUNG"));
            km.setThoiGianKetThuc(rs.getDate("THOIGIANKETTHUC"));            
            lstKM.add(km);
        }
        return lstKM;
    }
    
    public List<ChiTietKhuyenMai> getAllCTKM() throws SQLException, ClassNotFoundException {
        List<ChiTietKhuyenMai> lstCTKM = new ArrayList<>();
        String query = "SELECT * FROM CHITIETKHUYENMAI";
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            ChiTietKhuyenMai ctkm = new ChiTietKhuyenMai();
            ctkm.setId(rs.getString("ID"));
            ctkm.setHinhThucGiam(rs.getString("HINHTHUCGIAM"));
            ctkm.setSoTienGiamGia(rs.getFloat("SOTIENGIAMGIA"));
            ctkm.setSanPhamid(rs.getString("SANPHAMID"));
            ctkm.setMucGiamGia(rs.getFloat("MUCGIAMGIA"));
            ctkm.setQuaTang(rs.getString("QUATANG"));           
            lstCTKM.add(ctkm);
        }
        return lstCTKM;
    }    

    public int addKM(KhuyenMai km) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO KHUYENMAI (ID, CHITIETID, KHACHHANGID, TEN, MOTA, SOLUONG, THOIGIANAPDUNG, THOIGIANKETTHUC) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, km.getId());
            pstm.setString(2, km.getChiTietid());
            pstm.setString(3, km.getKhachHangid());
            pstm.setString(4, km.getTen());
            pstm.setString(5, km.getMoTa());
            pstm.setInt(6, km.getSoLuong());
            pstm.setDate(7, (Date) km.getThoiGianApDung());
            pstm.setDate(8, (Date) km.getThoiGianKetThuc());            

            if (pstm.executeUpdate() > 0) {
                return 1;
            }
        }
        return 0;
    }
    
    public int addCTKM(ChiTietKhuyenMai ctkm) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO CHITIETKHUYENMAI (ID, HINHTHUCGIAM, SOTIENGIAMGIA, SANPHAMID, MUCGIAMGIA, QUATANG) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, ctkm.getId());
            pstm.setString(2, ctkm.getHinhThucGiam());
            pstm.setFloat(3, ctkm.getSoTienGiamGia());            
            pstm.setString(4, ctkm.getSanPhamid());
            pstm.setFloat(5, ctkm.getMucGiamGia());            
            pstm.setString(6, ctkm.getQuaTang());         

            if (pstm.executeUpdate() > 0) {
                return 1;
            }
        }
        return 0;
    }    

    public int editKM(KhuyenMai km, String oldId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE SANPHAM SET "
                + "ID = ?, "
                + "CHITIETID = ?, "                
                + "KHACHHANGID = ?, "                
                + "TEN = ?, "
                + "MOTA = ?, "
                + "SOLUONG = ?, "
                + "THOIGIANAPDUNG = ? "
                + "THOIGIANKETTHUC = ? "                
                + "WHERE ID = ?";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, km.getId());
            pstm.setString(2, km.getChiTietid());
            pstm.setString(3, km.getKhachHangid());
            pstm.setString(4, km.getTen());
            pstm.setString(5, km.getMoTa());
            pstm.setInt(6, km.getSoLuong());
            pstm.setDate(7, (Date) km.getThoiGianApDung());
            pstm.setDate(8, (Date) km.getThoiGianKetThuc());  
            pstm.setString(9, oldId);

            if (pstm.executeUpdate() > 0) {
                return 1;
            }
        }
        return 0;
    }

    public int editCTKM(ChiTietKhuyenMai ctkm, String oldId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE SANPHAM SET "
                + "ID = ?, "
                + "HINHTHUCGIAM = ?, "                
                + "SOTIENGIAMGIA = ?, "                
                + "SANPHAMID = ?, "
                + "MUCGIAMGIA = ?, "
                + "QUATANG = ?, "             
                + "WHERE ID = ?";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, ctkm.getId());
            pstm.setString(2, ctkm.getHinhThucGiam());
            pstm.setFloat(3, ctkm.getSoTienGiamGia());            
            pstm.setString(4, ctkm.getSanPhamid());
            pstm.setFloat(5, ctkm.getMucGiamGia());            
            pstm.setString(6, ctkm.getQuaTang());
            pstm.setString(7, oldId);

            if (pstm.executeUpdate() > 0) {
                return 1;
            }
        }
        return 0;
    }
    
    public int deleteKM(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM KHUYENMAI WHERE ID = ?";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, id);
            return pstm.executeUpdate();
        }
    }
    
    public int deleteCTKM(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM CHITIETKHUYENMAI WHERE ID = ?";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, id);
            return pstm.executeUpdate();
        }
    }    

    public Object[] getRow(KhuyenMai km) {
        return new Object[]{
            km.getId(),
            km.getChiTietid(),
            km.getKhachHangid(),
            km.getTen(),
            km.getMoTa(),
            km.getSoLuong(),
            km.getThoiGianApDung(),
            km.getThoiGianKetThuc()     
        };
    }
    
    public Object[] getRow(ChiTietKhuyenMai ctkm) {
        return new Object[]{
            ctkm.getId(),
            ctkm.getHinhThucGiam(),
            ctkm.getSoTienGiamGia(),
            ctkm.getSanPhamid(),
            ctkm.getMucGiamGia(),
            ctkm.getQuaTang(),     
        };
    }    
}
