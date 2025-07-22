package view;

import controller.QLKHDAO;
import java.util.List;
import model.KhachHang;

public final class QLKHPanel extends BasePanel<KhachHang> {

    private final QLKHDAO qlkh = new QLKHDAO();

    public QLKHPanel() {
        initComponents();
        this.baseJTable = jTable;
        this.baseTxtTimKiem = txtTimKiem;
        super.initTable();
        super.fillTable();
        super.addTableSelectionListener();
        super.enableAutoFilter();
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"ID", "Tên", "Điện thoại", "Địa chỉ", "Hạng KH"};
    }

    @Override
    protected void setFormFromRow(int row) {
        txtID.setText(getValue(row, 0));
        txtTen.setText(getValue(row, 1));
        txtDienThoai.setText(getValue(row, 2));
        txtDiaChi.setText(getValue(row, 3));
        jcbHangKhachHang.setSelectedItem(getValue(row, 4));
    }

    @Override
    protected boolean validateForm() {
        if (txtID.getText().trim().isEmpty()
                || txtTen.getText().trim().isEmpty()
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
        return new KhachHang(
                txtID.getText().trim(),
                txtTen.getText().trim(),
                txtDienThoai.getText().trim(),
                txtDiaChi.getText().trim(),
                (String) jcbHangKhachHang.getSelectedItem()
        );
    }

    @Override
    protected void clearForm() {
        txtID.setText("");
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
    protected void addEntityToTable(KhachHang entity) {
        tableModel.addRow(new Object[]{
            entity.getId(), entity.getTen(), entity.getDienThoai(), entity.getDiaChi(), entity.gethangKhachHangId()
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

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        txtID = new javax.swing.JTextField();
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

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("ID:");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 19, -1, -1));

        jLabel2.setText("Tên:");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 60, -1, -1));

        jLabel3.setText("Hạng Khách Hàng:");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 180, -1, -1));

        jLabel5.setText("Địa chỉ:");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 140, -1, -1));

        jLabel6.setText("Điện thoại:");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(51, 100, -1, -1));

        txtTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenActionPerformed(evt);
            }
        });
        add(txtTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 57, 175, -1));

        txtID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDActionPerformed(evt);
            }
        });
        add(txtID, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 16, 175, -1));

        txtDienThoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDienThoaiActionPerformed(evt);
            }
        });
        add(txtDienThoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 97, 175, -1));

        txtDiaChi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiaChiActionPerformed(evt);
            }
        });
        add(txtDiaChi, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 137, 175, -1));

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

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(319, 57, 556, 270));

        jbtThem.setBackground(new java.awt.Color(41, 62, 80));
        jbtThem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtThem.setForeground(new java.awt.Color(255, 255, 255));
        jbtThem.setText("Thêm");
        jbtThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtThemActionPerformed(evt);
            }
        });
        add(jbtThem, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 217, 293, -1));

        jbtSua.setBackground(new java.awt.Color(41, 62, 80));
        jbtSua.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtSua.setForeground(new java.awt.Color(255, 255, 255));
        jbtSua.setText("Sửa");
        jbtSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSuaActionPerformed(evt);
            }
        });
        add(jbtSua, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 246, 293, -1));

        jbtXoa.setBackground(new java.awt.Color(41, 62, 80));
        jbtXoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtXoa.setForeground(new java.awt.Color(255, 255, 255));
        jbtXoa.setText("Xóa");
        jbtXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtXoaActionPerformed(evt);
            }
        });
        add(jbtXoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 275, 293, -1));

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });
        add(txtTimKiem, new org.netbeans.lib.awtextra.AbsoluteConstraints(319, 16, 410, -1));

        jbtTimKiem.setBackground(new java.awt.Color(41, 62, 80));
        jbtTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        jbtTimKiem.setText("Tìm kiếm bằng ID");
        jbtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtTimKiemActionPerformed(evt);
            }
        });
        add(jbtTimKiem, new org.netbeans.lib.awtextra.AbsoluteConstraints(747, 16, -1, -1));

        jbtLamMoi.setBackground(new java.awt.Color(41, 62, 80));
        jbtLamMoi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtLamMoi.setForeground(new java.awt.Color(255, 255, 255));
        jbtLamMoi.setText("Làm mới");
        jbtLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtLamMoiActionPerformed(evt);
            }
        });
        add(jbtLamMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 304, 293, -1));

        jcbHangKhachHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HC001 - Đồng", "HC002 - Bạc", "HC003 - Vàng", "HC004 - Bạch kim", "HC005 - Kim cương" }));
        add(jcbHangKhachHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 177, 175, -1));
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
    private javax.swing.JComboBox<String> jcbHangKhachHang;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtDienThoai;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
