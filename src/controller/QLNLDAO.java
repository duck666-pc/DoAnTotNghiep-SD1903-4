/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.NguyenVatLieu;

/**
 *
 * @author minhd
 */
public class QLNLDAO extends BaseDAO<NguyenVatLieu> {

    @Override
    protected String getTableName() {
        return "NGUYENVATLIEU";
    }

    @Override
    protected String getPrimaryKeyColumn() {
        return "ID";
    }

    @Override
    protected NguyenVatLieu mapResultSetToObject(ResultSet rs) throws SQLException {
        NguyenVatLieu nl = new NguyenVatLieu();
        nl.setId(rs.getString("ID"));
        nl.setTen(rs.getString("TEN"));
        nl.setDonVi(rs.getString("DONVI"));
        nl.setSoLuong(rs.getInt("SOLUONGCOSAN"));
        nl.setMucCanDatThem(rs.getInt("MUCCANDATTHEM"));
        return nl;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO NGUYENVATLIEU (ID, TEN, DONVI, SOLUONGCOSAN, MUCCANDATTHEM) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, NguyenVatLieu nl) throws SQLException {
        ps.setString(1, nl.getId());
        ps.setString(2, nl.getTen());
        ps.setString(3, nl.getDonVi());
        ps.setInt(4, nl.getSoLuong());
        ps.setInt(5, nl.getMucCanDatThem());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE NGUYENVATLIEU SET "
             + "ID = ?, "
             + "TEN = ?, "
             + "DONVI = ?, "
             + "SOLUONGCOSAN = ?, "
             + "MUCCANDATTHEM = ? "
             + "WHERE ID = ?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, NguyenVatLieu nl) throws SQLException {
        ps.setString(1, nl.getId());
        ps.setString(2, nl.getTen());
        ps.setString(3, nl.getDonVi());
        ps.setInt(4, nl.getSoLuong());
        ps.setInt(5, nl.getMucCanDatThem());
    }

    @Override
    protected int getUpdateWhereIndex() {
        return 6; 
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
