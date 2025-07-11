/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.LoaiSanPham;

/**
 *
 * @author minhd
 */
public class QLLSPDAO extends BaseDAO<LoaiSanPham> {

    @Override
    protected String getTableName() {
        return "LOAISANPHAM";
    }

    @Override
    protected String getPrimaryKeyColumn() {
        return "ID";
    }

    @Override
    protected LoaiSanPham mapResultSetToObject(ResultSet rs) throws SQLException {
        LoaiSanPham lsp = new LoaiSanPham();
        lsp.setId(rs.getString("ID"));
        lsp.setTen(rs.getString("TEN"));
        lsp.setMoTa(rs.getString("MOTA"));
        return lsp;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO LOAISANPHAM (ID, TEN, MOTA) VALUES (?, ?, ?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, LoaiSanPham lsp) throws SQLException {
        ps.setString(1, lsp.getId());
        ps.setString(2, lsp.getTen());
        ps.setString(3, lsp.getMoTa());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE LOAISANPHAM SET "
                + "ID = ?, "
                + "TEN = ?, "
                + "MOTA = ?, "
                + "WHERE ID = ?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, LoaiSanPham lsp) throws SQLException {
        ps.setString(1, lsp.getId());
        ps.setString(2, lsp.getTen());
        ps.setString(3, lsp.getMoTa());

    }

    @Override
    protected int getUpdateWhereIndex() {
        return 4;
    }

    public Object[] getRow(LoaiSanPham lsp) {
        return new Object[]{
            lsp.getId(),
            lsp.getTen(),
            lsp.getMoTa()
        };
    }
}
