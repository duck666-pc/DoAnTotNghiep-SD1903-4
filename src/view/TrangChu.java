/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import model.NhanVien;

public class TrangChu extends javax.swing.JFrame {

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(246, 248, 250);
    private static final Color SIDEBAR_COLOR = new Color(44, 62, 80);
    private static final Color BUTTON_HOVER = new Color(52, 73, 94);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color CONTENT_BG = Color.WHITE;

    public TrangChu(NhanVien n) {
        initComponents();
        customizeUI();
        setFullScreen();
    }

    private TrangChu() {
        initComponents();
        customizeUI();
        setFullScreen();
    }

    private void setFullScreen() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void customizeUI() {
        setTitle("Hệ Thống Quản Lý - Trang Chủ");
        customizeNavigationPanel();
        customizeButtons();
        customizeContentPanel();
        showWelcomePanel();
    }

    private void customizeNavigationPanel() {
        navigationPanel.setBackground(SIDEBAR_COLOR);
        navigationPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(189, 195, 199)),
                new EmptyBorder(15, 10, 15, 10)
        ));
    }

    private void customizeButtons() {
        JButton[] buttons = {
            jbtTRANGCHU, jbtQLNV, jbtQLSP, jbtLOAISANPHAM, jbtQLHD,
            jbtQLNL, jbtQLKH, jbtLOAIKHACHHANG, jbtQLKM, jbtDOANHTHU
        };

        for (JButton button : buttons) {
            styleNavigationButton(button);
        }

        styleLogoutButton(jbtDangXuat);
    }

    private void styleNavigationButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(TEXT_COLOR);
        button.setBackground(SIDEBAR_COLOR);
        button.setBorder(new EmptyBorder(12, 16, 12, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setOpaque(true);

        addHoverEffect(button, SIDEBAR_COLOR, BUTTON_HOVER);
    }

    private void styleLogoutButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(TEXT_COLOR);
        button.setBackground(ACCENT_COLOR);
        button.setBorder(new EmptyBorder(12, 16, 12, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        addHoverEffect(button, ACCENT_COLOR, new Color(192, 57, 43));
    }

    private void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }

    private void customizeContentPanel() {
        jPanel.setBackground(CONTENT_BG);
        jPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 221, 225), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
    }

    private void showWelcomePanel() {
        JPanel welcomePanel = createWelcomePanel();
        displayPanel(welcomePanel);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CONTENT_BG);

        // Main welcome content
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(CONTENT_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Welcome title
        JLabel titleLabel = new JLabel("Chào Mừng Đến Với Hệ Thống Quản Lý Bán Đồ Ăn");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy++;
        JLabel subtitleLabel = new JLabel("Vui lòng chọn chức năng từ menu bên trái để bắt đầu");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(subtitleLabel, gbc);

        // Add center panel to main panel
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFeatureCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BACKGROUND_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 221, 225), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(73, 80, 87));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(label, BorderLayout.CENTER);
        return card;
    }

    private void showPanel(JPanel panel) {
        displayPanel(panel);
    }

    private void displayPanel(JPanel panel) {
        jPanel.removeAll();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(panel, BorderLayout.CENTER);
        jPanel.revalidate();
        jPanel.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        navigationPanel = new javax.swing.JPanel();
        jbtQLSP = new javax.swing.JButton();
        jbtQLNV = new javax.swing.JButton();
        jbtTRANGCHU = new javax.swing.JButton();
        jbtLOAISANPHAM = new javax.swing.JButton();
        jbtQLHD = new javax.swing.JButton();
        jbtQLNL = new javax.swing.JButton();
        jbtQLKH = new javax.swing.JButton();
        jbtLOAIKHACHHANG = new javax.swing.JButton();
        jbtQLKM = new javax.swing.JButton();
        jbtDOANHTHU = new javax.swing.JButton();
        jbtDangXuat = new javax.swing.JButton();
        jPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setMaximumSize(new java.awt.Dimension(0, 0));
        setResizable(false);

        jbtQLSP.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbtQLSP.setText("Sản Phẩm");
        jbtQLSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtQLSPActionPerformed(evt);
            }
        });

        jbtQLNV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbtQLNV.setText("Nhân Viên");
        jbtQLNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtQLNVActionPerformed(evt);
            }
        });

        jbtTRANGCHU.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbtTRANGCHU.setText("Trang Chủ");
        jbtTRANGCHU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtTRANGCHUActionPerformed(evt);
            }
        });

        jbtLOAISANPHAM.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbtLOAISANPHAM.setText("Loại Sản Phẩm");
        jbtLOAISANPHAM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtLOAISANPHAMActionPerformed(evt);
            }
        });

        jbtQLHD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbtQLHD.setText("Hóa Đơn");
        jbtQLHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtQLHDActionPerformed(evt);
            }
        });

        jbtQLNL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbtQLNL.setText("Nguyên Liệu");
        jbtQLNL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtQLNLActionPerformed(evt);
            }
        });

        jbtQLKH.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbtQLKH.setText("Khách Hàng");
        jbtQLKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtQLKHActionPerformed(evt);
            }
        });

        jbtLOAIKHACHHANG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbtLOAIKHACHHANG.setText("Loại Khách Hàng");
        jbtLOAIKHACHHANG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtLOAIKHACHHANGActionPerformed(evt);
            }
        });

        jbtQLKM.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbtQLKM.setText("Khuyến Mại");
        jbtQLKM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtQLKMActionPerformed(evt);
            }
        });

        jbtDOANHTHU.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbtDOANHTHU.setText("Doanh Thu");
        jbtDOANHTHU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDOANHTHUActionPerformed(evt);
            }
        });

        jbtDangXuat.setBackground(new java.awt.Color(255, 0, 0));
        jbtDangXuat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbtDangXuat.setForeground(new java.awt.Color(255, 255, 255));
        jbtDangXuat.setText("Đăng Xuất");
        jbtDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDangXuatActionPerformed(evt);
            }
        });

        jPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 603, Short.MAX_VALUE)
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout navigationPanelLayout = new javax.swing.GroupLayout(navigationPanel);
        navigationPanel.setLayout(navigationPanelLayout);
        navigationPanelLayout.setHorizontalGroup(
            navigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, navigationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(navigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jbtDangXuat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtLOAIKHACHHANG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtQLSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtQLNV, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtTRANGCHU, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtLOAISANPHAM, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtQLHD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtQLNL, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtQLKH, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtQLKM, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtDOANHTHU, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        navigationPanelLayout.setVerticalGroup(
            navigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navigationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(navigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, navigationPanelLayout.createSequentialGroup()
                        .addComponent(jbtTRANGCHU)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtQLNV)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtQLSP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtLOAISANPHAM)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtQLHD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtQLNL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtQLKH)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtLOAIKHACHHANG)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtQLKM)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtDOANHTHU)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtDangXuat)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(navigationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(navigationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtQLSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtQLSPActionPerformed
        showPanel(new QLSPPanel());
    }//GEN-LAST:event_jbtQLSPActionPerformed

    private void jbtQLNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtQLNVActionPerformed
        showPanel(new QLNVPanel());
    }//GEN-LAST:event_jbtQLNVActionPerformed

    private void jbtQLHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtQLHDActionPerformed

    }//GEN-LAST:event_jbtQLHDActionPerformed

    private void jbtQLNLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtQLNLActionPerformed
        showPanel(new QLNLPanel());
    }//GEN-LAST:event_jbtQLNLActionPerformed

    private void jbtQLKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtQLKHActionPerformed
        showPanel(new QLKHPanel());
    }//GEN-LAST:event_jbtQLKHActionPerformed

    private void jbtQLKMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtQLKMActionPerformed
        showPanel(new QLKMPanel());
    }//GEN-LAST:event_jbtQLKMActionPerformed

    private void jbtDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDangXuatActionPerformed
        int option = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn đăng xuất không ?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            this.dispose();

            SwingUtilities.invokeLater(() -> {
                new Login().setVisible(true);
            });
        }
    }//GEN-LAST:event_jbtDangXuatActionPerformed

    private void jbtDOANHTHUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDOANHTHUActionPerformed
        showPanel(new DOANHTHUPanel());
    }//GEN-LAST:event_jbtDOANHTHUActionPerformed

    private void jbtLOAISANPHAMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLOAISANPHAMActionPerformed
        showPanel(new QLLSPPanel());
    }//GEN-LAST:event_jbtLOAISANPHAMActionPerformed

    private void jbtLOAIKHACHHANGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLOAIKHACHHANGActionPerformed
        showPanel(new QLHKHPanel());
    }//GEN-LAST:event_jbtLOAIKHACHHANGActionPerformed

    private void jbtTRANGCHUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtTRANGCHUActionPerformed
        showWelcomePanel();
    }//GEN-LAST:event_jbtTRANGCHUActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrangChu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrangChu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrangChu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> {
            new TrangChu().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel;
    private javax.swing.JButton jbtDOANHTHU;
    private javax.swing.JButton jbtDangXuat;
    private javax.swing.JButton jbtLOAIKHACHHANG;
    private javax.swing.JButton jbtLOAISANPHAM;
    private javax.swing.JButton jbtQLHD;
    private javax.swing.JButton jbtQLKH;
    private javax.swing.JButton jbtQLKM;
    private javax.swing.JButton jbtQLNL;
    private javax.swing.JButton jbtQLNV;
    private javax.swing.JButton jbtQLSP;
    private javax.swing.JButton jbtTRANGCHU;
    private javax.swing.JPanel navigationPanel;
    // End of variables declaration//GEN-END:variables
}
