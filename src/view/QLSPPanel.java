package view;

import controller.QLLSPDAO;
import controller.QLSPDAO;
import java.util.List;
import model.SanPham;
import javax.swing.DefaultComboBoxModel;

public final class QLSPPanel extends BasePanel<SanPham> {

    private final QLSPDAO qlsp = new QLSPDAO();
    private final QLLSPDAO qllsp = new QLLSPDAO();

    public QLSPPanel() {
        initComponents();
        this.baseJTable = jTable;
        this.baseTxtTimKiem = txtTimKiem;
        super.initTable();
        super.fillTable();
        super.addTableSelectionListener();
        super.enableAutoFilter();

        // Register this panel to receive updates from QLLSPPanel
        QLLSPPanel.registerQLSPPanel(this);

        // Initialize combobox
        updateProductTypeComboBox();
    }

    /**
     * Update the product type combobox with latest data from database
     */
    public void updateProductTypeComboBox() {
        try {
            String[] items = qllsp.getProductTypeComboBoxItems(); 
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(items);
            jcbLoaiSanPham.setModel(model);
        } catch (Exception e) {
            showMessage("Lỗi khi cập nhật danh sách loại sản phẩm: " + e.getMessage());
        }
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"ID", "Tên", "Mô tả", "Giá", "Loại Sản Phẩm"};
    }

    @Override
    protected void setFormFromRow(int row) {
        txtTen.setText(getValue(row, 1));
        txtMoTa.setText(getValue(row, 2));
        txtGia.setText(getValue(row, 3));

        String productTypeId = getValue(row, 4);
        if (productTypeId != null) {
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) jcbLoaiSanPham.getModel();
            for (int i = 0; i < model.getSize(); i++) {
                String item = model.getElementAt(i);
                if (item.equals(productTypeId)) { // So sánh trực tiếp ID
                    jcbLoaiSanPham.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    @Override
    protected boolean validateForm() {
        if (txtTen.getText().trim().isEmpty()
                || txtGia.getText().trim().isEmpty()
                || jcbLoaiSanPham.getSelectedItem() == null) {
            showMessage("Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        try {
            float gia = Float.parseFloat(txtGia.getText().trim());
            if (gia <= 10000) {
                showMessage("Giá phải lớn hơn 10000!");
                return false;
            }
        } catch (NumberFormatException e) {
            showMessage("Giá phải là số!");
            return false;
        }
        return true;
    }

    @Override
    protected SanPham getEntityFromForm() {
        SanPham sp = new SanPham();
        sp.setTen(txtTen.getText().trim());
        sp.setMoTa(txtMoTa.getText().trim());
        sp.setGia(Float.parseFloat(txtGia.getText().trim()));

        // Lấy trực tiếp ID từ ComboBox
        String productTypeId = (String) jcbLoaiSanPham.getSelectedItem();
        if (productTypeId != null) {
            sp.setLoaiSanPham(productTypeId);
        }

        return sp;
    }

    @Override
    protected void clearForm() {
        txtTen.setText("");
        txtMoTa.setText("");
        txtGia.setText("");
        jcbLoaiSanPham.setSelectedIndex(-1);
        currentRow = -1;
    }

    @Override
    protected List<SanPham> getAllEntities() throws Exception {
        return qlsp.getAll();
    }

    @Override
    protected String getEntityId(SanPham entity) {
        return entity.getId();
    }

    @Override
    protected String getEntityName(SanPham entity) {
        return entity.getTen();
    }

    @Override
    protected void addEntityToTable(SanPham entity) {
        tableModel.addRow(qlsp.getRow(entity));
    }

    @Override
    protected int addEntity(SanPham entity) throws Exception {
        return qlsp.add(entity);
    }

    @Override
    protected int deleteEntity(String id) throws Exception {
        return qlsp.delete(id);
    }

    @Override
    protected int updateEntity(SanPham entity, String oldId) throws Exception {
        return qlsp.edit(entity, oldId);
    }

    @Override
    protected String getIdPrefix() {
        return "SP";
    }

    @Override
    protected void setEntityId(SanPham entity, String id) {
        entity.setId(id);
    }

    @Override
    protected void updateEntityId(SanPham entity, String newId) throws Exception {
        String oldId = entity.getId();
        entity.setId(newId);
        qlsp.edit(entity, oldId);
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
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
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Tên:");

        jLabel3.setText("Loại sản phẩm:");

        jLabel5.setText("Giá:");

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
        jbtTimKiem.setText("Tìm kiếm ");
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

        jcbLoaiSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "001-Đồ uống", "002-Đồ ăn", "003-Tráng miệng", "004-Kem", "005-Đồ ăn nhanh", "006-Salad", "007-Nước ép", "008-Bánh mì", "009-Mì xào", "010-Món chính", "Khác" }));

        jPanel3.setBackground(new java.awt.Color(41, 62, 80));

        jLabel10.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("🍜");
        jLabel10.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Quản Lý Sản Phẩm");

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
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jcbLoaiSanPham, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtGia)
                                    .addComponent(txtMoTa)
                                    .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jbtThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtXoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtLamMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtTimKiem)
                                .addGap(18, 18, 18)
                                .addComponent(jbtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtTimKiem))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtMoTa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcbLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(13, 13, 13)
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JButton jbtLamMoi;
    private javax.swing.JButton jbtSua;
    private javax.swing.JButton jbtThem;
    private javax.swing.JButton jbtTimKiem;
    private javax.swing.JButton jbtXoa;
    private javax.swing.JComboBox<String> jcbLoaiSanPham;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtMoTa;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
