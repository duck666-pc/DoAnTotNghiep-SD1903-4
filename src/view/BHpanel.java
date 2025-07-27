/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import Model.HoaDon;
import controller.BHDAO1;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Admin
 */
public final class BHpanel extends javax.swing.JPanel {

    private final BHDAO1 BHdao;
    private final DefaultTableModel modelhd;
    private final DefaultTableModel modelcthd;
    private final DefaultTableModel modelsp;
    private int currentRowHD = -1;
    private int currentRowSP = -1;
    private int currentRowCTHD = -1;
    private final ArrayList<HoaDon> hdList;

    /**
     * Creates new form BHpanel
     */
    public BHpanel() {
        this.BHdao = new BHDAO1();
        this.hdList = new ArrayList<>();

        initComponents();

        this.modelhd = (DefaultTableModel) tblhd.getModel();
        this.modelcthd = (DefaultTableModel) tblcthd.getModel();
        this.modelsp = (DefaultTableModel) tblsp.getModel();

        displayHD();
        displaySP();
    }

    /**
     * Displays all invoices in the table
     */
    public void displayHD() {
        modelhd.setRowCount(0);
        hdList.clear();

        try (ResultSet rs = BHdao.getHD()) {
            if (rs != null) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon(
                            rs.getString(1),
                            rs.getTimestamp(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getBigDecimal(5),
                            rs.getBigDecimal(6),
                            rs.getBigDecimal(7)
                    );

                    modelhd.addRow(new Object[]{
                        rs.getString(1),
                        rs.getTimestamp(2),
                        rs.getString(3),
                        rs.getString(4)
                    });

                    hdList.add(hd);
                }
            }
        } catch (SQLException e) {
            showErrorMessage("Có lỗi trong lúc tải danh sách hóa đơn: " + e.getMessage());
        }
    }

    /**
     * Displays all products in the table
     */
    public void displaySP() {
        modelsp.setRowCount(0);

        try (ResultSet rs = BHdao.getSP()) {
            if (rs != null) {
                while (rs.next()) {
                    modelsp.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getDouble(3)
                    });
                }
            }
        } catch (SQLException e) {
            showErrorMessage("Có lỗi trong lúc tải danh sách sản phẩm: " + e.getMessage());
        }
    }

    /**
     * Displays invoice details for a specific invoice
     *
     * @param ID Invoice ID
     */
    public void displayCTHD(String ID) {
        if (ID == null || ID.trim().isEmpty()) {
            return;
        }

        modelcthd.setRowCount(0);
        BigDecimal tongtien = BigDecimal.ZERO;

        try (ResultSet rs = BHdao.getCTHD(ID)) {
            if (rs != null) {
                while (rs.next()) {
                    double soLuong = rs.getDouble(3);
                    double donGia = rs.getDouble(4);
                    double thanhTien = soLuong * donGia;

                    modelcthd.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        soLuong,
                        donGia,
                        thanhTien
                    });

                    tongtien = tongtien.add(BigDecimal.valueOf(thanhTien));
                }
            }

            txtTongtien.setText(tongtien.setScale(2, RoundingMode.HALF_UP).toString());
        } catch (SQLException e) {
            showErrorMessage("Có lỗi trong lúc tải chi tiết hóa đơn: " + e.getMessage());
        }
    }

    /**
     * Validates input fields for invoice creation/update
     *
     * @return true if all fields are valid
     */
    private boolean validateInvoiceFields() {
        String id = txtidhd.getText().trim();
        String tgian = txttgian.getText().trim();
        String idkh = txtidkh.getText().trim();
        String idnv = txtidnv.getText().trim();
        String mgg = txtmgg.getText().trim();

        if (id.isEmpty()) {
            showErrorMessage("ID hóa đơn không được để trống");
            txtidhd.requestFocus();
            return false;
        }

        if (tgian.isEmpty()) {
            showErrorMessage("Thời gian không được để trống");
            txttgian.requestFocus();
            return false;
        }

        if (idkh.isEmpty()) {
            showErrorMessage("ID khách hàng không được để trống");
            txtidkh.requestFocus();
            return false;
        }

        if (idnv.isEmpty()) {
            showErrorMessage("ID nhân viên không được để trống");
            txtidnv.requestFocus();
            return false;
        }

        if (mgg.isEmpty()) {
            showErrorMessage("Mã giảm giá không được để trống");
            txtmgg.requestFocus();
            return false;
        }

        // Validate discount amount is numeric
        try {
            Double.valueOf(mgg);
        } catch (NumberFormatException e) {
            showErrorMessage("Mã giảm giá phải là số");
            txtmgg.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Validates input fields for invoice detail creation/update
     *
     * @return true if all fields are valid
     */
    private boolean validateInvoiceDetailFields() {
        if (currentRowHD == -1) {
            showErrorMessage("Vui lòng chọn hóa đơn trước");
            return false;
        }

        if (currentRowSP == -1) {
            showErrorMessage("Vui lòng chọn sản phẩm");
            return false;
        }

        String idcthd = txtidcthd.getText().trim();
        String sl = txtsl.getText().trim();

        if (idcthd.isEmpty()) {
            showErrorMessage("ID chi tiết hóa đơn không được để trống");
            txtidcthd.requestFocus();
            return false;
        }

        if (sl.isEmpty()) {
            showErrorMessage("Số lượng không được để trống");
            txtsl.requestFocus();
            return false;
        }

        try {
            int quantity = Integer.parseInt(sl);
            if (quantity <= 0) {
                showErrorMessage("Số lượng phải lớn hơn 0");
                txtsl.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Số lượng phải là số nguyên dương");
            txtsl.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Shows error message dialog
     *
     * @param message Error message to display
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows success message dialog
     *
     * @param message Success message to display
     */
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Clears all input fields
     */
    private void clearFields() {
        txtidhd.setText("");
        txttgian.setText("");
        txtidkh.setText("");
        txtidnv.setText("");
        txtmgg.setText("");
        txtidcthd.setText("");
        txtsl.setText("");
        txtTongtien.setText("");
    }

    /**
     * Updates the total amount for the current invoice
     */
    private void updateInvoiceTotal() {
        if (currentRowHD >= 0) {
            String idHD = String.valueOf(tblhd.getValueAt(currentRowHD, 0));
            double calculatedTotal = BHdao.calculateInvoiceTotal(idHD);
            boolean success = BHdao.updateHoaDonTotal(calculatedTotal, idHD);

            if (success) {
                displayHD(); // Refresh the invoice list to show updated totals
            }
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

        txttgian = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblcthd = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtidhd = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btntaoHD = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnthaydoiHD = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblhd = new javax.swing.JTable();
        btnxoaHD = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtmgg = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtTongtien = new javax.swing.JTextField();
        btnthanhtoan = new javax.swing.JButton();
        btnadd = new javax.swing.JButton();
        btndelete = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtidcthd = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtsl = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtidnv = new javax.swing.JTextField();
        btnupdate = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblsp = new javax.swing.JTable();
        txtidkh = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        tblcthd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblcthd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblcthdMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblcthd);

        jScrollPane4.setViewportView(jScrollPane2);

        jLabel10.setText("Thời Gian");

        jLabel11.setText("ID Chi Tiết Hóa Đơn");

        txtidhd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidhdActionPerformed(evt);
            }
        });

        jLabel1.setText("Hóa Đơn");

        btntaoHD.setBackground(new java.awt.Color(41, 62, 80));
        btntaoHD.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btntaoHD.setForeground(new java.awt.Color(255, 255, 255));
        btntaoHD.setText("Tạo Hóa Đơn");
        btntaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntaoHDActionPerformed(evt);
            }
        });

        jLabel2.setText("Chi tiết hóa đơn");

        btnthaydoiHD.setBackground(new java.awt.Color(41, 62, 80));
        btnthaydoiHD.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnthaydoiHD.setForeground(new java.awt.Color(255, 255, 255));
        btnthaydoiHD.setText("Sửa Hóa Đơn");
        btnthaydoiHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthaydoiHDActionPerformed(evt);
            }
        });

        tblhd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Thời Gian", "Khách Hàng", "Người Dùng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblhd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblhdMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblhd);

        jScrollPane5.setViewportView(jScrollPane1);

        btnxoaHD.setBackground(new java.awt.Color(41, 62, 80));
        btnxoaHD.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnxoaHD.setForeground(new java.awt.Color(255, 255, 255));
        btnxoaHD.setText("Xóa Hóa Đơn");
        btnxoaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxoaHDActionPerformed(evt);
            }
        });

        jLabel9.setText("Chi tiết hóa đơn");

        jLabel12.setText("Hóa Đơn");

        jLabel3.setText("Sản Phẩm");

        jLabel4.setText("Tổng Tiền");

        jLabel13.setText("Mã Giảm Giá");

        btnthanhtoan.setBackground(new java.awt.Color(41, 62, 80));
        btnthanhtoan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnthanhtoan.setForeground(new java.awt.Color(255, 255, 255));
        btnthanhtoan.setText("Thanh Toán");
        btnthanhtoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthanhtoanActionPerformed(evt);
            }
        });

        btnadd.setBackground(new java.awt.Color(41, 62, 80));
        btnadd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnadd.setForeground(new java.awt.Color(255, 255, 255));
        btnadd.setText("Thêm Vào Hóa Đơn");
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });

        btndelete.setBackground(new java.awt.Color(41, 62, 80));
        btndelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btndelete.setForeground(new java.awt.Color(255, 255, 255));
        btndelete.setText("Xóa Khỏi Hóa Đơn");
        btndelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndeleteActionPerformed(evt);
            }
        });

        jLabel5.setText("ID Hóa Đơn");

        jLabel6.setText("Số Lượng");

        jLabel7.setText("ID Nhân Viên");

        btnupdate.setBackground(new java.awt.Color(41, 62, 80));
        btnupdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnupdate.setForeground(new java.awt.Color(255, 255, 255));
        btnupdate.setText("Thay Đổi Hóa Đơn");
        btnupdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnupdateActionPerformed(evt);
            }
        });

        jLabel8.setText("ID Khách Hàng");

        tblsp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Tên", "Giá"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane7.setViewportView(tblsp);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(btnadd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnupdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btndelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnthanhtoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel11)
                                            .addComponent(txtidcthd, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtsl, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel4)
                                            .addComponent(txtTongtien, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txttgian, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel5)
                                    .addComponent(txtidhd, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(txtidnv, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                            .addComponent(txtidkh))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtmgg)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnthaydoiHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnxoaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btntaoHD, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTongtien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(22, 22, 22)
                                                .addComponent(txtidhd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(38, 38, 38)
                                        .addComponent(txttgian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtidcthd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtsl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtidkh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(16, 16, 16)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel13))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtidnv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtmgg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(2, 2, 2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(28, 28, 28))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnthanhtoan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnadd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnupdate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btndelete))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btntaoHD)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnthaydoiHD)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnxoaHD)))))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblcthdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblcthdMouseClicked
        try {
            currentRowCTHD = tblcthd.getSelectedRow();
            if (currentRowCTHD >= 0) {
                String idct = String.valueOf(tblcthd.getValueAt(currentRowCTHD, 0));
                double sl = Double.parseDouble(String.valueOf(tblcthd.getValueAt(currentRowCTHD, 2)));
                String idsp = String.valueOf(tblcthd.getValueAt(currentRowCTHD, 1));

                txtidcthd.setText(idct);
                txtsl.setText(String.valueOf((int) sl));

                // Find and select the corresponding product in the product table
                for (int i = 0; i < tblsp.getRowCount(); i++) {
                    if (String.valueOf(tblsp.getValueAt(i, 0)).equals(idsp)) {
                        tblsp.setRowSelectionInterval(i, i);
                        currentRowSP = i;
                        break;
                    }
                }
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Lỗi khi đọc dữ liệu từ bảng chi tiết hóa đơn");
        }
    }//GEN-LAST:event_tblcthdMouseClicked

    private void txtidhdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidhdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidhdActionPerformed

    private void btntaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntaoHDActionPerformed
        if (!validateInvoiceFields()) {
            return;
        }

        try {
            String ID = txtidhd.getText().trim();
            String tgian = txttgian.getText().trim();
            String IDKH = txtidkh.getText().trim();
            String IDND = txtidnv.getText().trim();
            double magiamgia = Double.parseDouble(txtmgg.getText().trim());

            boolean success = BHdao.addHD(ID, tgian, IDKH, IDND, 0.0, magiamgia, 0.0);

            if (success) {
                displayHD();
                modelcthd.setRowCount(0);
                clearFields();
                showSuccessMessage("Tạo hóa đơn thành công");
            } else {
                showErrorMessage("Tạo hóa đơn thất bại. Có thể ID đã tồn tại.");
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Mã giảm giá phải là số hợp lệ");
        } catch (Exception e) {
            showErrorMessage("Tạo hóa đơn thất bại: " + e.getMessage());
        }
    }//GEN-LAST:event_btntaoHDActionPerformed

    private void btnthaydoiHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthaydoiHDActionPerformed
        if (currentRowHD == -1) {
            showErrorMessage("Vui lòng chọn hóa đơn cần sửa");
            return;
        }

        if (!validateInvoiceFields()) {
            return;
        }

        try {
            String ID = txtidhd.getText().trim();
            String tgian = txttgian.getText().trim();
            String IDKH = txtidkh.getText().trim();
            String IDND = txtidnv.getText().trim();
            double magiamgia = Double.parseDouble(txtmgg.getText().trim());
            String IDcheck = String.valueOf(tblhd.getValueAt(currentRowHD, 0));

            // Get current totals from the HoaDon object
            HoaDon currentHD = hdList.get(currentRowHD);
            double tongTien = currentHD.getTongTienGoc().doubleValue();
            double tongTienSauGiamGia = tongTien - magiamgia;

            boolean success = BHdao.updateHD(ID, tgian, IDKH, IDND, tongTien, magiamgia, tongTienSauGiamGia, IDcheck);

            if (success) {
                displayHD();
                modelcthd.setRowCount(0);
                clearFields();
                showSuccessMessage("Sửa hóa đơn thành công");
            } else {
                showErrorMessage("Sửa hóa đơn thất bại");
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Mã giảm giá phải là số hợp lệ");
        } catch (Exception e) {
            showErrorMessage("Sửa hóa đơn thất bại: " + e.getMessage());
        }
    }//GEN-LAST:event_btnthaydoiHDActionPerformed

    private void tblhdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblhdMouseClicked
        try {
            currentRowHD = tblhd.getSelectedRow();
            if (currentRowHD >= 0 && currentRowHD < hdList.size()) {
                String ID = String.valueOf(tblhd.getValueAt(currentRowHD, 0));
                String tgian = String.valueOf(tblhd.getValueAt(currentRowHD, 1));
                String IDKH = String.valueOf(tblhd.getValueAt(currentRowHD, 2));
                String IDND = String.valueOf(tblhd.getValueAt(currentRowHD, 3));
                String magiamgia = String.valueOf(hdList.get(currentRowHD).getMucGiamGia());

                txtidhd.setText(ID);
                txttgian.setText(tgian);
                txtidkh.setText(IDKH);
                txtidnv.setText(IDND);
                txtmgg.setText(magiamgia);

                displayCTHD(ID);
            }
        } catch (Exception e) {
            showErrorMessage("Lỗi khi hiển thị thông tin hóa đơn: " + e.getMessage());
        }
    }//GEN-LAST:event_tblhdMouseClicked

    private void btnxoaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxoaHDActionPerformed
        if (currentRowHD == -1) {
            showErrorMessage("Vui lòng chọn hóa đơn cần xóa");
            return;
        }

        String ID = String.valueOf(tblhd.getValueAt(currentRowHD, 0));

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa hóa đơn " + ID + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = BHdao.deleteHD(ID);

                if (success) {
                    displayHD();
                    modelcthd.setRowCount(0);
                    clearFields();
                    currentRowHD = -1;
                    showSuccessMessage("Xóa hóa đơn thành công");
                } else {
                    showErrorMessage("Xóa hóa đơn thất bại");
                }
            } catch (Exception e) {
                showErrorMessage("Xóa hóa đơn thất bại: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnxoaHDActionPerformed

    private void btnthanhtoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthanhtoanActionPerformed
        currentRowCTHD = tblcthd.getSelectedRow();
        currentRowSP = tblsp.getSelectedRow();
        currentRowHD = tblhd.getSelectedRow();
        if (currentRowHD == -1) {
            showErrorMessage("Vui lòng chọn hóa đơn cần thanh toán");
            return;
        }

        if (tblcthd.getRowCount() == 0) {
            showErrorMessage("Hóa đơn chưa có sản phẩm nào");
            return;
        }

        try {
            String fileName = "HoaDon_" + System.currentTimeMillis() + ".pdf";
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(fileName));
            doc.open();

            // Create fonts that support Vietnamese characters - using UTF-8 encoding
            BaseFont baseFont;
            Font titleFont;
            Font normalFont;
            Font headerFont;

            try {
                // Try to create font with UTF-8 encoding first
                baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "UTF-8", BaseFont.EMBEDDED);
                titleFont = new Font(baseFont, 18, Font.BOLD);
                normalFont = new Font(baseFont, 12, Font.NORMAL);
                headerFont = new Font(baseFont, 12, Font.BOLD);
            } catch (Exception fontException) {
                // Fallback to default fonts if UTF-8 fails
                System.out.println("UTF-8 encoding failed, using default fonts: " + fontException.getMessage());
                titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
                normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
                headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            }

            // Add invoice header
            Paragraph title = new Paragraph("HOA DON BAN HANG", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph("\n"));

            // Add invoice information
            doc.add(new Paragraph("Khach hang: " + tblhd.getValueAt(currentRowHD, 2), normalFont));
            doc.add(new Paragraph("Thoi gian: " + tblhd.getValueAt(currentRowHD, 1), normalFont));
            doc.add(new Paragraph("Nhan vien: " + tblhd.getValueAt(currentRowHD, 3), normalFont));
            doc.add(new Paragraph("\n"));

            // Create table for invoice details
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 1, 2, 2});

            // Add table headers
            PdfPCell cell1 = new PdfPCell(new Phrase("San pham", headerFont));
            PdfPCell cell2 = new PdfPCell(new Phrase("So luong", headerFont));
            PdfPCell cell3 = new PdfPCell(new Phrase("Don gia", headerFont));
            PdfPCell cell4 = new PdfPCell(new Phrase("Thanh tien", headerFont));

            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);

            // Add table data
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (int i = 0; i < tblcthd.getRowCount(); i++) {
                PdfPCell cellData1 = new PdfPCell(new Phrase(tblcthd.getValueAt(i, 1).toString(), normalFont));
                PdfPCell cellData2 = new PdfPCell(new Phrase(tblcthd.getValueAt(i, 2).toString(), normalFont));
                PdfPCell cellData3 = new PdfPCell(new Phrase(String.format("%,.0f", Double.valueOf(tblcthd.getValueAt(i, 3).toString())), normalFont));

                double thanhTien = Double.parseDouble(tblcthd.getValueAt(i, 4).toString());
                PdfPCell cellData4 = new PdfPCell(new Phrase(String.format("%,.0f", thanhTien), normalFont));

                cellData2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellData3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellData4.setHorizontalAlignment(Element.ALIGN_RIGHT);

                table.addCell(cellData1);
                table.addCell(cellData2);
                table.addCell(cellData3);
                table.addCell(cellData4);

                totalAmount = totalAmount.add(BigDecimal.valueOf(thanhTien));
            }

            doc.add(table);
            doc.add(new Paragraph("\n"));

            // Add total
            HoaDon currentInvoice = hdList.get(currentRowHD);
            doc.add(new Paragraph("Tong tien goc: " + String.format("%,.0f VND", totalAmount.doubleValue()), normalFont));
            doc.add(new Paragraph("Giam gia: " + String.format("%,.0f VND", currentInvoice.getMucGiamGia().doubleValue()), normalFont));
            doc.add(new Paragraph("Tong tien sau giam gia: " + String.format("%,.0f VND",
                    totalAmount.subtract(currentInvoice.getMucGiamGia()).doubleValue()), normalFont));

            doc.close();
            showSuccessMessage("Thanh toan thanh cong! File PDF da duoc tao: " + fileName);

        } catch (DocumentException | FileNotFoundException e) {
            showErrorMessage("Tao hoa don PDF that bai: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showErrorMessage("Thanh toan that bai: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnthanhtoanActionPerformed

    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
        if (!validateInvoiceDetailFields()) {
            return;
        }

        try {
            String IDHD = String.valueOf(tblhd.getValueAt(currentRowHD, 0));
            String IDSP = String.valueOf(tblsp.getValueAt(currentRowSP, 0));
            String IdCtHd = txtidcthd.getText().trim();
            int soluong = Integer.parseInt(txtsl.getText().trim());
            double GiaBan = Double.parseDouble(String.valueOf(tblsp.getValueAt(currentRowSP, 2)));

            boolean success = BHdao.addCTHD(IdCtHd, soluong, IDHD, IDSP, GiaBan);

            if (success) {
                displayCTHD(IDHD);
                updateInvoiceTotal();

                // Clear detail fields
                txtidcthd.setText("");
                txtsl.setText("");

                showSuccessMessage("Thêm sản phẩm vào hóa đơn thành công");
            } else {
                showErrorMessage("Thêm sản phẩm thất bại. Có thể ID chi tiết đã tồn tại.");
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Số lượng phải là số nguyên hợp lệ");
        } catch (Exception e) {
            showErrorMessage("Thêm sản phẩm thất bại: " + e.getMessage());
        }

    }//GEN-LAST:event_btnaddActionPerformed

    private void btndeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndeleteActionPerformed
        currentRowCTHD = tblcthd.getSelectedRow();
        currentRowSP = tblsp.getSelectedRow();
        currentRowHD = tblhd.getSelectedRow();
        if (currentRowHD == -1) {
            showErrorMessage("Vui lòng chọn hóa đơn");
            return;
        }

        if (currentRowCTHD == -1) {
            showErrorMessage("Vui lòng chọn sản phẩm cần xóa");
            return;
        }

        try {
            String IDHD = String.valueOf(tblhd.getValueAt(currentRowHD, 0));
            String ID = String.valueOf(tblcthd.getValueAt(currentRowCTHD, 1));

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn xóa sản phẩm này khỏi hóa đơn?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = BHdao.deleteCTHD(IDHD, ID);

                if (success) {
                    displayCTHD(IDHD);
                    updateInvoiceTotal();

                    // Clear detail fields
                    txtidcthd.setText("");
                    txtsl.setText("");
                    currentRowCTHD = -1;

                    showSuccessMessage("Xóa sản phẩm khỏi hóa đơn thành công");
                } else {
                    showErrorMessage("Xóa sản phẩm thất bại");
                }
            }
        } catch (Exception e) {
            showErrorMessage("Xóa sản phẩm thất bại: " + e.getMessage());
        }

    }//GEN-LAST:event_btndeleteActionPerformed

    private void btnupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnupdateActionPerformed
        currentRowCTHD = tblcthd.getSelectedRow();
        currentRowSP = tblsp.getSelectedRow();
        currentRowHD = tblhd.getSelectedRow();
        if (currentRowCTHD == -1) {
            showErrorMessage("Vui lòng chọn chi tiết hóa đơn cần sửa");
            return;
        }

        if (!validateInvoiceDetailFields()) {
            return;
        }

        try {
            String ID = txtidcthd.getText().trim();
            int sl = Integer.parseInt(txtsl.getText().trim());
            String idsp = String.valueOf(tblsp.getValueAt(currentRowSP, 0));
            double gia = Double.parseDouble(String.valueOf(tblsp.getValueAt(currentRowSP, 2)));
            String IDCTHD = String.valueOf(tblcthd.getValueAt(currentRowCTHD, 0));
            String IDHD = String.valueOf(tblhd.getValueAt(currentRowHD, 0));

            System.out.println(idsp + " " + gia);
            boolean success = BHdao.updateCTHD1(ID, sl, idsp, gia, IDCTHD);

            if (success) {
                displayCTHD(IDHD);
                updateInvoiceTotal();

                // Clear detail fields
                txtidcthd.setText("");
                txtsl.setText("");

                showSuccessMessage("Cập nhật chi tiết hóa đơn thành công");
            } else {
                showErrorMessage("Cập nhật chi tiết hóa đơn thất bại");
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Số lượng phải là số nguyên hợp lệ");
        } catch (Exception e) {
            showErrorMessage("Cập nhật chi tiết hóa đơn thất bại: " + e.getMessage());
        }

    }//GEN-LAST:event_btnupdateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btndelete;
    private javax.swing.JButton btntaoHD;
    private javax.swing.JButton btnthanhtoan;
    private javax.swing.JButton btnthaydoiHD;
    private javax.swing.JButton btnupdate;
    private javax.swing.JButton btnxoaHD;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable tblcthd;
    private javax.swing.JTable tblhd;
    private javax.swing.JTable tblsp;
    private javax.swing.JTextField txtTongtien;
    private javax.swing.JTextField txtidcthd;
    private javax.swing.JTextField txtidhd;
    private javax.swing.JTextField txtidkh;
    private javax.swing.JTextField txtidnv;
    private javax.swing.JTextField txtmgg;
    private javax.swing.JTextField txtsl;
    private javax.swing.JTextField txttgian;
    // End of variables declaration//GEN-END:variables
}
