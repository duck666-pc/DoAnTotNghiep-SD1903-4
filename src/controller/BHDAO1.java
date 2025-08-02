/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.*;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BHDAO1 {
    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(BHDAO1.class.getName());

    public BHDAO1() {
        try {
            // Kết nối database
            String url = "jdbc:sqlserver://localhost:1433;databaseName=DoAnTotNghiep;trustServerCertificate=true";
            String username = "sa";
            String password = "123456789";
            connection = DriverManager.getConnection(url, username, password);
            LOGGER.info("Database connection established successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed", e);
            JOptionPane.showMessageDialog(null, "Lỗi kết nối database: " + e.getMessage());
        }
    }

    // Kiểm tra kết nối và tự động kết nối lại nếu cần
    private boolean ensureConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String url = "jdbc:sqlserver://localhost:1433;databaseName=DoAnTotNghiep;trustServerCertificate=true";
                String username = "sa";
                String password = "123456789";
                connection = DriverManager.getConnection(url, username, password);
                LOGGER.info("Database connection re-established");
            }
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to ensure database connection", e);
            return false;
        }
    }

    // Lấy danh sách tất cả sản phẩm
    public List<SanPham> getAllSanPham() {
        List<SanPham> danhSach = new ArrayList<>();
        if (!ensureConnection()) {
            return danhSach;
        }

        String sql = "SELECT * FROM SanPham WHERE 1=1"; // Added WHERE clause for potential filtering

        try (PreparedStatement stmt = connection.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setId(rs.getString("ID"));
                sp.setTen(rs.getString("Ten"));
                sp.setMoTa(rs.getString("MoTa"));
                sp.setGia(rs.getFloat("Gia"));
                sp.setLoaiSanPham(rs.getString("LoaiSanPhamID"));
                danhSach.add(sp);
            }
            LOGGER.info("Retrieved " + danhSach.size() + " products");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving products", e);
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
        }

        return danhSach;
    }

    // Tìm khách hàng theo số điện thoại
    public KhachHang findKhachHangBySDT(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) {
            return null;
        }

        if (!ensureConnection()) {
            return null;
        }

        String sql = "SELECT * FROM KhachHang WHERE DienThoai = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sdt.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setId(rs.getString("ID"));
                kh.setTen(rs.getString("Ten"));
                kh.setDienThoai(rs.getString("DienThoai"));
                kh.setDiaChi(rs.getString("DiaChi"));
                kh.sethangKhachHangId(rs.getString("HangKhachHangID"));
                LOGGER.info("Found customer by phone: " + sdt);
                return kh;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding customer by phone", e);
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm khách hàng: " + e.getMessage());
        }

        return null;
    }

    // ADDED: Tìm khách hàng theo ID (method missing in original code)
    public KhachHang findKhachHangById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        if (!ensureConnection()) {
            return null;
        }

        String sql = "SELECT * FROM KhachHang WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setId(rs.getString("ID"));
                kh.setTen(rs.getString("Ten"));
                kh.setDienThoai(rs.getString("DienThoai"));
                kh.setDiaChi(rs.getString("DiaChi"));
                kh.sethangKhachHangId(rs.getString("HangKhachHangID"));
                LOGGER.info("Found customer by ID: " + id);
                return kh;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding customer by ID", e);
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm khách hàng theo ID: " + e.getMessage());
        }

        return null;
    }

    // Lấy khuyến mãi theo sản phẩm
    public List<ChiTietKhuyenMai> getKhuyenMaiBySanPham(String sanPhamId) {
        List<ChiTietKhuyenMai> danhSach = new ArrayList<>();
        if (sanPhamId == null || sanPhamId.trim().isEmpty()) {
            return danhSach;
        }

        if (!ensureConnection()) {
            return danhSach;
        }

        String sql = "SELECT ctkm.*, km.Ten as TenKM FROM ChiTietKhuyenMai ctkm "
                + "INNER JOIN KhuyenMai km ON ctkm.ID = km.ChiTietKhuyenMaiID "
                + "WHERE ctkm.SanPhamID = ? AND km.ThoiGianApDung <= GETDATE() "
                + "AND km.ThoiGianKetThuc >= GETDATE()";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sanPhamId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ChiTietKhuyenMai ctkm = new ChiTietKhuyenMai();
                ctkm.setId(rs.getString("ID"));
                ctkm.setHinhThucGiam(rs.getString("HinhThucGiam"));
                ctkm.setSoTienGiamGia(rs.getFloat("SoTienGiamGia"));
                ctkm.setSanPhamid(rs.getString("SanPhamID"));
                ctkm.setMucGiamGia(rs.getFloat("MucGiamGia"));
                ctkm.setQuaTang(rs.getString("QuaTang"));
                danhSach.add(ctkm);
            }
            LOGGER.info("Retrieved " + danhSach.size() + " promotions for product: " + sanPhamId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving promotions", e);
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy khuyến mãi: " + e.getMessage());
        }

        return danhSach;
    }

    // Tạo hóa đơn mới
    public String taoHoaDon(String khachHangId, String nguoiDungId, BigDecimal tongTienGoc,
            BigDecimal mucGiamGia, BigDecimal tongTienSauGiamGia, String trangThai) {

        if (khachHangId == null || nguoiDungId == null || tongTienGoc == null) {
            JOptionPane.showMessageDialog(null, "Thông tin hóa đơn không hợp lệ!");
            return null;
        }

        if (!ensureConnection()) {
            return null;
        }

        // Set default values for null parameters
        if (mucGiamGia == null) {
            mucGiamGia = BigDecimal.ZERO;
        }
        if (tongTienSauGiamGia == null) {
            tongTienSauGiamGia = tongTienGoc.subtract(mucGiamGia);
        }
        if (trangThai == null || trangThai.trim().isEmpty()) {
            trangThai = "Chưa Thanh Toán";
        }

        String hoaDonId = null;
        String insertSql = "INSERT INTO HoaDon (ID, ThoiGian, KhachHangID, NguoiDungID, TongTienGoc, "
                + "MucGiamGia, TongTienSauGiamGia, TrangThai) VALUES (?, GETDATE(), ?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false); // bắt đầu transaction

            String lockedMaxSql = "SELECT MAX(CAST(RIGHT(ID, 3) AS INT)) AS MaxSuffix "
                    + "FROM HoaDon WITH (UPDLOCK, HOLDLOCK) "
                    + "WHERE ID LIKE 'HD[0-9][0-9][0-9]'";

            int nextNumber = 1;
            try (PreparedStatement stmtMax = connection.prepareStatement(lockedMaxSql); 
                 ResultSet rs = stmtMax.executeQuery()) {
                if (rs.next()) {
                    int maxSuffix = rs.getInt("MaxSuffix");
                    if (!rs.wasNull()) {
                        nextNumber = maxSuffix + 1;
                    }
                }
            }

            // Handle overflow
            if (nextNumber > 999) {
                nextNumber = 1;
            }

            hoaDonId = String.format("HD%03d", nextNumber); // kiểu HD001, HD002, ...

            try (PreparedStatement stmtInsert = connection.prepareStatement(insertSql)) {
                stmtInsert.setString(1, hoaDonId);
                stmtInsert.setString(2, khachHangId);
                stmtInsert.setString(3, nguoiDungId);
                stmtInsert.setBigDecimal(4, tongTienGoc);
                stmtInsert.setBigDecimal(5, mucGiamGia);
                stmtInsert.setBigDecimal(6, tongTienSauGiamGia);
                stmtInsert.setString(7, trangThai);
                
                int rowsAffected = stmtInsert.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Creating invoice failed, no rows affected.");
                }
            }

            connection.commit();
            LOGGER.info("Created new invoice: " + hoaDonId);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Rollback failed", ex);
            }
            LOGGER.log(Level.SEVERE, "Error creating invoice", e);
            JOptionPane.showMessageDialog(null, "Lỗi khi tạo hóa đơn: " + e.getMessage());
            return null;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.log(Level.WARNING, "Failed to reset auto-commit", ex);
            }
        }

        return hoaDonId;
    }

    // Thêm chi tiết hóa đơn
    public boolean themChiTietHoaDon(String hoaDonId, String sanPhamId, int soLuong, double donGia) {
        if (hoaDonId == null || sanPhamId == null || soLuong <= 0 || donGia <= 0) {
            LOGGER.warning("Invalid parameters for adding invoice detail");
            return false;
        }

        if (!ensureConnection()) {
            return false;
        }

        String insertSql = "INSERT INTO ChiTietHoaDon (ID, SoSanPhamThanhToan, HoaDonID, SanPhamID, GiaBanMoiSanPham) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            String lockedMaxSuffixSql = "SELECT MAX(CAST(RIGHT(ID, 3) AS INT)) AS MaxSuffix "
                    + "FROM ChiTietHoaDon WITH (UPDLOCK, HOLDLOCK) "
                    + "WHERE ID LIKE 'CT[0-9][0-9][0-9]'";

            int nextNumber = 1;
            try (PreparedStatement stmtMax = connection.prepareStatement(lockedMaxSuffixSql); 
                 ResultSet rs = stmtMax.executeQuery()) {
                if (rs.next()) {
                    int maxSuffix = rs.getInt("MaxSuffix");
                    if (!rs.wasNull()) {
                        nextNumber = maxSuffix + 1;
                    }
                }
            }

            // Handle overflow
            if (nextNumber > 999) {
                nextNumber = 1;
            }

            String chiTietId = String.format("CT%03d", nextNumber); // CT001, CT002, ...

            try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
                stmt.setString(1, chiTietId);
                stmt.setInt(2, soLuong);
                stmt.setString(3, hoaDonId);
                stmt.setString(4, sanPhamId);
                stmt.setDouble(5, donGia);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Adding invoice detail failed, no rows affected.");
                }
            }

            connection.commit();
            LOGGER.info("Added invoice detail: " + chiTietId + " for invoice: " + hoaDonId);
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Rollback failed", ex);
            }
            LOGGER.log(Level.SEVERE, "Error adding invoice detail", e);
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm chi tiết hóa đơn: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.log(Level.WARNING, "Failed to reset auto-commit", ex);
            }
        }
    }

    // Cập nhật trạng thái hóa đơn
    public boolean capNhatTrangThaiHoaDon(String hoaDonId, String trangThai) {
        if (hoaDonId == null || trangThai == null) {
            LOGGER.warning("Invalid parameters for updating invoice status");
            return false;
        }

        if (!ensureConnection()) {
            return false;
        }

        String sql = "UPDATE HoaDon SET TrangThai = ? WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, trangThai);
            stmt.setString(2, hoaDonId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Updated invoice status: " + hoaDonId + " to " + trangThai);
                return true;
            } else {
                LOGGER.warning("No invoice found with ID: " + hoaDonId);
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating invoice status", e);
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật trạng thái hóa đơn: " + e.getMessage());
        }

        return false;
    }

    // Fixed: Lấy danh sách hóa đơn với sắp xếp đúng
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> danhSach = new ArrayList<>();
        if (!ensureConnection()) {
            return danhSach;
        }

        // Fixed: Order by numeric part of ID in descending order for newest first
        String sql = "SELECT hd.*, kh.Ten as TenKhachHang FROM HoaDon hd "
                + "LEFT JOIN KhachHang kh ON hd.KhachHangID = kh.ID "
                + "ORDER BY CAST(RIGHT(hd.ID, 3) AS INT) DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setId(rs.getString("ID"));
                hd.setThoiGian(rs.getTimestamp("ThoiGian"));
                hd.setIdKhachHang(rs.getString("KhachHangID"));
                hd.setIdNguoiDung(rs.getString("NguoiDungID"));
                hd.setTongTienGoc(rs.getBigDecimal("TongTienGoc"));
                hd.setMucGiamGia(rs.getBigDecimal("MucGiamGia"));
                hd.setTongTienSauGiamGia(rs.getBigDecimal("TongTienSauGiamGia"));
                hd.setTrangThai(rs.getString("TrangThai"));
                danhSach.add(hd);
            }
            LOGGER.info("Retrieved " + danhSach.size() + " invoices");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving invoices", e);
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách hóa đơn: " + e.getMessage());
        }

        return danhSach;
    }

    // Lấy chi tiết hóa đơn theo ID hóa đơn
    public List<ChiTietHoaDon> getChiTietHoaDon(String hoaDonId) {
        List<ChiTietHoaDon> danhSach = new ArrayList<>();
        if (hoaDonId == null || hoaDonId.trim().isEmpty()) {
            return danhSach;
        }

        if (!ensureConnection()) {
            return danhSach;
        }

        String sql = "SELECT ct.*, sp.Ten as TenSanPham FROM ChiTietHoaDon ct "
                + "INNER JOIN SanPham sp ON ct.SanPhamID = sp.ID "
                + "WHERE ct.HoaDonID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hoaDonId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setId(rs.getString("ID"));
                ct.setSoLuong(rs.getInt("SoSanPhamThanhToan"));
                ct.setIdHoaDon(rs.getString("HoaDonID"));
                ct.setIdSanPham(rs.getString("SanPhamID"));
                ct.setDonGia(rs.getDouble("GiaBanMoiSanPham"));
                danhSach.add(ct);
            }
            LOGGER.info("Retrieved " + danhSach.size() + " invoice details for invoice: " + hoaDonId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving invoice details", e);
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy chi tiết hóa đơn: " + e.getMessage());
        }

        return danhSach;
    }

    // Đếm số đơn chờ xử lý
    public int demSoDonChoXuLy() {
        if (!ensureConnection()) {
            return 0;
        }

        String sql = "SELECT COUNT(*) as SoDon FROM HoaDon WHERE TrangThai = N'Chưa Thanh Toán'";

        try (PreparedStatement stmt = connection.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int count = rs.getInt("SoDon");
                LOGGER.info("Pending invoices count: " + count);
                return count;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting pending invoices", e);
            JOptionPane.showMessageDialog(null, "Lỗi khi đếm số đơn chờ xử lý: " + e.getMessage());
        }

        return 0;
    }

    // Tìm hóa đơn theo ID
    public HoaDon findHoaDonById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        if (!ensureConnection()) {
            return null;
        }

        String sql = "SELECT * FROM HoaDon WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setId(rs.getString("ID"));
                hd.setThoiGian(rs.getTimestamp("ThoiGian"));
                hd.setIdKhachHang(rs.getString("KhachHangID"));
                hd.setIdNguoiDung(rs.getString("NguoiDungID"));
                hd.setTongTienGoc(rs.getBigDecimal("TongTienGoc"));
                hd.setMucGiamGia(rs.getBigDecimal("MucGiamGia"));
                hd.setTongTienSauGiamGia(rs.getBigDecimal("TongTienSauGiamGia"));
                hd.setTrangThai(rs.getString("TrangThai"));
                LOGGER.info("Found invoice by ID: " + id);
                return hd;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding invoice by ID", e);
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm hóa đơn: " + e.getMessage());
        }

        return null;
    }

    // Fixed: Lấy danh sách hóa đơn theo trạng thái với sắp xếp đúng
    public List<HoaDon> getHoaDonByTrangThai(String trangThai) {
        List<HoaDon> danhSach = new ArrayList<>();
        if (!ensureConnection()) {
            return danhSach;
        }

        String sql;
        boolean hasWhereClause = false;

        if ("Tất Cả".equals(trangThai)) {
            sql = "SELECT hd.*, kh.Ten as TenKhachHang FROM HoaDon hd "
                    + "LEFT JOIN KhachHang kh ON hd.KhachHangID = kh.ID "
                    + "ORDER BY CAST(RIGHT(hd.ID, 3) AS INT) DESC";
        } else {
            sql = "SELECT hd.*, kh.Ten as TenKhachHang FROM HoaDon hd "
                    + "LEFT JOIN KhachHang kh ON hd.KhachHangID = kh.ID "
                    + "WHERE hd.TrangThai = ? ORDER BY CAST(RIGHT(hd.ID, 3) AS INT) DESC";
            hasWhereClause = true;
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (hasWhereClause) {
                stmt.setString(1, trangThai);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setId(rs.getString("ID"));
                hd.setThoiGian(rs.getTimestamp("ThoiGian"));
                hd.setIdKhachHang(rs.getString("KhachHangID"));
                hd.setIdNguoiDung(rs.getString("NguoiDungID"));
                hd.setTongTienGoc(rs.getBigDecimal("TongTienGoc"));
                hd.setMucGiamGia(rs.getBigDecimal("MucGiamGia"));
                hd.setTongTienSauGiamGia(rs.getBigDecimal("TongTienSauGiamGia"));
                hd.setTrangThai(rs.getString("TrangThai"));
                danhSach.add(hd);
            }
            LOGGER.info("Retrieved " + danhSach.size() + " invoices with status: " + trangThai);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving invoices by status", e);
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy hóa đơn theo trạng thái: " + e.getMessage());
        }

        return danhSach;
    }

    // ADDED: Method to validate if a customer exists
    public boolean customerExists(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            return false;
        }

        if (!ensureConnection()) {
            return false;
        }

        String sql = "SELECT COUNT(*) as Count FROM KhachHang WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Count") > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking customer existence", e);
        }

        return false;
    }

    // ADDED: Method to validate if a product exists
    public boolean productExists(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            return false;
        }

        if (!ensureConnection()) {
            return false;
        }

        String sql = "SELECT COUNT(*) as Count FROM SanPham WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Count") > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking product existence", e);
        }

        return false;
    }

    // ADDED: Method to get product by ID
    public SanPham findSanPhamById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        if (!ensureConnection()) {
            return null;
        }

        String sql = "SELECT * FROM SanPham WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                SanPham sp = new SanPham();
                sp.setId(rs.getString("ID"));
                sp.setTen(rs.getString("Ten"));
                sp.setMoTa(rs.getString("MoTa"));
                sp.setGia(rs.getFloat("Gia"));
                sp.setLoaiSanPham(rs.getString("LoaiSanPhamID"));
                LOGGER.info("Found product by ID: " + id);
                return sp;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding product by ID", e);
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm sản phẩm theo ID: " + e.getMessage());
        }

        return null;
    }

    // IMPROVED: Method to check database connection status
    public boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Connection validation failed", e);
            return false;
        }
    }

    // Đóng kết nối
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOGGER.info("Database connection closed");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing database connection", e);
        }
    }

    // ADDED: Finalize method to ensure connection is closed
    @Override
    protected void finalize() throws Throwable {
        try {
            closeConnection();
        } finally {
            super.finalize();
        }
    }
}
