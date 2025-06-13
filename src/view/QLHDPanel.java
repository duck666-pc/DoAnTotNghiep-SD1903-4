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
            initCustomComponents();
        } catch (SQLException ex) {
            Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + ex.getMessage());
        }
    }

    private void initCustomComponents() {
        txtKhachHang.setText("KH00001"); // Mã KH mặc định
        txtNhanVien.setText("NV00001");   // Mã NV mặc định
        txtGiamGia.setText("0");
    }

    private void loadSanPham() throws SQLException, ClassNotFoundException {
        DefaultTableModel model = (DefaultTableModel) tblSanPham.getModel();
        model.setRowCount(0);
        
        List<SanPham> dsSanPham = qlhdDAO.getAllSanPham();
        for (SanPham sp : dsSanPham) {
            model.addRow(new Object[]{
                sp.getId(),
                sp.getTen(),
                sp.getGia()
            });
        }
    }

    private void loadHoaDon() throws SQLException, ClassNotFoundException {
        DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0);
        
        List<HoaDon> dsHoaDon = qlhdDAO.getAllHoaDon();
        for (HoaDon hd : dsHoaDon) {
            model.addRow(new Object[]{
                hd.getId(),
                hd.getThoiGian(),
                hd.getIdKhachHang(),
                hd.getTongTienGoc(),
                hd.getMucGiamGia(),
                hd.getTongTienSauGiamGia()
            });
        }
    }

    private void loadChiTietHoaDon(String idHoaDon) throws SQLException, ClassNotFoundException {
        DefaultTableModel model = (DefaultTableModel) tblCTHD.getModel();
        model.setRowCount(0);
        
        List<String[]> dsCTHD = qlhdDAO.getChiTietHoaDon(idHoaDon);
        for (String[] ct : dsCTHD) {
            double thanhTien = Integer.parseInt(ct[1]) * Double.parseDouble(ct[3]);
            model.addRow(new Object[]{
                ct[0], // ID CTHD
                ct[2], // Tên SP
                ct[3], // Đơn giá
                ct[1], // Số lượng
                thanhTien
            });
        }
        calculateTongTien();
    }

    private void calculateTongTien() {
        DefaultTableModel model = (DefaultTableModel) tblCTHD.getModel();
        double tongTien = 0;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            tongTien += (double) model.getValueAt(i, 4);
        }
        
        double giamGia = 0;
        try {
            giamGia = Double.parseDouble(txtGiamGia.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá trị giảm giá không hợp lệ");
        }
        
        double tongSauGiam = tongTien * (1 - giamGia / 100);
        
        lblTongTien.setText(String.format("%,.0f VND", tongTien));
        lblTongSauGiam.setText(String.format("%,.0f VND", tongSauGiam));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        btnTaoHD = new javax.swing.JButton();
        btnThemSP = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblCTHD = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnXoaHD = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtKhachHang = new javax.swing.JTextField();
        txtNhanVien = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtGiamGia = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblTongSauGiam = new javax.swing.JLabel();
        btnCapNhatGG = new javax.swing.JButton();
        btnXoaCTHD = new javax.swing.JButton();

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã HD", "Thời gian", "Khách hàng", "Tổng tiền", "Giảm giá (%)", "Thành tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SP", "Tên sản phẩm", "Đơn giá"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblSanPham);

        btnTaoHD.setText("Tạo hóa đơn");
        btnTaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHDActionPerformed(evt);
            }
        });

        btnThemSP.setText("Thêm vào hóa đơn");
        btnThemSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSPActionPerformed(evt);
            }
        });

        tblCTHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã CTHD", "Sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblCTHD);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("CHI TIẾT HÓA ĐƠN");

        btnXoaHD.setText("Xóa hóa đơn");
        btnXoaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHDActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin thanh toán"));

        jLabel2.setText("Khách hàng:");

        jLabel3.setText("Nhân viên:");

        txtKhachHang.setText("KH00001");

        txtNhanVien.setText("NV00001");

        jLabel4.setText("Giảm giá (%):");

        txtGiamGia.setText("0");

        jLabel5.setText("Tổng tiền:");

        lblTongTien.setText("0 VND");

        jLabel7.setText("Thành tiền:");

        lblTongSauGiam.setText("0 VND");

        btnCapNhatGG.setText("Cập nhật");
        btnCapNhatGG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatGGActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCapNhatGG))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTongSauGiam, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCapNhatGG))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblTongTien)
                    .addComponent(jLabel7)
                    .addComponent(lblTongSauGiam))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnXoaCTHD.setText("Xóa khỏi hóa đơn");
        btnXoaCTHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaCTHDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTaoHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnThemSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnXoaHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaCTHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTaoHD, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoaCTHD, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemSP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        int row = tblHoaDon.getSelectedRow();
        if (row >= 0) {
            selectedHDId = (String) tblHoaDon.getValueAt(row, 0);
            try {
                loadChiTietHoaDon(selectedHDId);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi tải chi tiết: " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void btnTaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHDActionPerformed
        try {
            HoaDon hd = new HoaDon();
            hd.setThoiGian(new Timestamp(System.currentTimeMillis()));
            hd.setIdKhachHang(txtKhachHang.getText());
            hd.setIdNguoiDung(txtNhanVien.getText());
            hd.setTongTienGoc(0);
            hd.setMucGiamGia(0);
            hd.setTongTienSauGiamGia(0);
            
            if (qlhdDAO.ThemHoaDon(hd)) {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công");
                loadHoaDon();
            } else {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thất bại");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTaoHDActionPerformed

    private void tblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamMouseClicked
        int row = tblSanPham.getSelectedRow();
        if (row >= 0) {
            selectedSPId = (String) tblSanPham.getValueAt(row, 0);
        }
    }//GEN-LAST:event_tblSanPhamMouseClicked

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
            // Kiểm tra sản phẩm đã có trong hóa đơn chưa
            List<ChiTietHoaDon> dsCTHD = qlhdDAO.getAllChiTietHoaDon(selectedHDId);
            boolean exists = false;
            String existingCTHDId = null;
            
            for (ChiTietHoaDon ct : dsCTHD) {
                if (ct.getIdSanPham().equals(selectedSPId)) {
                    exists = true;
                    existingCTHDId = ct.getId();
                    break;
                }
            }
            
            if (exists) {
                // Cập nhật số lượng nếu đã tồn tại
                int newQuantity = 0;
                for (ChiTietHoaDon ct : dsCTHD) {
                    if (ct.getIdSanPham().equals(selectedSPId)) {
                        newQuantity = ct.getSoLuong() + 1;
                        break;
                    }
                }
                
                if (qlhdDAO.UpdateChiTietHD(newQuantity, existingCTHDId)) {
                    JOptionPane.showMessageDialog(this, "Đã cập nhật số lượng sản phẩm");
                    loadChiTietHoaDon(selectedHDId);
                    updateHoaDonTotal();
                }
            } else {
                // Thêm mới chi tiết hóa đơn
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setIdHoaDon(selectedHDId);
                cthd.setIdSanPham(selectedSPId);
                cthd.setSoLuong(1);
                
                // Lấy giá sản phẩm
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
                    loadChiTietHoaDon(selectedHDId);
                    updateHoaDonTotal();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
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
                // Lấy thông tin hóa đơn để xóa
                HoaDon hd = new HoaDon();
                hd.setId(selectedHDId);
                
                if (qlhdDAO.XoaHoaDon(hd)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa hóa đơn");
                    selectedHDId = null;
                    loadHoaDon();
                    tblCTHD.setModel(new DefaultTableModel());
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
        
        int row = tblCTHD.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết hóa đơn");
            return;
        }
        
        String cthdId = (String) tblCTHD.getValueAt(row, 0);
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

    private void updateHoaDonTotal() throws ClassNotFoundException {
        if (selectedHDId == null) return;
        
        try {
            double tongTien = 0;
            List<ChiTietHoaDon> dsCTHD = qlhdDAO.getAllChiTietHoaDon(selectedHDId);
            
            for (ChiTietHoaDon ct : dsCTHD) {
                tongTien += ct.getSoLuong() * ct.getDonGia();
            }
            
            double giamGia = Double.parseDouble(txtGiamGia.getText());
            double tongSauGiam = tongTien * (1 - giamGia / 100);
            
            if (qlhdDAO.UpdateHoaDon(tongTien, selectedHDId)) {
                // Cập nhật lại bảng hóa đơn
                for (int i = 0; i < tblHoaDon.getRowCount(); i++) {
                    if (tblHoaDon.getValueAt(i, 0).equals(selectedHDId)) {
                        tblHoaDon.setValueAt(tongTien, i, 3);
                        tblHoaDon.setValueAt(giamGia, i, 4);
                        tblHoaDon.setValueAt(tongSauGiam, i, 5);
                        break;
                    }
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhatGG;
    private javax.swing.JButton btnTaoHD;
    private javax.swing.JButton btnThemSP;
    private javax.swing.JButton btnXoaCTHD;
    private javax.swing.JButton btnXoaHD;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblTongSauGiam;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JTable tblCTHD;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTextField txtGiamGia;
    private javax.swing.JTextField txtKhachHang;
    private javax.swing.JTextField txtNhanVien;
    // End of variables declaration//GEN-END:variables
}