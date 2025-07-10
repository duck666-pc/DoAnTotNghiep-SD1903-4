/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public abstract class BasePanel<T> extends javax.swing.JPanel {

    protected DefaultTableModel tableModel;
    protected int currentRow = -1;
    protected JTable jTable;
    protected JButton jbtThem;
    protected JButton jbtSua;
    protected JButton jbtXoa;
    protected JButton jbtTimKiem;
    protected JButton jbtLamMoi;
    protected JTextField txtTimKiem;

    public BasePanel() {

    }

    protected final void addTableSelectionListener() {
        jTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTable.getSelectedRow() >= 0) {
                currentRow = jTable.getSelectedRow();
                setFormFromRow(currentRow);
            }
        });
    }

    protected String getValue(int row, int col) {
        Object val = jTable.getValueAt(row, col);
        return val == null ? "" : val.toString();
    }

    public final void initTable() {
        tableModel = new DefaultTableModel(getColumnNames(), 0);

        if (jTable == null) {
            throw new IllegalStateException("jTable is null. Ensure UI is initialized before calling initTable()");
        }

        jTable.setModel(tableModel);
    }

    protected abstract String[] getColumnNames();

    public void fillTable() {
        tableModel.setRowCount(0);
        try {
            for (T entity : getAllEntities()) {
                addEntityToTable(entity);
            }
        } catch (Exception ex) {
            showError(ex, "Lỗi khi tải dữ liệu: ");
        }
    }

    public void searchById(String id) {
        if (!id.isEmpty()) {
            tableModel.setRowCount(0);
            boolean found = false;
            try {
                for (T entity : getAllEntities()) {
                    if (getEntityId(entity).equalsIgnoreCase(id)) {
                        addEntityToTable(entity);
                        found = true;
                        int row = tableModel.getRowCount() - 1;
                        jTable.setRowSelectionInterval(row, row);
                        jTable.scrollRectToVisible(jTable.getCellRect(row, 0, true));
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy với ID: " + id);
                }
            } catch (Exception ex) {
                showError(ex, "Lỗi khi tải dữ liệu: ");
            }
        } else {
            fillTable();
        }
    }

    protected void showError(Exception ex, String prefix) {
        JOptionPane.showMessageDialog(this, prefix + ex.getMessage());
    }

    protected abstract void setFormFromRow(int row);

    protected abstract boolean validateForm();

    protected abstract T getEntityFromForm();

    protected abstract void clearForm();

    protected abstract List<T> getAllEntities() throws Exception;

    protected abstract String getEntityId(T entity);

    protected abstract void addEntityToTable(T entity);

    protected abstract int addEntity(T entity) throws Exception;

    protected abstract int deleteEntity(String id) throws Exception;

    protected abstract int updateEntity(T entity, String oldId) throws Exception;

    protected void handleAddAction() {
        if (!validateForm()) {
            return;
        }
        try {
            int result = addEntity(getEntityFromForm());
            String entityName = getEntityName();
            JOptionPane.showMessageDialog(this, result == 1
                    ? "Thêm " + entityName + " thành công!" : "Thêm " + entityName + " thất bại!");
            fillTable();
            clearForm();
        } catch (Exception ex) {
            showError(ex, "Lỗi: ");
        }
    }

    protected void handleDeleteAction() {
        if (currentRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!");
            return;
        }

        String entityName = getEntityName();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa " + entityName + " này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            String id = getEntityIdFromRow(currentRow);
            int result = deleteEntity(id);
            JOptionPane.showMessageDialog(this, result == 1
                    ? "Xóa " + entityName + " thành công!" : "Xóa " + entityName + " thất bại!");
            fillTable();
            clearForm();
            currentRow = -1;
        } catch (Exception ex) {
            showError(ex, "Lỗi: ");
        }
    }

    protected void handleUpdateAction() {
        if (currentRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa!");
            return;
        }
        if (!validateForm()) {
            return;
        }

        String entityName = getEntityName();
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn sửa?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            String oldId = getEntityIdFromRow(currentRow);
            int result = updateEntity(getEntityFromForm(), oldId);
            fillTable();
            JOptionPane.showMessageDialog(this, result == 1
                    ? "Cập nhật thành công!" : "Cập nhật thất bại!");
        } catch (Exception ex) {
            showError(ex, "Lỗi: ");
        }
        clearForm();
    }

    protected void handleSearchAction() {
        searchById(txtTimKiem.getText().trim());
    }

    protected void handleRefreshAction() {
        clearForm();
    }

    private String getEntityIdFromRow(int row) {
        return getValue(row, 0);
    }

    private String getEntityName() {
        String className = this.getClass().getSimpleName();
        if (className.contains("QLKH")) {
            return "khách hàng";
        }
        if (className.contains("QLNL")) {
            return "nguyên liệu";
        }
        if (className.contains("QLNV")) {
            return "nhân viên";
        }
        if (className.contains("QLSP")) {
            return "sản phẩm";
        }
        return "đối tượng";
    }
}
