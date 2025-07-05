/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.KhachHang;

/**
 *
 * @author minhd
 */
public class QLKHDAO extends BaseDAO<KhachHang> {

    @Override
    protected String getTableName() {
        return "KHACHHANG";
    }

    @Override
    protected String getPrimaryKeyColumn() {
        return "ID";
    }

    @Override
    protected KhachHang mapResultSetToObject(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setId(rs.getString("ID"));
        kh.setTen(rs.getString("TEN"));
        kh.setDienThoai(rs.getString("DIENTHOAI"));
        kh.setDiaChi(rs.getString("DIACHI"));
        kh.sethangKhachHangId(rs.getString("HANGKHACHHANGID"));
        return kh;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO KHACHHANG (ID, TEN, DIENTHOAI, DIACHI, HANGKHACHHANGID) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, KhachHang kh) throws SQLException {
        ps.setString(1, kh.getId());
        ps.setString(2, kh.getTen());
        ps.setString(3, kh.getDienThoai());
        ps.setString(4, kh.getDiaChi());
        ps.setString(5, kh.gethangKhachHangId());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE KHACHHANG SET "
             + "ID = ?, "
             + "TEN = ?, "
             + "DIENTHOAI = ?, "
             + "DIACHI = ?, "
             + "HANGKHACHHANGID = ? "
             + "WHERE ID = ?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, KhachHang kh) throws SQLException {
        ps.setString(1, kh.getId());
        ps.setString(2, kh.getTen());
        ps.setString(3, kh.getDienThoai());
        ps.setString(4, kh.getDiaChi());
        ps.setString(5, kh.gethangKhachHangId());
    }

    @Override
    protected int getUpdateWhereIndex() {
        return 6; 
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