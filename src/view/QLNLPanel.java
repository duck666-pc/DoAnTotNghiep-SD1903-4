package view;

import controller.QLNLDAO;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import model.NguyenVatLieu;

public final class QLNLPanel extends javax.swing.JPanel { // Đổi từ JFrame -> JPanel

    DefaultTableModel tableModel;
    QLNLDAO qlnl = new QLNLDAO();
    int currentRow = -1;

    public QLNLPanel() {
        initComponents();
        initTable();
        fillTable();
        addTableSelectionListener();
    }

    private void addTableSelectionListener() {
        jTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTable.getSelectedRow() >= 0) {
                currentRow = jTable.getSelectedRow();
                setFormFromRow(currentRow);
            }
        });
    }

    private void setFormFromRow(int row) {
        txtID.setText(getValue(row, 0));
        txtTen.setText(getValue(row, 1));
        txtDonVi.setText(getValue(row, 2));
        txtSoLuong.setText(getValue(row, 3));
        txtMucCanThem.setText(getValue(row, 4));
    }

    private String getValue(int row, int col) {
        Object val = jTable.getValueAt(row, col);
        return val == null ? "" : val.toString();
    }

    public void initTable() {
        String[] cols = {"ID", "Tên nguyên liệu", "Đơn vị", "Số lượng có sẵn", "Mức cần đặt thêm"};
        tableModel = new DefaultTableModel(cols, 0);
        jTable.setModel(tableModel);
    }

    public void fillTable() {
        tableModel.setRowCount(0);
        try {
            for (NguyenVatLieu nl : qlnl.getAll()) {
                tableModel.addRow(new Object[]{
                    nl.getId(),
                    nl.getTen(),
                    nl.getDonVi(),
                    nl.getSoLuong(),
                    nl.getMucCanDatThem()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
        }
    }

    public boolean validateForm() {
        if (txtID.getText().trim().isEmpty()
                || txtTen.getText().trim().isEmpty()
                || txtDonVi.getText().trim().isEmpty()
                || txtSoLuong.getText().trim().isEmpty()
                || txtMucCanThem.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        // Kiểm tra số nếu cần
        try {
            Integer.parseInt(txtSoLuong.getText().trim());
            Integer.parseInt(txtMucCanThem.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng và mức cần thêm phải là số!");
            return false;
        }
        return true;
    }

    private NguyenVatLieu getNguyenLieuFromForm() {
        return new NguyenVatLieu(
            txtID.getText().trim(),
            txtTen.getText().trim(),
            txtDonVi.getText().trim(),
            Integer.parseInt(txtSoLuong.getText().trim()),
            Integer.parseInt(txtMucCanThem.getText().trim())
        );
    }

    private void clearForm() {
        txtID.setText("");
        txtTen.setText("");
        txtDonVi.setText("");
        txtSoLuong.setText("");
        txtMucCanThem.setText("");
        currentRow = -1;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        txtID = new javax.swing.JTextField();
        txtDonVi = new javax.swing.JTextField();
        txtSoLuong = new javax.swing.JTextField();
        txtMucCanThem = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jbtThem = new javax.swing.JButton();
        jbtSua = new javax.swing.JButton();
        jbtXoa = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        jbtTimKiem = new javax.swing.JButton();
        jbtLamMoi = new javax.swing.JButton();

        jLabel1.setText("ID:");

        jLabel2.setText("Tên:");

        jLabel3.setText("Mức cần đặt thêm:");

        jLabel5.setText("Số lượng có sẵn:");

        jLabel6.setText("Đơn vị:");

        txtTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenActionPerformed(evt);
            }
        });

        txtID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDActionPerformed(evt);
            }
        });

        txtDonVi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDonViActionPerformed(evt);
            }
        });

        txtSoLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoLuongActionPerformed(evt);
            }
        });

        txtMucCanThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMucCanThemActionPerformed(evt);
            }
        });

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Tên", "Đơn vị", "Số lượng có sẵn", "Mức cần đặt thêm"
            }
        ));
        jScrollPane1.setViewportView(jTable);

        jbtThem.setText("Thêm");
        jbtThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtThemActionPerformed(evt);
            }
        });

        jbtSua.setText("Sửa");
        jbtSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSuaActionPerformed(evt);
            }
        });

        jbtXoa.setText("Xóa");
        jbtXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtXoaActionPerformed(evt);
            }
        });

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        jbtTimKiem.setText("Tìm kiếm bằng ID");
        jbtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtTimKiemActionPerformed(evt);
            }
        });

        jbtLamMoi.setText("Làm mới");
        jbtLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtLamMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel3))
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMucCanThem)
                            .addComponent(txtSoLuong)
                            .addComponent(txtTen)
                            .addComponent(txtID)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtDonVi, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbtXoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                            .addComponent(jbtSua, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtThem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtLamMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtTimKiem)
                        .addGap(18, 18, 18)
                        .addComponent(jbtTimKiem))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtTimKiem))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtDonVi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtMucCanThem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jbtThem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtSua)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtXoa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtLamMoi))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbtLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLamMoiActionPerformed
        clearForm();
    }//GEN-LAST:event_jbtLamMoiActionPerformed

    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtDonViActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtSoLuongActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtMucCanThemActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtTenActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void jbtThemActionPerformed(java.awt.event.ActionEvent evt) {
        if (!validateForm()) return;
        try {
            NguyenVatLieu nl = getNguyenLieuFromForm();
            int result = qlnl.add(nl);
            JOptionPane.showMessageDialog(this, result == 1 ? "Thêm nguyên liệu thành công!" : "Thêm thất bại!");
            fillTable();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void jbtXoaActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nguyên liệu cần xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nguyên liệu này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            String id = txtID.getText();
            int result = qlnl.delete(id);
            JOptionPane.showMessageDialog(this, result == 1 ? "Xóa nguyên liệu thành công!" : "Xóa thất bại!");
            fillTable();
            clearForm();
            currentRow = -1;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void jbtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {
        String id = txtTimKiem.getText().trim();
        if (!id.isEmpty()) {
            tableModel.setRowCount(0);
            boolean found = false;
            try {
                for (NguyenVatLieu nl : qlnl.getAll()) {
                    if (nl.getId().equalsIgnoreCase(id)) {
                        tableModel.addRow(new Object[]{
                            nl.getId(),
                            nl.getTen(),
                            nl.getDonVi(),
                            nl.getSoLuong(),
                            nl.getMucCanDatThem()
                        });
                        found = true;
                        int row = tableModel.getRowCount() - 1;
                        jTable.setRowSelectionInterval(row, row);
                        jTable.scrollRectToVisible(jTable.getCellRect(row, 0, true));
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy nguyên liệu có ID: " + id);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
            }
        } else {
            fillTable();
        }

    }

    private void jbtSuaActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nguyên liệu cần sửa!");
            return;
        }
        if (!validateForm()) return;
        try {
            String oldId = tableModel.getValueAt(currentRow, 0).toString();
            int result = qlnl.edit(getNguyenLieuFromForm(), oldId);
            fillTable();
            JOptionPane.showMessageDialog(this, result == 1 ? "Cập nhật thành công!" : "Cập nhật thất bại!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
        clearForm();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JButton jbtLamMoi;
    private javax.swing.JButton jbtSua;
    private javax.swing.JButton jbtThem;
    private javax.swing.JButton jbtTimKiem;
    private javax.swing.JButton jbtXoa;
    private javax.swing.JTextField txtDonVi;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtMucCanThem;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
