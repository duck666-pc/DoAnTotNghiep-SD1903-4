package Controller;

import java.sql.*;
import java.util.*;

import controller.MyConnection;

public class DOANHTHUDAO {

    private final MyConnection conn;

    public DOANHTHUDAO() {
        conn = new MyConnection();
    }

    public static class DoanhThuSanPham {

        private final String idSanPham;
        private final String tenSanPham;
        private final double gia;
        private final int soLuongBan;
        private final double thanhTien;

        public DoanhThuSanPham(String idSanPham, String tenSanPham, double gia, int soLuongBan) {
            this.idSanPham = idSanPham;
            this.tenSanPham = tenSanPham;
            this.gia = gia;
            this.soLuongBan = soLuongBan;
            this.thanhTien = gia * soLuongBan;
        }

        public String getIdSanPham() {
            return idSanPham;
        }

        public String getTenSanPham() {
            return tenSanPham;
        }

        public double getGia() {
            return gia;
        }

        public int getSoLuongBan() {
            return soLuongBan;
        }

        public double getThanhTien() {
            return thanhTien;
        }
    }

    public List<DoanhThuSanPham> getDoanhThuTheoKhoangThoiGian(String ngayBatDau, String ngayKetThuc) {
        List<DoanhThuSanPham> danhSachDoanhThu = new ArrayList<>();

        String sql = """
    SELECT 
        sp.ID AS id_san_pham,
        sp.Ten AS ten_san_pham,
        sp.Gia AS gia_san_pham,
        COALESCE(SUM(cthd.SoSanPhamThanhToan), 0) AS tong_so_luong_ban
    FROM 
        SanPham sp
    LEFT JOIN ChiTietHoaDon cthd ON sp.ID = cthd.SanPhamID
    LEFT JOIN HoaDon hd ON cthd.HoaDonID = hd.ID
    WHERE hd.ThoiGian BETWEEN ? AND ?
    GROUP BY sp.ID, sp.Ten, sp.Gia
    HAVING COALESCE(SUM(cthd.SoSanPhamThanhToan), 0) > 0
    ORDER BY tong_so_luong_ban DESC, sp.Ten ASC
    """;

        try (Connection connection = conn.DBConnect(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, ngayBatDau);
            stmt.setString(2, ngayKetThuc);

            // Debug output
            System.out.println("DEBUG - Executing query with dates: " + ngayBatDau + " to " + ngayKetThuc);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String idSanPham = rs.getString("id_san_pham");
                    String tenSanPham = rs.getString("ten_san_pham");
                    double gia = rs.getDouble("gia_san_pham");
                    int soLuongBan = rs.getInt("tong_so_luong_ban");

                    DoanhThuSanPham doanhThu = new DoanhThuSanPham(idSanPham, tenSanPham, gia, soLuongBan);
                    danhSachDoanhThu.add(doanhThu);

                    // Debug output
                    System.out.println("DEBUG - Found product: " + idSanPham + " - " + tenSanPham + " - Qty: " + soLuongBan);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Lỗi truy vấn doanh thu: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("DEBUG - Total products found: " + danhSachDoanhThu.size());
        return danhSachDoanhThu;
    }

    public int getTongSanPhamBanRa(String ngayBatDau, String ngayKetThuc) {
        String sql = """
        SELECT COALESCE(SUM(cthd.SoSanPhamThanhToan), 0) AS tong_san_pham
        FROM ChiTietHoaDon cthd
        JOIN HoaDon hd ON cthd.HoaDonID = hd.ID
        WHERE hd.ThoiGian BETWEEN ? AND ?
        """;

        try (Connection connection = conn.DBConnect(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, ngayBatDau);
            stmt.setString(2, ngayKetThuc);

            System.out.println("DEBUG - Getting total products for dates: " + ngayBatDau + " to " + ngayKetThuc);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("tong_san_pham");
                    System.out.println("DEBUG - Total products from DB: " + total);
                    return total;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving total products sold: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    public double getTongDoanhThu(String ngayBatDau, String ngayKetThuc) {
        String sql = """
        SELECT COALESCE(SUM(cthd.SoSanPhamThanhToan * cthd.GiaBanMoiSanPham), 0) AS tong_doanh_thu
        FROM ChiTietHoaDon cthd
        JOIN HoaDon hd ON cthd.HoaDonID = hd.ID
        WHERE hd.ThoiGian BETWEEN ? AND ?
        """;

        try (Connection connection = conn.DBConnect(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, ngayBatDau);
            stmt.setString(2, ngayKetThuc);

            System.out.println("DEBUG - Getting total revenue for dates: " + ngayBatDau + " to " + ngayKetThuc);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double total = rs.getDouble("tong_doanh_thu");
                    System.out.println("DEBUG - Total revenue from DB: " + total);
                    return total;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving total revenue: " + e.getMessage());
            e.printStackTrace();
        }

        return 0.0;
    }

    public List<DoanhThuSanPham> getAllProducts() {
        List<DoanhThuSanPham> danhSachSanPham = new ArrayList<>();

        String sql = "SELECT ID, Ten, Gia FROM SanPham ORDER BY Ten";

        try (Connection connection = conn.DBConnect(); PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("ID");
                String ten = rs.getString("Ten");
                double gia = rs.getDouble("Gia");

                DoanhThuSanPham sanPham = new DoanhThuSanPham(id, ten, gia, 0);
                danhSachSanPham.add(sanPham);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving all products: " + e.getMessage());
            e.printStackTrace();
        }

        return danhSachSanPham;
    }

    public List<DoanhThuSanPham> getAllProductsWithSales() {
        List<DoanhThuSanPham> danhSachSanPham = new ArrayList<>();

        String sql = """
            SELECT 
                sp.ID AS id_san_pham,
                sp.Ten AS ten_san_pham,
                sp.Gia AS gia_san_pham,
                COALESCE(SUM(cthd.SoSanPhamThanhToan), 0) AS tong_so_luong_ban
            FROM 
                SanPham sp
            LEFT JOIN ChiTietHoaDon cthd ON sp.ID = cthd.SanPhamID
            LEFT JOIN HoaDon hd ON cthd.HoaDonID = hd.ID
            GROUP BY sp.ID, sp.Ten, sp.Gia
            ORDER BY tong_so_luong_ban DESC, sp.Ten ASC
            """;

        try (Connection connection = conn.DBConnect(); PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id_san_pham");
                String ten = rs.getString("ten_san_pham");
                double gia = rs.getDouble("gia_san_pham");
                int soLuongBan = rs.getInt("tong_so_luong_ban");

                DoanhThuSanPham sanPham = new DoanhThuSanPham(id, ten, gia, soLuongBan);
                danhSachSanPham.add(sanPham);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving products with sales: " + e.getMessage());
            e.printStackTrace();
        }

        return danhSachSanPham;
    }

    public int getTongSanPhamBanRaToanBo() {
        String sql = """
            SELECT COALESCE(SUM(cthd.SoSanPhamThanhToan), 0) AS tong_san_pham
            FROM ChiTietHoaDon cthd
            JOIN HoaDon hd ON cthd.HoaDonID = hd.ID
            """;

        try (Connection connection = conn.DBConnect(); PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("tong_san_pham");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving total products sold: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    public double getTongDoanhThuToanBo() {
        String sql = """
            SELECT COALESCE(SUM(cthd.SoSanPhamThanhToan * cthd.GiaBanMoiSanPham), 0) AS tong_doanh_thu
            FROM ChiTietHoaDon cthd
            JOIN HoaDon hd ON cthd.HoaDonID = hd.ID
            """;

        try (Connection connection = conn.DBConnect(); PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("tong_doanh_thu");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving total revenue: " + e.getMessage());
            e.printStackTrace();
        }

        return 0.0;
    }

    public List<DoanhThuSanPham> getDoanhThuTheoThang(int month, int year) {
        String startDate = String.format("%04d-%02d-01", year, month);
        String endDate = String.format("%04d-%02d-31", year, month);
        return getDoanhThuTheoKhoangThoiGian(startDate, endDate);
    }

    public List<DoanhThuSanPham> getTopSellingProducts(int limit) {
        List<DoanhThuSanPham> danhSachSanPham = new ArrayList<>();

        String sql = """
            SELECT TOP (?) 
                sp.ID AS id_san_pham,
                sp.Ten AS ten_san_pham,
                sp.Gia AS gia_san_pham,
                COALESCE(SUM(cthd.SoSanPhamThanhToan), 0) AS tong_so_luong_ban
            FROM 
                SanPham sp
            LEFT JOIN ChiTietHoaDon cthd ON sp.ID = cthd.SanPhamID
            LEFT JOIN HoaDon hd ON cthd.HoaDonID = hd.ID
            GROUP BY sp.ID, sp.Ten, sp.Gia
            HAVING COALESCE(SUM(cthd.SoSanPhamThanhToan), 0) > 0
            ORDER BY tong_so_luong_ban DESC
            """;

        try (Connection connection = conn.DBConnect(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id_san_pham");
                    String ten = rs.getString("ten_san_pham");
                    double gia = rs.getDouble("gia_san_pham");
                    int soLuongBan = rs.getInt("tong_so_luong_ban");

                    DoanhThuSanPham sanPham = new DoanhThuSanPham(id, ten, gia, soLuongBan);
                    danhSachSanPham.add(sanPham);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving top selling products: " + e.getMessage());
            e.printStackTrace();
        }

        return danhSachSanPham;
    }

    // Method to check if a date range is valid
    public boolean isValidDateRange(String startDate, String endDate) {
        try {
            return startDate.compareTo(endDate) <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidDate(int day, int month, int year) {
        try {
            if (year < 1900 || year > 2100) {
                return false;
            }
            if (month < 1 || month > 12) {
                return false;
            }
            if (day < 1 || day > 31) {
                return false;
            }

            if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
                return false;
            }

            if (month == 2) {
                boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                if (day > (isLeapYear ? 29 : 28)) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String formatDate(int day, int month, int year) {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    public void closeConnection() {
    }
}
