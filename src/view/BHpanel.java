/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import controller.BHDAO1;
import model.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import javax.swing.event.TableModelEvent;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public final class BHpanel extends javax.swing.JPanel {

    private final BHDAO1 bhDAO;
    private List<SanPham> danhSachSanPham;
    private final List<SanPham> sanPhamDaChon;
    private String currentHoaDonId;
    private KhachHang khachHangHienTai;
    private final NumberFormat currencyFormat;
    private NhanVien loggedInUser; // Added field for logged-in user

    // Modified constructor that accepts NhanVien parameter
    public BHpanel(NhanVien loggedInUser) {
        this.loggedInUser = loggedInUser;
        initComponents();
        bhDAO = new BHDAO1();
        danhSachSanPham = new ArrayList<>();
        sanPhamDaChon = new ArrayList<>();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        initializeData();
        setupEventHandlers();
        updateSoDonChoXuLy();
    }

    // Keep the existing parameterless constructor for backward compatibility
    public BHpanel() {
        this(null); // Call the new constructor with null
    }

    // Add getter method to access logged-in user
    public NhanVien getLoggedInUser() {
        return loggedInUser;
    }

    private void initializeData() {
        javax.swing.table.DefaultTableCellRenderer currencyRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            private final java.text.NumberFormat nf = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("vi", "VN"));

            @Override
            public void setValue(Object value) {
                if (value instanceof Number) {
                    setText(nf.format(((Number) value).doubleValue()));
                } else {
                    setText("");
                }
            }
        };

        tblcthd.getColumnModel().getColumn(3).setCellRenderer(currencyRenderer); // Đơn Giá
        tblcthd.getColumnModel().getColumn(4).setCellRenderer(currencyRenderer); // Thành Tiền        

        loadSanPham();
        loadHoaDon();

        jcbKhachHang.setSelectedIndex(0);
        txtSDT.setEnabled(false);

        jlbThanhTien.setText("0 đ");
        jlbTienDu.setText("0 đ");

        // Make invoice details table non-editable
        tblcthd.setDefaultEditor(Object.class, null);
    }

    private void setupEventHandlers() {
        jcbKhachHang.addActionListener((ActionEvent e) -> {
            if (jcbKhachHang.getSelectedIndex() == 0) {
                txtSDT.setEnabled(false);
                txtSDT.setText("");
                khachHangHienTai = null;
            } else {
                txtSDT.setEnabled(true);
            }
        });

        txtSDT.addActionListener((ActionEvent e) -> {
            timKhachHangTheoSDT();
        });

        jcbTrangThai.addActionListener((ActionEvent e) -> {
            loadHoaDonByTrangThai();
        });

        txtSDT1.addActionListener((ActionEvent e) -> {
            tinhTienDu();
        });

        // Add DocumentListener for txtSDT1 to calculate change in real-time
        txtSDT1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                tinhTienDu();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                tinhTienDu();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                tinhTienDu();
            }
        });

        tblsp.getModel().addTableModelListener((TableModelEvent e) -> {
            if (e.getColumn() == 0 || e.getColumn() == 2) {
                capNhatKhuyenMai();
            }
        });

        // Fixed: Properly handle row selection for invoice table
        tblhd.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tblhd.getSelectedRow();
                    if (selectedRow >= 0) {
                        currentHoaDonId = (String) tblhd.getValueAt(selectedRow, 0);
                        // Use SwingUtilities.invokeLater to ensure proper timing
                        SwingUtilities.invokeLater(() -> {
                            loadChiTietHoaDon(currentHoaDonId);
                        });
                    }
                }
            }
        });
    }

    private void loadSanPham() {
        try {
            danhSachSanPham = bhDAO.getAllSanPham();
            DefaultTableModel model = (DefaultTableModel) tblsp.getModel();
            model.setRowCount(0);

            for (SanPham sp : danhSachSanPham) {
                model.addRow(new Object[]{
                    false,
                    sp.getTen(),
                    "1"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách sản phẩm: " + e.getMessage());
        }
    }

    private void loadHoaDon() {
        try {
            List<HoaDon> danhSachHD = bhDAO.getAllHoaDon();
            DefaultTableModel model = (DefaultTableModel) tblhd.getModel();
            model.setRowCount(0);

            // Fixed: Sort invoices by ID in descending order for proper display
            danhSachHD.sort((h1, h2) -> {
                try {
                    // Extract numeric part from ID (e.g., HD001 -> 1)
                    int num1 = Integer.parseInt(h1.getId().substring(2));
                    int num2 = Integer.parseInt(h2.getId().substring(2));
                    return Integer.compare(num2, num1); // Descending order
                } catch (NumberFormatException e) {
                    return h2.getId().compareTo(h1.getId()); // Fallback to string comparison
                }
            });

            for (HoaDon hd : danhSachHD) {
                String tenKH = "Khách Vãng Lai";
                if (!hd.getIdKhachHang().equals("KH000")) {
                    try {
                        // Get customer by ID instead of phone number
                        KhachHang kh = bhDAO.findKhachHangById(hd.getIdKhachHang());
                        if (kh != null) {
                            tenKH = kh.getTen();
                        } else {
                            tenKH = "Khách quen";
                        }
                    } catch (Exception e) {
                        tenKH = "Khách quen";
                    }
                }

                model.addRow(new Object[]{
                    hd.getId(),
                    hd.getThoiGian(),
                    tenKH
                });
            }

            // Fixed: Refresh the table display immediately
            model.fireTableDataChanged();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách hóa đơn: " + e.getMessage());
        }
    }

    private void loadHoaDonByTrangThai() {
        try {
            String trangThai = (String) jcbTrangThai.getSelectedItem();
            List<HoaDon> danhSachHD = bhDAO.getHoaDonByTrangThai(trangThai);
            DefaultTableModel model = (DefaultTableModel) tblhd.getModel();
            model.setRowCount(0);

            // Fixed: Sort invoices by ID in descending order
            danhSachHD.sort((h1, h2) -> {
                try {
                    int num1 = Integer.parseInt(h1.getId().substring(2));
                    int num2 = Integer.parseInt(h2.getId().substring(2));
                    return Integer.compare(num2, num1);
                } catch (NumberFormatException e) {
                    return h2.getId().compareTo(h1.getId());
                }
            });

            for (HoaDon hd : danhSachHD) {
                String tenKH = "Khách Vãng Lai";
                if (!hd.getIdKhachHang().equals("KH000")) {
                    try {
                        KhachHang kh = bhDAO.findKhachHangById(hd.getIdKhachHang());
                        if (kh != null) {
                            tenKH = kh.getTen();
                        } else {
                            tenKH = "Khách quen";
                        }
                    } catch (Exception e) {
                        tenKH = "Khách quen";
                    }
                }

                model.addRow(new Object[]{
                    hd.getId(),
                    hd.getThoiGian(),
                    tenKH
                });
            }

            // Fixed: Refresh the table display
            model.fireTableDataChanged();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lọc hóa đơn theo trạng thái: " + e.getMessage());
        }
    }

    private void loadChiTietHoaDon(String hoaDonId) {
        try {
            // Lấy danh sách chi tiết
            List<ChiTietHoaDon> chiTiets = bhDAO.getChiTietHoaDon(hoaDonId);
            DefaultTableModel model = (DefaultTableModel) tblcthd.getModel();
            model.setRowCount(0);

            for (ChiTietHoaDon ct : chiTiets) {
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
                    ct.getDonGia(),
                    thanhTien
                });
            }

            // Cập nhật label tổng tiền: lấy hóa đơn để lấy tongTienSauGiamGia (nếu có giảm giá)
            HoaDon hd = bhDAO.findHoaDonById(hoaDonId);
            if (hd != null) {
                jlbThanhTien.setText(currencyFormat.format(hd.getTongTienSauGiamGia()));
            } else {
                jlbThanhTien.setText("0 đ");
            }

            // Force refresh
            model.fireTableDataChanged();
            tblcthd.revalidate();
            tblcthd.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết hóa đơn: " + e.getMessage());
        }
    }

    private void timKhachHangTheoSDT() {
        String sdt = txtSDT.getText().trim();
        if (!sdt.isEmpty()) {
            try {
                khachHangHienTai = bhDAO.findKhachHangBySDT(sdt);
                if (khachHangHienTai == null) {
                    int option = JOptionPane.showConfirmDialog(this,
                            "Không tìm thấy khách hàng với số điện thoại này.\n"
                            + "Bạn có muốn thêm khách hàng mới không?",
                            "Thông báo",
                            JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(this,
                                "Chức năng thêm khách hàng sẽ được tích hợp với QLKHPanel");
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Tìm thấy khách hàng: " + khachHangHienTai.getTen());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm khách hàng: " + e.getMessage());
            }
        }
    }

    private void capNhatKhuyenMai() {
        try {
            sanPhamDaChon.clear();
            DefaultTableModel modelSP = (DefaultTableModel) tblsp.getModel();
            DefaultTableModel modelKM = (DefaultTableModel) tblsp1.getModel();
            modelKM.setRowCount(0);

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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật khuyến mãi: " + e.getMessage());
        }
    }

    private void tinhThanhTien() {
        try {
            BigDecimal tongTien = BigDecimal.ZERO;
            DefaultTableModel modelSP = (DefaultTableModel) tblsp.getModel();

            for (int i = 0; i < modelSP.getRowCount(); i++) {
                Boolean selected = (Boolean) modelSP.getValueAt(i, 0);
                if (selected != null && selected) {
                    try {
                        String soLuongStr = modelSP.getValueAt(i, 2).toString();
                        int soLuong = Integer.parseInt(soLuongStr);

                        if (soLuong <= 0) {
                            continue; // Skip invalid quantities
                        }

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

                        // Ensure non-negative total
                        if (thanhTienSP.compareTo(BigDecimal.ZERO) < 0) {
                            thanhTienSP = BigDecimal.ZERO;
                        }

                        tongTien = tongTien.add(thanhTienSP);
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        // Skip invalid entries
                        continue;
                    }
                }
            }

            jlbThanhTien.setText(currencyFormat.format(tongTien));
            tinhTienDu();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tính thành tiền: " + e.getMessage());
            jlbThanhTien.setText("0 đ");
        }
    }

    private void tinhTienDu() {
        try {
            String thanhTienText = jlbThanhTien.getText().replaceAll("[^0-9]", "");
            String tienKhachTraText = txtSDT1.getText().trim().replaceAll("[^0-9]", "");

            if (!thanhTienText.isEmpty() && !tienKhachTraText.isEmpty()) {
                double thanhTien = Double.parseDouble(thanhTienText);
                double tienKhachTra = Double.parseDouble(tienKhachTraText);
                double tienDu = tienKhachTra - thanhTien;

                jlbTienDu.setText(currencyFormat.format(Math.max(0, tienDu)));
            } else {
                jlbTienDu.setText("0 đ");
            }
        } catch (NumberFormatException e) {
            jlbTienDu.setText("0 đ");
        }
    }

    private void updateSoDonChoXuLy() {
        try {
            int soDon = bhDAO.demSoDonChoXuLy();
            jlbSoDonChoXuLy.setText(String.valueOf(soDon));
        } catch (Exception e) {
            jlbSoDonChoXuLy.setText("0");
        }
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

    // Fixed: Create Vietnamese-compatible PDF with proper font handling
    private void xuatHoaDonPDF(String hoaDonId) {
        try {
            Document document = new Document();
            String fileName = "HoaDon_" + hoaDonId + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            document.open();

            // Fixed: Use proper font for Vietnamese text
            BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(bf, 18, Font.BOLD);
            Font normalFont = new Font(bf, 12, Font.NORMAL);
            Font boldFont = new Font(bf, 12, Font.BOLD);

            Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" ", normalFont));

            HoaDon hoaDon = bhDAO.findHoaDonById(hoaDonId);
            if (hoaDon != null) {
                document.add(new Paragraph("Mã hóa đơn: " + hoaDon.getId(), normalFont));
                document.add(new Paragraph("Thời gian: " + hoaDon.getThoiGian(), normalFont));

                String tenKhachHang = "Khách vãng lai";
                if (!hoaDon.getIdKhachHang().equals("KH000")) {
                    try {
                        KhachHang kh = bhDAO.findKhachHangById(hoaDon.getIdKhachHang());
                        if (kh != null) {
                            tenKhachHang = kh.getTen();
                        }
                    } catch (Exception e) {
                        tenKhachHang = "Khách quen";
                    }
                }
                document.add(new Paragraph("Khách hàng: " + tenKhachHang, normalFont));

                // Add employee information to PDF
                String nhanVienInfo = "Nhân viên: ";
                if (loggedInUser != null) {
                    nhanVienInfo += loggedInUser.getTenDayDu() + " (" + loggedInUser.getId() + ")";
                } else {
                    nhanVienInfo += "Không xác định";
                }
                document.add(new Paragraph(nhanVienInfo, normalFont));
                document.add(new Paragraph(" ", normalFont));

                // Fixed: Create table with proper Vietnamese font
                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{3, 1, 2, 2});

                // Header cells with proper font
                PdfPCell headerCell1 = new PdfPCell(new Phrase("Sản phẩm", boldFont));
                PdfPCell headerCell2 = new PdfPCell(new Phrase("Số lượng", boldFont));
                PdfPCell headerCell3 = new PdfPCell(new Phrase("Đơn giá", boldFont));
                PdfPCell headerCell4 = new PdfPCell(new Phrase("Thành tiền", boldFont));

                headerCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                headerCell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
                headerCell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
                headerCell4.setBackgroundColor(BaseColor.LIGHT_GRAY);

                table.addCell(headerCell1);
                table.addCell(headerCell2);
                table.addCell(headerCell3);
                table.addCell(headerCell4);

                List<ChiTietHoaDon> chiTiets = bhDAO.getChiTietHoaDon(hoaDonId);
                for (ChiTietHoaDon ct : chiTiets) {
                    String tenSP = "Không xác định";
                    for (SanPham sp : danhSachSanPham) {
                        if (sp.getId().equals(ct.getIdSanPham())) {
                            tenSP = sp.getTen();
                            break;
                        }
                    }

                    table.addCell(new PdfPCell(new Phrase(tenSP, normalFont)));
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(ct.getSoLuong()), normalFont)));
                    table.addCell(new PdfPCell(new Phrase(currencyFormat.format((long) ct.getDonGia()), normalFont)));
                    table.addCell(new PdfPCell(new Phrase(currencyFormat.format(ct.getDonGia() * ct.getSoLuong()), normalFont)));
                }

                document.add(table);
                document.add(new Paragraph(" ", normalFont));

                Paragraph total = new Paragraph("Tổng tiền: " + currencyFormat.format(hoaDon.getTongTienSauGiamGia()), boldFont);
                total.setAlignment(Element.ALIGN_RIGHT);
                document.add(total);

                document.add(new Paragraph(" ", normalFont));
                document.add(new Paragraph("Cảm ơn quý khách!", normalFont));
            }

            document.close();

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(new File(fileName));
            } else {
                JOptionPane.showMessageDialog(this, "PDF đã được tạo: " + fileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to simple PDF without Vietnamese fonts
            try {
                Document document = new Document();
                String fileName = "HoaDon_" + hoaDonId + "_simple.pdf";
                PdfWriter.getInstance(document, new FileOutputStream(fileName));
                document.open();

                Font simpleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
                Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);

                document.add(new Paragraph("HOA DON BAN HANG", titleFont));
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Invoice ID: " + hoaDonId, simpleFont));
                document.add(new Paragraph("Generated successfully", simpleFont));

                document.close();

                JOptionPane.showMessageDialog(this, "PDF được tạo với font đơn giản: " + fileName);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + ex.getMessage());
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

        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnTaoHD = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblhd = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblcthd = new javax.swing.JTable();

        jLabel5.setText("jLabel5");

        setBackground(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlbTienDu))
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtTimKiem))
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlbThanhTien))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSDT1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jlbSoDonChoXuLy))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(18, 18, 18)
                                        .addComponent(jcbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTaoHD)
                    .addComponent(jLabel10)
                    .addComponent(jlbThanhTien)
                    .addComponent(jLabel11)
                    .addComponent(jcbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXuatHoaDon)
                    .addComponent(jLabel12)
                    .addComponent(txtSDT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jlbSoDonChoXuLy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
        try {
            DefaultTableModel modelSP = (DefaultTableModel) tblsp.getModel();
            boolean hasSelectedProduct = false;

            // Check if any product is selected
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

            // Calculate totals
            BigDecimal tongTienGoc = BigDecimal.ZERO;
            BigDecimal tongGiamGia = BigDecimal.ZERO;

            for (int i = 0; i < modelSP.getRowCount(); i++) {
                Boolean selected = (Boolean) modelSP.getValueAt(i, 0);
                if (selected != null && selected) {
                    try {
                        String soLuongStr = modelSP.getValueAt(i, 2).toString();
                        int soLuong = Integer.parseInt(soLuongStr);

                        if (soLuong <= 0) {
                            JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                            return;
                        }

                        SanPham sp = danhSachSanPham.get(i);

                        BigDecimal giaSP = BigDecimal.valueOf(sp.getGia());
                        BigDecimal thanhTienGoc = giaSP.multiply(BigDecimal.valueOf(soLuong));
                        tongTienGoc = tongTienGoc.add(thanhTienGoc);

                        // Apply promotions
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
                    } catch (IndexOutOfBoundsException e) {
                        JOptionPane.showMessageDialog(this, "Lỗi dữ liệu sản phẩm!");
                        return;
                    }
                }
            }

            BigDecimal tongTienSauGiamGia = tongTienGoc.subtract(tongGiamGia);

            // Determine customer ID
            String khachHangId = "KH000";
            if (jcbKhachHang.getSelectedIndex() == 1 && khachHangHienTai != null) {
                khachHangId = khachHangHienTai.getId();
            }

            // Use the logged-in user's ID instead of hardcoded "ND001"
            String nhanVienId = (loggedInUser != null) ? loggedInUser.getId() : "ND001";

            // Create invoice
            currentHoaDonId = bhDAO.taoHoaDon(khachHangId, nhanVienId,
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
                        } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            // Skip invalid entries
                            continue;
                        }
                    }
                }

                String userName = (loggedInUser != null) ? loggedInUser.getTenDayDu() : "Unknown";
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công! ID: " + currentHoaDonId
                        + "\nNhân viên: " + userName);

                // Fixed: Use SwingUtilities.invokeLater to ensure proper refresh timing
                SwingUtilities.invokeLater(() -> {
                    loadHoaDon();
                    updateSoDonChoXuLy();
                    loadChiTietHoaDon(currentHoaDonId);
                });

                // Reset form
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thất bại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tạo hóa đơn: " + e.getMessage());
        }
    }//GEN-LAST:event_btnTaoHDActionPerformed

    private void tblhdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblhdMouseClicked
    }//GEN-LAST:event_tblhdMouseClicked

    private void btnXuatHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatHoaDonActionPerformed
        try {
            if (currentHoaDonId == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!");
                return;
            }

            if (!validatePayment()) {
                JOptionPane.showMessageDialog(this, "Khách hàng chưa thanh toán đủ!");
                return;
            }

            // Update invoice status
            if (bhDAO.capNhatTrangThaiHoaDon(currentHoaDonId, "Đã Thanh Toán")) {
                JOptionPane.showMessageDialog(this, "Xuất hóa đơn thành công!");

                // Export PDF
                xuatHoaDonPDF(currentHoaDonId);

                // Fixed: Refresh with proper timing
                SwingUtilities.invokeLater(() -> {
                    loadHoaDon();
                    updateSoDonChoXuLy();
                });
            } else {
                JOptionPane.showMessageDialog(this, "Xuất hóa đơn thất bại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất hóa đơn: " + e.getMessage());
        }
    }//GEN-LAST:event_btnXuatHoaDonActionPerformed

    private void btnHuyHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyHDActionPerformed
        try {
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
                    // Fixed: Refresh with proper timing
                    SwingUtilities.invokeLater(() -> {
                        loadHoaDon();
                        updateSoDonChoXuLy();
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Hủy hóa đơn thất bại!");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi hủy hóa đơn: " + e.getMessage());
        }
    }//GEN-LAST:event_btnHuyHDActionPerformed

    private void jcbKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbKhachHangActionPerformed

    private void jbtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtTimKiemActionPerformed
        try {
            String hoaDonId = txtTimKiem.getText().trim();

            if (hoaDonId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập ID hóa đơn!");
                return;
            }

            HoaDon hoaDon = bhDAO.findHoaDonById(hoaDonId);
            if (hoaDon != null) {
                DefaultTableModel model = (DefaultTableModel) tblhd.getModel();
                model.setRowCount(0);

                String tenKH = "Khách Vãng Lai";
                if (!hoaDon.getIdKhachHang().equals("KH000")) {
                    try {
                        KhachHang kh = bhDAO.findKhachHangById(hoaDon.getIdKhachHang());
                        if (kh != null) {
                            tenKH = kh.getTen();
                        } else {
                            tenKH = "Khách quen";
                        }
                    } catch (Exception e) {
                        tenKH = "Khách quen";
                    }
                }

                model.addRow(new Object[]{
                    hoaDon.getId(),
                    hoaDon.getThoiGian(),
                    tenKH
                });

                currentHoaDonId = hoaDonId;
                // Fixed: Use SwingUtilities.invokeLater for proper timing
                SwingUtilities.invokeLater(() -> {
                    loadChiTietHoaDon(hoaDonId);
                });

                JOptionPane.showMessageDialog(this, "Tìm thấy hóa đơn!");
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn với ID: " + hoaDonId);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm hóa đơn: " + e.getMessage());
        }
    }//GEN-LAST:event_jbtTimKiemActionPerformed

    private void resetForm() {
        DefaultTableModel modelSP = (DefaultTableModel) tblsp.getModel();
        for (int i = 0; i < modelSP.getRowCount(); i++) {
            modelSP.setValueAt(false, i, 0);
            modelSP.setValueAt("1", i, 2);
        }

        DefaultTableModel modelKM = (DefaultTableModel) tblsp1.getModel();
        modelKM.setRowCount(0);

        jcbKhachHang.setSelectedIndex(0);
        txtSDT.setText("");
        txtSDT1.setText("");
        khachHangHienTai = null;

        jlbThanhTien.setText("0 đ");
        jlbTienDu.setText("0 đ");
    }

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
