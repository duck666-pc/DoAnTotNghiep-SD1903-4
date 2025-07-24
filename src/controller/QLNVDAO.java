/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.NhanVien;

/**
 *
 * @author minhd
 */
public class QLNVDAO extends BaseDAO<NhanVien> {

    @Override
    protected String getTableName() {
        return "NGUOIDUNG";
    }

    @Override
    protected String getPrimaryKeyColumn() {
        return "ID";
    }

    @Override
    protected NhanVien mapResultSetToObject(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();
        nv.setId(rs.getString("ID"));
        nv.setMatKhau(rs.getString("MATKHAU"));
        nv.setTenDayDu(rs.getString("TENDAYDU"));
        nv.setNgaySinh(rs.getDate("NGAYSINH"));
        nv.setGioiTinh(rs.getString("GIOITINH"));
        nv.setEmail(rs.getString("EMAIL"));
        nv.setChucVu(rs.getString("CHUCVU"));
        return nv;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO NGUOIDUNG (ID, MATKHAU, TENDAYDU, NGAYSINH, GIOITINH, EMAIL, CHUCVU) "
             + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, NhanVien nv) throws SQLException {
        ps.setString(1, nv.getId());
        ps.setString(2, nv.getMatKhau());
        ps.setString(3, nv.getTenDayDu());
        ps.setDate(4, new java.sql.Date(nv.getNgaySinh().getTime()));
        ps.setString(5, nv.getGioiTinh());
        ps.setString(6, nv.getEmail());
        ps.setString(7, nv.getChucVu());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE NGUOIDUNG SET "
             + "ID = ?, "
             + "MATKHAU = ?, "
             + "TENDAYDU = ?, "
             + "NGAYSINH = ?, "
             + "GIOITINH = ?, "
             + "EMAIL = ?, "
             + "CHUCVU = ? "
             + "WHERE ID = ?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, NhanVien nv) throws SQLException {
        ps.setString(1, nv.getId());
        ps.setString(2, nv.getMatKhau());
        ps.setString(3, nv.getTenDayDu());
        ps.setDate(4, new java.sql.Date(nv.getNgaySinh().getTime()));
        ps.setString(5, nv.getGioiTinh());
        ps.setString(6, nv.getEmail());
        ps.setString(7, nv.getChucVu());
    }

    @Override
    protected int getUpdateWhereIndex() {
        return 8; 
    }

    public Object[] getRow(NhanVien nv) {
        return new Object[]{
            nv.getId(),
            nv.getMatKhau(),
            nv.getTenDayDu(),
            nv.getNgaySinh(),
            nv.getGioiTinh(),
            nv.getEmail(),
            nv.getChucVu()
        };
    }
}
