/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.KhuyenMai;
import model.ChiTietKhuyenMai;

/**
 *
 * @author minhd
 */
public class QLKMDAO {

    // Database connection parameters
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=DoAnTotNghiep;encrypt=true;trustServerCertificate=true";
    private static final String USERNAME = "sa"; // Change this to your SQL Server username
    private static final String PASSWORD = "123456789"; // Change this to your SQL Server password

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // KhuyenMai methods
    public List<KhuyenMai> getAllKM() throws SQLException, ClassNotFoundException {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setId(rs.getString("ID"));
                km.setChiTietid(rs.getString("ChiTietKhuyenMaiID"));
                km.setTen(rs.getString("Ten"));
                km.setMoTa(rs.getString("MoTa"));
                km.setSoLuong(rs.getInt("SoLuong"));
                km.setThoiGianApDung(rs.getDate("ThoiGianApDung"));
                km.setThoiGianKetThuc(rs.getDate("ThoiGianKetThuc"));
                list.add(km);
            }
        }

        return list;
    }

    public int addKM(KhuyenMai km) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO KhuyenMai (ID, ChiTietKhuyenMaiID, Ten, MoTa, SoLuong, ThoiGianApDung, ThoiGianKetThuc) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getId());
            ps.setString(2, km.getChiTietid());
            ps.setString(3, km.getTen());
            ps.setString(4, km.getMoTa());
            ps.setInt(5, km.getSoLuong());
            ps.setDate(6, new Date(km.getThoiGianApDung().getTime()));
            ps.setDate(7, new Date(km.getThoiGianKetThuc().getTime()));

            return ps.executeUpdate();
        }
    }

    public int editKM(KhuyenMai km, String oldId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE KhuyenMai SET ChiTietKhuyenMaiID = ?, Ten = ?, MoTa = ?, SoLuong = ?, ThoiGianApDung = ?, ThoiGianKetThuc = ? WHERE ID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getChiTietid());
            ps.setString(2, km.getTen());
            ps.setString(3, km.getMoTa());
            ps.setInt(4, km.getSoLuong());
            ps.setDate(5, new Date(km.getThoiGianApDung().getTime()));
            ps.setDate(6, new Date(km.getThoiGianKetThuc().getTime()));
            ps.setString(7, oldId);

            return ps.executeUpdate();
        }
    }

    public int deleteKM(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM KhuyenMai WHERE ID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate();
        }
    }

    public KhuyenMai getKMById(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM KhuyenMai WHERE ID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KhuyenMai km = new KhuyenMai();
                    km.setId(rs.getString("ID"));
                    km.setChiTietid(rs.getString("ChiTietKhuyenMaiID"));
                    km.setTen(rs.getString("Ten"));
                    km.setMoTa(rs.getString("MoTa"));
                    km.setSoLuong(rs.getInt("SoLuong"));
                    km.setThoiGianApDung(rs.getDate("ThoiGianApDung"));
                    km.setThoiGianKetThuc(rs.getDate("ThoiGianKetThuc"));
                    return km;
                }
            }
        }

        return null;
    }

    // ChiTietKhuyenMai methods
    public List<ChiTietKhuyenMai> getAllCTKM() throws SQLException, ClassNotFoundException {
        List<ChiTietKhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietKhuyenMai";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ChiTietKhuyenMai ctkm = new ChiTietKhuyenMai();
                ctkm.setId(rs.getString("ID"));
                ctkm.setHinhThucGiam(rs.getString("HinhThucGiam"));
                ctkm.setSoTienGiamGia(rs.getFloat("SoTienGiamGia"));
                ctkm.setSanPhamid(rs.getString("SanPhamID"));
                ctkm.setMucGiamGia(rs.getFloat("MucGiamGia"));
                ctkm.setQuaTang(rs.getString("QuaTang"));
                list.add(ctkm);
            }
        }

        return list;
    }

    public int addCTKM(ChiTietKhuyenMai ctkm) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO ChiTietKhuyenMai (ID, HinhThucGiam, SoTienGiamGia, SanPhamID, MucGiamGia, QuaTang) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ctkm.getId());
            ps.setString(2, ctkm.getHinhThucGiam());
            ps.setFloat(3, ctkm.getSoTienGiamGia());
            ps.setString(4, ctkm.getSanPhamid());
            ps.setFloat(5, ctkm.getMucGiamGia());
            ps.setString(6, ctkm.getQuaTang());

            return ps.executeUpdate();
        }
    }

    public int editCTKM(ChiTietKhuyenMai ctkm, String oldId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE ChiTietKhuyenMai SET HinhThucGiam = ?, SoTienGiamGia = ?, SanPhamID = ?, MucGiamGia = ?, QuaTang = ? WHERE ID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ctkm.getHinhThucGiam());
            ps.setFloat(2, ctkm.getSoTienGiamGia());
            ps.setString(3, ctkm.getSanPhamid());
            ps.setFloat(4, ctkm.getMucGiamGia());
            ps.setString(5, ctkm.getQuaTang());
            ps.setString(6, oldId);

            return ps.executeUpdate();
        }
    }

    public int deleteCTKM(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ChiTietKhuyenMai WHERE ID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate();
        }
    }

    public ChiTietKhuyenMai getCTKMById(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM ChiTietKhuyenMai WHERE ID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ChiTietKhuyenMai ctkm = new ChiTietKhuyenMai();
                    ctkm.setId(rs.getString("ID"));
                    ctkm.setHinhThucGiam(rs.getString("HinhThucGiam"));
                    ctkm.setSoTienGiamGia(rs.getFloat("SoTienGiamGia"));
                    ctkm.setSanPhamid(rs.getString("SanPhamID"));
                    ctkm.setMucGiamGia(rs.getFloat("MucGiamGia"));
                    ctkm.setQuaTang(rs.getString("QuaTang"));
                    return ctkm;
                }
            }
        }

        return null;
    }

    // Helper methods for table display
    public Object[] getRow(KhuyenMai km) {
        return new Object[]{
            km.getId(),
            km.getTen(),
            km.getMoTa(),
            km.getSoLuong(),
            km.getThoiGianApDung(),
            km.getThoiGianKetThuc()
        };
    }

    public Object[] getRow(ChiTietKhuyenMai ctkm) {
        return new Object[]{
            ctkm.getId(),
            ctkm.getHinhThucGiam(),
            ctkm.getSoTienGiamGia(),
            ctkm.getSanPhamid(),
            ctkm.getMucGiamGia(),
            ctkm.getQuaTang()
        };
    }

    public ChiTietKhuyenMai getCTKMByKMId(String khuyenMaiId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT ctkm.* FROM ChiTietKhuyenMai ctkm "
                + "INNER JOIN KhuyenMai km ON ctkm.ID = km.ChiTietKhuyenMaiID "
                + "WHERE km.ID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, khuyenMaiId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ChiTietKhuyenMai ctkm = new ChiTietKhuyenMai();
                    ctkm.setId(rs.getString("ID"));
                    ctkm.setHinhThucGiam(rs.getString("HinhThucGiam"));
                    ctkm.setSoTienGiamGia(rs.getFloat("SoTienGiamGia"));
                    ctkm.setSanPhamid(rs.getString("SanPhamID"));
                    ctkm.setMucGiamGia(rs.getFloat("MucGiamGia"));
                    ctkm.setQuaTang(rs.getString("QuaTang"));
                    return ctkm;
                }
            }
        }

        return null;
    }

    // Test connection method
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                conn.close();
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return false;
    }
}
