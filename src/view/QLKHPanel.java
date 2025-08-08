package view;

import controller.QLHKHDAO;
import controller.QLKHDAO;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import model.KhachHang;

public final class QLKHPanel extends BasePanel<KhachHang> {

    private final QLKHDAO qlkh = new QLKHDAO();
    private final QLHKHDAO qlhkh = new QLHKHDAO();

    public QLKHPanel() {
        initComponents();
        this.baseJTable = jTable;
        this.baseTxtTimKiem = txtTimKiem;
        updateCustomerRankComboBox();
        super.initTable();
        super.fillTable();
        super.addTableSelectionListener();
        super.enableAutoFilter();
    }

    // Hàm cập nhật combobox lấy dữ liệu từ DB
    private void updateCustomerRankComboBox() {
        try {
            String[] items = qlhkh.getCustomerRankComboBoxItems();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(items);
            jcbHangKhachHang.setModel(model);
        } catch (Exception e) {
            showMessage("Lỗi khi cập nhật danh sách hạng khách hàng: " + e.getMessage());
        }
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"ID", "Tên", "Điện thoại", "Địa chỉ", "Hạng KH"};
    }

    @Override
    protected void setFormFromRow(int row) {
        txtTen.setText(getValue(row, 1));
        txtDienThoai.setText(getValue(row, 2));
        txtDiaChi.setText(getValue(row, 3));
        String rankId = getValue(row, 4);
        if (rankId != null) {
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) jcbHangKhachHang.getModel();
            for (int i = 0; i < model.getSize(); i++) {
                String item = model.getElementAt(i);
                if (item.startsWith(rankId)) { // item có dạng "HC001 - Đồng"
                    jcbHangKhachHang.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    @Override
    protected boolean validateForm() {
        if (txtTen.getText().trim().isEmpty()
                || txtDienThoai.getText().trim().isEmpty()
                || txtDiaChi.getText().trim().isEmpty()
                || jcbHangKhachHang.getSelectedItem() == null) {
            showMessage("Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        if (!txtDienThoai.getText().trim().matches("\\d+")) {
            showMessage("Số điện thoại phải là số!");
            return false;
        }
        return true;
    }

    @Override
    protected KhachHang getEntityFromForm() {
        KhachHang kh = new KhachHang();
        kh.setTen(txtTen.getText().trim());
        kh.setDienThoai(txtDienThoai.getText().trim());
        kh.setDiaChi(txtDiaChi.getText().trim());
        String selectedItem = (String) jcbHangKhachHang.getSelectedItem();
        if (selectedItem != null && selectedItem.length() >= 5) {
            String rankId = selectedItem.substring(0, 5).trim(); // VD: "HC001"
            kh.sethangKhachHangId(rankId);
        }
        return kh;
    }

    @Override
    protected void clearForm() {
        txtTen.setText("");
        txtDienThoai.setText("");
        txtDiaChi.setText("");
        jcbHangKhachHang.setSelectedIndex(-1);
        currentRow = -1;
    }

    @Override
    protected List<KhachHang> getAllEntities() throws Exception {
        return qlkh.getAll();
    }

    @Override
    protected String getEntityId(KhachHang entity) {
        return entity.getId();
    }

    @Override
    protected String getEntityName(KhachHang entity) {
        return entity.getTen();
    }

    @Override
    protected void addEntityToTable(KhachHang entity) {
        tableModel.addRow(new Object[]{
            entity.getId(),
            entity.getTen(),
            entity.getDienThoai(),
            entity.getDiaChi(),
            entity.gethangKhachHangId()
        });
    }

    @Override
    protected int addEntity(KhachHang entity) throws Exception {
        return qlkh.add(entity);
    }

    @Override
    protected int deleteEntity(String id) throws Exception {
        return qlkh.delete(id);
    }

    @Override
    protected int updateEntity(KhachHang entity, String oldId) throws Exception {
        return qlkh.edit(entity, oldId);
    }

    @Override
    protected String getIdPrefix() {
        return "KH";
    }

    @Override
    protected void setEntityId(KhachHang entity, String id) {
        entity.setId(id);
    }

    @Override
    protected void updateEntityId(KhachHang entity, String newId) throws Exception {
        String oldId = entity.getId();
        entity.setId(newId);
        qlkh.edit(entity, oldId);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        txtDienThoai = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jbtThem = new javax.swing.JButton();
        jbtSua = new javax.swing.JButton();
        jbtXoa = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        jbtTimKiem = new javax.swing.JButton();
        jbtLamMoi = new javax.swing.JButton();
        jcbHangKhachHang = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Tên:");

        jLabel3.setText("Hạng Khách Hàng:");

        jLabel5.setText("Địa chỉ:");

        jLabel6.setText("Điện thoại:");

        txtTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenActionPerformed(evt);
            }
        });

        txtDienThoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDienThoaiActionPerformed(evt);
            }
        });

        txtDiaChi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiaChiActionPerformed(evt);
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
                "ID", "Tên", "Điện Thoại", "Địa Chỉ", "ID Hạng Khách Hàng"
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

        jcbHangKhachHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HC001 - Đồng", "HC002 - Bạc", "HC003 - Vàng", "HC004 - Bạch kim", "HC005 - Kim cương" }));

        jPanel3.setBackground(new java.awt.Color(41, 62, 80));

        jLabel10.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("🍽️");
        jLabel10.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Quản Lý Khách Hàng");

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
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jcbHangKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jbtThem, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtSua, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtTimKiem)
                                .addGap(18, 18, 18)
                                .addComponent(jbtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE))
                        .addGap(8, 8, 8))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel2))
                    .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbtTimKiem)))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel6))
                            .addComponent(txtDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel5))
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel3))
                            .addComponent(jcbHangKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jbtThem)
                        .addGap(6, 6, 6)
                        .addComponent(jbtSua)
                        .addGap(6, 6, 6)
                        .addComponent(jbtXoa)
                        .addGap(6, 6, 6)
                        .addComponent(jbtLamMoi))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbtLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLamMoiActionPerformed
        clearForm();
    }//GEN-LAST:event_jbtLamMoiActionPerformed

    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtDienThoaiActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtDiaChiActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void txtHangKhachHangActionPerformed(java.awt.event.ActionEvent evt) {
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JComboBox<String> jcbHangKhachHang;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtDienThoai;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
