/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.QLNVDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.sql.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import model.NhanVien;

/**
 *
 * @author minhd
 */
public class KiemTraThongTin extends javax.swing.JFrame {

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color BACKGROUND_COLOR = new Color(246, 248, 250);
    private static final Color TEXT_COLOR = new Color(73, 80, 87);
    private static final Color BORDER_COLOR = new Color(220, 221, 225);

    private final QLNVDAO qlnv = new QLNVDAO();

    public KiemTraThongTin() {
        initComponents();
        customizeUI();
        centerWindow();
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
        setResizable(true);
        // Set minimum size to ensure all components are visible
        setMinimumSize(new java.awt.Dimension(500, 400));
    }

    private void customizeUI() {
        setTitle("Kiểm Tra Thông Tin - Đặt Lại Mật Khẩu");
        getContentPane().setBackground(BACKGROUND_COLOR);

        customizeLabels();
        customizeInputFields();
        customizeButtons();
        applyModernStyling();
    }

    private void customizeLabels() {
        // Increase font size of jLabel8 from 22 to 26
        jLabel8.setFont(new Font("Segoe UI", Font.BOLD, 26));
        jLabel8.setForeground(PRIMARY_COLOR);

        jLabel9.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jLabel9.setForeground(TEXT_COLOR);

        JComponent[] labels = {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6};
        for (JComponent label : labels) {
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(TEXT_COLOR);
        }
    }

    private void customizeInputFields() {
        JTextField[] textFields = {txtID, txtTen, txtEmail, jcbNamSinh};
        for (JTextField field : textFields) {
            styleTextField(field);
        }

        JComboBox<?>[] comboBoxes = {jcbGioiTinh, jcbNgaySinh, jcbThangSinh, jcbChucVu};
        for (JComboBox<?> combo : comboBoxes) {
            styleComboBox(combo);
        }
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);

        // Add focus effect
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        new EmptyBorder(5, 9, 5, 9)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(6, 10, 6, 10)
                ));
            }
        });
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(3, 6, 3, 6)
        ));
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXT_COLOR);
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void customizeButtons() {
        styleButton(jButton1, PRIMARY_COLOR, Color.WHITE, new Color(31, 97, 141));
        styleButton(jbtQuayLai, new Color(108, 117, 125), Color.WHITE, new Color(90, 98, 104));
        styleButton(jbtLamMoi, WARNING_COLOR, Color.WHITE, new Color(212, 136, 16));
    }

    private void styleButton(javax.swing.JButton button, Color bgColor, Color textColor, Color hoverColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void applyModernStyling() {
        getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
    }

    public static class FormUtils {

        public static String getText(Component c) {
            if (c instanceof JTextField jTextField) {
                return jTextField.getText().trim();
            } else if (c instanceof JComboBox) {
                Object item = ((JComboBox<?>) c).getSelectedItem();
                return item != null ? item.toString().trim() : "";
            }
            return "";
        }

        public static java.sql.Date parseDate(String day, String month, String year) {
            if (day.isEmpty() || month.isEmpty() || year.isEmpty()) {
                return null;
            }

            try {
                int dayInt = Integer.parseInt(day);
                int monthInt = Integer.parseInt(month);
                int yearInt = Integer.parseInt(year);

                if (dayInt < 1 || dayInt > 31 || monthInt < 1 || monthInt > 12 || yearInt < 1900) {
                    return null;
                }

                String dateString = String.format("%d-%02d-%02d", yearInt, monthInt, dayInt);
                return java.sql.Date.valueOf(dateString);
            } catch (IllegalArgumentException e) {
                System.err.println("Lỗi khi chuyển đổi ngày tháng: " + e.getMessage());
                return null;
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

        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jcbGioiTinh = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        jcbNgaySinh = new javax.swing.JComboBox<>();
        txtID = new javax.swing.JTextField();
        jcbThangSinh = new javax.swing.JComboBox<>();
        txtEmail = new javax.swing.JTextField();
        jcbNamSinh = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jcbChucVu = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jbtQuayLai = new javax.swing.JButton();
        jbtLamMoi = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 0, 0));
        jLabel8.setText("--- Đặt lại mật khẩu ---");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("Mời bạn nhập thông tin của bạn vào đây");

        jLabel4.setText("Chức vụ:");

        jcbGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));

        jLabel5.setText("Giới tính:");

        jLabel6.setText("Ngày sinh:");

        txtTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenActionPerformed(evt);
            }
        });

        jcbNgaySinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        txtID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDActionPerformed(evt);
            }
        });

        jcbThangSinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jcbThangSinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbThangSinhActionPerformed(evt);
            }
        });

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        jcbNamSinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbNamSinhActionPerformed(evt);
            }
        });

        jLabel1.setText("ID:");

        jcbChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhân viên", "Quản lý" }));
        jcbChucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbChucVuActionPerformed(evt);
            }
        });

        jLabel2.setText("Tên:");

        jLabel3.setText("Email:");

        jButton1.setText("Kiểm tra");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jbtQuayLai.setText("Quay Lại");
        jbtQuayLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtQuayLaiActionPerformed(evt);
            }
        });

        jbtLamMoi.setText("Làm Mới");
        jbtLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtLamMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jcbChucVu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtEmail)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jcbNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jcbThangSinh, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jcbNamSinh, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jcbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jbtQuayLai)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jbtLamMoi)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jcbNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbThangSinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbNamSinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jcbChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jbtQuayLai)
                    .addComponent(jbtLamMoi))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenActionPerformed

    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDActionPerformed

    private void jcbThangSinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbThangSinhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbThangSinhActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void jcbNamSinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbNamSinhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbNamSinhActionPerformed

    private void jcbChucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbChucVuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbChucVuActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            String id = FormUtils.getText(txtID);
            String ten = FormUtils.getText(txtTen);
            String ngay = FormUtils.getText(jcbNgaySinh);
            String thang = FormUtils.getText(jcbThangSinh);
            String nam = FormUtils.getText(jcbNamSinh);
            Date ngaySinh = FormUtils.parseDate(ngay, thang, nam);
            String email = FormUtils.getText(txtEmail);
            String gioiTinh = FormUtils.getText(jcbGioiTinh);
            String chucVu = FormUtils.getText(jcbChucVu);

            boolean hopLe = false;
            for (NhanVien n : qlnv.getAll()) {
                if (Objects.equals(n.getId(), id)
                        && Objects.equals(n.getTenDayDu(), ten)
                        && Objects.equals(n.getNgaySinh(), ngaySinh)
                        && Objects.equals(n.getEmail(), email)
                        && Objects.equals(n.getGioiTinh(), gioiTinh)
                        && Objects.equals(n.getChucVu(), chucVu)) {
                    new TrangChu(n).setVisible(true);
                    this.dispose();
                    hopLe = true;
                    break;
                }
            }

            if (!hopLe) {
                JOptionPane.showMessageDialog(this, "Sai thông tin! Mời bạn nhập lại.",
                        "Kiểm tra thất bại", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống khi kiểm tra!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jbtQuayLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtQuayLaiActionPerformed
        new Login().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jbtQuayLaiActionPerformed

    private void jbtLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLamMoiActionPerformed
        txtID.setText("");
        txtTen.setText("");
        txtEmail.setText("");
        jcbNamSinh.setText("");
        jcbThangSinh.setSelectedIndex(0);
        jcbNgaySinh.setSelectedIndex(0);
    }//GEN-LAST:event_jbtLamMoiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(KiemTraThongTin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KiemTraThongTin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KiemTraThongTin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KiemTraThongTin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KiemTraThongTin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new KiemTraThongTin().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JButton jbtLamMoi;
    private javax.swing.JButton jbtQuayLai;
    private javax.swing.JComboBox<String> jcbChucVu;
    private javax.swing.JComboBox<String> jcbGioiTinh;
    private javax.swing.JTextField jcbNamSinh;
    private javax.swing.JComboBox<String> jcbNgaySinh;
    private javax.swing.JComboBox<String> jcbThangSinh;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtTen;
    // End of variables declaration//GEN-END:variables
}
