/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import controller.QLKMDAO;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.ChiTietKhuyenMai;
import model.KhuyenMai;

public final class QLKMPanel extends javax.swing.JPanel {

    private final QLKMDAO dao;
    private String selectedProductId = "";
    
    /**
     * Creates new form QLKMPanel
     */
    public QLKMPanel() {
        initComponents();
        this.dao = new QLKMDAO();
        initializeDiscountTypeListener();
        setupTableSelectionListener();
        loadData();
    }
    
    private void initializeDiscountTypeListener() {
        jcbNgaySinh3.addActionListener(e -> {
            String selectedType = (String) jcbNgaySinh3.getSelectedItem();
            updateDiscountFields(selectedType);
        });
        
        // Initialize visibility based on current selection
        updateDiscountFields((String) jcbNgaySinh3.getSelectedItem());
    }
    
    private void updateDiscountFields(String discountType) {
        if (null == discountType) {
            jLabelChange.setVisible(false);
            txtChange.setVisible(false);
        } else switch (discountType) {
            case "--Mời bạn chọn--" -> {
                jLabelChange.setVisible(false);
                txtChange.setVisible(false);
            }
            case "Phần trăm" -> {
                jLabelChange.setText("Mức Giảm (%):");
                jLabelChange.setVisible(true);
                txtChange.setVisible(true);
            }
            case "Theo tiền" -> {
                jLabelChange.setText("Số tiền giảm (VND):");
                jLabelChange.setVisible(true);
                txtChange.setVisible(true);
            }
            default -> {
            }
        }
    }
    
    private void setupTableSelectionListener() {
        jTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowData();
            }
        });
    }
    
    public void loadData() {
        loadKhuyenMaiData();
        loadChiTietKhuyenMaiData();
    }
    
    private void loadKhuyenMaiData() {
        try {
            List<KhuyenMai> list = dao.getAllKM();
            DefaultTableModel model = (DefaultTableModel) jTable.getModel();
            model.setRowCount(0);
            
            for (KhuyenMai km : list) {
                model.addRow(dao.getRow(km));
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu khuyến mãi: " + e.getMessage());
        }
    }
    
    private void loadChiTietKhuyenMaiData() {
        try {
            List<ChiTietKhuyenMai> list = dao.getAllCTKM();
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);
            
            for (ChiTietKhuyenMai ctkm : list) {
                model.addRow(dao.getRow(ctkm));
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu chi tiết khuyến mãi: " + e.getMessage());
        }
    }
    
    public void setSelectedProduct(String productId) {
        this.selectedProductId = productId;
        JOptionPane.showMessageDialog(this, "Đã chọn sản phẩm: " + productId);
    }
    
    private void addPromotion() {
        if (!validateInput()) {
            return;
        }
        
        try {
            // Generate new IDs
            String newCTKMId = generateNewCTKMId();
            String newKMId = generateNewKMId();
            
            // Create ChiTietKhuyenMai first
            ChiTietKhuyenMai ctkm = createChiTietKhuyenMaiFromInput(newCTKMId);
            int ctkmResult = dao.addCTKM(ctkm);
            
            if (ctkmResult > 0) {
                // Create KhuyenMai
                KhuyenMai km = createKhuyenMaiFromInput(newKMId, newCTKMId);
                int kmResult = dao.addKM(km);
                
                if (kmResult > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công!");
                    clearForm();
                    loadData();
                } else {
                    // Rollback CTKM if KM creation failed
                    dao.deleteCTKM(newCTKMId);
                    JOptionPane.showMessageDialog(this, "Lỗi khi thêm khuyến mãi!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm chi tiết khuyến mãi!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void editPromotion() {
        int selectedRow = jTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần sửa!");
            return;
        }
        
        if (!validateInput()) {
            return;
        }
        
        try {
            String oldKMId = (String) jTable.getValueAt(selectedRow, 0);
            String oldCTKMId = (String) jTable.getValueAt(selectedRow, 1);
            
            // Update ChiTietKhuyenMai
            ChiTietKhuyenMai ctkm = createChiTietKhuyenMaiFromInput(oldCTKMId);
            int ctkmResult = dao.editCTKM(ctkm, oldCTKMId);
            
            if (ctkmResult > 0) {
                // Update KhuyenMai
                KhuyenMai km = createKhuyenMaiFromInput(oldKMId, oldCTKMId);
                int kmResult = dao.editKM(km, oldKMId);
                
                if (kmResult > 0) {
                    JOptionPane.showMessageDialog(this, "Sửa khuyến mãi thành công!");
                    clearForm();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi sửa khuyến mãi!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi sửa chi tiết khuyến mãi!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void deletePromotion() {
        int selectedRow = jTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khuyến mãi này?", 
                                                   "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            String kmId = (String) jTable.getValueAt(selectedRow, 0);
            String ctkmId = (String) jTable.getValueAt(selectedRow, 1);
            
            // Delete KhuyenMai first
            int kmResult = dao.deleteKM(kmId);
            if (kmResult > 0) {
                // Delete ChiTietKhuyenMai
                int ctkmResult = dao.deleteCTKM(ctkmId);
                if (ctkmResult > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa khuyến mãi thành công!");
                    clearForm();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa chi tiết khuyến mãi!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa khuyến mãi!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void loadSelectedRowData() {
        int selectedRow = jTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        try {
            String kmId = (String) jTable.getValueAt(selectedRow, 0);
            String ctkmId = (String) jTable.getValueAt(selectedRow, 1);
            
            KhuyenMai km = dao.getKMById(kmId);
            ChiTietKhuyenMai ctkm = dao.getCTKMById(ctkmId);
            
            if (km != null && ctkm != null) {
                txtDiaChi.setText(km.getTen());
                txtHangKhachHang.setText(km.getMoTa());
                txtHangKhachHang1.setText(String.valueOf(km.getSoLuong()));
                
                // Set dates
                setDateToComponents(km.getThoiGianApDung(), jcbNgaySinh, jcbThangSinh, jcbNamSinh);
                setDateToComponents(km.getThoiGianKetThuc(), jcbNgaySinh1, jcbThangSinh1, jcbNamSinh1);
                
                // Set discount type and related fields
                jcbNgaySinh3.setSelectedItem(ctkm.getHinhThucGiam());
                jcbNgaySinh2.setSelectedItem(ctkm.getQuaTang());
                
                if (ctkm.getHinhThucGiam().equals("Phần trăm")) {
                    txtChange.setText(String.valueOf(ctkm.getMucGiamGia()));
                } else if (ctkm.getHinhThucGiam().equals("Theo tiền")) {
                    txtChange.setText(String.valueOf(ctkm.getSoTienGiamGia()));
                }
                
                selectedProductId = ctkm.getSanPhamid();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }
    
    private boolean validateInput() {
        if (txtDiaChi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khuyến mãi!");
            return false;
        }
        
        if (txtHangKhachHang.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mô tả khuyến mãi!");
            return false;
        }
        
        if (txtHangKhachHang1.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!");
            return false;
        }
        
        try {
            int soLuong = Integer.parseInt(txtHangKhachHang1.getText().trim());
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!");
            return false;
        }
        
        if (selectedProductId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm áp dụng khuyến mãi!");
            return false;
        }
        
        String discountType = (String) jcbNgaySinh3.getSelectedItem();
        if (discountType == null || discountType.equals("--Mời bạn chọn--")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hình thức giảm giá!");
            return false;
        }
        
        if (txtChange.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá trị giảm giá!");
            return false;
        }
        
        try {
            float value = Float.parseFloat(txtChange.getText().trim());
            if (value <= 0) {
                JOptionPane.showMessageDialog(this, "Giá trị giảm giá phải lớn hơn 0!");
                return false;
            }
            
            if (discountType.equals("Phần trăm") && value > 100) {
                JOptionPane.showMessageDialog(this, "Mức giảm phần trăm không được vượt quá 100%!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá trị giảm giá phải là số!");
            return false;
        }
        
        return true;
    }
    
    private String generateNewKMId() throws SQLException, ClassNotFoundException {
        List<KhuyenMai> list = dao.getAllKM();
        int maxNum = 0;
        
        for (KhuyenMai km : list) {
            String id = km.getId();
            if (id.startsWith("KM")) {
                try {
                    int num = Integer.parseInt(id.substring(2));
                    maxNum = Math.max(maxNum, num);
                } catch (NumberFormatException e) {
                    // Skip invalid format
                }
            }
        }
        
        return String.format("KM%03d", maxNum + 1);
    }
    
    private String generateNewCTKMId() throws SQLException, ClassNotFoundException {
        List<ChiTietKhuyenMai> list = dao.getAllCTKM();
        int maxNum = 0;
        
        for (ChiTietKhuyenMai ctkm : list) {
            String id = ctkm.getId();
            if (id.startsWith("CTKM")) {
                try {
                    int num = Integer.parseInt(id.substring(4));
                    maxNum = Math.max(maxNum, num);
                } catch (NumberFormatException e) {
                    // Skip invalid format
                }
            }
        }
        
        return String.format("CTKM%03d", maxNum + 1);
    }
    
    private ChiTietKhuyenMai createChiTietKhuyenMaiFromInput(String id) {
        ChiTietKhuyenMai ctkm = new ChiTietKhuyenMai();
        ctkm.setId(id);
        ctkm.setHinhThucGiam((String) jcbNgaySinh3.getSelectedItem());
        ctkm.setSanPhamid(selectedProductId);
        ctkm.setQuaTang((String) jcbNgaySinh2.getSelectedItem());
        
        float changeValue = Float.parseFloat(txtChange.getText().trim());
        if (jcbNgaySinh3.getSelectedItem().equals("Phần trăm")) {
            ctkm.setMucGiamGia(changeValue);
            ctkm.setSoTienGiamGia(0.0f);
        } else {
            ctkm.setSoTienGiamGia(changeValue);
            ctkm.setMucGiamGia(0.0f);
        }
        
        return ctkm;
    }
    
    private KhuyenMai createKhuyenMaiFromInput(String id, String ctkmId) throws ParseException {
        KhuyenMai km = new KhuyenMai();
        km.setId(id);
        km.setChiTietid(ctkmId);
        km.setTen(txtDiaChi.getText().trim());
        km.setMoTa(txtHangKhachHang.getText().trim());
        km.setSoLuong(Integer.parseInt(txtHangKhachHang1.getText().trim()));
        
        km.setThoiGianApDung(getDateFromComponents(jcbNgaySinh, jcbThangSinh, jcbNamSinh));
        km.setThoiGianKetThuc(getDateFromComponents(jcbNgaySinh1, jcbThangSinh1, jcbNamSinh1));
        
        return km;
    }
    
    private Date getDateFromComponents(javax.swing.JComboBox<String> dayCombo, javax.swing.JComboBox<String> monthCombo, javax.swing.JTextField yearField) throws ParseException {
        String day = (String) dayCombo.getSelectedItem();
        String month = (String) monthCombo.getSelectedItem();
        String year = yearField.getText().trim();
        
        String dateString = year + "-" + month + "-" + day;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateString);
    }
    
    private void setDateToComponents(Date date, javax.swing.JComboBox<String> dayCombo, javax.swing.JComboBox<String> monthCombo, javax.swing.JTextField yearField) {
        if (date != null) {
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            
            dayCombo.setSelectedItem(dayFormat.format(date));
            monthCombo.setSelectedItem(monthFormat.format(date));
            yearField.setText(yearFormat.format(date));
        }
    }
    
    private void clearForm() {
        txtDiaChi.setText("");
        txtHangKhachHang.setText("");
        txtHangKhachHang1.setText("");
        txtChange.setText("");
        jcbNamSinh.setText("");
        jcbNamSinh1.setText("");
        jcbNgaySinh.setSelectedIndex(0);
        jcbThangSinh.setSelectedIndex(0);
        jcbNgaySinh1.setSelectedIndex(0);
        jcbThangSinh1.setSelectedIndex(0);
        jcbNgaySinh2.setSelectedIndex(0);
        jcbNgaySinh3.setSelectedIndex(0);
        selectedProductId = "";
    }
    
    private void searchPromotion(String keyword) {
        try {
            List<KhuyenMai> allPromotions = dao.getAllKM();
            DefaultTableModel model = (DefaultTableModel) jTable.getModel();
            model.setRowCount(0);
            
            for (KhuyenMai km : allPromotions) {
                if (km.getTen().toLowerCase().contains(keyword.toLowerCase()) ||
                    km.getMoTa().toLowerCase().contains(keyword.toLowerCase()) ||
                    km.getId().toLowerCase().contains(keyword.toLowerCase())) {
                    model.addRow(dao.getRow(km));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + e.getMessage());
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

        popupMenu1 = new java.awt.PopupMenu();
        popupMenu2 = new java.awt.PopupMenu();
        popupMenu3 = new java.awt.PopupMenu();
        txtDiaChi = new javax.swing.JTextField();
        txtHangKhachHang = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jbtThem = new javax.swing.JButton();
        jbtSua = new javax.swing.JButton();
        jbtXoa = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        jbtTimKiem = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jbtLamMoi = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtHangKhachHang1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jcbNgaySinh = new javax.swing.JComboBox<>();
        jcbThangSinh = new javax.swing.JComboBox<>();
        jcbNamSinh = new javax.swing.JTextField();
        jcbNgaySinh1 = new javax.swing.JComboBox<>();
        jcbThangSinh1 = new javax.swing.JComboBox<>();
        jcbNamSinh1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jcbNgaySinh2 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jcbNgaySinh3 = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jbtSanPham = new javax.swing.JButton();
        jLabelChange = new javax.swing.JLabel();
        txtChange = new javax.swing.JTextField();

        popupMenu1.setLabel("popupMenu1");

        popupMenu2.setLabel("popupMenu2");

        popupMenu3.setLabel("popupMenu3");

        setBackground(new java.awt.Color(255, 255, 255));

        txtDiaChi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiaChiActionPerformed(evt);
            }
        });

        txtHangKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHangKhachHangActionPerformed(evt);
            }
        });

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Tên", "Mô Tả", "Số Lượng", "Thời gian áp dụng", "Thời gian kết thúc"
            }
        ));
        jScrollPane1.setViewportView(jTable);

        jbtThem.setBackground(new java.awt.Color(41, 62, 80));
        jbtThem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtThem.setForeground(new java.awt.Color(255, 255, 255));
        jbtThem.setText("Thêm");
        jbtThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtThemActionPerformed(evt);
            }
        });

        jbtSua.setBackground(new java.awt.Color(41, 62, 80));
        jbtSua.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtSua.setForeground(new java.awt.Color(255, 255, 255));
        jbtSua.setText("Sửa");
        jbtSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSuaActionPerformed(evt);
            }
        });

        jbtXoa.setBackground(new java.awt.Color(41, 62, 80));
        jbtXoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtXoa.setForeground(new java.awt.Color(255, 255, 255));
        jbtXoa.setText("Xóa");
        jbtXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtXoaActionPerformed(evt);
            }
        });

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        jbtTimKiem.setBackground(new java.awt.Color(41, 62, 80));
        jbtTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        jbtTimKiem.setText("Tìm kiếm");
        jbtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtTimKiemActionPerformed(evt);
            }
        });

        jLabel3.setText("Mô tả:");

        jLabel5.setText("Tên:");

        jbtLamMoi.setBackground(new java.awt.Color(41, 62, 80));
        jbtLamMoi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtLamMoi.setForeground(new java.awt.Color(255, 255, 255));
        jbtLamMoi.setText("Làm mới");
        jbtLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtLamMoiActionPerformed(evt);
            }
        });

        jLabel4.setText("Số lượng:");

        txtHangKhachHang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHangKhachHang1ActionPerformed(evt);
            }
        });

        jLabel7.setText("Thời gian áp dụng:");

        jLabel8.setText("Thời gian kết thúc:");

        jcbNgaySinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        jcbThangSinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jcbThangSinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbThangSinhActionPerformed(evt);
            }
        });

        jcbNamSinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbNamSinhActionPerformed(evt);
            }
        });

        jcbNgaySinh1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        jcbThangSinh1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jcbThangSinh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbThangSinh1ActionPerformed(evt);
            }
        });

        jcbNamSinh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbNamSinh1ActionPerformed(evt);
            }
        });

        jLabel9.setText("Sản phẩm ảnh hưởng:");

        jLabel10.setText("Quà tặng:");

        jcbNgaySinh2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Không", "Phiếu mua hàng" }));

        jLabel11.setText("Hình thức giảm:");

        jcbNgaySinh3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Mời bạn chọn--", "Phần trăm", "Theo tiền" }));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Hình Thức Giảm", "Điện Thoại", "Địa Chỉ", "ID Hạng Khách Hàng"
            }
        ));
        jScrollPane3.setViewportView(jTable2);

        jbtSanPham.setText("Chọn sản phẩm");
        jbtSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSanPhamActionPerformed(evt);
            }
        });

        jLabelChange.setText(" ");

        txtChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChangeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 42, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel8)
                            .addComponent(jLabelChange, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtHangKhachHang1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jcbNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jcbThangSinh, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jcbNamSinh, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jcbNgaySinh1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jcbThangSinh1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jcbNamSinh1))
                                    .addComponent(jcbNgaySinh2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jcbNgaySinh3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtHangKhachHang)
                                    .addComponent(txtDiaChi))
                                .addGap(18, 21, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(txtChange)
                                .addGap(18, 18, 18))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jbtSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbtXoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtSua, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtThem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtLamMoi, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE))
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(txtTimKiem)
                            .addGap(18, 18, 18)
                            .addComponent(jbtTimKiem))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txtHangKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(jbtSanPham))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtHangKhachHang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jcbNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jcbThangSinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jcbNamSinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jcbNgaySinh1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jcbThangSinh1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jcbNamSinh1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(jcbNgaySinh2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(jcbNgaySinh3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelChange)
                                    .addComponent(txtChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jbtThem)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtSua)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbtXoa))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jbtTimKiem))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtLamMoi)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtDiaChiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiaChiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiaChiActionPerformed

    private void txtHangKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHangKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHangKhachHangActionPerformed

    private void jbtThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtThemActionPerformed
        addPromotion();
    }//GEN-LAST:event_jbtThemActionPerformed

    private void jbtSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSuaActionPerformed
        editPromotion();
    }//GEN-LAST:event_jbtSuaActionPerformed

    private void jbtXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtXoaActionPerformed
        deletePromotion();
    }//GEN-LAST:event_jbtXoaActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void jbtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtTimKiemActionPerformed
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
        } else {
            searchPromotion(keyword);
        }
    }//GEN-LAST:event_jbtTimKiemActionPerformed

    private void jbtLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLamMoiActionPerformed
        clearForm();
        loadData();
        txtTimKiem.setText("");
    }//GEN-LAST:event_jbtLamMoiActionPerformed

    private void txtHangKhachHang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHangKhachHang1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHangKhachHang1ActionPerformed

    private void jcbThangSinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbThangSinhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbThangSinhActionPerformed

    private void jcbNamSinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbNamSinhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbNamSinhActionPerformed

    private void jcbThangSinh1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbThangSinh1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbThangSinh1ActionPerformed

    private void jcbNamSinh1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbNamSinh1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbNamSinh1ActionPerformed

    private void txtChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChangeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChangeActionPerformed

    private void jbtSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSanPhamActionPerformed
        ChonSPKMPanel productPanel = new ChonSPKMPanel();
        productPanel.setParentPanel(this);
        productPanel.setVisible(true);
    }//GEN-LAST:event_jbtSanPhamActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelChange;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton jbtLamMoi;
    private javax.swing.JButton jbtSanPham;
    private javax.swing.JButton jbtSua;
    private javax.swing.JButton jbtThem;
    private javax.swing.JButton jbtTimKiem;
    private javax.swing.JButton jbtXoa;
    private javax.swing.JTextField jcbNamSinh;
    private javax.swing.JTextField jcbNamSinh1;
    private javax.swing.JComboBox<String> jcbNgaySinh;
    private javax.swing.JComboBox<String> jcbNgaySinh1;
    private javax.swing.JComboBox<String> jcbNgaySinh2;
    private javax.swing.JComboBox<String> jcbNgaySinh3;
    private javax.swing.JComboBox<String> jcbThangSinh;
    private javax.swing.JComboBox<String> jcbThangSinh1;
    private java.awt.PopupMenu popupMenu1;
    private java.awt.PopupMenu popupMenu2;
    private java.awt.PopupMenu popupMenu3;
    private javax.swing.JTextField txtChange;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtHangKhachHang;
    private javax.swing.JTextField txtHangKhachHang1;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
