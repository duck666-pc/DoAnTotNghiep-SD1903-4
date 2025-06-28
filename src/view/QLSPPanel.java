package view;

import controller.QLSPDAO;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
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
        addTableSelectionListener();
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
                tableModel.addRow(qlsp.getRow(sp));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
        }
    }
    
    private void addTableSelectionListener() {
        jTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting() && jTable.getSelectedRow() >= 0) {
                currentRow = jTable.getSelectedRow();
                SanPham sp = getSelectedSanPham(currentRow);
                if (sp != null) {
                    fillForm(sp);
                }
            }
        });
    }
    
    private SanPham getSelectedSanPham(int row) {
        if (row >= 0) {
            SanPham sp = new SanPham();
            sp.setId(tableModel.getValueAt(row, 0).toString());
            sp.setTen(tableModel.getValueAt(row, 1).toString());
            sp.setMoTa(tableModel.getValueAt(row, 2).toString());
            sp.setGia(Float.parseFloat(tableModel.getValueAt(row, 3).toString()));
            sp.setLoaiSanPham(tableModel.getValueAt(row, 4).toString());
            return sp;
        }
        return null;
    }
    
    private void fillForm(SanPham sp) {
        txtID.setText(sp.getId());
        txtTen.setText(sp.getTen());
        txtMoTa.setText(sp.getMoTa());
        txtGia.setText(String.valueOf(sp.getGia()));
        jcbLoaiSanPham.setSelectedItem(sp.getLoaiSanPham());
    }
    
    public boolean validateForm() {
        if (txtID.getText().isEmpty()
                || txtTen.getText().isEmpty()
                || txtGia.getText().isEmpty()
                || jcbLoaiSanPham.getSelectedItem() == null) {
            
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        
        try {
            float gia = Float.parseFloat(txtGia.getText());
            if (gia <= 0) {
                JOptionPane.showMessageDialog(this, "Giá phải lớn hơn 0!");
                return false;
            }
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
        jcbLoaiSanPham.setSelectedIndex(-1);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jbtThem = new javax.swing.JButton();
        jbtSua = new javax.swing.JButton();
        jbtXoa = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        jbtTimKiem = new javax.swing.JButton();
        jbtLamMoi = new javax.swing.JButton();
        jcbLoaiSanPham = new javax.swing.JComboBox<>();

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

        jcbLoaiSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "001-Đồ uống", "002-Đồ ăn", "003-Tráng miệng", "004-Kem", "005-Đồ ăn nhanh", "006-Salad", "007-Nước ép", "008-Bánh mì", "009-Mì xào", "010-Món chính", "Khác" }));

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGia, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(txtMoTa)
                            .addComponent(txtTen)
                            .addComponent(txtID)
                            .addComponent(jcbLoaiSanPham, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                            .addComponent(jcbLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            SanPham sp = new SanPham();
            sp.setId(txtID.getText());
            sp.setTen(txtTen.getText());
            sp.setMoTa(txtMoTa.getText());
            sp.setGia(Float.parseFloat(txtGia.getText()));
            sp.setLoaiSanPham(jcbLoaiSanPham.getSelectedItem().toString());
            
            int result = qlsp.addSP(sp);
            if (result == 1) {
                fillTable();
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại!");
            }
        } catch (SQLException | ClassNotFoundException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
        
        clearForm();
    }
    
    private void jbtXoaActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!");
            return;
        }
        
        String id = tableModel.getValueAt(currentRow, 0).toString();
        int option = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa sản phẩm này?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            try {
                int result = qlsp.deleteSP(id);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                    fillTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại!");
                }
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
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
            boolean found = false;
            try {
                List<SanPham> listSP = qlsp.getAll();
                for (SanPham sp : listSP) {
                    if (sp.getId().equalsIgnoreCase(id)) {
                        tableModel.addRow(qlsp.getRow(sp));
                        found = true;
                        // Chọn dòng vừa thêm
                        int row = tableModel.getRowCount() - 1;
                        jTable.setRowSelectionInterval(row, row);
                        jTable.scrollRectToVisible(jTable.getCellRect(row, 0, true));
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm có ID: " + id);
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
            SanPham sp = new SanPham();
            sp.setId(txtID.getText());
            sp.setTen(txtTen.getText());
            sp.setMoTa(txtMoTa.getText());
            sp.setGia(Float.parseFloat(txtGia.getText()));
            sp.setLoaiSanPham(jcbLoaiSanPham.getSelectedItem().toString());
            
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
    private javax.swing.JComboBox<String> jcbLoaiSanPham;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtMoTa;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
