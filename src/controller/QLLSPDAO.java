/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                + "MOTA = ? "
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

    /**
     * Generates the next ID based on the highest existing ID Format:
     * XXX-ProductTypeName where XXX is sequential number
     *
     * @param productTypeName
     * @return
     * @throws java.sql.SQLException
     */
    public String generateNextId(String productTypeName) throws SQLException {
        String sql = "SELECT TOP 1 ID FROM LOAISANPHAM ORDER BY ID DESC";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            int nextNumber = 1;

            if (rs.next()) {
                String lastId = rs.getString("ID");
                if (lastId != null && lastId.length() >= 3) {
                    try {
                        // Find the first dash or take first 3 characters
                        int dashIndex = lastId.indexOf('-');
                        String numericPart = dashIndex > 0
                                ? lastId.substring(0, dashIndex)
                                : lastId.substring(0, Math.min(3, lastId.length()));

                        nextNumber = Integer.parseInt(numericPart) + 1;
                    } catch (NumberFormatException e) {
                        // If parsing fails, start from 1
                        nextNumber = 1;
                    }
                }
            }

            // Format: 001-ProductTypeName, 002-ProductTypeName, etc.
            return String.format("%03d-%s", nextNumber, productTypeName);
        }
    }

    /**
     * Gets all product types formatted for combobox (ID-Name format)
     *
     * @return
     * @throws java.lang.Exception
     */
    public String[] getProductTypeComboBoxItems() throws Exception {
        java.util.List<LoaiSanPham> list = getAll();
        String[] items = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            LoaiSanPham lsp = list.get(i);
            items[i] = lsp.getId() + "-" + lsp.getTen();
        }

        return items;
    }

    /**
     * Override add method to auto-generate ID
     *
     * @param entity
     * @return
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    @Override
    public int add(LoaiSanPham entity) throws SQLException, ClassNotFoundException {
        // Generate ID automatically
        String generatedId = generateNextId(entity.getTen());
        entity.setId(generatedId);

        return super.add(entity);
    }
}
