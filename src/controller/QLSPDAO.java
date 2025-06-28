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
import model.SanPham;

/**
 *
 * @author minhd
 */
public class QLSPDAO {

    private final MyConnection conn;

    public QLSPDAO() {
        conn = new MyConnection();
    }

    public List<SanPham> getAll() throws SQLException, ClassNotFoundException {
        List<SanPham> lstSP = new ArrayList<>();
        String query = "SELECT * FROM SANPHAM"; 
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            SanPham sp = new SanPham(); 
            sp.setId(rs.getString("ID"));
            sp.setTen(rs.getString("TEN"));
            sp.setMoTa(rs.getString("MOTA"));
            sp.setGia(rs.getFloat("GIA"));
            sp.setLoaiSanPham(rs.getString("LOAISANPHAMID"));
            lstSP.add(sp);
        }
        return lstSP;
    }

    public int addSP(SanPham sp) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO SANPHAM (ID, TEN, MOTA, GIA, LOAISANPHAMID) VALUES (?, ?, ?, ?, ?)"; 
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, sp.getId());
            pstm.setString(2, sp.getTen());
            pstm.setString(3, sp.getMoTa());
            pstm.setFloat(4, sp.getGia());
            pstm.setString(5, sp.getLoaiSanPham());

            if (pstm.executeUpdate() > 0) {
                return 1;
            }
        }
        return 0;
    }

    public int editSP(SanPham sp, String oldId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE SANPHAM SET "
                + "ID = ?, "
                + "TEN = ?, "
                + "MOTA = ?, "
                + "GIA = ?, "
                + "LOAISANPHAMID = ? "
                + "WHERE ID = ?"; 
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, sp.getId());
            pstm.setString(2, sp.getTen());
            pstm.setString(3, sp.getMoTa());
            pstm.setFloat(4, sp.getGia());
            pstm.setString(5, sp.getLoaiSanPham());
            pstm.setString(6, oldId);

            if (pstm.executeUpdate() > 0) {
                return 1;
            }
        }
        return 0;
    }

    public int deleteSP(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM SANPHAM WHERE ID = ?"; 
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, id);
            return pstm.executeUpdate();
        }
    }

    public Object[] getRow(SanPham sp) { 
        return new Object[]{
            sp.getId(),
            sp.getTen(),
            sp.getMoTa(),
            sp.getGia(),
            sp.getLoaiSanPham()
        };
    }
}
