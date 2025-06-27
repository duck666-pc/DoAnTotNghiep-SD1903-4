/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.KhachHang;

/**
 *
 * @author minhd
 */
public class QLKHDAO {

    private final MyConnection conn;

    public QLKHDAO() {
        conn = new MyConnection();
    }

    public List<KhachHang> getAll() throws SQLException, ClassNotFoundException {
        List<KhachHang> lstKH = new ArrayList<>();
        String query = "SELECT * FROM KHACHHANG";
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            KhachHang kh = new KhachHang();
            kh.setId(rs.getString("ID"));
            kh.setTen(rs.getString("TEN"));
            kh.setDienThoai(rs.getString("DIENTHOAI"));
            kh.setDiaChi(rs.getString("DIACHI"));
            kh.sethangKhachHangId(rs.getString("HANGKHACHHANGID"));
            lstKH.add(kh);
        }
        return lstKH;
    }

    public int addKH(KhachHang kh) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO SANPHAM (ID, TEN, DIENTHOAI, DIACHI, HANGKHACHHANGID) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, kh.getId());
            pstm.setString(2, kh.getTen());
            pstm.setString(3, kh.getDienThoai());
            pstm.setString(4, kh.getDiaChi());
            pstm.setString(5, kh.gethangKhachHangId());

            if (pstm.executeUpdate() > 0) {
                return 1;
            }
        }
        return 0;
    }

    public int editKH(KhachHang kh, String oldId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE KHACHHANG SET "
                + "ID = ?, "
                + "TEN = ?, "
                + "DIENTHOAI = ?, "
                + "DIACHI = ?, "
                + "HANGKHACHHANGID = ? "
                + "WHERE ID = ?";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, kh.getId());
            pstm.setString(2, kh.getTen());
            pstm.setString(3, kh.getDienThoai());
            pstm.setString(4, kh.getDiaChi());
            pstm.setString(5, kh.gethangKhachHangId());
            pstm.setString(6, oldId);

            if (pstm.executeUpdate() > 0) {
                return 1;
            }
        }
        return 0;
    }

    public int deleteKH(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM KHACHHANG WHERE ID = ?";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, id);
            return pstm.executeUpdate();
        }
    }

    public Object[] getRow(KhachHang kh) {
        return new Object[]{
            kh.getId(),
            kh.getTen(),
            kh.getDienThoai(),
            kh.getDiaChi(),
            kh.gethangKhachHangId()
        };
    }
}
