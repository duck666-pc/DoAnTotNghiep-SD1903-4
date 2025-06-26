package view;

import controller.QLNLDAO;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
        setupTableSelection();
    }

    public void initTable() {
        String[] cols = new String[]{"ID", "Tên", "Đơn vị", "Số lượng có sẵn", "Mức Cần Đặt Thêm"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(cols);
        jTable.setModel(tableModel);
    }

    private void fillTable() {
        tableModel.setRowCount(0);
        try {
            List<NguyenVatLieu> listNL = qlnl.getAll();
            for (NguyenVatLieu nl : listNL) {
                Object[] row = new Object[]{
                    nl.getId(),
                    nl.getTen(),
                    nl.getDonVi(),
                    nl.getSoLuong(),
                    nl.getMucCanDatThem()
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
                    NguyenVatLieu nl = getSelectedNguyenVatLieu();
                    if (nl != null) {
                        fillForm(nl);
                    }
                }
            }
        });
    }

    private NguyenVatLieu getSelectedNguyenVatLieu() {
        if (currentRow >= 0) {
            String id = tableModel.getValueAt(currentRow, 0).toString();
            try {
                List<NguyenVatLieu> listNL = qlnl.getAll();
                for (NguyenVatLieu nl : listNL) {
                    if (nl.getId().equals(id)) {
                        return nl;
                    }
                }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
            }
        }
        return null;
    }

    private void fillForm(NguyenVatLieu nl) {
        txtID.setText(nl.getId());
        txtTen.setText(nl.getTen());
        txtDonVi.setText(nl.getDonVi());
        txtSoLuongCoSan.setText(String.valueOf(nl.getSoLuong()));
        txtMucCanDatThem.setText(String.valueOf(nl.getMucCanDatThem()));
    }

    private NguyenVatLieu getFormData() {
        String id = txtID.getText().trim();
        String ten = txtTen.getText().trim();
        String donVi = txtDonVi.getText().trim();
        int soLuong = Integer.parseInt(txtSoLuongCoSan.getText().trim());
        int mucCanDatThem = Integer.parseInt(txtMucCanDatThem.getText().trim());
        return new NguyenVatLieu(id, ten, donVi, soLuong, mucCanDatThem);
    }

    public boolean validateForm() {
        if (txtID.getText().isEmpty()
                || txtTen.getText().isEmpty()
                || txtDonVi.getText().isEmpty()
                || txtSoLuongCoSan.getText().isEmpty()
                || txtMucCanDatThem.getText().isEmpty()) {
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtID.setText("");
        txtTen.setText("");
        txtDonVi.setText("");
        txtSoLuongCoSan.setText("");
        txtMucCanDatThem.setText("");
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
        txtSoLuongCoSan = new javax.swing.JTextField();
        txtMucCanDatThem = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jbtThem = new javax.swing.JButton();
        jbtSua = new javax.swing.JButton();
        jbtXoa = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        jbtTimKiem = new javax.swing.JButton();

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

        txtSoLuongCoSan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoLuongCoSanActionPerformed(evt);
            }
        });

        txtMucCanDatThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMucCanDatThemActionPerformed(evt);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
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
                            .addComponent(txtMucCanDatThem)
                            .addComponent(txtSoLuongCoSan)
                            .addComponent(txtTen)
                            .addComponent(txtID)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtDonVi, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jbtXoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                        .addComponent(jbtSua, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtThem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, Short.MAX_VALUE)
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
                            .addComponent(txtSoLuongCoSan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtMucCanDatThem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jbtThem)
                        .addGap(18, 18, 18)
                        .addComponent(jbtSua)
                        .addGap(18, 18, 18)
                        .addComponent(jbtXoa))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtDonViActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtSoLuongCoSanActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtMucCanDatThemActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtTenActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void jbtThemActionPerformed(java.awt.event.ActionEvent evt) {
        if (!validateForm()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        NguyenVatLieu nl = getFormData();
        if (nl == null) {
            return;
        }
        try {
            int result = qlnl.addSP(nl);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Thêm nguyên vật liệu thành công!");
                fillTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + ex.getMessage());
        }
    }

    private void jbtXoaActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nguyên vật liệu để xóa!");
            return;
        }
        String id = tableModel.getValueAt(currentRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int result = qlnl.deleteSP(id);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    fillTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage());
            }
        }
    }

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void jbtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {
        String id = txtTimKiem.getText().trim();
        if (!id.isEmpty()) {
            tableModel.setRowCount(0);
            try {
                List<NguyenVatLieu> listNL = qlnl.getAll();
                for (NguyenVatLieu nl : listNL) {
                    if (nl.getId().equalsIgnoreCase(id)) {
                        tableModel.addRow(qlnl.getRow(nl));
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
    }

    private void jbtSuaActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nguyên vật liệu để sửa!");
            return;
        }
        if (!validateForm()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        NguyenVatLieu nl = getFormData();
        if (nl == null) {
            return;
        }
        String oldId = tableModel.getValueAt(currentRow, 0).toString();
        try {
            int result = qlnl.editSP(nl, oldId);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Sửa nguyên vật liệu thành công!");
                fillTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Sửa thất bại!");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa: " + ex.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JButton jbtSua;
    private javax.swing.JButton jbtThem;
    private javax.swing.JButton jbtTimKiem;
    private javax.swing.JButton jbtXoa;
    private javax.swing.JTextField txtDonVi;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtMucCanDatThem;
    private javax.swing.JTextField txtSoLuongCoSan;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
