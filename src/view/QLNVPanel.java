/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import controller.QLNVDAO;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import model.NhanVien;

public final class QLNVPanel extends BasePanel<NhanVien> {
    private final QLNVDAO qlnv = new QLNVDAO();

    public QLNVPanel() {
        initComponents();
        this.baseJTable = jTable;
        this.baseTxtTimKiem = txtTimKiem;
        super.initTable();
        super.fillTable();
        super.addTableSelectionListener();
        super.enableAutoFilter();
    }

    private boolean isEmpty(JTextField... fields) {
        for (JTextField f : fields) {
            if (f.getText().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void clearFields(JTextField... fields) {
        for (JTextField f : fields) {
            f.setText("");
        }
    }

    private void clearDateFields() {
        jcbNamSinh.setText("");
        jcbThangSinh.setSelectedIndex(0);
        jcbNgaySinh.setSelectedIndex(0);
    }

    private boolean isValidBirthday(String y, String m, String d) {
        try {
            LocalDate date = LocalDate.of(Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d));
            int age = Period.between(date, LocalDate.now()).getYears();
            return age >= 18 && age <= 60 && !date.isAfter(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"ID", "Mật Khẩu", "Tên", "Ngày sinh", "Giới tính", "Email", "Chức vụ"};
    }

    @Override
    protected void setFormFromRow(int row) {
        txtMatKhau.setText(getValue(row, 1));
        txtTen.setText(getValue(row, 2));
        txtEmail.setText(getValue(row, 5));
        jcbGioiTinh.setSelectedItem(getValue(row, 4));
        jcbChucVu.setSelectedItem(getValue(row, 6));
        String[] dateParts = getValue(row, 3).split("-");
        if (dateParts.length == 3) {
            jcbNamSinh.setText(dateParts[0]);
            jcbThangSinh.setSelectedItem(dateParts[1]);
            jcbNgaySinh.setSelectedItem(dateParts[2]);
        } else {
            clearDateFields();
        }
    }

    @Override
    protected boolean validateForm() {
        if (isEmpty(txtTen, txtMatKhau, txtEmail, jcbNamSinh)) {
            showMessage("Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        if (!isValidBirthday(jcbNamSinh.getText(), (String) jcbThangSinh.getSelectedItem(), (String) jcbNgaySinh.getSelectedItem())) {
            showMessage("Ngày sinh không hợp lệ hoặc tuổi ngoài 18-60!");
            return false;
        }
        return true;
    }

    @Override
    protected NhanVien getEntityFromForm() {
        NhanVien nv = new NhanVien();
        nv.setMatKhau(txtMatKhau.getText().trim());
        nv.setTenDayDu(txtTen.getText().trim());
        nv.setEmail(txtEmail.getText().trim());
        nv.setGioiTinh((String) jcbGioiTinh.getSelectedItem());
        nv.setChucVu((String) jcbChucVu.getSelectedItem());
        String ngaySinh = String.format("%s-%s-%s", jcbNamSinh.getText().trim(), jcbThangSinh.getSelectedItem(), jcbNgaySinh.getSelectedItem());
        nv.setNgaySinh(java.sql.Date.valueOf(ngaySinh));
        return nv;
    }

    @Override
    protected void clearForm() {
        clearFields(txtTen, txtMatKhau, txtEmail, jcbNamSinh);
        clearDateFields();
        currentRow = -1;
    }

    @Override
    protected List<NhanVien> getAllEntities() throws Exception {
        return qlnv.getAll();
    }

    @Override
    protected String getEntityId(NhanVien entity) {
        return entity.getId();
    }

    // New method implementation for searching by name
    @Override
    protected String getEntityName(NhanVien entity) {
        return entity.getTenDayDu();
    }

    @Override
    protected void addEntityToTable(NhanVien entity) {
        tableModel.addRow(qlnv.getRow(entity));
    }

    @Override
    protected int addEntity(NhanVien entity) throws Exception {
        return qlnv.add(entity);
    }

    @Override
    protected int deleteEntity(String id) throws Exception {
        return qlnv.delete(id);
    }

    @Override
    protected int updateEntity(NhanVien entity, String oldId) throws Exception {
        return qlnv.edit(entity, oldId);
    }

    @Override
    protected String getIdPrefix() {
        return "ND"; 
    }

    @Override
    protected void setEntityId(NhanVien entity, String id) {
        entity.setId(id);
    }

    @Override
    protected void updateEntityId(NhanVien entity, String newId) throws Exception {
        entity.setId(newId);
        qlnv.edit(entity, entity.getId()); // Update the entity with new ID in database
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtMatKhau = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jbtThem = new javax.swing.JButton();
        jbtSua = new javax.swing.JButton();
        jbtXoa = new javax.swing.JButton();
        jcbGioiTinh = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        jcbNgaySinh = new javax.swing.JComboBox<>();
        jcbThangSinh = new javax.swing.JComboBox<>();
        jcbNamSinh = new javax.swing.JTextField();
        jbtTimKiem = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        jcbChucVu = new javax.swing.JComboBox<>();
        jbtLamMoi = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Tên:");

        jLabel3.setText("Email:");

        jLabel4.setText("Chức vụ:");

        jLabel5.setText("Giới tính:");

        jLabel6.setText("Ngày sinh:");

        txtMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMatKhauActionPerformed(evt);
            }
        });

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Mật khẩu", "Tên", "Ngày sinh", "Giới tính", "Email", "Chức vụ", "Trạng thái"
            }
        ));
        jTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(jTable);

        jbtThem.setBackground(new java.awt.Color(41, 62, 80));
        jbtThem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtThem.setForeground(new java.awt.Color(255, 255, 255));
        jbtThem.setText("Thêm");
        jbtThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtThemActionPerformed(evt);
            }
        });

        jbtSua.setBackground(new java.awt.Color(41, 62, 80));
        jbtSua.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtSua.setForeground(new java.awt.Color(255, 255, 255));
        jbtSua.setText("Sửa");
        jbtSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSuaActionPerformed(evt);
            }
        });

        jbtXoa.setBackground(new java.awt.Color(41, 62, 80));
        jbtXoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtXoa.setForeground(new java.awt.Color(255, 255, 255));
        jbtXoa.setText("Cho Nghỉ Việc");
        jbtXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtXoaActionPerformed(evt);
            }
        });

        jcbGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));

        jLabel7.setText("Mật khẩu:");

        txtTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenActionPerformed(evt);
            }
        });

        jcbNgaySinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        jcbThangSinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jcbThangSinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbThangSinhActionPerformed(evt);
            }
        });

        jcbNamSinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbNamSinhActionPerformed(evt);
            }
        });

        jbtTimKiem.setBackground(new java.awt.Color(41, 62, 80));
        jbtTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        jbtTimKiem.setText("Tìm kiếm");
        jbtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtTimKiemActionPerformed(evt);
            }
        });

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        jcbChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhân viên", "Quản lý" }));
        jcbChucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbChucVuActionPerformed(evt);
            }
        });

        jbtLamMoi.setBackground(new java.awt.Color(41, 62, 80));
        jbtLamMoi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtLamMoi.setForeground(new java.awt.Color(255, 255, 255));
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
                        .addGap(20, 20, 20)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEmail)
                            .addComponent(jcbChucVu, 0, 228, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMatKhau)
                            .addComponent(txtTen)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jcbNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jcbThangSinh, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcbNamSinh, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jbtLamMoi, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jbtXoa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                                .addComponent(jbtSua, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jbtThem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtTimKiem)
                        .addGap(18, 18, 18)
                        .addComponent(jbtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbtTimKiem)))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jcbNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6))
                            .addComponent(jcbThangSinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcbNamSinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcbChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(10, 10, 10)
                        .addComponent(jbtThem)
                        .addGap(6, 6, 6)
                        .addComponent(jbtSua)
                        .addGap(6, 6, 6)
                        .addComponent(jbtXoa)
                        .addGap(6, 6, 6)
                        .addComponent(jbtLamMoi))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(13, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMatKhauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMatKhauActionPerformed

    private void jbtThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtThemActionPerformed
        handleAddAction();
    }//GEN-LAST:event_jbtThemActionPerformed

    private void jbtXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtXoaActionPerformed
        handleDeleteAction();
    }//GEN-LAST:event_jbtXoaActionPerformed

    private void txtTenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenActionPerformed

    private void jcbNamSinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbNamSinhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbNamSinhActionPerformed

    private void jbtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtTimKiemActionPerformed
        handleSearchAction();
    }//GEN-LAST:event_jbtTimKiemActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void jcbChucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbChucVuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbChucVuActionPerformed

    private void jbtSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSuaActionPerformed
        handleUpdateAction();
    }//GEN-LAST:event_jbtSuaActionPerformed

    private void jcbThangSinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbThangSinhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbThangSinhActionPerformed

    private void jbtLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLamMoiActionPerformed
        handleRefreshAction();
        fillTable();
    }//GEN-LAST:event_jbtLamMoiActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JButton jbtLamMoi;
    private javax.swing.JButton jbtSua;
    private javax.swing.JButton jbtThem;
    private javax.swing.JButton jbtTimKiem;
    private javax.swing.JButton jbtXoa;
    private javax.swing.JComboBox<String> jcbChucVu;
    private javax.swing.JComboBox<String> jcbGioiTinh;
    private javax.swing.JTextField jcbNamSinh;
    private javax.swing.JComboBox<String> jcbNgaySinh;
    private javax.swing.JComboBox<String> jcbThangSinh;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtMatKhau;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
