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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;

// iTextPDF imports
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

public final class DOANHTHUPanel extends javax.swing.JPanel {

    private DOANHTHUDAO doanhThuDAO;
    private DefaultTableModel tableModel;
    private NumberFormat currencyFormat;
    private TableRowSorter<TableModel> tableSorter; // Add sorter for Excel-like functionality

    public DOANHTHUPanel() {
        initComponents();
        initializeBusinessLogic();
        setupEventListeners();
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
        // Add action listeners for combo boxes only - no automatic filtering
        ActionListener dateChangeListener = (ActionEvent e) -> {
            // Only silently update - no automatic filtering or error messages
            // Users will click Search button when ready
        };

        jcbNgayBatDau.addActionListener(dateChangeListener);
        jcbThangBatDau.addActionListener(dateChangeListener);
        jcbNgayKetThuc.addActionListener(dateChangeListener);
        jcbThangKetThuc.addActionListener(dateChangeListener);

        // No document listeners for year fields to prevent constant validation
        // Users will use the Search button when they're done entering dates
    }

    private void setupTableModel() {
        String[] columnNames = {"ID Sản phẩm", "Tên sản phẩm", "Giá", "Số lượng", "Thành tiền"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 ->
                        String.class;
                    case 1 ->
                        String.class;
                    case 2 ->
                        Double.class;
                    case 3 ->
                        Integer.class;
                    case 4 ->
                        Double.class;
                    default ->
                        String.class;
                }; // ID
                // Tên
                // Giá (for proper numeric sorting)
                // Số lượng (for proper numeric sorting)
                // Thành tiền (for proper numeric sorting)
            }
        };

        jTable1.setModel(tableModel);

        // Set up table sorter for Excel-like sorting functionality
        tableSorter = new TableRowSorter<>(tableModel);
        jTable1.setRowSorter(tableSorter);

        // Custom comparators for currency formatted columns
        tableSorter.setComparator(2, (Double o1, Double o2) -> Double.compare(o1, o2));

        tableSorter.setComparator(4, (Double o1, Double o2) -> Double.compare(o1, o2));

        // Set column widths
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(80);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(120);

        // Setup table renderers immediately
        updateTableRenderer();
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
            int tongSanPham = doanhThuDAO.getTongSanPhamBanRaToanBo();
            double tongDoanhThu = doanhThuDAO.getTongDoanhThuToanBo();
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

            // Validate dates only if we have complete 4-digit years
            if (startYear.length() != 4 || endYear.length() != 4) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập năm đầy đủ (4 chữ số)!",
                        "Lỗi định dạng năm",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!doanhThuDAO.isValidDate(dayStart, monthStart, yearStart)) {
                JOptionPane.showMessageDialog(this,
                        "Ngày bắt đầu không hợp lệ!",
                        "Lỗi ngày tháng",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!doanhThuDAO.isValidDate(dayEnd, monthEnd, yearEnd)) {
                JOptionPane.showMessageDialog(this,
                        "Ngày kết thúc không hợp lệ!",
                        "Lỗi ngày tháng",
                        JOptionPane.WARNING_MESSAGE);
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

    // Search button functionality - Fixed to properly filter by date range
    private void performSearch() {
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

            // Validate year format
            if (startYear.length() != 4 || endYear.length() != 4) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập năm đầy đủ (4 chữ số)!",
                        "Lỗi định dạng năm",
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
                        "Dữ liệu ngày tháng không hợp lệ!",
                        "Lỗi định dạng",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate dates
            if (!doanhThuDAO.isValidDate(dayStart, monthStart, yearStart)) {
                JOptionPane.showMessageDialog(this,
                        "Ngày bắt đầu không hợp lệ!",
                        "Lỗi ngày tháng",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!doanhThuDAO.isValidDate(dayEnd, monthEnd, yearEnd)) {
                JOptionPane.showMessageDialog(this,
                        "Ngày kết thúc không hợp lệ!",
                        "Lỗi ngày tháng",
                        JOptionPane.WARNING_MESSAGE);
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

            // Get filtered data for the date range - This should now work correctly
            List<DoanhThuSanPham> danhSach = doanhThuDAO.getDoanhThuTheoKhoangThoiGian(ngayBatDau, ngayKetThuc);

            if (danhSach.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không có dữ liệu trong khoảng thời gian đã chọn!",
                        "Không có dữ liệu",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            updateTable(danhSach);

            // Get totals for the date range from database
            int tongSanPham = doanhThuDAO.getTongSanPhamBanRa(ngayBatDau, ngayKetThuc);
            double tongDoanhThu = doanhThuDAO.getTongDoanhThu(ngayBatDau, ngayKetThuc);
            updateSummaryLabels(tongSanPham, tongDoanhThu);

            System.out.println("Debug - Date range: " + ngayBatDau + " to " + ngayKetThuc);
            System.out.println("Debug - Found " + danhSach.size() + " products");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi hệ thống: " + e.getMessage(),
                    "Lỗi nghiêm trọng",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Refresh button functionality
    private void performRefresh() {
        try {
            // Reset all date fields to default
            initializeDateFields();

            // Load initial data (all products with sales)
            loadInitialData();

            JOptionPane.showMessageDialog(this,
                    "Dữ liệu đã được làm mới!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi làm mới dữ liệu: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Report export functionality - PDF export
    private void exportReport() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu báo cáo");

            // Set default filename with timestamp
            String defaultFileName = "BaoCaoDoanhThu_"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            fileChooser.setSelectedFile(new java.io.File(defaultFileName));

            // Set file filter for PDF files
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files (*.pdf)", "pdf");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();

                // Ensure file has .pdf extension
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                    fileToSave = new java.io.File(filePath);
                }

                // Get current data from table
                List<DoanhThuSanPham> currentData = getCurrentTableData();

                // Write PDF report
                writePDFReport(fileToSave, currentData);

                JOptionPane.showMessageDialog(this,
                        "Báo cáo đã được xuất thành công!\nĐường dẫn: " + filePath,
                        "Xuất báo cáo thành công",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi xuất báo cáo: " + e.getMessage(),
                    "Lỗi xuất báo cáo",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Get current data from the table - FIXED to handle Double values correctly
    private List<DoanhThuSanPham> getCurrentTableData() {
        java.util.List<DoanhThuSanPham> data = new java.util.ArrayList<>();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String id = (String) tableModel.getValueAt(i, 0);
            String ten = (String) tableModel.getValueAt(i, 1);

            // Handle Double values directly - no string parsing needed
            Double giaObj = (Double) tableModel.getValueAt(i, 2);
            double gia = giaObj != null ? giaObj : 0.0;

            Integer soLuongObj = (Integer) tableModel.getValueAt(i, 3);
            int soLuong = soLuongObj != null ? soLuongObj : 0;

            DoanhThuSanPham item = new DoanhThuSanPham(id, ten, gia, soLuong);
            data.add(item);
        }

        return data;
    }

    // Write PDF report using iTextPDF
    private void writePDFReport(java.io.File file, List<DoanhThuSanPham> data) throws IOException {
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Create fonts for Vietnamese text
            BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            Font headerFont = new Font(baseFont, 12, Font.BOLD);
            Font normalFont = new Font(baseFont, 10);
            Font tableHeaderFont = new Font(baseFont, 10, Font.BOLD);

            // Title
            Paragraph title = new Paragraph("BÁO CÁO DOANH THU SẢN PHẨM", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Report info
            Paragraph info = new Paragraph("Thời gian tạo báo cáo: "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), normalFont);
            document.add(info);

            // Date range info
            String startYear = jcbNamBatDau.getText().trim();
            String endYear = jcbNamKetThuc.getText().trim();
            if (!startYear.isEmpty() && !endYear.isEmpty()) {
                String startDate = String.format("%s/%s/%s",
                        jcbNgayBatDau.getSelectedItem(),
                        jcbThangBatDau.getSelectedItem(),
                        startYear);
                String endDate = String.format("%s/%s/%s",
                        jcbNgayKetThuc.getSelectedItem(),
                        jcbThangKetThuc.getSelectedItem(),
                        endYear);
                Paragraph dateRange = new Paragraph("Khoảng thời gian: " + startDate + " - " + endDate, normalFont);
                document.add(dateRange);
            } else {
                Paragraph dateRange = new Paragraph("Khoảng thời gian: Tất cả", normalFont);
                document.add(dateRange);
            }

            document.add(new Paragraph(" ")); // Empty line

            // Create table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Set column widths
            float[] columnWidths = {15f, 35f, 15f, 15f, 20f};
            table.setWidths(columnWidths);

            // Table headers
            String[] headers = {"ID Sản phẩm", "Tên sản phẩm", "Giá", "Số lượng", "Thành tiền"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, tableHeaderFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new com.itextpdf.text.BaseColor(230, 230, 230));
                cell.setPadding(8);
                table.addCell(cell);
            }

            // Table data
            for (DoanhThuSanPham item : data) {
                // ID
                PdfPCell idCell = new PdfPCell(new Phrase(item.getIdSanPham(), normalFont));
                idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                idCell.setPadding(5);
                table.addCell(idCell);

                // Name
                PdfPCell nameCell = new PdfPCell(new Phrase(item.getTenSanPham(), normalFont));
                nameCell.setPadding(5);
                table.addCell(nameCell);

                // Price
                PdfPCell priceCell = new PdfPCell(new Phrase(currencyFormat.format(item.getGia()), normalFont));
                priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                priceCell.setPadding(5);
                table.addCell(priceCell);

                // Quantity
                PdfPCell qtyCell = new PdfPCell(new Phrase(String.valueOf(item.getSoLuongBan()), normalFont));
                qtyCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                qtyCell.setPadding(5);
                table.addCell(qtyCell);

                // Total
                PdfPCell totalCell = new PdfPCell(new Phrase(currencyFormat.format(item.getThanhTien()), normalFont));
                totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalCell.setPadding(5);
                table.addCell(totalCell);
            }

            document.add(table);

            // Summary
            document.add(new Paragraph(" ")); // Empty line
            Paragraph summaryTitle = new Paragraph("TỔNG KẾT", headerFont);
            summaryTitle.setSpacingBefore(10);
            document.add(summaryTitle);

            Paragraph totalProducts = new Paragraph("Tổng số sản phẩm bán ra: " + jLabel1.getText(), normalFont);
            document.add(totalProducts);

            Paragraph totalRevenue = new Paragraph("Tổng doanh thu: " + jLabel2.getText(), normalFont);
            document.add(totalRevenue);

        } catch (DocumentException | IOException e) {
            throw new IOException("Lỗi khi tạo file PDF: " + e.getMessage(), e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
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
                item.getIdSanPham(), // String
                item.getTenSanPham(), // String  
                item.getGia(), // Double (raw value for sorting)
                item.getSoLuongBan(), // Integer (raw value for sorting)
                item.getThanhTien() // Double (raw value for sorting)
            };
            tableModel.addRow(row);
        }
        tableModel.fireTableDataChanged();

        // Ensure table renderer is properly applied after data update
        updateTableRenderer();
    }

    private void updateTableRenderer() {
        // Set custom renderer for price columns to display formatted currency
        jTable1.getColumnModel().getColumn(2).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof Double aDouble) {
                    setText(currencyFormat.format(aDouble));
                } else {
                    setText(value != null ? value.toString() : "");
                }
                setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            }
        });

        jTable1.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof Double aDouble) {
                    setText(currencyFormat.format(aDouble));
                } else {
                    setText(value != null ? value.toString() : "");
                }
                setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            }
        });

        // Right-align quantity column
        jTable1.getColumnModel().getColumn(3).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                setText(value != null ? value.toString() : "");
                setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            }
        });

        // Force table to repaint to show the updated formatting
        jTable1.repaint();
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
        performSearch();
    }//GEN-LAST:event_jbtTimKiemActionPerformed

    private void jbtLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLamMoiActionPerformed
        performRefresh();
    }//GEN-LAST:event_jbtLamMoiActionPerformed

    private void jbtBaoCaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtBaoCaoActionPerformed
        exportReport();
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
