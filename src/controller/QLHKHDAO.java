/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.HangKhachHang;

/**
 *
 * @author minhd
 */
public class QLHKHDAO extends BaseDAO<HangKhachHang> {
    @Override
    protected String getTableName() {
        return "HANGKHACHHANG";
    }

    @Override
    protected String getPrimaryKeyColumn() {
        return "ID";
    }

    @Override
    protected HangKhachHang mapResultSetToObject(ResultSet rs) throws SQLException {
        HangKhachHang hkh = new HangKhachHang();
        hkh.setId(rs.getString("ID"));
        hkh.setTen(rs.getString("TEN"));
        hkh.setMucGiamGia(rs.getFloat("MUCGIAMGIA"));
        return hkh;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO HANGKHACHHANG (ID, TEN, MUCGIAMGIA) VALUES (?, ?, ?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, HangKhachHang hkh) throws SQLException {
        ps.setString(1, hkh.getId());
        ps.setString(2, hkh.getTen());
        ps.setFloat(3, hkh.getMucGiamGia());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE HANGKHACHHANG SET "
                + "ID = ?, "
                + "TEN = ?, "
                + "MUCGIAMGIA = ? "
                + "WHERE ID = ?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, HangKhachHang hkh) throws SQLException {
        ps.setString(1, hkh.getId());
        ps.setString(2, hkh.getTen());
        ps.setFloat(3, hkh.getMucGiamGia());
    }

    @Override
    protected int getUpdateWhereIndex() {
        return 4;
    }

    public Object[] getRow(HangKhachHang hkh) {
        return new Object[]{
            hkh.getId(),
            hkh.getTen(),
            hkh.getMucGiamGia()
        };
    }

    // Thêm phương thức này để lấy danh sách hạng KH cho combobox
    public String[] getCustomerRankComboBoxItems() throws Exception {
        List<HangKhachHang> list = getAll();
        String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            HangKhachHang hkh = list.get(i);
            items[i] = hkh.getId() + " - " + hkh.getTen();
        }
        return items;
    }  
}
