package view;

import controller.QLSPDAO;
import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.SanPham;

public final class QLSPPanel extends javax.swing.JPanel { // Đổi từ JFrame -> JPanel

    DefaultTableModel tableModel;
    QLSPDAO qlsp = new QLSPDAO();
    int currentRow = -1;

    public QLSPPanel() {
        initComponents();
        initTable();
        fillTable();
        setupTableSelection();
    }

    public void initTable() {
        String[] cols = new String[]{"ID", "Tên", "Mô tả", "Giá", "Loại Sản Phẩm"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(cols);
        jTable.setModel(tableModel);
    }

    private void fillTable() {
        tableModel.setRowCount(0);
        try {
            List<SanPham> listSP = qlsp.getAll();
            for (SanPham sp : listSP) {
                Object gia = sp.getGia();
                if (gia == null) {
                    gia = 0.0f;
                }
                if (!(gia instanceof Number)) {
                    try {
                        gia = Float.valueOf(gia.toString());
                    } catch (Exception e) {
                        gia = 0.0f;
                    }
                }
                Object[] row = new Object[]{
                    sp.getId(),
                    sp.getTen(),
                    sp.getMoTa(),
                    gia,
                    sp.getLoaiSanPham()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
        }
    }

    private void setupTableSelection() {
        jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && jTable.getSelectedRow() != -1) {
                    currentRow = jTable.getSelectedRow();
                    SanPham sp = getSelectedSanPham();
                    if (sp != null) {
                        fillForm(sp);
                    }
                }
            }
        });
    }

    private SanPham getSelectedSanPham() {
        if (currentRow >= 0) {
            String id = tableModel.getValueAt(currentRow, 0).toString();
            try {
                List<SanPham> listSP = qlsp.getAll();
                for (SanPham sp : listSP) {
                    if (sp.getId().equals(id)) {
                        return sp;
                    }
                }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
            }
        }
        return null;
    }

    private void fillForm(SanPham sp) {
        txtID.setText(sp.getId());
        txtTen.setText(sp.getTen());
        txtMoTa.setText(sp.getMoTa());
        txtGia.setText(String.valueOf(sp.getGia()));
        txtLoaiSanPham.setText(sp.getLoaiSanPham());
    }

    public boolean validateForm() {
        if (txtID.getText().isEmpty()
                || txtTen.getText().isEmpty()
                || txtGia.getText().isEmpty()
                || txtLoaiSanPham.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }

        try {
            Float.valueOf(txtGia.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá phải là số!");
            return false;
        }

        return true;
    }

    private void clearForm() {
        txtID.setText("");
        txtTen.setText("");
        txtMoTa.setText("");
        txtGia.setText("");
        txtLoaiSanPham.setText("");
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
        txtMoTa = new javax.swing.JTextField();
        txtGia = new javax.swing.JTextField();
        txtLoaiSanPham = new javax.swing.JTextField();
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

        jLabel3.setText("Loại sản phẩm:");

        jLabel5.setText("Giá:");

        jLabel6.setText("Mô tả:");

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

        txtMoTa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMoTaActionPerformed(evt);
            }
        });

        txtGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGiaActionPerformed(evt);
            }
        });

        txtLoaiSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoaiSanPhamActionPerformed(evt);
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
                "ID", "Tên", "Mô tả", "Giá", "Loại Sản Phẩm"
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
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtLoaiSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(txtGia)
                            .addComponent(txtMoTa)
                            .addComponent(txtTen)
                            .addComponent(txtID)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jbtXoa, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                        .addComponent(jbtLamMoi, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                        .addComponent(jbtSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtTimKiem)
                        .addGap(18, 18, 18)
                        .addComponent(jbtTimKiem))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE))
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
                            .addComponent(txtMoTa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtThem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtSua)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtXoa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtLamMoi))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbtLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLamMoiActionPerformed
        clearForm();
    }//GEN-LAST:event_jbtLamMoiActionPerformed

    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtMoTaActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtGiaActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtLoaiSanPhamActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtTenActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void jbtThemActionPerformed(java.awt.event.ActionEvent evt) {
        if (!validateForm()) {
            return;
        }

        try {
            SanPham sp = new SanPham(
                    txtID.getText(),
                    txtTen.getText(),
                    txtMoTa.getText(),
                    Float.parseFloat(txtGia.getText()),
                    txtLoaiSanPham.getText()
            );

            int result = qlsp.addSP(sp);
            if (result == 1) {
                fillTable();
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại!");
            }
        } catch (SQLException | ClassNotFoundException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }

        clearForm();
    }

    private void jbtXoaActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void jbtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {
        String id = txtTimKiem.getText().trim();
        if (!id.isEmpty()) {
            tableModel.setRowCount(0);
            try {
                List<SanPham> listSP = qlsp.getAll();
                for (SanPham sp : listSP) {
                    if (sp.getId().equalsIgnoreCase(id)) {
                        tableModel.addRow(qlsp.getRow(sp));
                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            if (tableModel.getValueAt(i, 0).toString().equalsIgnoreCase(id)) {
                                jTable.setRowSelectionInterval(i, i);
                                jTable.scrollRectToVisible(jTable.getCellRect(i, 0, true));
                                break;
                            }
                        }
                        break;
                    }
                }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
            }
        } else {
            fillTable();
        }

        clearForm();
    }

    private void jbtSuaActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!");
            return;
        }
        if (!validateForm()) {
            return;
        }

        try {
            SanPham sp = new SanPham(
                    txtID.getText(),
                    txtTen.getText(),
                    txtMoTa.getText(),
                    Float.parseFloat(txtGia.getText()),
                    txtLoaiSanPham.getText()
            );

            String oldId = tableModel.getValueAt(currentRow, 0).toString();

            int result = qlsp.editSP(sp, oldId);
            if (result == 1) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                fillTable();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (SQLException | ClassNotFoundException | NumberFormatException ex) {
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
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtLoaiSanPham;
    private javax.swing.JTextField txtMoTa;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
