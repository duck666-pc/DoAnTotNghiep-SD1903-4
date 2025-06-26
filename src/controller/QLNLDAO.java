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
import model.NguyenVatLieu;

/**
 *
 * @author minhd
 */
public class QLNLDAO {

    private final MyConnection conn;

    public QLNLDAO() {
        conn = new MyConnection();
    }

    public List<NguyenVatLieu> getAll() throws SQLException, ClassNotFoundException {
        List<NguyenVatLieu> lstNL = new ArrayList<>();
        String query = "SELECT * FROM NGUYENVATLIEU";
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            NguyenVatLieu nl = new NguyenVatLieu();
            nl.setId(rs.getString("ID"));
            nl.setTen(rs.getString("TEN"));
            nl.setDonVi(rs.getString("DONVI"));
            nl.setSoLuong(rs.getInt("SOLUONGCOSAN"));
            nl.setMucCanDatThem(rs.getInt("MUCCANDATTHEM"));
            lstNL.add(nl);
        }
        return lstNL;
    }

    public int addSP(NguyenVatLieu nl) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO SANPHAM (ID, TEN, DONVI, SOLUONGCOSAN, MUCCANDATTHEM) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, nl.getId());
            pstm.setString(2, nl.getTen());
            pstm.setString(3, nl.getDonVi());
            pstm.setInt(4, nl.getSoLuong());
            pstm.setInt(5, nl.getMucCanDatThem());

            if (pstm.executeUpdate() > 0) {
                return 1;
            }
        }
        return 0;
    }

    public int editSP(NguyenVatLieu nl, String oldId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE SANPHAM SET "
                + "ID = ?, "
                + "TEN = ?, "
                + "DONVI = ?, "
                + "SOLUONGCOSAN = ?, "
                + "MUCCANDATTHEM = ? "
                + "WHERE ID = ?";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, nl.getId());
            pstm.setString(2, nl.getTen());
            pstm.setString(3, nl.getDonVi());
            pstm.setInt(4, nl.getSoLuong());
            pstm.setInt(5, nl.getMucCanDatThem());
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

    public Object[] getRow(NguyenVatLieu nl) {
        return new Object[]{
            nl.getId(),
            nl.getTen(),
            nl.getDonVi(),
            nl.getSoLuong(),
            nl.getMucCanDatThem()
        };
    }
}
