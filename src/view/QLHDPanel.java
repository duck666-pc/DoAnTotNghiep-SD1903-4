/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import Controller.QLHDDAO;
import Model.ChiTietHoaDon;
import Model.HoaDon;
import model.SanPham;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class QLHDPanel extends javax.swing.JPanel {

    private QLHDDAO qlhdDAO = null;
    private String selectedHDId;
    private String selectedSPId;

    public QLHDPanel() throws ClassNotFoundException {
        try {
            qlhdDAO = new QLHDDAO();
            initComponents();
            loadSanPham();
            loadHoaDon();
        } catch (SQLException ex) {
            Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + ex.getMessage());
        }
    }

    private void initCustomComponents() {

    }

    private void loadSanPham() throws SQLException, ClassNotFoundException {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Mã", "Tên", "Giá"});
        for (SanPham sp : qlhdDAO.getAllSanPham()) {
            model.addRow(new Object[]{
                sp.getId(), sp.getTen(), sp.getGia()
            });
        }
        tblSanPham.setModel(model);
    }

    private void loadHoaDon() throws SQLException, ClassNotFoundException {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Mã", "Thời gian", "Khách hàng", "Tổng tiền"});
        for (HoaDon hd : qlhdDAO.getAllHoaDon()) {
            model.addRow(new Object[]{
                hd.getId(), hd.getThoiGian(), hd.getIdKhachHang(), hd.getTongTienGoc()
            });
        }
        tblHoaDon.setModel(model);
    }

    private void loadChiTietHoaDon(String idHoaDon) throws SQLException, ClassNotFoundException {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Mã CTHD", "Mã HD", "Mã SP", "Tên SP", "Số lượng", "Đơn giá"});
        for (String[] obj : qlhdDAO.getChiTietHoaDon(idHoaDon)) {
            model.addRow(new Object[]{
                obj[0], obj[1], obj[2], obj[3], obj[6], obj[7]
            });
        }
        tblGioHang.setModel(model);
    }

    private void calculateTongTien() {
        DefaultTableModel model = (DefaultTableModel) tblGioHang.getModel();
        double tongTien = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            tongTien += (double) model.getValueAt(i, 4);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        btnTaoHoaDon = new javax.swing.JButton();
        btnThemSPVaoHD = new javax.swing.JButton();
        btnXoaSPKhoiHD = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblGioHang = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnXoaHoaDon = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblSanPham);

        btnTaoHoaDon.setText("Tạo Hóa Đơn");
        btnTaoHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHoaDonActionPerformed(evt);
            }
        });

        btnThemSPVaoHD.setText("Thêm Vào Hóa Đơn");
        btnThemSPVaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSPVaoHDActionPerformed(evt);
            }
        });

        btnXoaSPKhoiHD.setText("Xóa khỏi Hóa Đơn");

        tblGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblGioHang);

        jLabel1.setText("Giỏ hàng");

        btnXoaHoaDon.setText("Xóa Hóa Đơn");

        jLabel2.setText("Hóa Đơn");

        jLabel3.setText("Sản Phẩm");

        jLabel4.setText("ID:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnThemSPVaoHD, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnTaoHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnXoaHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnXoaSPKhoiHD, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4)))
                .addGap(109, 109, 109)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 692, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 692, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 692, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(426, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTaoHoaDon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaHoaDon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaSPKhoiHD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnThemSPVaoHD))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(134, 134, 134))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHDActionPerformed
        try {
            HoaDon hd = new HoaDon();
            hd.setThoiGian(new java.sql.Timestamp(System.currentTimeMillis()));
            hd.setIdKhachHang("Khach Vang Lai");
            hd.setIdNguoiDung(""); // Nếu không có trường nhân viên
            hd.setTongTienGoc(0);
            hd.setMucGiamGia(0);
            hd.setTongTienSauGiamGia(0);
            if (qlhdDAO.ThemHoaDon(hd)) {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công");
                loadHoaDon();
            } else {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thất bại");
            }
        } catch (Exception ex) {
            Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi tạo hóa đơn: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnTaoHDActionPerformed

    private void btnThemSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSPActionPerformed
        if (selectedHDId == null || selectedHDId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn");
            return;
        }
        if (selectedSPId == null || selectedSPId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm");
            return;
        }
        try {
            List<ChiTietHoaDon> dsCTHD = qlhdDAO.getAllChiTietHoaDon(selectedHDId);
            boolean exists = false;
            String existingCTHDId = null;
            int soLuongMoi = 1;
            for (ChiTietHoaDon ct : dsCTHD) {
                if (ct.getIdSanPham().equals(selectedSPId)) {
                    exists = true;
                    existingCTHDId = ct.getId();
                    soLuongMoi = ct.getSoLuong() + 1;
                    break;
                }
            }
            if (exists) {
                if (qlhdDAO.UpdateChiTietHD(soLuongMoi, existingCTHDId)) {
                    JOptionPane.showMessageDialog(this, "Đã cập nhật số lượng sản phẩm");
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
                }
            } else {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setIdHoaDon(selectedHDId);
                cthd.setIdSanPham(selectedSPId);
                cthd.setSoLuong(1);
                double price = 0;
                for (SanPham sp : qlhdDAO.getAllSanPham()) {
                    if (sp.getId().equals(selectedSPId)) {
                        price = sp.getGia();
                        break;
                    }
                }
                cthd.setDonGia(price);
                if (qlhdDAO.ThemChiTietHD(cthd)) {
                    JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm vào hóa đơn");
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại");
                }
            }
            loadChiTietHoaDon(selectedHDId);
            updateHoaDonTotal();
        } catch (Exception ex) {
            Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi thêm sản phẩm: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnThemSPActionPerformed

    private void btnXoaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaHDActionPerformed
        if (selectedHDId == null || selectedHDId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa hóa đơn này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                HoaDon hd = new HoaDon();
                hd.setId(selectedHDId);

                if (qlhdDAO.XoaHoaDon(hd)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa hóa đơn");
                    selectedHDId = null;
                    loadHoaDon();
                    tblGioHang.setModel(new DefaultTableModel());
                    calculateTongTien();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa hóa đơn thất bại");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnXoaHDActionPerformed

    private void btnCapNhatGGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatGGActionPerformed
        calculateTongTien();
        try {
            updateHoaDonTotal();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCapNhatGGActionPerformed

    private void btnXoaCTHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaCTHDActionPerformed
        if (selectedHDId == null || selectedHDId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn");
            return;
        }

        int row = tblGioHang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết hóa đơn");
            return;
        }

        String cthdId = (String) tblGioHang.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa sản phẩm này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (qlhdDAO.XoaChiTietHD(cthdId)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa sản phẩm khỏi hóa đơn");
                    loadChiTietHoaDon(selectedHDId);
                    updateHoaDonTotal();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnXoaCTHDActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        int row = tblHoaDon.getSelectedRow();
        if (row >= 0) {
            selectedHDId = tblHoaDon.getValueAt(row, 0).toString();
            try {
                loadChiTietHoaDon(selectedHDId);
            } catch (Exception ex) {
                Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Không thể load chi tiết hóa đơn");
            }
        }
    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void tblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamMouseClicked
        int row = tblSanPham.getSelectedRow();
        if (row >= 0) {
            selectedSPId = tblSanPham.getValueAt(row, 0).toString();
        }
    }//GEN-LAST:event_tblSanPhamMouseClicked

    private void updateHoaDonTotal() throws ClassNotFoundException {
        if (selectedHDId == null) {
            return;
        }

        try {
            double tongTien = 0;
            List<ChiTietHoaDon> dsCTHD = qlhdDAO.getAllChiTietHoaDon(selectedHDId);

            for (ChiTietHoaDon ct : dsCTHD) {
                tongTien += ct.getSoLuong() * ct.getDonGia();
            }


            if (qlhdDAO.UpdateHoaDon(tongTien, selectedHDId)) {
                // Cập nhật lại bảng hóa đơn
                for (int i = 0; i < tblHoaDon.getRowCount(); i++) {
                    if (tblHoaDon.getValueAt(i, 0).equals(selectedHDId)) {
                        tblHoaDon.setValueAt(tongTien, i, 3);
                        break;
                    }
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTaoHoaDon;
    private javax.swing.JButton btnThemSPVaoHD;
    private javax.swing.JButton btnXoaHoaDon;
    private javax.swing.JButton btnXoaSPKhoiHD;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblGioHang;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTable tblSanPham;
    // End of variables declaration//GEN-END:variables
}
