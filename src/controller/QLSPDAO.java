/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.SanPham;

/**
 *
 * @author minhd
 */
public class QLSPDAO extends BaseDAO<SanPham> {

    @Override
    protected String getTableName() {
        return "SANPHAM";
    }

    @Override
    protected String getPrimaryKeyColumn() {
        return "ID";
    }

    @Override
    protected SanPham mapResultSetToObject(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham();
        sp.setId(rs.getString("ID"));
        sp.setTen(rs.getString("TEN"));
        sp.setMoTa(rs.getString("MOTA"));
        sp.setGia(rs.getFloat("GIA"));
        sp.setLoaiSanPham(rs.getString("LOAISANPHAMID"));
        return sp;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO SANPHAM (ID, TEN, MOTA, GIA, LOAISANPHAMID) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, SanPham sp) throws SQLException {
        ps.setString(1, sp.getId());
        ps.setString(2, sp.getTen());
        ps.setString(3, sp.getMoTa());
        ps.setFloat(4, sp.getGia());
        ps.setString(5, sp.getLoaiSanPham());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE SANPHAM SET "
             + "ID = ?, "
             + "TEN = ?, "
             + "MOTA = ?, "
             + "GIA = ?, "
             + "LOAISANPHAMID = ? "
             + "WHERE ID = ?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, SanPham sp) throws SQLException {
        ps.setString(1, sp.getId());
        ps.setString(2, sp.getTen());
        ps.setString(3, sp.getMoTa());
        ps.setFloat(4, sp.getGia());
        ps.setString(5, sp.getLoaiSanPham());
    }

    @Override
    protected int getUpdateWhereIndex() {
        return 6; 
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
