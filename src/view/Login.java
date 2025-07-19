/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.QLNVDAO;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import model.NhanVien;

public class Login extends javax.swing.JFrame {

    // Modern color scheme matching TrangChu
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(246, 248, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color BUTTON_HOVER = new Color(52, 73, 94);
    private static final Color INPUT_BORDER = new Color(220, 221, 225);

    /**
     * Creates new form Login
     */
    QLNVDAO qlnv = new QLNVDAO();
    private final char defaultEchoChar;

    public Login() {
        initComponents();
        defaultEchoChar = txtMatKhau.getEchoChar();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        customizeUI();
        setLocationRelativeTo(null); // Center the window
    }

    private final KiemTraThongTin ktttInstance = null;

    private void customizeUI() {
        setTitle("Đăng Nhập - Hệ Thống Quản Lý");
        getContentPane().setBackground(BACKGROUND_COLOR);
        customizeLabels();
        customizeInputFields();
        customizeButtons();
    }

    private void customizeLabels() {
        // Title label
        jLabel2.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jLabel2.setForeground(PRIMARY_COLOR);

        // Input labels
        jLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel3.setForeground(TEXT_COLOR);

        jLabel4.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel4.setForeground(TEXT_COLOR);
    }

    private void customizeInputFields() {
        // ID field
        txtID.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtID.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INPUT_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        txtID.setBackground(Color.WHITE);

        // Password field
        txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMatKhau.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INPUT_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        txtMatKhau.setBackground(Color.WHITE);

        // Checkbox
        jCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jCheckBox.setForeground(TEXT_COLOR);
        jCheckBox.setBackground(BACKGROUND_COLOR);
        jCheckBox.setFocusPainted(false);
    }

    private void customizeButtons() {
        // Login button
        styleButton(jbtDangNhap, PRIMARY_COLOR, Color.WHITE, SECONDARY_COLOR);

        // Forgot password button
        styleLinkButton(jbtQuenMatKhau);
    }

    private void styleButton(JButton button, Color bgColor, Color textColor, Color hoverColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setBorder(new EmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }

    private void styleLinkButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(PRIMARY_COLOR);
        button.setBackground(BACKGROUND_COLOR);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(SECONDARY_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(PRIMARY_COLOR);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        jbtDangNhap = new javax.swing.JButton();
        txtMatKhau = new javax.swing.JPasswordField();
        jCheckBox = new javax.swing.JCheckBox();
        jbtQuenMatKhau = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 0, 0));
        jLabel2.setText("Đăng Nhập");

        jLabel3.setText("ID:");

        jLabel4.setText("Mật khẩu:");

        txtID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDActionPerformed(evt);
            }
        });

        jbtDangNhap.setText("Đăng nhập");
        jbtDangNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDangNhapActionPerformed(evt);
            }
        });

        jCheckBox.setText("Hiện mật khẩu");
        jCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxActionPerformed(evt);
            }
        });

        jbtQuenMatKhau.setText("--- Quên mật khẩu? ---");
        jbtQuenMatKhau.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jbtQuenMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtQuenMatKhauActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(93, 93, 93)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtMatKhau)
                                    .addComponent(jCheckBox)
                                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbtDangNhap)
                        .addGap(110, 110, 110))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtQuenMatKhau)
                .addGap(92, 92, 92))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtDangNhap)
                .addGap(12, 12, 12)
                .addComponent(jbtQuenMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDActionPerformed

    private void jbtDangNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDangNhapActionPerformed
        String id = txtID.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();

        // Input validation
        if (id.isEmpty()) {
            showErrorMessage("Vui lòng nhập ID!");
            txtID.requestFocus();
            return;
        }
        
        if (matKhau.isEmpty()) {
            showErrorMessage("Vui lòng nhập mật khẩu!");
            txtMatKhau.requestFocus();
            return;
        }

        try {
            // Optimized authentication
            NhanVien authenticatedUser = authenticateUser(id, matKhau);
            
            if (authenticatedUser != null) {
                // Successful login
                new TrangChu(authenticatedUser).setVisible(true);
                this.dispose();
            } else {
                showErrorMessage("Sai ID hoặc mật khẩu!");
                clearFields();
            }
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            showErrorMessage("Lỗi hệ thống khi đăng nhập!");
        }
    }//GEN-LAST:event_jbtDangNhapActionPerformed

    private void jCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxActionPerformed
        txtMatKhau.setEchoChar(jCheckBox.isSelected() ? (char) 0 : '*');
    }//GEN-LAST:event_jCheckBoxActionPerformed

    private void jbtQuenMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtQuenMatKhauActionPerformed
        new KiemTraThongTin().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jbtQuenMatKhauActionPerformed

    
    private NhanVien authenticateUser(String id, String matKhau) throws SQLException, ClassNotFoundException {
        for (NhanVien n : qlnv.getAll()) {
            if (id.equals(n.getId()) && matKhau.equals(n.getMatKhau())) {
                return n;
            }
        }
        return null;
    }
    
    // Helper method for consistent error messaging
    private void showErrorMessage(String message) {
        javax.swing.JOptionPane.showMessageDialog(this,
                message,
                "Thông báo",
                javax.swing.JOptionPane.WARNING_MESSAGE);
    }
    
    // Helper method to clear input fields
    private void clearFields() {
        txtMatKhau.setText("");
        txtID.requestFocus();
    }
    
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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton jbtDangNhap;
    private javax.swing.JButton jbtQuenMatKhau;
    private javax.swing.JTextField txtID;
    private javax.swing.JPasswordField txtMatKhau;
    // End of variables declaration//GEN-END:variables
}
