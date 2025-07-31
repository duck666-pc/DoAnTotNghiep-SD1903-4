/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import controller.BHDAO1;
import model.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public final class BHpanel extends javax.swing.JPanel {

    private BHDAO1 bhDAO;
    private List<SanPham> danhSachSanPham;
    private List<SanPham> sanPhamDaChon;
    private String currentHoaDonId;
    private KhachHang khachHangHienTai;
    private NumberFormat currencyFormat;

    public BHpanel() {
        initComponents();
        bhDAO = new BHDAO1();
        danhSachSanPham = new ArrayList<>();
        sanPhamDaChon = new ArrayList<>();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        initializeData();
        setupEventHandlers();
        updateSoDonChoXuLy();
    }

    private void initializeData() {
        loadSanPham();
        loadHoaDon();

        // Set default customer to "Khách Vãng Lai"
        jcbKhachHang.setSelectedIndex(0);
        txtSDT.setEnabled(false);

        // Clear labels
        jlbThanhTien.setText("0 đ");
        jlbTienDu.setText("0 đ");
    }

    private void setupEventHandlers() {
        // Customer type selection handler
        jcbKhachHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jcbKhachHang.getSelectedIndex() == 0) {
                    // Khách Vãng Lai
                    txtSDT.setEnabled(false);
                    txtSDT.setText("");
                    khachHangHienTai = null;
                } else {
                    // Khách Quen
                    txtSDT.setEnabled(true);
                }
            }
        });

        // Phone number change handler
        txtSDT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timKhachHangTheoSDT();
            }
        });

        // Status filter handler
        jcbTrangThai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadHoaDonByTrangThai();
            }
        });

        // Payment amount change handler
        txtSDT1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tinhTienDu();
            }
        });

        // Product table selection handler
        tblsp.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == 0 || e.getColumn() == 2) { // Checkbox or quantity column
                    capNhatKhuyenMai();
                }
            }
        });

        // Invoice table selection handler
        tblhd.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblhd.getSelectedRow();
                if (selectedRow >= 0) {
                    currentHoaDonId = (String) tblhd.getValueAt(selectedRow, 0);
                    loadChiTietHoaDon(currentHoaDonId);
                }
            }
        });
    }

    private void loadSanPham() {
        danhSachSanPham = bhDAO.getAllSanPham();
        DefaultTableModel model = (DefaultTableModel) tblsp.getModel();
        model.setRowCount(0);

        for (SanPham sp : danhSachSanPham) {
            model.addRow(new Object[]{
                false, // Checkbox
                sp.getTen(),
                "1" // Default quantity
            });
        }
    }

    private void loadHoaDon() {
        List<HoaDon> danhSachHD = bhDAO.getAllHoaDon();
        DefaultTableModel model = (DefaultTableModel) tblhd.getModel();
        model.setRowCount(0);

        for (HoaDon hd : danhSachHD) {
            KhachHang kh = null;
            if (!hd.getIdKhachHang().equals("KH000")) {
                // Try to get customer info, but don't break if not found
                try {
                    kh = bhDAO.findKhachHangBySDT(""); // This method needs the phone number
                } catch (Exception e) {
                    // Ignore and continue
                }
            }

            String tenKH = hd.getIdKhachHang().equals("KH000") ? "Khách Vãng Lai"
                    : (kh != null ? kh.getTen() : "Khách quen");

            model.addRow(new Object[]{
                hd.getId(),
                hd.getThoiGian(),
                tenKH
            });
        }
    }

    private void loadHoaDonByTrangThai() {
        String trangThai = (String) jcbTrangThai.getSelectedItem();
        List<HoaDon> danhSachHD = bhDAO.getHoaDonByTrangThai(trangThai);
        DefaultTableModel model = (DefaultTableModel) tblhd.getModel();
        model.setRowCount(0);

        for (HoaDon hd : danhSachHD) {
            model.addRow(new Object[]{
                hd.getId(),
                hd.getThoiGian(),
                hd.getIdKhachHang().equals("KH000") ? "Khách Vãng Lai" : "Khách quen"
            });
        }
    }

    private void loadChiTietHoaDon(String hoaDonId) {
        List<ChiTietHoaDon> chiTiets = bhDAO.getChiTietHoaDon(hoaDonId);
        DefaultTableModel model = (DefaultTableModel) tblcthd.getModel();
        model.setRowCount(0);

        for (ChiTietHoaDon ct : chiTiets) {
            // Find product name
            String tenSP = "Không xác định";
            for (SanPham sp : danhSachSanPham) {
                if (sp.getId().equals(ct.getIdSanPham())) {
                    tenSP = sp.getTen();
                    break;
                }
            }

            double thanhTien = ct.getDonGia() * ct.getSoLuong();
            model.addRow(new Object[]{
                ct.getId(),
                tenSP,
                ct.getSoLuong(),
                currencyFormat.format(ct.getDonGia()),
                currencyFormat.format(thanhTien)
            });
        }
    }

    private void timKhachHangTheoSDT() {
        String sdt = txtSDT.getText().trim();
        if (!sdt.isEmpty()) {
            khachHangHienTai = bhDAO.findKhachHangBySDT(sdt);
            if (khachHangHienTai == null) {
                int option = JOptionPane.showConfirmDialog(this,
                        "Không tìm thấy khách hàng với số điện thoại này.\n"
                        + "Bạn có muốn thêm khách hàng mới không?",
                        "Thông báo",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    // Open customer management panel (placeholder)
                    JOptionPane.showMessageDialog(this,
                            "Chức năng thêm khách hàng sẽ được tích hợp với QLKHPanel");
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Tìm thấy khách hàng: " + khachHangHienTai.getTen());
            }
        }
    }

    private void capNhatKhuyenMai() {
        sanPhamDaChon.clear();
        DefaultTableModel modelSP = (DefaultTableModel) tblsp.getModel();
        DefaultTableModel modelKM = (DefaultTableModel) tblsp1.getModel();
        modelKM.setRowCount(0);

        // Get selected products
        for (int i = 0; i < modelSP.getRowCount(); i++) {
            Boolean selected = (Boolean) modelSP.getValueAt(i, 0);
            if (selected != null && selected) {
                SanPham sp = danhSachSanPham.get(i);
                sanPhamDaChon.add(sp);

                // Load promotions for this product
                List<ChiTietKhuyenMai> khuyenMais = bhDAO.getKhuyenMaiBySanPham(sp.getId());
                for (ChiTietKhuyenMai km : khuyenMais) {
                    modelKM.addRow(new Object[]{
                        sp.getTen(),
                        km.getHinhThucGiam(),
                        km.getHinhThucGiam().equals("Phần trăm")
                        ? km.getMucGiamGia() + "%"
                        : currencyFormat.format(km.getSoTienGiamGia()),
                        km.getHinhThucGiam().equals("Phần trăm")
                        ? currencyFormat.format(sp.getGia() * km.getMucGiamGia() / 100)
                        : currencyFormat.format(km.getSoTienGiamGia()),
                        km.getQuaTang()
                    });
                }
            }
        }

        tinhThanhTien();
    }

    private void tinhThanhTien() {
        BigDecimal tongTien = BigDecimal.ZERO;
        DefaultTableModel modelSP = (DefaultTableModel) tblsp.getModel();

        for (int i = 0; i < modelSP.getRowCount(); i++) {
            Boolean selected = (Boolean) modelSP.getValueAt(i, 0);
            if (selected != null && selected) {
                try {
                    String soLuongStr = modelSP.getValueAt(i, 2).toString();
                    int soLuong = Integer.parseInt(soLuongStr);
                    SanPham sp = danhSachSanPham.get(i);

                    BigDecimal giaSP = BigDecimal.valueOf(sp.getGia());
                    BigDecimal thanhTienSP = giaSP.multiply(BigDecimal.valueOf(soLuong));

                    // Apply promotions
                    List<ChiTietKhuyenMai> khuyenMais = bhDAO.getKhuyenMaiBySanPham(sp.getId());
                    for (ChiTietKhuyenMai km : khuyenMais) {
                        if (km.getHinhThucGiam().equals("Phần trăm")) {
                            BigDecimal giamGia = thanhTienSP.multiply(BigDecimal.valueOf(km.getMucGiamGia() / 100));
                            thanhTienSP = thanhTienSP.subtract(giamGia);
                        } else {
                            thanhTienSP = thanhTienSP.subtract(BigDecimal.valueOf(km.getSoTienGiamGia()));
                        }
                    }

                    tongTien = tongTien.add(thanhTienSP);
                } catch (NumberFormatException e) {
                    // Skip invalid quantity
                }
            }
        }

        jlbThanhTien.setText(currencyFormat.format(tongTien));
        tinhTienDu();
    }

    private void tinhTienDu() {
        try {
            String thanhTienText = jlbThanhTien.getText().replaceAll("[^0-9]", "");
            String tienKhachTraText = txtSDT1.getText().trim().replaceAll("[^0-9]", "");

            if (!thanhTienText.isEmpty() && !tienKhachTraText.isEmpty()) {
                double thanhTien = Double.parseDouble(thanhTienText);
                double tienKhachTra = Double.parseDouble(tienKhachTraText);
                double tienDu = tienKhachTra - thanhTien;

                jlbTienDu.setText(currencyFormat.format(tienDu));
            } else {
                jlbTienDu.setText("0 đ");
            }
        } catch (NumberFormatException e) {
            jlbTienDu.setText("0 đ");
        }
    }

    private void updateSoDonChoXuLy() {
        int soDon = bhDAO.demSoDonChoXuLy();
        jlbSoDonChoXuLy.setText(String.valueOf(soDon));
    }

    private boolean validatePayment() {
        try {
            String thanhTienText = jlbThanhTien.getText().replaceAll("[^0-9]", "");
            String tienKhachTraText = txtSDT1.getText().trim().replaceAll("[^0-9]", "");

            if (thanhTienText.isEmpty() || tienKhachTraText.isEmpty()) {
                return false;
            }

            double thanhTien = Double.parseDouble(thanhTienText);
            double tienKhachTra = Double.parseDouble(tienKhachTraText);

            return tienKhachTra >= thanhTien;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void xuatHoaDonPDF(String hoaDonId) {
        try {
            // Create PDF
            Document document = new Document();
            String fileName = "HoaDon_" + hoaDonId + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            document.open();

            // Add content
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            // Invoice details
            HoaDon hoaDon = bhDAO.findHoaDonById(hoaDonId);
            if (hoaDon != null) {
                document.add(new Paragraph("Mã hóa đơn: " + hoaDon.getId(), normalFont));
                document.add(new Paragraph("Thời gian: " + hoaDon.getThoiGian(), normalFont));
                document.add(new Paragraph("Khách hàng: "
                        + (hoaDon.getIdKhachHang().equals("KH000") ? "Khách Vãng Lai" : "Khách quen"), normalFont));
                document.add(new Paragraph(" "));

                // Product table
                PdfPTable table = new PdfPTable(4);
                table.addCell("Sản phẩm");
                table.addCell("Số lượng");
                table.addCell("Đơn giá");
                table.addCell("Thành tiền");

                List<ChiTietHoaDon> chiTiets = bhDAO.getChiTietHoaDon(hoaDonId);
                for (ChiTietHoaDon ct : chiTiets) {
                    // Find product name
                    String tenSP = "";
                    for (SanPham sp : danhSachSanPham) {
                        if (sp.getId().equals(ct.getIdSanPham())) {
                            tenSP = sp.getTen();
                            break;
                        }
                    }

                    table.addCell(tenSP);
                    table.addCell(String.valueOf(ct.getSoLuong()));
                    table.addCell(currencyFormat.format(ct.getDonGia()));
                    table.addCell(currencyFormat.format(ct.getDonGia() * ct.getSoLuong()));
                }

                document.add(table);
                document.add(new Paragraph(" "));

                // Total
                document.add(new Paragraph("Tổng tiền: " + currencyFormat.format(hoaDon.getTongTienSauGiamGia()), normalFont));
            }

            document.close();

            // Open PDF
            Desktop.getDesktop().open(new File(fileName));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + e.getMessage());
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

        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblcthd = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnTaoHD = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblhd = new javax.swing.JTable();
        btnHuyHD = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnXuatHoaDon = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblsp = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblsp1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jcbKhachHang = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jcbTrangThai = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jlbThanhTien = new javax.swing.JLabel();
        txtSDT1 = new javax.swing.JTextField();
        jlbTienDu = new javax.swing.JLabel();
        jlbSoDonChoXuLy = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        jbtTimKiem = new javax.swing.JButton();

        jLabel5.setText("jLabel5");

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

        jLabel1.setText("Hóa Đơn");

        btnTaoHD.setBackground(new java.awt.Color(41, 62, 80));
        btnTaoHD.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTaoHD.setForeground(new java.awt.Color(255, 255, 255));
        btnTaoHD.setText("Tạo Hóa Đơn");
        btnTaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHDActionPerformed(evt);
            }
        });

        jLabel2.setText("Chi tiết hóa đơn");

        tblhd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Thời Gian", "Khách Hàng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.String.class
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

        btnHuyHD.setBackground(new java.awt.Color(41, 62, 80));
        btnHuyHD.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHuyHD.setForeground(new java.awt.Color(255, 255, 255));
        btnHuyHD.setText("Hủy Hóa Đơn");
        btnHuyHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyHDActionPerformed(evt);
            }
        });

        jLabel3.setText("Sản Phẩm");

        btnXuatHoaDon.setBackground(new java.awt.Color(41, 62, 80));
        btnXuatHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXuatHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        btnXuatHoaDon.setText("Xuất Hóa Đơn");
        btnXuatHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHoaDonActionPerformed(evt);
            }
        });

        jLabel8.setText("Khách Hàng:");

        tblsp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "", "Tên", "Số lượng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane7.setViewportView(tblsp);
        if (tblsp.getColumnModel().getColumnCount() > 0) {
            tblsp.getColumnModel().getColumn(0).setHeaderValue("");
        }

        tblsp1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tên", "Hình thức giảm", "Mức giảm", "Số tiền giảm", "Quà tặng"
            }
        ));
        jScrollPane8.setViewportView(tblsp1);

        jLabel4.setText("Khuyến Mại");

        jcbKhachHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Khách Vãng Lai", "Khách Quen" }));
        jcbKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbKhachHangActionPerformed(evt);
            }
        });

        jLabel9.setText("SĐT:");

        jLabel11.setText("Trạng Thái Hóa Đơn:");

        jcbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất Cả", "Đã thanh toán", "Chưa thanh toán", "Đã hủy" }));

        jLabel10.setText("Thành Tiền:");

        jLabel12.setText("Tiền Khách Trả:");

        jLabel13.setText("Tiền Dư:");

        jLabel14.setText("Số Đơn Chờ Xủ Lý:");

        jlbThanhTien.setText(" ");

        jlbTienDu.setText(" ");

        jlbSoDonChoXuLy.setText(" ");

        jbtTimKiem.setBackground(new java.awt.Color(41, 62, 80));
        jbtTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        jbtTimKiem.setText("Tìm kiếm bằng ID Hóa Đơn");
        jbtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtTimKiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcbKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSDT))
                    .addComponent(btnHuyHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXuatHoaDon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnTaoHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlbTienDu))
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane5)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlbThanhTien))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSDT1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlbSoDonChoXuLy))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(jcbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jcbKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtTimKiem))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(5, 5, 5)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(jcbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTaoHD)
                        .addComponent(jLabel10)
                        .addComponent(jlbThanhTien)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXuatHoaDon)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14)
                    .addComponent(txtSDT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbSoDonChoXuLy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHuyHD)
                    .addComponent(jLabel13)
                    .addComponent(jlbTienDu))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblcthdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblcthdMouseClicked

    }//GEN-LAST:event_tblcthdMouseClicked

    private void btnTaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHDActionPerformed
        DefaultTableModel modelSP = (DefaultTableModel) tblsp.getModel();
        boolean hasSelectedProduct = false;

        for (int i = 0; i < modelSP.getRowCount(); i++) {
            Boolean selected = (Boolean) modelSP.getValueAt(i, 0);
            if (selected != null && selected) {
                hasSelectedProduct = true;
                break;
            }
        }

        if (!hasSelectedProduct) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một sản phẩm!");
            return;
        }

        // Calculate total amount
        BigDecimal tongTienGoc = BigDecimal.ZERO;
        BigDecimal tongGiamGia = BigDecimal.ZERO;

        for (int i = 0; i < modelSP.getRowCount(); i++) {
            Boolean selected = (Boolean) modelSP.getValueAt(i, 0);
            if (selected != null && selected) {
                try {
                    String soLuongStr = modelSP.getValueAt(i, 2).toString();
                    int soLuong = Integer.parseInt(soLuongStr);
                    SanPham sp = danhSachSanPham.get(i);

                    BigDecimal giaSP = BigDecimal.valueOf(sp.getGia());
                    BigDecimal thanhTienGoc = giaSP.multiply(BigDecimal.valueOf(soLuong));
                    tongTienGoc = tongTienGoc.add(thanhTienGoc);

                    // Calculate discount
                    List<ChiTietKhuyenMai> khuyenMais = bhDAO.getKhuyenMaiBySanPham(sp.getId());
                    for (ChiTietKhuyenMai km : khuyenMais) {
                        if (km.getHinhThucGiam().equals("Phần trăm")) {
                            BigDecimal giamGia = thanhTienGoc.multiply(BigDecimal.valueOf(km.getMucGiamGia() / 100));
                            tongGiamGia = tongGiamGia.add(giamGia);
                        } else {
                            tongGiamGia = tongGiamGia.add(BigDecimal.valueOf(km.getSoTienGiamGia()));
                        }
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
                    return;
                }
            }
        }

        BigDecimal tongTienSauGiamGia = tongTienGoc.subtract(tongGiamGia);

        // Determine customer ID
        String khachHangId = "KH000"; // Default to walk-in customer
        if (jcbKhachHang.getSelectedIndex() == 1 && khachHangHienTai != null) {
            khachHangId = khachHangHienTai.getId();
        }

        // Create invoice
        currentHoaDonId = bhDAO.taoHoaDon(khachHangId, "ND001", // Default employee ID
                tongTienGoc, tongGiamGia, tongTienSauGiamGia, "Chưa Thanh Toán");

        if (currentHoaDonId != null) {
            // Add invoice details
            for (int i = 0; i < modelSP.getRowCount(); i++) {
                Boolean selected = (Boolean) modelSP.getValueAt(i, 0);
                if (selected != null && selected) {
                    try {
                        String soLuongStr = modelSP.getValueAt(i, 2).toString();
                        int soLuong = Integer.parseInt(soLuongStr);
                        SanPham sp = danhSachSanPham.get(i);

                        bhDAO.themChiTietHoaDon(currentHoaDonId, sp.getId(), soLuong, sp.getGia());
                    } catch (NumberFormatException e) {
                        // Skip invalid quantities
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công! ID: " + currentHoaDonId);
            loadHoaDon();
            updateSoDonChoXuLy();

            // Load invoice details
            loadChiTietHoaDon(currentHoaDonId);
        } else {
            JOptionPane.showMessageDialog(this, "Tạo hóa đơn thất bại!");
        }
    }//GEN-LAST:event_btnTaoHDActionPerformed

    private void tblhdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblhdMouseClicked
    }//GEN-LAST:event_tblhdMouseClicked

    private void btnXuatHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatHoaDonActionPerformed
        if (currentHoaDonId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!");
            return;
        }

        // Validate payment
        if (!validatePayment()) {
            JOptionPane.showMessageDialog(this, "Khách hàng chưa thanh toán đủ!");
            return;
        }

        // Update invoice status
        if (bhDAO.capNhatTrangThaiHoaDon(currentHoaDonId, "Đã Thanh Toán")) {
            JOptionPane.showMessageDialog(this, "Xuất hóa đơn thành công!");

            // Generate PDF
            xuatHoaDonPDF(currentHoaDonId);

            loadHoaDon();
            updateSoDonChoXuLy();
        } else {
            JOptionPane.showMessageDialog(this, "Xuất hóa đơn thất bại!");
        }
    }//GEN-LAST:event_btnXuatHoaDonActionPerformed

    private void btnHuyHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyHDActionPerformed
        if (currentHoaDonId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!");
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn hủy hóa đơn này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            if (bhDAO.capNhatTrangThaiHoaDon(currentHoaDonId, "Đã Hủy")) {
                JOptionPane.showMessageDialog(this, "Hủy hóa đơn thành công!");
                loadHoaDon();
                updateSoDonChoXuLy();
            } else {
                JOptionPane.showMessageDialog(this, "Hủy hóa đơn thất bại!");
            }
        }
    }//GEN-LAST:event_btnHuyHDActionPerformed

    private void jcbKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbKhachHangActionPerformed

    private void jbtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtTimKiemActionPerformed
        String hoaDonId = txtTimKiem.getText().trim();
        
        if (hoaDonId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ID hóa đơn!");
            return;
        }
        
        HoaDon hoaDon = bhDAO.findHoaDonById(hoaDonId);
        if (hoaDon != null) {
            // Clear and add found invoice to table
            DefaultTableModel model = (DefaultTableModel) tblhd.getModel();
            model.setRowCount(0);
            
            String tenKH = hoaDon.getIdKhachHang().equals("KH000") ? "Khách Vãng Lai" : "Khách quen";
            model.addRow(new Object[]{
                hoaDon.getId(),
                hoaDon.getThoiGian(),
                tenKH
            });
            
            currentHoaDonId = hoaDonId;
            loadChiTietHoaDon(hoaDonId);
            
            JOptionPane.showMessageDialog(this, "Tìm thấy hóa đơn!");
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn với ID: " + hoaDonId);
        }
    }//GEN-LAST:event_jbtTimKiemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHuyHD;
    private javax.swing.JButton btnTaoHD;
    private javax.swing.JButton btnXuatHoaDon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JButton jbtTimKiem;
    private javax.swing.JComboBox<String> jcbKhachHang;
    private javax.swing.JComboBox<String> jcbTrangThai;
    private javax.swing.JLabel jlbSoDonChoXuLy;
    private javax.swing.JLabel jlbThanhTien;
    private javax.swing.JLabel jlbTienDu;
    private javax.swing.JTable tblcthd;
    private javax.swing.JTable tblhd;
    private javax.swing.JTable tblsp;
    private javax.swing.JTable tblsp1;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtSDT1;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
