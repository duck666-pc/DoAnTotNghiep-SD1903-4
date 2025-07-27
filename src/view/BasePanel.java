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
import java.util.ArrayList;
import java.util.Collections;

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

    // Changed from searchById to searchByName
    public void searchByName(String name) {
        tableModel.setRowCount(0);
        if (name.isEmpty()) {
            fillTable();
            return;
        }
        boolean found = false;
        try {
            for (T entity : getAllEntities()) {
                // Use case-insensitive contains search for partial name matching
                if (getEntityName(entity).toLowerCase().contains(name.toLowerCase())) {
                    addEntityToTable(entity);
                    found = true;
                }
            }
            if (!found) {
                showMessage("Không tìm thấy với tên: " + name);
            } else {
                // If found, select the first matching row
                if (tableModel.getRowCount() > 0) {
                    baseJTable.setRowSelectionInterval(0, 0);
                    baseJTable.scrollRectToVisible(baseJTable.getCellRect(0, 0, true));
                }
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

    protected String generateNextId() {
        try {
            List<T> entities = getAllEntities();
            if (entities.isEmpty()) {
                return getIdPrefix() + "001";
            }

            List<String> ids = new ArrayList<>();
            for (T entity : entities) {
                ids.add(getEntityId(entity));
            }

            Collections.sort(ids, (String id1, String id2) -> extractLastThreeDigits(id1) - extractLastThreeDigits(id2));

            String highestId = ids.get(ids.size() - 1);
            int highestNumber = extractLastThreeDigits(highestId);
            int nextNumber = highestNumber + 1;

            return getIdPrefix() + String.format("%03d", nextNumber);
        } catch (Exception ex) {
            showError(ex, "Lỗi khi tạo ID mới: ");
            return getIdPrefix() + "001";
        }
    }

    protected int extractLastThreeDigits(String id) {
        if (id == null || id.length() < 3) {
            return 0;
        }
        try {
            String lastThree = id.substring(id.length() - 3);
            return Integer.parseInt(lastThree);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    protected void updateIdsAfterDeletion(String deletedId) {
        try {
            int deletedNumber = extractLastThreeDigits(deletedId);
            List<T> entities = getAllEntities();

            for (T entity : entities) {
                String currentId = getEntityId(entity);
                int currentNumber = extractLastThreeDigits(currentId);

                if (currentNumber > deletedNumber) {
                    int newNumber = currentNumber - 1;
                    String newId = getIdPrefix() + String.format("%03d", newNumber);
                    updateEntityId(entity, newId);
                }
            }
        } catch (Exception ex) {
            showError(ex, "Lỗi khi cập nhật ID sau khi xóa: ");
        }
    }

    protected void handleAddAction() {
        if (!validateForm()) {
            return;
        }
        try {
            T entity = getEntityFromForm();
            String newId = generateNextId();
            setEntityId(entity, newId);

            int result = addEntity(entity);
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
            String deletedId = getEntityIdFromRow(currentRow);
            int result = deleteEntity(deletedId);

            if (result == 1) {
                updateIdsAfterDeletion(deletedId);
                showMessage("Xóa dữ liệu thành công!");
            } else {
                showMessage("Xóa dữ liệu thất bại!");
            }

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
            T entity = getEntityFromForm();
            String currentId = getEntityIdFromRow(currentRow);
            setEntityId(entity, currentId); // Ensure the entity has the correct ID for updating

            int result = updateEntity(entity, currentId);
            fillTable();
            showMessage(result == 1 ? "Cập nhật thành công!" : "Cập nhật thất bại!");
        } catch (Exception ex) {
            showError(ex, "Lỗi: ");
        }
        clearForm();
    }

    // Changed method name to reflect searching by name
    protected void handleSearchAction() {
        searchByName(baseTxtTimKiem.getText().trim());
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

    // New abstract method to get entity name for searching
    protected abstract String getEntityName(T entity);

    protected abstract void addEntityToTable(T entity);

    protected abstract int addEntity(T entity) throws Exception;

    protected abstract int deleteEntity(String id) throws Exception;

    protected abstract int updateEntity(T entity, String oldId) throws Exception;

    protected abstract String getIdPrefix();

    protected abstract void setEntityId(T entity, String id);

    protected abstract void updateEntityId(T entity, String newId) throws Exception;

}
