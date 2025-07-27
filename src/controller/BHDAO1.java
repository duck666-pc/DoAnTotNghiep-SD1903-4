/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.*;
import java.time.LocalDate;
/**
 *
 * @author Admin
 */
public class BHDAO1 {
    
    // Method to get database connection - should be centralized
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        MyConnection DBconnect = new MyConnection();
        return DBconnect.DBConnect();
    }
    
    // Method to close resources safely
    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public ResultSet getHD(){
        try{
            Connection conn = getConnection();
            String sql = "SELECT * FROM HoaDon ORDER BY ThoiGian DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    
    public ResultSet getSP(){
        try{
            Connection conn = getConnection();
            String sql = "SELECT ID, Ten, Gia FROM SanPham ORDER BY Ten";
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    
    // Fixed the complex aggregation query
    public ResultSet getCTHD(String ID){
        try{
            Connection conn = getConnection();
            String sql = "SELECT c.ID, c.SanPhamID, c.SoSanPhamThanhToan, c.GiaBanMoiSanPham, " +
                        "(c.SoSanPhamThanhToan * c.GiaBanMoiSanPham) as ThanhTien " +
                        "FROM ChiTietHoaDon c " +
                        "WHERE c.HoaDonID = ? " +
                        "ORDER BY c.ID";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ID);
            return stmt.executeQuery();
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    
    // Check if product already exists in invoice before adding
    private boolean isProductInInvoice(String idHoaDon, String idSanPham) {
        try {
            Connection conn = getConnection();
            String sql = "SELECT COUNT(*) FROM ChiTietHoaDon WHERE HoaDonID = ? AND SanPhamID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idHoaDon);
            stmt.setString(2, idSanPham);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            closeResources(conn, stmt, rs);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean addCTHD(String ID, int soLuong, String idHoaDon, String idSanPham, double donGia){
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try{
            // Check if product already exists in this invoice
            if (isProductInInvoice(idHoaDon, idSanPham)) {
                // Update existing record instead of adding new one
                return updateExistingCTHD(idHoaDon, idSanPham, soLuong);
            }
            
            conn = getConnection();
            String sql = "INSERT INTO ChiTietHoaDon (ID, SoSanPhamThanhToan, HoaDonID, SanPhamID, GiaBanMoiSanPham) " +
                        "VALUES (?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, ID);
            stmt.setInt(2, soLuong);
            stmt.setString(3, idHoaDon);
            stmt.setString(4, idSanPham);
            stmt.setDouble(5, donGia);
            
            return stmt.executeUpdate() > 0;
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return false;
        }
        finally {
            closeResources(conn, stmt, null);
        }
    }
    
    // Helper method to update existing product quantity in invoice
    private boolean updateExistingCTHD(String idHoaDon, String idSanPham, int additionalQuantity) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            String sql = "UPDATE ChiTietHoaDon SET SoSanPhamThanhToan = SoSanPhamThanhToan + ? " +
                        "WHERE HoaDonID = ? AND SanPhamID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, additionalQuantity);
            stmt.setString(2, idHoaDon);
            stmt.setString(3, idSanPham);
            
            return stmt.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            closeResources(conn, stmt, null);
        }
    }
    
    public boolean deleteCTHD(String IDHD, String idSanPham){
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try{
            conn = getConnection();
            String sql = "DELETE FROM ChiTietHoaDon WHERE HoaDonID = ? AND SanPhamID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, IDHD);
            stmt.setString(2, idSanPham);
            return stmt.executeUpdate() > 0;
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return false;
        }
        finally {
            closeResources(conn, stmt, null);
        }
    }
    
    // Fixed method name and logic for updating invoice total
    public boolean updateHoaDonTotal(double thanhTien, String idHoaDon){
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try{
            conn = getConnection();
            String sql = "UPDATE HoaDon SET TongTienGoc = ?, TongTienSauGiamGia = (? - MucGiamGia) " +
                        "WHERE ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, thanhTien);
            stmt.setDouble(2, thanhTien);
            stmt.setString(3, idHoaDon);
            return stmt.executeUpdate() > 0;
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return false;
        }
        finally {
            closeResources(conn, stmt, null);
        }
    }
    
    // Fixed the update method for ChiTietHoaDon
    public boolean updateCTHD1(String newID, int soLuong, String idSanPham, double donGia, String originalCTHDId){
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try{
            conn = getConnection();
            String sql = "UPDATE ChiTietHoaDon SET ID = ?, SoSanPhamThanhToan = ?, " +
                        "SanPhamID = ?, GiaBanMoiSanPham = ? WHERE ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, newID);
            stmt.setInt(2, soLuong);
            stmt.setString(3, idSanPham);
            stmt.setDouble(4, donGia);
            stmt.setString(5, originalCTHDId);
            return stmt.executeUpdate() > 0;
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace(); 
            return false;
        }
        finally {
            closeResources(conn, stmt, null);
        }
    }
    
    public boolean addHD(String id, String thoiGian, String idKhachHang, String idNguoiDung, 
                        double tongTienGoc, double mucGiamGia, double tongTienSauGiamGia){
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try{
            conn = getConnection();
            String query = "INSERT INTO HoaDon (ID, ThoiGian, KhachHangID, NguoiDungID, " +
                          "TongTienGoc, MucGiamGia, TongTienSauGiamGia) VALUES(?,?,?,?,?,?,?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            stmt.setDate(2, Date.valueOf(thoiGian));
            stmt.setString(3, idKhachHang);
            stmt.setString(4, idNguoiDung);
            stmt.setDouble(5, tongTienGoc);
            stmt.setDouble(6, mucGiamGia);
            stmt.setDouble(7, tongTienSauGiamGia);
            return stmt.executeUpdate() > 0;
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            return false;
        }
        finally {
            closeResources(conn, stmt, null);
        }
    }
    
    // Fixed the update method with proper transaction handling
    public boolean updateHD(String id, String thoiGian, String idKhachHang, String idNguoiDung, 
                           double tongTienGoc, double mucGiamGia, double tongTienSauGiamGia, String originalID){
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        
        try{
            conn = getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Update HoaDon
            String query1 = "UPDATE HoaDon SET ID = ?, ThoiGian = ?, KhachHangID = ?, " +
                           "NguoiDungID = ?, TongTienGoc = ?, MucGiamGia = ?, TongTienSauGiamGia = ? " +
                           "WHERE ID = ?";
            stmt1 = conn.prepareStatement(query1);
            stmt1.setString(1, id);
            stmt1.setDate(2, Date.valueOf(thoiGian));
            stmt1.setString(3, idKhachHang);
            stmt1.setString(4, idNguoiDung);
            stmt1.setDouble(5, tongTienGoc);
            stmt1.setDouble(6, mucGiamGia);
            stmt1.setDouble(7, tongTienSauGiamGia);
            stmt1.setString(8, originalID);
            
            int result1 = stmt1.executeUpdate();
            
            // Update ChiTietHoaDon if ID changed
            if (!id.equals(originalID)) {
                String query2 = "UPDATE ChiTietHoaDon SET HoaDonID = ? WHERE HoaDonID = ?";
                stmt2 = conn.prepareStatement(query2);
                stmt2.setString(1, id);
                stmt2.setString(2, originalID);
                stmt2.executeUpdate();
            }
            
            conn.commit(); // Commit transaction
            return result1 > 0;
        }
        catch(ClassNotFoundException | SQLException e){
            try {
                if (conn != null) conn.rollback(); // Rollback on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if (conn != null) conn.setAutoCommit(true); // Reset auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeResources(conn, stmt2, null);
            try {
                if (stmt1 != null) stmt1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Fixed the delete method with proper transaction handling
    public boolean deleteHD(String idHoaDon){
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        
        try{
            conn = getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Delete ChiTietHoaDon first (foreign key constraint)
            String query1 = "DELETE FROM ChiTietHoaDon WHERE HoaDonID = ?";
            stmt1 = conn.prepareStatement(query1);
            stmt1.setString(1, idHoaDon);
            stmt1.executeUpdate();
            
            // Delete HoaDon
            String query2 = "DELETE FROM HoaDon WHERE ID = ?";
            stmt2 = conn.prepareStatement(query2);
            stmt2.setString(1, idHoaDon);
            int result = stmt2.executeUpdate();
            
            conn.commit(); // Commit transaction
            return result > 0;
        }
        catch(ClassNotFoundException | SQLException e){
            try {
                if (conn != null) conn.rollback(); // Rollback on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if (conn != null) conn.setAutoCommit(true); // Reset auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeResources(conn, stmt2, null);
            try {
                if (stmt1 != null) stmt1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Utility method to calculate total for an invoice
    public double calculateInvoiceTotal(String idHoaDon) {
        try {
            Connection conn = getConnection();
            String sql = "SELECT SUM(SoSanPhamThanhToan * GiaBanMoiSanPham) as Total " +
                        "FROM ChiTietHoaDon WHERE HoaDonID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idHoaDon);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                double total = rs.getDouble("Total");
                closeResources(conn, stmt, rs);
                return total;
            }
            closeResources(conn, stmt, rs);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // Backward compatibility methods - delegate to new method names
    @Deprecated
    public boolean updateCTHD(double thanhTien, String ID) {
        return updateHoaDonTotal(thanhTien, ID);
    }
}
