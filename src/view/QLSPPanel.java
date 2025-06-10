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

    public void fillTable() {
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

    @SuppressWarnings("unchecked")
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

        // Xóa dòng setDefaultCloseOperation (chỉ dành cho JFrame)
        // Xóa các lệnh quản lý cửa sổ

        jLabel1.setText("ID:");

        jLabel2.setText("Tên:");

        jLabel3.setText("Loại sản phẩm:");

        jLabel5.setText("Giá:");

        jLabel6.setText("Mô tả:");

        txtTen.addActionListener(this::txtTenActionPerformed);

        txtID.addActionListener(this::txtIDActionPerformed);

        txtMoTa.addActionListener(this::txtMoTaActionPerformed);

        txtGia.addActionListener(this::txtGiaActionPerformed);

        txtLoaiSanPham.addActionListener(this::txtLoaiSanPhamActionPerformed);

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
        jbtThem.addActionListener(this::jbtThemActionPerformed);

        jbtSua.setText("Sửa");
        jbtSua.addActionListener(this::jbtSuaActionPerformed);

        jbtXoa.setText("Xóa");
        jbtXoa.addActionListener(this::jbtXoaActionPerformed);

        txtTimKiem.addActionListener(this::txtTimKiemActionPerformed);

        jbtTimKiem.setText("Tìm kiếm bằng ID");
        jbtTimKiem.addActionListener(this::jbtTimKiemActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this); // Sửa thành this (JPanel)
        this.setLayout(layout); // Thiết lập layout cho chính panel
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                            .addComponent(txtLoaiSanPham)
                            .addComponent(txtGia)
                            .addComponent(txtMoTa)
                            .addComponent(txtTen)
                            .addComponent(txtID)))
                    .addComponent(jbtThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtXoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtTimKiem)
                        .addGap(18, 18, 18)
                        .addComponent(jbtTimKiem))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE))
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
                        .addGap(18, 18, 18)
                        .addComponent(jbtThem)
                        .addGap(18, 18, 18)
                        .addComponent(jbtSua)
                        .addGap(18, 18, 18)
                        .addComponent(jbtXoa))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

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
        // Logic thêm sản phẩm (giữ nguyên)
    }

    private void jbtXoaActionPerformed(java.awt.event.ActionEvent evt) {
        // Logic xóa sản phẩm (giữ nguyên)
    }

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {
        // Xử lý sự kiện
    }

    private void jbtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {
        // Logic tìm kiếm (giữ nguyên)
    }

    private void jbtSuaActionPerformed(java.awt.event.ActionEvent evt) {
        // Logic sửa sản phẩm (giữ nguyên)
    }

    // Xóa toàn bộ phần main() vì không cần thiết cho JPanel

    // Giữ nguyên khai báo biến thành phần
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
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtLoaiSanPham;
    private javax.swing.JTextField txtMoTa;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiem;
}