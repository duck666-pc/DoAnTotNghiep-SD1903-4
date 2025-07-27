/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import Controller.DOANHTHUDAO;
import Controller.DOANHTHUDAO.DoanhThuSanPham;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class DOANHTHUPanel extends javax.swing.JPanel {

    private DOANHTHUDAO doanhThuDAO;
    private DefaultTableModel tableModel;
    private NumberFormat currencyFormat;

    public DOANHTHUPanel() {
        initComponents();
        initializeBusinessLogic();
        setupEventListeners(); // Add this line
    }

    public void initializeBusinessLogic() {
        try {
            doanhThuDAO = new DOANHTHUDAO();
            currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            setupTableModel();
            initializeDateFields();
            loadInitialData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khởi tạo: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Add this new method to setup event listeners
    private void setupEventListeners() {
        // Add action listeners for combo boxes
        ActionListener dateChangeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Only filter if both start and end years are filled
                if (!jcbNamBatDau.getText().trim().isEmpty()
                        && !jcbNamKetThuc.getText().trim().isEmpty()) {
                    filterData();
                }
            }
        };

        jcbNgayBatDau.addActionListener(dateChangeListener);
        jcbThangBatDau.addActionListener(dateChangeListener);
        jcbNgayKetThuc.addActionListener(dateChangeListener);
        jcbThangKetThuc.addActionListener(dateChangeListener);

        // Add document listeners for text fields (year inputs)
        DocumentListener yearChangeListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleYearChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleYearChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleYearChange();
            }

            private void handleYearChange() {
                // Use SwingUtilities.invokeLater to ensure the text field has been updated
                javax.swing.SwingUtilities.invokeLater(() -> {
                    String startYear = jcbNamBatDau.getText().trim();
                    String endYear = jcbNamKetThuc.getText().trim();
                    
                    if (startYear.isEmpty() && endYear.isEmpty()) {
                        // If both years are empty, load all data
                        loadInitialData();
                    } else if (!startYear.isEmpty() && !endYear.isEmpty()) {
                        // If both years are filled, filter data
                        try {
                            Integer.valueOf(startYear);
                            Integer.valueOf(endYear);
                            filterData();
                        } catch (NumberFormatException ex) {
                            // Invalid year format, do nothing
                        }
                    }
                });
            }
        };

        jcbNamBatDau.getDocument().addDocumentListener(yearChangeListener);
        jcbNamKetThuc.getDocument().addDocumentListener(yearChangeListener);
    }

    private void setupTableModel() {
        String[] columnNames = {"ID Sản phẩm", "Tên sản phẩm", "Giá", "Số lượng", "Thành tiền"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(tableModel);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(80);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(120);
    }

    private void initializeDateFields() {
        jcbNgayBatDau.setSelectedIndex(0);
        jcbThangBatDau.setSelectedIndex(0);
        jcbNamBatDau.setText("");

        jcbNgayKetThuc.setSelectedIndex(0);
        jcbThangKetThuc.setSelectedIndex(0);
        jcbNamKetThuc.setText("");
    }

    private void loadInitialData() {
        try {
            // Load all products with their total sales data
            List<DoanhThuSanPham> danhSach = doanhThuDAO.getAllProductsWithSales();
            updateTable(danhSach);

            // Calculate and display total statistics for all time
            int tongSanPham = calculateTotalProductsSold(danhSach);
            double tongDoanhThu = calculateTotalRevenue(danhSach);
            updateSummaryLabels(tongSanPham, tongDoanhThu);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void filterData() {
        try {
            String startYear = jcbNamBatDau.getText().trim();
            String endYear = jcbNamKetThuc.getText().trim();

            if (startYear.isEmpty() || endYear.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập đầy đủ thông tin ngày tháng!",
                        "Thiếu thông tin",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int dayStart, monthStart, yearStart;
            int dayEnd, monthEnd, yearEnd;

            try {
                dayStart = Integer.parseInt((String) jcbNgayBatDau.getSelectedItem());
                monthStart = Integer.parseInt((String) jcbThangBatDau.getSelectedItem());
                yearStart = Integer.parseInt(startYear);

                dayEnd = Integer.parseInt((String) jcbNgayKetThuc.getSelectedItem());
                monthEnd = Integer.parseInt((String) jcbThangKetThuc.getSelectedItem());
                yearEnd = Integer.parseInt(endYear);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Dữ liệu ngày tháng không hợp lệ! Vui lòng kiểm tra lại.",
                        "Lỗi định dạng",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String ngayBatDau = DOANHTHUDAO.formatDate(dayStart, monthStart, yearStart);
            String ngayKetThuc = DOANHTHUDAO.formatDate(dayEnd, monthEnd, yearEnd);

            if (!isValidDateRange(ngayBatDau, ngayKetThuc)) {
                JOptionPane.showMessageDialog(this,
                        "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc!",
                        "Lỗi ngày tháng",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Get filtered data for the date range
            List<DoanhThuSanPham> danhSach = doanhThuDAO.getDoanhThuTheoKhoangThoiGian(ngayBatDau, ngayKetThuc);
            updateTable(danhSach);

            // Get totals for the date range from database
            int tongSanPham = doanhThuDAO.getTongSanPhamBanRa(ngayBatDau, ngayKetThuc);
            double tongDoanhThu = doanhThuDAO.getTongDoanhThu(ngayBatDau, ngayKetThuc);
            updateSummaryLabels(tongSanPham, tongDoanhThu);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi hệ thống: " + e.getMessage(),
                    "Lỗi nghiêm trọng",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Helper methods to calculate totals from the list
    private int calculateTotalProductsSold(List<DoanhThuSanPham> danhSach) {
        return danhSach.stream()
                .mapToInt(DoanhThuSanPham::getSoLuongBan)
                .sum();
    }

    private double calculateTotalRevenue(List<DoanhThuSanPham> danhSach) {
        return danhSach.stream()
                .mapToDouble(DoanhThuSanPham::getThanhTien)
                .sum();
    }

    private String getSelectedStartDate() {
        if (jcbNamBatDau.getText().trim().isEmpty()) {
            return null;
        }

        int day = Integer.parseInt((String) jcbNgayBatDau.getSelectedItem());
        int month = Integer.parseInt((String) jcbThangBatDau.getSelectedItem());
        int year = Integer.parseInt(jcbNamBatDau.getText().trim());

        return DOANHTHUDAO.formatDate(day, month, year);
    }

    private String getSelectedEndDate() {
        if (jcbNamKetThuc.getText().trim().isEmpty()) {
            return null;
        }

        int day = Integer.parseInt((String) jcbNgayKetThuc.getSelectedItem());
        int month = Integer.parseInt((String) jcbThangKetThuc.getSelectedItem());
        int year = Integer.parseInt(jcbNamKetThuc.getText().trim());

        return DOANHTHUDAO.formatDate(day, month, year);
    }

    private boolean isValidDateRange(String startDate, String endDate) {
        return startDate.compareTo(endDate) <= 0;
    }

    private void updateTable(List<DoanhThuSanPham> danhSach) {
        tableModel.setRowCount(0);
        for (DoanhThuSanPham item : danhSach) {
            Object[] row = {
                item.getIdSanPham(),
                item.getTenSanPham(),
                currencyFormat.format(item.getGia()),
                item.getSoLuongBan(),
                currencyFormat.format(item.getThanhTien())
            };
            tableModel.addRow(row);
        }
        tableModel.fireTableDataChanged(); // Ensure table updates
    }

    private void updateSummaryLabels(int tongSanPham, double tongDoanhThu) {
        jLabel1.setText(String.valueOf(tongSanPham));
        jLabel2.setText(currencyFormat.format(tongDoanhThu));

        // Force label refresh
        jLabel1.repaint();
        jLabel2.repaint();
    }

    public void cleanup() {
        if (doanhThuDAO != null) {
            doanhThuDAO.closeConnection();
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
        jTable1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jcbNgayBatDau = new javax.swing.JComboBox<>();
        jcbThangBatDau = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jcbNamBatDau = new javax.swing.JTextField();
        jcbNgayKetThuc = new javax.swing.JComboBox<>();
        jcbThangKetThuc = new javax.swing.JComboBox<>();
        jcbNamKetThuc = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jbtTimKiem = new javax.swing.JButton();
        jbtLamMoi = new javax.swing.JButton();
        jbtBaoCao = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        setInheritsPopupMenu(true);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Sản phẩm", "Tên sản phẩm", "Giá", "Số lượng", "Thành tiền"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel6.setText("Từ:");

        jcbNgayBatDau.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        jcbThangBatDau.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jcbThangBatDau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbThangBatDauActionPerformed(evt);
            }
        });

        jLabel7.setText("Đến:");

        jcbNamBatDau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbNamBatDauActionPerformed(evt);
            }
        });

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

        jLabel8.setText("Tổng sản phẩm bán ra:");

        jLabel9.setText("Doanh thu:");

        jLabel1.setText("  ");

        jLabel2.setText("  ");

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

        jbtBaoCao.setBackground(new java.awt.Color(41, 62, 80));
        jbtBaoCao.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtBaoCao.setForeground(new java.awt.Color(255, 255, 255));
        jbtBaoCao.setText("Xuất Báo Cáo");
        jbtBaoCao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtBaoCaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcbNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcbThangKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcbNamKetThuc))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jcbNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jcbThangBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jcbNamBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbtTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtLamMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtBaoCao))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jcbNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addComponent(jcbThangBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jcbNamBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbtTimKiem)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcbNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addComponent(jcbThangKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jcbNamKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jbtLamMoi))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel1)
                    .addComponent(jbtBaoCao))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel2))
                .addGap(29, 29, 29))
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

    private void jcbThangBatDauActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void jcbNamBatDauActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void jcbThangKetThucActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void jcbNamKetThucActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void FIlterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FIlterActionPerformed
        filterData();
    }//GEN-LAST:event_FIlterActionPerformed

    private void jbtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtTimKiemActionPerformed

    }//GEN-LAST:event_jbtTimKiemActionPerformed

    private void jbtLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLamMoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtLamMoiActionPerformed

    private void jbtBaoCaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtBaoCaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtBaoCaoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton jbtBaoCao;
    private javax.swing.JButton jbtLamMoi;
    private javax.swing.JButton jbtTimKiem;
    private javax.swing.JTextField jcbNamBatDau;
    private javax.swing.JTextField jcbNamKetThuc;
    private javax.swing.JComboBox<String> jcbNgayBatDau;
    private javax.swing.JComboBox<String> jcbNgayKetThuc;
    private javax.swing.JComboBox<String> jcbThangBatDau;
    private javax.swing.JComboBox<String> jcbThangKetThuc;
    // End of variables declaration//GEN-END:variables
}
