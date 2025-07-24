/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.KhuyenMai;
import model.ChiTietKhuyenMai;

/**
 *
 * @author minhd
 */
public class QLKMDAO {

    public static class KhuyenMaiDAO extends BaseDAO<KhuyenMai> {

        @Override
        protected String getTableName() {
            return "KHUYENMAI";
        }

        @Override
        protected String getPrimaryKeyColumn() {
            return "ID";
        }

        @Override
        protected KhuyenMai mapResultSetToObject(ResultSet rs) throws SQLException {
            KhuyenMai km = new KhuyenMai();
            km.setId(rs.getString("ID"));
            km.setChiTietid(rs.getString("CHITIETID"));
            km.setKhachHangid(rs.getString("KHACHHANGID"));
            km.setTen(rs.getString("TEN"));
            km.setMoTa(rs.getString("MOTA"));
            km.setSoLuong(rs.getInt("SOLUONG"));
            km.setThoiGianApDung(rs.getDate("THOIGIANAPDUNG"));
            km.setThoiGianKetThuc(rs.getDate("THOIGIANKETTHUC"));
            return km;
        }

        @Override
        protected String getInsertQuery() {
            return "INSERT INTO KHUYENMAI (ID, CHITIETID, KHACHHANGID, TEN, MOTA, SOLUONG, THOIGIANAPDUNG, THOIGIANKETTHUC) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        }

        @Override
        protected void setInsertParameters(PreparedStatement ps, KhuyenMai km) throws SQLException {
            ps.setString(1, km.getId());
            ps.setString(2, km.getChiTietid());
            ps.setString(3, km.getKhachHangid());
            ps.setString(4, km.getTen());
            ps.setString(5, km.getMoTa());
            ps.setInt(6, km.getSoLuong());
            ps.setDate(7, (Date) km.getThoiGianApDung());
            ps.setDate(8, (Date) km.getThoiGianKetThuc());
        }

        @Override
        protected String getUpdateQuery() {
            return "UPDATE KHUYENMAI SET "
                    + "ID = ?, "
                    + "CHITIETID = ?, "
                    + "KHACHHANGID = ?, "
                    + "TEN = ?, "
                    + "MOTA = ?, "
                    + "SOLUONG = ?, "
                    + "THOIGIANAPDUNG = ?, "
                    + "THOIGIANKETTHUC = ? "
                    + "WHERE ID = ?";
        }

        @Override
        protected void setUpdateParameters(PreparedStatement ps, KhuyenMai km) throws SQLException {
            ps.setString(1, km.getId());
            ps.setString(2, km.getChiTietid());
            ps.setString(3, km.getKhachHangid());
            ps.setString(4, km.getTen());
            ps.setString(5, km.getMoTa());
            ps.setInt(6, km.getSoLuong());
            ps.setDate(7, (Date) km.getThoiGianApDung());
            ps.setDate(8, (Date) km.getThoiGianKetThuc());
        }

        @Override
        protected int getUpdateWhereIndex() {
            return 9;
        }
    }

    public static class ChiTietKhuyenMaiDAO extends BaseDAO<ChiTietKhuyenMai> {

        @Override
        protected String getTableName() {
            return "CHITIETKHUYENMAI";
        }

        @Override
        protected String getPrimaryKeyColumn() {
            return "ID";
        }

        @Override
        protected ChiTietKhuyenMai mapResultSetToObject(ResultSet rs) throws SQLException {
            ChiTietKhuyenMai ctkm = new ChiTietKhuyenMai();
            ctkm.setId(rs.getString("ID"));
            ctkm.setHinhThucGiam(rs.getString("HINHTHUCGIAM"));
            ctkm.setSoTienGiamGia(rs.getFloat("SOTIENGIAMGIA"));
            ctkm.setSanPhamid(rs.getString("SANPHAMID"));
            ctkm.setMucGiamGia(rs.getFloat("MUCGIAMGIA"));
            ctkm.setQuaTang(rs.getString("QUATANG"));
            return ctkm;
        }

        @Override
        protected String getInsertQuery() {
            return "INSERT INTO CHITIETKHUYENMAI (ID, HINHTHUCGIAM, SOTIENGIAMGIA, SANPHAMID, MUCGIAMGIA, QUATANG) VALUES (?, ?, ?, ?, ?, ?)";
        }

        @Override
        protected void setInsertParameters(PreparedStatement ps, ChiTietKhuyenMai ctkm) throws SQLException {
            ps.setString(1, ctkm.getId());
            ps.setString(2, ctkm.getHinhThucGiam());
            ps.setFloat(3, ctkm.getSoTienGiamGia());
            ps.setString(4, ctkm.getSanPhamid());
            ps.setFloat(5, ctkm.getMucGiamGia());
            ps.setString(6, ctkm.getQuaTang());
        }

        @Override
        protected String getUpdateQuery() {
            return "UPDATE CHITIETKHUYENMAI SET "
                    + "ID = ?, "
                    + "HINHTHUCGIAM = ?, "
                    + "SOTIENGIAMGIA = ?, "
                    + "SANPHAMID = ?, "
                    + "MUCGIAMGIA = ?, "
                    + "QUATANG = ? "
                    + "WHERE ID = ?";
        }

        @Override
        protected void setUpdateParameters(PreparedStatement ps, ChiTietKhuyenMai ctkm) throws SQLException {
            ps.setString(1, ctkm.getId());
            ps.setString(2, ctkm.getHinhThucGiam());
            ps.setFloat(3, ctkm.getSoTienGiamGia());
            ps.setString(4, ctkm.getSanPhamid());
            ps.setFloat(5, ctkm.getMucGiamGia());
            ps.setString(6, ctkm.getQuaTang());
        }

        @Override
        protected int getUpdateWhereIndex() {
            return 7;
        }
    }

    // Instance của các DAO
    private final KhuyenMaiDAO khuyenMaiDAO;
    private final ChiTietKhuyenMaiDAO chiTietKhuyenMaiDAO;

    public QLKMDAO() {
        khuyenMaiDAO = new KhuyenMaiDAO();
        chiTietKhuyenMaiDAO = new ChiTietKhuyenMaiDAO();
    }

    public List<KhuyenMai> getAllKM() throws SQLException, ClassNotFoundException {
        return khuyenMaiDAO.getAll();
    }

    public int addKM(KhuyenMai km) throws SQLException, ClassNotFoundException {
        return khuyenMaiDAO.add(km);
    }

    public int editKM(KhuyenMai km, String oldId) throws SQLException, ClassNotFoundException {
        return khuyenMaiDAO.edit(km, oldId);
    }

    public int deleteKM(String id) throws SQLException, ClassNotFoundException {
        return khuyenMaiDAO.delete(id);
    }

    public KhuyenMai getKMById(String id) throws SQLException, ClassNotFoundException {
        return khuyenMaiDAO.getRow(id);
    }

    public List<ChiTietKhuyenMai> getAllCTKM() throws SQLException, ClassNotFoundException {
        return chiTietKhuyenMaiDAO.getAll();
    }

    public int addCTKM(ChiTietKhuyenMai ctkm) throws SQLException, ClassNotFoundException {
        return chiTietKhuyenMaiDAO.add(ctkm);
    }

    public int editCTKM(ChiTietKhuyenMai ctkm, String oldId) throws SQLException, ClassNotFoundException {
        return chiTietKhuyenMaiDAO.edit(ctkm, oldId);
    }

    public int deleteCTKM(String id) throws SQLException, ClassNotFoundException {
        return chiTietKhuyenMaiDAO.delete(id);
    }

    public ChiTietKhuyenMai getCTKMById(String id) throws SQLException, ClassNotFoundException {
        return chiTietKhuyenMaiDAO.getRow(id);
    }

    public Object[] getRow(KhuyenMai km) {
        return new Object[]{
            km.getId(),
            km.getChiTietid(),
            km.getKhachHangid(),
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
}
