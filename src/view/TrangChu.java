/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import model.NhanVien;

public class TrangChu extends javax.swing.JFrame {

    public TrangChu(NhanVien nv) {
        initComponents();
    }

    private TrangChu() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jbtQLNV = new javax.swing.JButton();
        jbtQLSP = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // Sửa layout của mainPanel thành BorderLayout
        mainPanel.setLayout(new java.awt.BorderLayout());
        mainPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jbtQLNV.setText("Quản lý nhân viên");
        jbtQLNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtQLNVActionPerformed(evt);
            }
        });

        jbtQLSP.setText("Quản lý sản phẩm");
        jbtQLSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtQLSPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtQLNV)
                    .addComponent(jbtQLSP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbtQLNV)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtQLSP)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtQLSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtQLSPActionPerformed
        // Sửa lại cách thêm panel
        QLSPPanel p2 = new QLSPPanel();
        mainPanel.removeAll();
        mainPanel.add(p2, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }//GEN-LAST:event_jbtQLSPActionPerformed

    private void jbtQLNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtQLNVActionPerformed
        // Sửa lại cách thêm panel
        QLNVPanel p1 = new QLNVPanel();
        mainPanel.removeAll();
        mainPanel.add(p1, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }//GEN-LAST:event_jbtQLNVActionPerformed

    public static void main(String args[]) {
        // ... (giữ nguyên phần look and feel)
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbtQLNV;
    private javax.swing.JButton jbtQLSP;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
}