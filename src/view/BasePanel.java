/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;


public abstract class BasePanel<T> extends javax.swing.JPanel {

    protected DefaultTableModel tableModel;
    protected int currentRow = -1;
    protected JTable baseJTable;
    protected JButton jbtThem, jbtSua, jbtXoa, jbtTimKiem, jbtLamMoi;
    protected JTextField baseTxtTimKiem;

    protected void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    protected void showError(Exception ex, String prefix) {
        showMessage(prefix + ex.getMessage());
    }

    protected String getValue(int row, int col) {
        Object val = baseJTable.getValueAt(row, col);
        return val == null ? "" : val.toString();
    }

    protected String getEntityIdFromRow(int row) {
        return getValue(row, 0);
    }

    public BasePanel() {
    }

    public final void initTable() {
        tableModel = new DefaultTableModel(getColumnNames(), 0);
        if (baseJTable == null) {
            throw new IllegalStateException("jTable is null. Ensure UI is initialized before calling initTable()");
        }
        baseJTable.setModel(tableModel);
    }

    protected final void addTableSelectionListener() {
        baseJTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && baseJTable.getSelectedRow() >= 0) {
                currentRow = baseJTable.getSelectedRow();
                setFormFromRow(currentRow);
            }
        });
    }

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
        tableModel.setRowCount(0);
        if (id.isEmpty()) {
            fillTable();
            return;
        }
        boolean found = false;
        try {
            for (T entity : getAllEntities()) {
                if (getEntityId(entity).equalsIgnoreCase(id)) {
                    addEntityToTable(entity);
                    found = true;
                    int row = tableModel.getRowCount() - 1;
                    baseJTable.setRowSelectionInterval(row, row);
                    baseJTable.scrollRectToVisible(baseJTable.getCellRect(row, 0, true));
                    break;
                }
            }
            if (!found) {
                showMessage("Không tìm thấy với ID: " + id);
            }
        } catch (Exception ex) {
            showError(ex, "Lỗi khi tải dữ liệu: ");
        }
    }

    protected void enableAutoFilter() {
        if (baseJTable == null || baseTxtTimKiem == null) {
            throw new IllegalStateException("baseJTable hoặc baseTxtTimKiem chưa được gán.");
        }

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(baseJTable.getModel());
        baseJTable.setRowSorter(sorter);

        baseTxtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String keyword = baseTxtTimKiem.getText().trim();
                if (keyword.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter();
            }
        });
    }

    protected void handleAddAction() {
        if (!validateForm()) {
            return;
        }
        try {
            int result = addEntity(getEntityFromForm());
            showMessage(result == 1 ? "Thêm dữ liệu thành công!" : "Thêm dữ liệu thất bại!");
            fillTable();
            clearForm();
        } catch (Exception ex) {
            showError(ex, "Lỗi: ");
        }
    }

    protected void handleDeleteAction() {
        if (currentRow < 0) {
            showMessage("Vui lòng chọn một dòng để xóa!");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa dữ liệu này?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            int result = deleteEntity(getEntityIdFromRow(currentRow));
            showMessage(result == 1 ? "Xóa dữ liệu thành công!" : "Xóa dữ liệu thất bại!");
            fillTable();
            clearForm();
            currentRow = -1;
        } catch (Exception ex) {
            showError(ex, "Lỗi: ");
        }
    }

    protected void handleUpdateAction() {
        if (currentRow < 0) {
            showMessage("Vui lòng chọn một dòng để sửa!");
            return;
        }
        if (!validateForm()) {
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn sửa?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            int result = updateEntity(getEntityFromForm(), getEntityIdFromRow(currentRow));
            fillTable();
            showMessage(result == 1 ? "Cập nhật thành công!" : "Cập nhật thất bại!");
        } catch (Exception ex) {
            showError(ex, "Lỗi: ");
        }
        clearForm();
    }

    protected void handleSearchAction() {
        searchById(baseTxtTimKiem.getText().trim());
    }

    protected void handleRefreshAction() {
        clearForm();
    }

    protected abstract String[] getColumnNames();

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
}
