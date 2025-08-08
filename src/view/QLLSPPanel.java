/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import controller.QLLSPDAO;
import java.util.List;
import model.LoaiSanPham;
import java.util.ArrayList;

/**
 *
 * @author minhd
 */
public class QLLSPPanel extends BasePanel<LoaiSanPham> {

    private final QLLSPDAO qllsp = new QLLSPDAO();
    private static final List<QLSPPanel> registeredPanels = new ArrayList<>();

    public QLLSPPanel() {
        initComponents();
        this.baseJTable = jTable;
        this.baseTxtTimKiem = txtTimKiem;
        super.initTable();
        super.fillTable();
        super.addTableSelectionListener();
        super.enableAutoFilter();
    }

    /**
     * Register a QLSPPanel to be notified when product types are updated
     * @param panel
     */
    public static void registerQLSPPanel(QLSPPanel panel) {
        if (!registeredPanels.contains(panel)) {
            registeredPanels.add(panel);
        }
    }

    /**
     * Unregister a QLSPPanel
     * @param panel
     */
    public static void unregisterQLSPPanel(QLSPPanel panel) {
        registeredPanels.remove(panel);
    }

    /**
     * Notify all registered QLSPPanels to update their comboboxes
     */
    private void notifyQLSPPanelsToUpdate() {
        for (QLSPPanel panel : registeredPanels) {
            panel.updateProductTypeComboBox();
        }
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"ID", "Tên", "Mô tả"};
    }

    @Override
    protected void setFormFromRow(int row) {
        // Chỉ hiển thị tên và mô tả, không hiển thị ID vì sẽ được tự động sinh
        txtTen.setText(getValue(row, 1));
        txtMoTa.setText(getValue(row, 2));
    }

    @Override
    protected boolean validateForm() {
        // Không kiểm tra ID vì sẽ được tự động sinh
        if (txtTen.getText().trim().isEmpty()
                || txtMoTa.getText().trim().isEmpty()) {
            showMessage("Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        
        // Check for duplicate product type name
        try {
            List<LoaiSanPham> existingTypes = qllsp.getAll();
            String newName = txtTen.getText().trim();
            
            for (LoaiSanPham lsp : existingTypes) {
                // Skip current item when editing
                if (currentRow >= 0) {
                    String currentId = getValue(currentRow, 0);
                    if (lsp.getId().equals(currentId)) {
                        continue;
                    }
                }
                
                if (lsp.getTen().equalsIgnoreCase(newName)) {
                    showMessage("Tên loại sản phẩm đã tồn tại!");
                    return false;
                }
            }
        } catch (Exception e) {
            showMessage("Lỗi khi kiểm tra dữ liệu: " + e.getMessage());
            return false;
        }
        
        return true;
    }

    @Override
    protected LoaiSanPham getEntityFromForm() {
        LoaiSanPham lsp = new LoaiSanPham();
        // ID sẽ được tự động sinh trong DAO, không cần set ở đây khi thêm mới
        lsp.setTen(txtTen.getText().trim());
        lsp.setMoTa(txtMoTa.getText().trim());
        return lsp;
    }

    @Override
    protected void clearForm() {
        txtTen.setText("");
        txtMoTa.setText("");
        currentRow = -1;
    }

    @Override
    protected List<LoaiSanPham> getAllEntities() throws Exception {
        return qllsp.getAll();
    }

    @Override
    protected String getEntityId(LoaiSanPham entity) {
        return entity.getId();
    }

    @Override
    protected String getEntityName(LoaiSanPham entity) {
        return entity.getTen(); // Trả về tên để tìm kiếm
    }

    @Override
    protected void addEntityToTable(LoaiSanPham entity) {
        tableModel.addRow(qllsp.getRow(entity));
    }

    @Override
    protected int addEntity(LoaiSanPham entity) throws Exception {
        int result = qllsp.add(entity);
        // Notify QLSPPanels to update their comboboxes
        notifyQLSPPanelsToUpdate();
        return result;
    }

    @Override
    protected int deleteEntity(String id) throws Exception {
        int result = qllsp.delete(id);
        // Notify QLSPPanels to update their comboboxes
        notifyQLSPPanelsToUpdate();
        return result;
    }

    @Override
    protected int updateEntity(LoaiSanPham entity, String oldId) throws Exception {
        int result = qllsp.edit(entity, oldId);
        // Notify QLSPPanels to update their comboboxes
        notifyQLSPPanelsToUpdate();
        return result;
    }

    @Override
    protected String getIdPrefix() {
        return "LSP"; 
    }

    @Override
    protected void setEntityId(LoaiSanPham entity, String id) {
        entity.setId(id);
    }

    @Override
    protected void updateEntityId(LoaiSanPham entity, String newId) throws Exception {
        String oldId = entity.getId();
        entity.setId(newId);
        qllsp.edit(entity, oldId); // Cập nhật ID trong database
        // Notify QLSPPanels to update their comboboxes
        notifyQLSPPanelsToUpdate();
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
        jLabel6 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        txtMoTa = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jbtThem = new javax.swing.JButton();
        jbtSua = new javax.swing.JButton();
        jbtXoa = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        jbtTimKiem = new javax.swing.JButton();
        jbtLamMoi = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Tên:");

        jLabel6.setText("Mô tả:");

        txtTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenActionPerformed(evt);
            }
        });

        txtMoTa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMoTaActionPerformed(evt);
            }
        });

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Tên", "Mô tả"
            }
        ));
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

        jbtTimKiem.setBackground(new java.awt.Color(41, 62, 80));
        jbtTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        jbtTimKiem.setText("Tìm kiếm");
        jbtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtTimKiemActionPerformed(evt);
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

        jPanel3.setBackground(new java.awt.Color(41, 62, 80));

        jLabel10.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("🛒");
        jLabel10.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Quản Lý Loại Sản Phẩm");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtMoTa, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jbtLamMoi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtThem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtSua, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtXoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtTimKiem)
                                .addGap(18, 18, 18)
                                .addComponent(jbtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbtTimKiem))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMoTa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addComponent(jbtThem)
                        .addGap(6, 6, 6)
                        .addComponent(jbtSua)
                        .addGap(6, 6, 6)
                        .addComponent(jbtXoa)
                        .addGap(6, 6, 6)
                        .addComponent(jbtLamMoi))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtTenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenActionPerformed

    private void txtMoTaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMoTaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMoTaActionPerformed

    private void jbtThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtThemActionPerformed
        handleAddAction();
    }//GEN-LAST:event_jbtThemActionPerformed

    private void jbtSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSuaActionPerformed
        handleUpdateAction();
    }//GEN-LAST:event_jbtSuaActionPerformed

    private void jbtXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtXoaActionPerformed
        handleDeleteAction();
    }//GEN-LAST:event_jbtXoaActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed

    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void jbtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtTimKiemActionPerformed
        handleSearchAction();
    }//GEN-LAST:event_jbtTimKiemActionPerformed

    private void jbtLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLamMoiActionPerformed
        clearForm();
    }//GEN-LAST:event_jbtLamMoiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JButton jbtLamMoi;
    private javax.swing.JButton jbtSua;
    private javax.swing.JButton jbtThem;
    private javax.swing.JButton jbtTimKiem;
    private javax.swing.JButton jbtXoa;
    private javax.swing.JTextField txtMoTa;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
