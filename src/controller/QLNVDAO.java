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
import model.NhanVien;

/**
 *
 * @author minhd
 */
public class QLNVDAO {

    private final MyConnection conn;

    public QLNVDAO() {
        conn = new MyConnection();
    }

    public List<NhanVien> getAll() throws SQLException, ClassNotFoundException {
        List<NhanVien> lstNV = new ArrayList<>();
        String query = "SELECT * FROM NGUOIDUNG";
        Connection connect = conn.DBConnect();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            NhanVien nv = new NhanVien();
            nv.setId(rs.getString(1));
            nv.setMatKhau(rs.getString(2));
            nv.setTenDayDu(rs.getString(3));
            nv.setNgaySinh(Date.valueOf(rs.getString(4)));
            nv.setGioiTinh(rs.getString(5));
            nv.setEmail(rs.getString(6));
            nv.setChucVu(rs.getString(7));
            lstNV.add(nv);
        }
        return lstNV;
    }

    public int addNV(NhanVien nv) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO NGUOIDUNG (ID, MATKHAU, TENDAYDU, NGAYSINH, GIOITINH, EMAIL) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, nv.getId());
            pstm.setString(2, nv.getMatKhau());
            pstm.setString(3, nv.getTenDayDu());
            pstm.setDate(4, new java.sql.Date(nv.getNgaySinh().getTime()));
            pstm.setString(5, nv.getGioiTinh());
            pstm.setString(6, nv.getEmail());
            pstm.setString(7, nv.getChucVu());

            if (pstm.executeUpdate() > 0) {
                return 1;
            }
        }
        return 0;
    }

    public int editNV(NhanVien nv, String oldId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE NGUOIDUNG SET "
                + "ID = ?, "
                + "MATKHAU = ?, "
                + "TENDAYDU = ?, "
                + "NGAYSINH = ?, "
                + "GIOITINH = ?, "
                + "EMAIL = ? "
                + "CHUCVU = ? "                
                + "WHERE ID = ?";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, nv.getId());
            pstm.setString(2, nv.getMatKhau());
            pstm.setString(3, nv.getTenDayDu());
            pstm.setDate(4, new java.sql.Date(nv.getNgaySinh().getTime()));
            pstm.setString(5, nv.getGioiTinh());
            pstm.setString(6, nv.getEmail());
            pstm.setString(7, nv.getChucVu());
            pstm.setString(8, oldId);

            if (pstm.executeUpdate() > 0) {
                return 1;
            }
        }
        return 0;
    }

    public int deleteNV(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM NGUOIDUNG WHERE ID = ?";
        try (Connection con = conn.DBConnect(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, id);
            return pstm.executeUpdate();
        }
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
