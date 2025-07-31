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

public class BHDAO1 {

    private Connection connection;

    public BHDAO1() {
        try {
            // Kết nối database
            String url = "jdbc:sqlserver://localhost:1433;databaseName=DoAnTotNghiep;trustServerCertificate=true";
            String username = "sa"; 
            String password = "123456789"; 
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối database: " + e.getMessage());
        }
    }

    // Lấy danh sách tất cả sản phẩm
    public List<SanPham> getAllSanPham() {
        List<SanPham> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";

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
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
        }

        return danhSach;
    }

    // Tìm khách hàng theo số điện thoại
    public KhachHang findKhachHangBySDT(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) {
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
                return kh;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm khách hàng: " + e.getMessage());
        }

        return null;
    }

    // Lấy khuyến mãi theo sản phẩm
    public List<ChiTietKhuyenMai> getKhuyenMaiBySanPham(String sanPhamId) {
        List<ChiTietKhuyenMai> danhSach = new ArrayList<>();
        if (sanPhamId == null || sanPhamId.trim().isEmpty()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy khuyến mãi: " + e.getMessage());
        }

        return danhSach;
    }

    // Tạo hóa đơn mới
    public String taoHoaDon(String khachHangId, String nguoiDungId, BigDecimal tongTienGoc,
            BigDecimal mucGiamGia, BigDecimal tongTienSauGiamGia, String trangThai) {
        
        // Validate input parameters
        if (khachHangId == null || nguoiDungId == null || tongTienGoc == null) {
            JOptionPane.showMessageDialog(null, "Thông tin hóa đơn không hợp lệ!");
            return null;
        }
        
        String hoaDonId = "HD" + System.currentTimeMillis();
        String sql = "INSERT INTO HoaDon (ID, ThoiGian, KhachHangID, NguoiDungID, TongTienGoc, "
                + "MucGiamGia, TongTienSauGiamGia, TrangThai) VALUES (?, GETDATE(), ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hoaDonId);
            stmt.setString(2, khachHangId);
            stmt.setString(3, nguoiDungId);
            stmt.setBigDecimal(4, tongTienGoc);
            stmt.setBigDecimal(5, mucGiamGia != null ? mucGiamGia : BigDecimal.ZERO);
            stmt.setBigDecimal(6, tongTienSauGiamGia);
            stmt.setString(7, trangThai != null ? trangThai : "Chưa Thanh Toán");

            int result = stmt.executeUpdate();
            if (result > 0) {
                return hoaDonId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tạo hóa đơn: " + e.getMessage());
        }

        return null;
    }

    // Thêm chi tiết hóa đơn
    public boolean themChiTietHoaDon(String hoaDonId, String sanPhamId, int soLuong, double donGia) {
        if (hoaDonId == null || sanPhamId == null || soLuong <= 0 || donGia <= 0) {
            return false;
        }
        
        String chiTietId = "CT" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 4);
        String sql = "INSERT INTO ChiTietHoaDon (ID, SoSanPhamThanhToan, HoaDonID, SanPhamID, GiaBanMoiSanPham) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, chiTietId);
            stmt.setInt(2, soLuong);
            stmt.setString(3, hoaDonId);
            stmt.setString(4, sanPhamId);
            stmt.setDouble(5, donGia);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm chi tiết hóa đơn: " + e.getMessage());
        }

        return false;
    }

    // Cập nhật trạng thái hóa đơn
    public boolean capNhatTrangThaiHoaDon(String hoaDonId, String trangThai) {
        if (hoaDonId == null || trangThai == null) {
            return false;
        }
        
        String sql = "UPDATE HoaDon SET TrangThai = ? WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, trangThai);
            stmt.setString(2, hoaDonId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật trạng thái hóa đơn: " + e.getMessage());
        }

        return false;
    }

    // Lấy danh sách hóa đơn
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> danhSach = new ArrayList<>();
        String sql = "SELECT hd.*, kh.Ten as TenKhachHang FROM HoaDon hd "
                + "LEFT JOIN KhachHang kh ON hd.KhachHangID = kh.ID "
                + "ORDER BY hd.ThoiGian DESC";

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
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy chi tiết hóa đơn: " + e.getMessage());
        }

        return danhSach;
    }

    // Đếm số đơn chờ xử lý
    public int demSoDonChoXuLy() {
        String sql = "SELECT COUNT(*) as SoDon FROM HoaDon WHERE TrangThai = N'Chưa Thanh Toán'";

        try (PreparedStatement stmt = connection.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("SoDon");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi đếm số đơn chờ xử lý: " + e.getMessage());
        }

        return 0;
    }

    // Tìm hóa đơn theo ID
    public HoaDon findHoaDonById(String id) {
        if (id == null || id.trim().isEmpty()) {
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
                return hd;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm hóa đơn: " + e.getMessage());
        }

        return null;
    }

    // Lấy danh sách hóa đơn theo trạng thái
    public List<HoaDon> getHoaDonByTrangThai(String trangThai) {
        List<HoaDon> danhSach = new ArrayList<>();
        String sql;

        if ("Tất Cả".equals(trangThai)) {
            sql = "SELECT hd.*, kh.Ten as TenKhachHang FROM HoaDon hd "
                    + "LEFT JOIN KhachHang kh ON hd.KhachHangID = kh.ID "
                    + "ORDER BY hd.ThoiGian DESC";
        } else {
            sql = "SELECT hd.*, kh.Ten as TenKhachHang FROM HoaDon hd "
                    + "LEFT JOIN KhachHang kh ON hd.KhachHangID = kh.ID "
                    + "WHERE hd.TrangThai = ? ORDER BY hd.ThoiGian DESC";
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (!"Tất Cả".equals(trangThai)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy hóa đơn theo trạng thái: " + e.getMessage());
        }

        return danhSach;
    }

    // Đóng kết nối
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
