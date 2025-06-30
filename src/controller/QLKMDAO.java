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

/**
 *
 * @author minhd
 */
public class QLKMDAO {

    private final MyConnection conn;

    public QLKMDAO() {
        conn = new MyConnection();
    }

    public List<KhuyenMai> getAll() throws SQLException, ClassNotFoundException {
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

    public int addSP(KhuyenMai km) throws SQLException, ClassNotFoundException {
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

    public int editSP(KhuyenMai km, String oldId) throws SQLException, ClassNotFoundException {
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

    public int deleteSP(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM KHUYENMAI WHERE ID = ?";
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
}
