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
import java.util.logging.Level;
import java.util.logging.Logger;

public class BHDAO1 {

    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(BHDAO1.class.getName());

    public BHDAO1() {
        connectDB();
    }

    private void connectDB() {
        try {
            MyConnection myConn = new MyConnection();
            connection = myConn.DBConnect();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database connection failed", e);
        }
    }

    private boolean ensureConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connectDB();
            }
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to ensure database connection", e);
            return false;
        }
    }

    // Lấy danh sách tất cả sản phẩm
    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        if (!ensureConnection()) {
            return list;
        }
        String sql = "SELECT * FROM SanPham";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setId(rs.getString("ID"));
                sp.setTen(rs.getString("Ten"));
                sp.setMoTa(rs.getString("MoTa"));
                sp.setGia(rs.getFloat("Gia"));
                sp.setLoaiSanPham(rs.getString("LoaiSanPhamID"));
                list.add(sp);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving products", e);
        }
        return list;
    }

    // Tìm khách hàng theo SDT hoặc ID
    private KhachHang findKhachHang(String sql, String param) {
        if (param == null || param.trim().isEmpty() || !ensureConnection()) {
            return null;
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, param.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setId(rs.getString("ID"));
                    kh.setTen(rs.getString("Ten"));
                    kh.setDienThoai(rs.getString("DienThoai"));
                    kh.setDiaChi(rs.getString("DiaChi"));
                    kh.sethangKhachHangId(rs.getString("HangKhachHangID"));
                    return kh;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding customer", e);
        }
        return null;
    }

    public KhachHang findKhachHangBySDT(String sdt) {
        return findKhachHang("SELECT * FROM KhachHang WHERE DienThoai = ?", sdt);
    }

    public KhachHang findKhachHangById(String id) {
        return findKhachHang("SELECT * FROM KhachHang WHERE ID = ?", id);
    }

    // Lấy khuyến mãi theo sản phẩm
    public List<ChiTietKhuyenMai> getKhuyenMaiBySanPham(String sanPhamId) {
        List<ChiTietKhuyenMai> list = new ArrayList<>();
        if (sanPhamId == null || sanPhamId.trim().isEmpty() || !ensureConnection()) {
            return list;
        }
        String sql = "SELECT ctkm.*, km.Ten as TenKM FROM ChiTietKhuyenMai ctkm "
                + "INNER JOIN KhuyenMai km ON ctkm.ID = km.ChiTietKhuyenMaiID "
                + "WHERE ctkm.SanPhamID = ? AND km.ThoiGianApDung <= GETDATE() "
                + "AND km.ThoiGianKetThuc >= GETDATE()";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sanPhamId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietKhuyenMai km = new ChiTietKhuyenMai();
                    km.setId(rs.getString("ID"));
                    km.setHinhThucGiam(rs.getString("HinhThucGiam"));
                    km.setSoTienGiamGia(rs.getFloat("SoTienGiamGia"));
                    km.setSanPhamid(rs.getString("SanPhamID"));
                    km.setMucGiamGia(rs.getFloat("MucGiamGia"));
                    km.setQuaTang(rs.getString("QuaTang"));
                    list.add(km);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving promotions", e);
        }
        return list;
    }

    // Tạo hóa đơn mới
    public String taoHoaDon(String khachHangId, String nguoiDungId, BigDecimal tongTienGoc,
            BigDecimal mucGiamGia, BigDecimal tongTienSauGiamGia, String trangThai) {
        if (khachHangId == null || nguoiDungId == null || tongTienGoc == null || !ensureConnection()) {
            return null;
        }
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
            connection.setAutoCommit(false);
            int nextNumber = 1;
            String lockedMaxSql = "SELECT MAX(CAST(RIGHT(ID, 3) AS INT)) AS MaxSuffix FROM HoaDon WHERE ID LIKE 'HD%'";
            try (PreparedStatement stmtMax = connection.prepareStatement(lockedMaxSql); ResultSet rs = stmtMax.executeQuery()) {
                if (rs.next()) {
                    nextNumber = rs.getInt(1) + 1;
                }
            }
            if (nextNumber > 999) {
                nextNumber = 1;
            }
            hoaDonId = String.format("HD%03d", nextNumber);
            try (PreparedStatement stmtInsert = connection.prepareStatement(insertSql)) {
                stmtInsert.setString(1, hoaDonId);
                stmtInsert.setString(2, khachHangId);
                stmtInsert.setString(3, nguoiDungId);
                stmtInsert.setBigDecimal(4, tongTienGoc);
                stmtInsert.setBigDecimal(5, mucGiamGia);
                stmtInsert.setBigDecimal(6, tongTienSauGiamGia);
                stmtInsert.setString(7, trangThai);
                stmtInsert.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
            }
            LOGGER.log(Level.SEVERE, "Error creating invoice", e);
            return null;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
            }
        }
        return hoaDonId;
    }

    // Thêm chi tiết hóa đơn
    public boolean themChiTietHoaDon(String hoaDonId, String sanPhamId, int soLuong, double donGia) {
        if (hoaDonId == null || sanPhamId == null || soLuong <= 0 || donGia <= 0 || !ensureConnection()) {
            return false;
        }
        String sql = "INSERT INTO ChiTietHoaDon (ID, SoSanPhamThanhToan, HoaDonID, SanPhamID, GiaBanMoiSanPham) VALUES (?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            int nextNumber = 1;
            String maxSql = "SELECT MAX(CAST(RIGHT(ID, 3) AS INT)) FROM ChiTietHoaDon WHERE ID LIKE 'CT%'";
            try (PreparedStatement stmtMax = connection.prepareStatement(maxSql); ResultSet rs = stmtMax.executeQuery()) {
                if (rs.next()) {
                    nextNumber = rs.getInt(1) + 1;
                }
            }
            if (nextNumber > 999) {
                nextNumber = 1;
            }
            String chiTietId = String.format("CT%03d", nextNumber);
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, chiTietId);
                stmt.setInt(2, soLuong);
                stmt.setString(3, hoaDonId);
                stmt.setString(4, sanPhamId);
                stmt.setDouble(5, donGia);
                stmt.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
            }
            LOGGER.log(Level.SEVERE, "Error adding invoice detail", e);
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
            }
        }
    }

    // Cập nhật trạng thái hóa đơn
    public boolean capNhatTrangThaiHoaDon(String hoaDonId, String trangThai) {
        if (hoaDonId == null || trangThai == null || !ensureConnection()) {
            return false;
        }
        String sql = "UPDATE HoaDon SET TrangThai = ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, trangThai);
            stmt.setString(2, hoaDonId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating invoice status", e);
        }
        return false;
    }

    // Lấy danh sách hóa đơn
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        if (!ensureConnection()) {
            return list;
        }
        String sql = "SELECT hd.*, kh.Ten as TenKhachHang FROM HoaDon hd "
                + "LEFT JOIN KhachHang kh ON hd.KhachHangID = kh.ID "
                + "ORDER BY CAST(RIGHT(hd.ID, 3) AS INT) DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
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
                list.add(hd);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving invoices", e);
        }
        return list;
    }

    // Lấy chi tiết hóa đơn
    public List<ChiTietHoaDon> getChiTietHoaDon(String hoaDonId) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        if (hoaDonId == null || hoaDonId.trim().isEmpty() || !ensureConnection()) {
            return list;
        }
        String sql = "SELECT ct.*, sp.Ten as TenSanPham FROM ChiTietHoaDon ct "
                + "INNER JOIN SanPham sp ON ct.SanPhamID = sp.ID "
                + "WHERE ct.HoaDonID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hoaDonId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDon ct = new ChiTietHoaDon();
                    ct.setId(rs.getString("ID"));
                    ct.setSoLuong(rs.getInt("SoSanPhamThanhToan"));
                    ct.setIdHoaDon(rs.getString("HoaDonID"));
                    ct.setIdSanPham(rs.getString("SanPhamID"));
                    ct.setDonGia(rs.getDouble("GiaBanMoiSanPham"));
                    list.add(ct);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving invoice details", e);
        }
        return list;
    }

    // Đếm số đơn chờ xử lý
    public int demSoDonChoXuLy() {
        if (!ensureConnection()) {
            return 0;
        }
        String sql = "SELECT COUNT(*) as SoDon FROM HoaDon WHERE TrangThai = N'Chưa Thanh Toán'";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("SoDon");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting pending invoices", e);
        }
        return 0;
    }

    // Tìm hóa đơn theo ID
    public HoaDon findHoaDonById(String id) {
        if (id == null || id.trim().isEmpty() || !ensureConnection()) {
            return null;
        }
        String sql = "SELECT * FROM HoaDon WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
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
                    return hd;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding invoice by ID", e);
        }
        return null;
    }

    // Lấy danh sách hóa đơn theo trạng thái
    public List<HoaDon> getHoaDonByTrangThai(String trangThai) {
        List<HoaDon> list = new ArrayList<>();
        if (!ensureConnection()) {
            return list;
        }
        String sql;
        boolean hasWhere = false;
        if ("Tất Cả".equals(trangThai)) {
            sql = "SELECT hd.*, kh.Ten as TenKhachHang FROM HoaDon hd "
                    + "LEFT JOIN KhachHang kh ON hd.KhachHangID = kh.ID "
                    + "ORDER BY CAST(RIGHT(hd.ID, 3) AS INT) DESC";
        } else {
            sql = "SELECT hd.*, kh.Ten as TenKhachHang FROM HoaDon hd "
                    + "LEFT JOIN KhachHang kh ON hd.KhachHangID = kh.ID "
                    + "WHERE hd.TrangThai = ? ORDER BY CAST(RIGHT(hd.ID, 3) AS INT) DESC";
            hasWhere = true;
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (hasWhere) {
                stmt.setString(1, trangThai);
            }
            try (ResultSet rs = stmt.executeQuery()) {
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
                    list.add(hd);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving invoices by status", e);
        }
        return list;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing database connection", e);
        }
    }
}
