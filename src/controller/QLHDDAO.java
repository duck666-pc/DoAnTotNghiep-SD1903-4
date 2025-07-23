/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.HoaDon;
import Model.ChiTietHoaDon;
import java.util.ArrayList;
import java.util.List;

class HoaDonDAO extends BaseDAO<HoaDon> {

    @Override
    protected String getTableName() {
        return "HoaDon";
    }

    @Override
    protected String getPrimaryKeyColumn() {
        return "ID";
    }

    @Override
    protected HoaDon mapResultSetToObject(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setId(rs.getString("ID"));
        hd.setThoiGian(rs.getTimestamp("ThoiGian"));
        hd.setIdKhachHang(rs.getString("KhachHangID"));
        hd.setIdNguoiDung(rs.getString("NguoiDungID"));
        hd.setTongTienGoc(rs.getBigDecimal("TongTienGoc"));
        hd.setMucGiamGia(rs.getBigDecimal("MucGiamGia"));
        hd.setTongTienSauGiamGia(rs.getBigDecimal("TongTienSauGiamGia"));
        return hd;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO HoaDon (ID, ThoiGian, KhachHangID, NguoiDungID, TongTienGoc, MucGiamGia, TongTienSauGiamGia) VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, HoaDon hd) throws SQLException {
        ps.setString(1, hd.getId());
        ps.setTimestamp(2, hd.getThoiGian());
        ps.setString(3, hd.getIdKhachHang());
        ps.setString(4, hd.getIdNguoiDung());
        ps.setBigDecimal(5, hd.getTongTienGoc());
        ps.setBigDecimal(6, hd.getMucGiamGia());
        ps.setBigDecimal(7, hd.getTongTienSauGiamGia());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE HoaDon SET ID = ?, ThoiGian = ?, KhachHangID = ?, NguoiDungID = ?, TongTienGoc = ?, MucGiamGia = ?, TongTienSauGiamGia = ? WHERE ID = ?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, HoaDon hd) throws SQLException {
        ps.setString(1, hd.getId());
        ps.setTimestamp(2, hd.getThoiGian());
        ps.setString(3, hd.getIdKhachHang());
        ps.setString(4, hd.getIdNguoiDung());
        ps.setBigDecimal(5, hd.getTongTienGoc());
        ps.setBigDecimal(6, hd.getMucGiamGia());
        ps.setBigDecimal(7, hd.getTongTienSauGiamGia());
    }

    @Override
    protected int getUpdateWhereIndex() {
        return 8;
    }

    public Object[] getRowArray(HoaDon hd) {
        return new Object[]{
            hd.getId(),
            hd.getThoiGian(),
            hd.getIdKhachHang(),
            hd.getIdNguoiDung(),
            hd.getTongTienGoc(),
            hd.getMucGiamGia(),
            hd.getTongTienSauGiamGia()
        };
    }
}

class ChiTietHoaDonDAO extends BaseDAO<ChiTietHoaDon> {

    @Override
    protected String getTableName() {
        return "ChiTietHoaDon";
    }

    @Override
    protected String getPrimaryKeyColumn() {
        return "ID";
    }

    @Override
    protected ChiTietHoaDon mapResultSetToObject(ResultSet rs) throws SQLException {
        ChiTietHoaDon cthd = new ChiTietHoaDon();
        cthd.setId(rs.getString("ID"));
        cthd.setSoLuong(rs.getInt("SoSanPhamThanhToan"));
        cthd.setIdHoaDon(rs.getString("HoaDonID"));
        cthd.setIdSanPham(rs.getString("SanPhamID"));
        cthd.setDonGia(rs.getDouble("GiaBanMoiSanPham"));
        return cthd;
    }

    public List<ChiTietHoaDon> getCTHD(String maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD = ?";

        try (
                PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietHoaDon ct = mapResultSetToObject(rs);
                list.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO ChiTietHoaDon (ID, SoSanPhamThanhToan, HoaDonID, SanPhamID, GiaBanMoiSanPham) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, ChiTietHoaDon cthd) throws SQLException {
        ps.setString(1, cthd.getId());
        ps.setInt(2, cthd.getSoLuong());
        ps.setString(3, cthd.getIdHoaDon());
        ps.setString(4, cthd.getIdSanPham());
        ps.setDouble(5, cthd.getDonGia());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE ChiTietHoaDon SET ID = ?, SoSanPhamThanhToan = ?, HoaDonID = ?, SanPhamID = ?, GiaBanMoiSanPham = ? WHERE ID = ?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, ChiTietHoaDon cthd) throws SQLException {
        ps.setString(1, cthd.getId());
        ps.setInt(2, cthd.getSoLuong());
        ps.setString(3, cthd.getIdHoaDon());
        ps.setString(4, cthd.getIdSanPham());
        ps.setDouble(5, cthd.getDonGia());
    }

    @Override
    protected int getUpdateWhereIndex() {
        return 6;
    }

    public Object[] getRowArray(ChiTietHoaDon cthd) {
        return new Object[]{
            cthd.getId(),
            cthd.getSoLuong(),
            cthd.getIdHoaDon(),
            cthd.getIdSanPham(),
            cthd.getDonGia()
        };
    }
}

public class QLHDDAO {

    private final HoaDonDAO hoaDonDAO;
    private final ChiTietHoaDonDAO chiTietHoaDonDAO;

    public QLHDDAO() {
        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
    }

    public java.util.List<HoaDon> getAllHD() throws SQLException, ClassNotFoundException {
        return hoaDonDAO.getAll();
    }

    public HoaDon getHD(String id) throws SQLException, ClassNotFoundException {
        return hoaDonDAO.getRow(id);
    }

    public int addHD(HoaDon hd) throws SQLException, ClassNotFoundException {
        return hoaDonDAO.add(hd);
    }

    public int editHD(HoaDon hd, String oldId) throws SQLException, ClassNotFoundException {
        return hoaDonDAO.edit(hd, oldId);
    }

    public int deleteHD(String id) throws SQLException, ClassNotFoundException {
        return hoaDonDAO.delete(id);
    }

    public java.util.List<ChiTietHoaDon> getAllCTHD() throws SQLException, ClassNotFoundException {
        return chiTietHoaDonDAO.getAll();
    }

    public ChiTietHoaDon getCTHD(String id) throws SQLException, ClassNotFoundException {
        return chiTietHoaDonDAO.getRow(id);
    }

    public int addCTHD(ChiTietHoaDon cthd) throws SQLException, ClassNotFoundException {
        return chiTietHoaDonDAO.add(cthd);
    }

    public int editCTHD(ChiTietHoaDon cthd, String oldId) throws SQLException, ClassNotFoundException {
        return chiTietHoaDonDAO.edit(cthd, oldId);
    }

    public int deleteCTHD(String id) throws SQLException, ClassNotFoundException {
        return chiTietHoaDonDAO.delete(id);
    }

    public Object[] getRow(HoaDon hd) {
        return hoaDonDAO.getRowArray(hd);
    }

    public Object[] getRow(ChiTietHoaDon cthd) {
        return chiTietHoaDonDAO.getRowArray(cthd);
    }
}
