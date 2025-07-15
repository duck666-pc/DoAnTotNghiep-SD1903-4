package view;

import controller.QLNLDAO;
import java.util.List;
import model.NguyenVatLieu;

public final class QLNLPanel extends BasePanel<NguyenVatLieu> {

    private final QLNLDAO qlnl = new QLNLDAO();

    public QLNLPanel() {
        initComponents();
        this.baseJTable = jTable;
        this.baseTxtTimKiem = txtTimKiem;
        super.initTable();
        super.fillTable();
        super.addTableSelectionListener();
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"ID", "Tên nguyên liệu", "Đơn vị", "Số lượng có sẵn", "Mức cần đặt thêm"};
    }

    @Override
    protected void setFormFromRow(int row) {
        txtID.setText(getValue(row, 0));
        txtTen.setText(getValue(row, 1));
        txtDonVi.setText(getValue(row, 2));
        txtSoLuong.setText(getValue(row, 3));
        txtMucCanThem.setText(getValue(row, 4));
    }

    @Override
    protected boolean validateForm() {
        if (txtID.getText().trim().isEmpty()
                || txtTen.getText().trim().isEmpty()
                || txtDonVi.getText().trim().isEmpty()
                || txtSoLuong.getText().trim().isEmpty()
                || txtMucCanThem.getText().trim().isEmpty()) {
            showMessage("Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        try {
            int SoLuong = Integer.parseInt(txtSoLuong.getText().trim());
            int MucCanThem = Integer.parseInt(txtMucCanThem.getText().trim());
            if (MucCanThem > SoLuong) {
                showMessage("Số lượng phải nhiều hơn mức cần thêm, nếu không phải mua thêm nguyên liệu!");
                return false;
            }
        } catch (NumberFormatException e) {
            showMessage("Số lượng và mức cần thêm phải là số!");
            return false;
        }
        return true;
    }

    @Override
    protected NguyenVatLieu getEntityFromForm() {
        return new NguyenVatLieu(
                txtID.getText().trim(),
                txtTen.getText().trim(),
                txtDonVi.getText().trim(),
                Integer.parseInt(txtSoLuong.getText().trim()),
                Integer.parseInt(txtMucCanThem.getText().trim())
        );
    }

    @Override
    protected void clearForm() {
        txtID.setText("");
        txtTen.setText("");
        txtDonVi.setText("");
        txtSoLuong.setText("");
        txtMucCanThem.setText("");
        currentRow = -1;
    }

    @Override
    protected List<NguyenVatLieu> getAllEntities() throws Exception {
        return qlnl.getAll();
    }

    @Override
    protected String getEntityId(NguyenVatLieu entity) {
        return entity.getId();
    }

    @Override
    protected void addEntityToTable(NguyenVatLieu entity) {
        tableModel.addRow(new Object[]{
            entity.getId(),
            entity.getTen(),
            entity.getDonVi(),
            entity.getSoLuong(),
            entity.getMucCanDatThem()
        });
    }

    @Override
    protected int addEntity(NguyenVatLieu entity) throws Exception {
        return qlnl.add(entity);
    }

    @Override
    protected int deleteEntity(String id) throws Exception {
        return qlnl.delete(id);
    }

    @Override
    protected int updateEntity(NguyenVatLieu entity, String oldId) throws Exception {
        return qlnl.edit(entity, oldId);
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
        handleAddAction();
    }

    private void jbtXoaActionPerformed(java.awt.event.ActionEvent evt) {
        handleDeleteAction();
    }

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void jbtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {
        handleSearchAction();
    }

    private void jbtSuaActionPerformed(java.awt.event.ActionEvent evt) {
        handleUpdateAction();
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
