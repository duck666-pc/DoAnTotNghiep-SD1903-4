/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.SanPham;

public class ChonSPKMDAO {

    // Database connection parameters
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=DoAnTotNghiep;encrypt=true;trustServerCertificate=true";
    private static final String USERNAME = "sa"; // Change this to your SQL Server username
    private static final String PASSWORD = "123456789"; // Change this to your SQL Server password

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * Get all products from the database
     *
     * @return List of SanPham objects
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<SanPham> getAllSP() throws SQLException, ClassNotFoundException {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setId(rs.getString("ID"));
                sp.setTen(rs.getString("Ten"));
                sp.setMoTa(rs.getString("MoTa"));
                sp.setGia(rs.getFloat("Gia"));
                sp.setLoaiSanPham(rs.getString("LoaiSanPhamID"));
                list.add(sp);
            }
        }

        return list;
    }

    /**
     * Get a specific product by ID
     *
     * @param id Product ID
     * @return SanPham object or null if not found
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public SanPham getSPById(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM SanPham WHERE ID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setId(rs.getString("ID"));
                    sp.setTen(rs.getString("Ten"));
                    sp.setMoTa(rs.getString("MoTa"));
                    sp.setGia(rs.getFloat("Gia"));
                    sp.setLoaiSanPham(rs.getString("LoaiSanPhamID"));
                    return sp;
                }
            }
        }

        return null;
    }

    /**
     * Search products by keyword (searches in name, description, and ID)
     *
     * @param keyword Search keyword
     * @return List of matching SanPham objects
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<SanPham> searchSP(String keyword) throws SQLException, ClassNotFoundException {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE Ten LIKE ? OR MoTa LIKE ? OR ID LIKE ? OR LoaiSanPhamID LIKE ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setId(rs.getString("ID"));
                    sp.setTen(rs.getString("Ten"));
                    sp.setMoTa(rs.getString("MoTa"));
                    sp.setGia(rs.getFloat("Gia"));
                    sp.setLoaiSanPham(rs.getString("LoaiSanPhamID"));
                    list.add(sp);
                }
            }
        }

        return list;
    }

    /**
     * Get products by category
     *
     * @param categoryId Category ID
     * @return List of SanPham objects in the specified category
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<SanPham> getSPByCategory(String categoryId) throws SQLException, ClassNotFoundException {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE LoaiSanPhamID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setId(rs.getString("ID"));
                    sp.setTen(rs.getString("Ten"));
                    sp.setMoTa(rs.getString("MoTa"));
                    sp.setGia(rs.getFloat("Gia"));
                    sp.setLoaiSanPham(rs.getString("LoaiSanPhamID"));
                    list.add(sp);
                }
            }
        }

        return list;
    }

    /**
     * Get products within a price range
     *
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of SanPham objects within the price range
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<SanPham> getSPByPriceRange(float minPrice, float maxPrice) throws SQLException, ClassNotFoundException {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE Gia >= ? AND Gia <= ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setFloat(1, minPrice);
            ps.setFloat(2, maxPrice);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setId(rs.getString("ID"));
                    sp.setTen(rs.getString("Ten"));
                    sp.setMoTa(rs.getString("MoTa"));
                    sp.setGia(rs.getFloat("Gia"));
                    sp.setLoaiSanPham(rs.getString("LoaiSanPhamID"));
                    list.add(sp);
                }
            }
        }

        return list;
    }

    /**
     * Check if a product exists by ID
     *
     * @param id Product ID
     * @return true if product exists, false otherwise
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean productExists(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM SanPham WHERE ID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    /**
     * Get the total count of products
     *
     * @return Total number of products
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int getTotalProductCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM SanPham";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    /**
     * Convert SanPham object to table row data
     *
     * @param sp SanPham object
     * @return Object array for table display
     */
    public Object[] getRow(SanPham sp) {
        return new Object[]{
            false, // Checkbox column
            sp.getId(),
            sp.getTen(),
            sp.getMoTa(),
            sp.getGia(),
            sp.getLoaiSanPham()
        };
    }

    /**
     * Test database connection
     *
     * @return true if connection successful, false otherwise
     */
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
