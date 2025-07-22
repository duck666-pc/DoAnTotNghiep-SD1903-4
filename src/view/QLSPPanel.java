package view;

import controller.QLSPDAO;
import java.util.List;
import model.SanPham;

public final class QLSPPanel extends BasePanel<SanPham> {

    private final QLSPDAO qlsp = new QLSPDAO();

    public QLSPPanel() {
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
        return new String[]{"ID", "Tên", "Mô tả", "Giá", "Loại Sản Phẩm"};
    }

    @Override
    protected void setFormFromRow(int row) {
        txtID.setText(getValue(row, 0));
        txtTen.setText(getValue(row, 1));
        txtMoTa.setText(getValue(row, 2));
        txtGia.setText(getValue(row, 3));
        jcbLoaiSanPham.setSelectedItem(getValue(row, 4));
    }

    @Override
    protected boolean validateForm() {
        if (txtID.getText().trim().isEmpty()
                || txtTen.getText().trim().isEmpty()
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
        sp.setId(txtID.getText().trim());
        sp.setTen(txtTen.getText().trim());
        sp.setMoTa(txtMoTa.getText().trim());
        sp.setGia(Float.parseFloat(txtGia.getText().trim()));
        sp.setLoaiSanPham((String) jcbLoaiSanPham.getSelectedItem());
        return sp;
    }

    @Override
    protected void clearForm() {
        txtID.setText("");
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

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("ID:");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 19, -1, -1));

        jLabel2.setText("Tên:");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 60, -1, -1));

        jLabel3.setText("Loại sản phẩm:");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 180, -1, -1));

        jLabel5.setText("Giá:");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(66, 140, -1, -1));

        jLabel6.setText("Mô tả:");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 100, -1, -1));

        txtTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenActionPerformed(evt);
            }
        });
        add(txtTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 57, 150, -1));

        txtID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDActionPerformed(evt);
            }
        });
        add(txtID, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 16, 150, -1));

        txtMoTa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMoTaActionPerformed(evt);
            }
        });
        add(txtMoTa, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 97, 150, -1));

        txtGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGiaActionPerformed(evt);
            }
        });
        add(txtGia, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 137, 150, -1));

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

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 60, 580, 260));

        jbtThem.setBackground(new java.awt.Color(41, 62, 80));
        jbtThem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtThem.setForeground(new java.awt.Color(255, 255, 255));
        jbtThem.setText("Thêm");
        jbtThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtThemActionPerformed(evt);
            }
        });
        add(jbtThem, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 212, 248, -1));

        jbtSua.setBackground(new java.awt.Color(41, 62, 80));
        jbtSua.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtSua.setForeground(new java.awt.Color(255, 255, 255));
        jbtSua.setText("Sửa");
        jbtSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSuaActionPerformed(evt);
            }
        });
        add(jbtSua, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 241, 248, -1));

        jbtXoa.setBackground(new java.awt.Color(41, 62, 80));
        jbtXoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtXoa.setForeground(new java.awt.Color(255, 255, 255));
        jbtXoa.setText("Xóa");
        jbtXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtXoaActionPerformed(evt);
            }
        });
        add(jbtXoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 270, 248, -1));

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });
        add(txtTimKiem, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, 440, 20));

        jbtTimKiem.setBackground(new java.awt.Color(41, 62, 80));
        jbtTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        jbtTimKiem.setText("Tìm kiếm bằng ID");
        jbtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtTimKiemActionPerformed(evt);
            }
        });
        add(jbtTimKiem, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 20, 130, 20));

        jbtLamMoi.setBackground(new java.awt.Color(41, 62, 80));
        jbtLamMoi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtLamMoi.setForeground(new java.awt.Color(255, 255, 255));
        jbtLamMoi.setText("Làm mới");
        jbtLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtLamMoiActionPerformed(evt);
            }
        });
        add(jbtLamMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 299, 248, -1));

        jcbLoaiSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "001-Đồ uống", "002-Đồ ăn", "003-Tráng miệng", "004-Kem", "005-Đồ ăn nhanh", "006-Salad", "007-Nước ép", "008-Bánh mì", "009-Mì xào", "010-Món chính", "Khác" }));
        add(jcbLoaiSanPham, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 177, 150, -1));
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
