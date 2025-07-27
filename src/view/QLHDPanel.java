/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import controller.QLHDDAO;
import Model.HoaDon;
import Model.ChiTietHoaDon;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author minhd
 */
public class QLHDPanel extends javax.swing.JPanel {
    DefaultTableModel tableModel;
    QLHDDAO orderinfo;

    public QLHDPanel() {
        initComponents();
        orderinfo = new QLHDDAO();
        addEventListeners();
        loadAllHoaDon(); // Load initial data
    }

    private void addEventListeners() {
        // Click bảng HD -> Hiện chi tiết hóa đơn
        jTableHD.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = jTableHD.getSelectedRow();
                if (selectedRow >= 0) {
                    String hoaDonID = jTableHD.getValueAt(selectedRow, 0).toString();
                    loadChiTietHoaDon(hoaDonID);
                }
            }
        });

        // Add document listeners for real-time filtering
        addFilterListener(jcbMinTongTien);
        addFilterListener(jcbMaxTongTien);
        addFilterListener(jcbNamBatDau);
        addFilterListener(jcbNamKetThuc);

        // Add action listeners for combo boxes
        jcbNgayBatDau.addActionListener(e -> {
            try {
                filterAll();
            } catch (SQLException ex) {
                Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        jcbThangBatDau.addActionListener(e -> {
            try {
                filterAll();
            } catch (SQLException ex) {
                Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        jcbNgayKetThuc.addActionListener(e -> {
            try {
                filterAll();
            } catch (SQLException ex) {
                Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        jcbThangKetThuc.addActionListener(e -> {
            try {
                filterAll();
            } catch (SQLException ex) {
                Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void addFilterListener(javax.swing.JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { try {
                filterAll();
                } catch (SQLException ex) {
                    Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
}
            @Override
            public void removeUpdate(DocumentEvent e) { try {
                filterAll();
                } catch (SQLException ex) {
                    Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
}
            @Override
            public void changedUpdate(DocumentEvent e) { try {
                filterAll();
                } catch (SQLException ex) {
                    Logger.getLogger(QLHDPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
}
        });
    }

    private void loadAllHoaDon() {
        try {
            List<HoaDon> hoaDonList = orderinfo.getAllHD();
            DefaultTableModel model = (DefaultTableModel) jTableHD.getModel();
            model.setRowCount(0);
            
            for (HoaDon hd : hoaDonList) {
                model.addRow(new Object[]{
                    hd.getId(),
                    hd.getThoiGian(),
                    hd.getIdKhachHang(),
                    hd.getIdNguoiDung(),
                    hd.getTongTienGoc(),
                    hd.getMucGiamGia(),
                    hd.getTongTienSauGiamGia()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadChiTietHoaDon(String hoaDonID) {
        List<ChiTietHoaDon> chiTietList = orderinfo.getCTHDByHoaDonID(hoaDonID);
        DefaultTableModel model = (DefaultTableModel) jTableCTHD.getModel();
        model.setRowCount(0);
        
        for (ChiTietHoaDon ct : chiTietList) {
            model.addRow(new Object[]{
                ct.getId(),
                ct.getSoLuong(),
                ct.getIdSanPham(),
                ct.getDonGia()
            });
        }
    }

    private void filterAll() throws SQLException {
        // Get time range
        Timestamp fromTime = getTimestampFromDate(true);
        Timestamp toTime = getTimestampFromDate(false);
        // Get amount range
        BigDecimal minTotal = null;
        BigDecimal maxTotal = null;
        try {
            if (!jcbMinTongTien.getText().trim().isEmpty()) {
                minTotal = new BigDecimal(jcbMinTongTien.getText().trim());
            }
        } catch (NumberFormatException e) {
            // Ignore invalid number
        }
        try {
            if (!jcbMaxTongTien.getText().trim().isEmpty()) {
                maxTotal = new BigDecimal(jcbMaxTongTien.getText().trim());
            }
        } catch (NumberFormatException e) {
            // Ignore invalid number
        }
        // Perform search
        List<HoaDon> searchResults = orderinfo.searchHoaDon(fromTime, toTime, minTotal, maxTotal);
        // Update table
        DefaultTableModel model = (DefaultTableModel) jTableHD.getModel();
        model.setRowCount(0);
        for (HoaDon hd : searchResults) {
            model.addRow(new Object[]{
                hd.getId(),
                hd.getThoiGian(),
                hd.getIdKhachHang(),
                hd.getIdNguoiDung(),
                hd.getTongTienGoc(),
                hd.getMucGiamGia(),
                hd.getTongTienSauGiamGia()
            });
        }
    }

    private Timestamp getTimestampFromDate(boolean isFromDate) {
        try {
            String day, month, year;
            
            if (isFromDate) {
                day = jcbNgayBatDau.getSelectedItem().toString();
                month = jcbThangBatDau.getSelectedItem().toString();
                year = jcbNamBatDau.getText().trim();
            } else {
                day = jcbNgayKetThuc.getSelectedItem().toString();
                month = jcbThangKetThuc.getSelectedItem().toString();
                year = jcbNamKetThuc.getText().trim();
            }
            
            if (year.isEmpty()) {
                return null;
            }
            
            String dateString = year + "-" + 
                               (month.length() == 1 ? "0" + month : month) + "-" + 
                               (day.length() == 1 ? "0" + day : day);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse(dateString);
            return new Timestamp(date.getTime());
            
        } catch (ParseException | NumberFormatException e) {
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCTHD = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableHD = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jcbNgayBatDau = new javax.swing.JComboBox<>();
        jcbThangBatDau = new javax.swing.JComboBox<>();
        jcbNamBatDau = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jcbNgayKetThuc = new javax.swing.JComboBox<>();
        jcbThangKetThuc = new javax.swing.JComboBox<>();
        jcbNamKetThuc = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jcbMaxTongTien = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jcbMinTongTien = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        setInheritsPopupMenu(true);

        jTableCTHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID Chi Tiết Hóa Đơn", "Số sản phẩm", "ID Sản phẩm", "Giá bán mỗi sản phẩm"
            }
        ));
        jScrollPane1.setViewportView(jTableCTHD);

        jTableHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Hóa Đơn", "Thời gian", "ID Khách Hàng", "ID Người Dùng", "Tổng tiền gốc", "Mức giảm giá", "Tổng tiền"
            }
        ));
        jScrollPane2.setViewportView(jTableHD);
        if (jTableHD.getColumnModel().getColumnCount() > 0) {
            jTableHD.getColumnModel().getColumn(4).setHeaderValue("Tổng tiền gốc");
            jTableHD.getColumnModel().getColumn(5).setHeaderValue("Mức giảm giá");
            jTableHD.getColumnModel().getColumn(6).setHeaderValue("Tổng tiền");
        }

        jLabel6.setText("Từ");

        jcbNgayBatDau.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        jcbThangBatDau.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jcbThangBatDau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbThangBatDauActionPerformed(evt);
            }
        });

        jcbNamBatDau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbNamBatDauActionPerformed(evt);
            }
        });

        jLabel7.setText("đến");

        jcbNgayKetThuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        jcbThangKetThuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jcbThangKetThuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbThangKetThucActionPerformed(evt);
            }
        });

        jcbNamKetThuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbNamKetThucActionPerformed(evt);
            }
        });

        jLabel3.setText("Tổng tiền:");

        jLabel5.setText("Thời gian:");

        jcbMaxTongTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbMaxTongTienActionPerformed(evt);
            }
        });

        jLabel8.setText("Từ");

        jcbMinTongTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbMinTongTienActionPerformed(evt);
            }
        });

        jLabel9.setText("đến");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbMinTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbMaxTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jcbThangBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jcbNamBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jcbThangKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jcbNamKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jLabel5)
                        .addComponent(jcbNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jcbThangBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jcbNamBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jcbNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jcbThangKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jcbNamKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jcbMinTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel3)
                    .addComponent(jcbMaxTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bmonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bmonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bmonthActionPerformed

    private void byearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_byearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_byearActionPerformed

    private void EmonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmonthActionPerformed

    private void EyearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EyearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EyearActionPerformed


    private void FIlterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FIlterActionPerformed

    }//GEN-LAST:event_FIlterActionPerformed

    private void jcbThangBatDauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbThangBatDauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbThangBatDauActionPerformed

    private void jcbNamBatDauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbNamBatDauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbNamBatDauActionPerformed

    private void jcbThangKetThucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbThangKetThucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbThangKetThucActionPerformed

    private void jcbNamKetThucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbNamKetThucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbNamKetThucActionPerformed

    private void jcbMaxTongTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbMaxTongTienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbMaxTongTienActionPerformed

    private void jcbMinTongTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbMinTongTienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbMinTongTienActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableCTHD;
    private javax.swing.JTable jTableHD;
    private javax.swing.JTextField jcbMaxTongTien;
    private javax.swing.JTextField jcbMinTongTien;
    private javax.swing.JTextField jcbNamBatDau;
    private javax.swing.JTextField jcbNamKetThuc;
    private javax.swing.JComboBox<String> jcbNgayBatDau;
    private javax.swing.JComboBox<String> jcbNgayKetThuc;
    private javax.swing.JComboBox<String> jcbThangBatDau;
    private javax.swing.JComboBox<String> jcbThangKetThuc;
    // End of variables declaration//GEN-END:variables
}
