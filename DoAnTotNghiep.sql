CREATE DATABASE DoAnTotNghiep;
GO

USE DoAnTotNghiep;
GO

-- 1. Tạo bảng Hạng khách hàng
CREATE TABLE HangKhachHang (
    ID NVARCHAR(10) PRIMARY KEY,
    Ten NVARCHAR(100),
    MucGiamGia DECIMAL(5,2)
);

-- 2. Tạo bảng Khách hàng
CREATE TABLE KhachHang (
    ID NVARCHAR(10) PRIMARY KEY,
    Ten NVARCHAR(100),
    DienThoai NVARCHAR(15),
    DiaChi NVARCHAR(255),
    HangKhachHangID NVARCHAR(10),
    CONSTRAINT FKKhachHangHangKhachHang FOREIGN KEY (HangKhachHangID)
        REFERENCES HangKhachHang(ID)
);

-- 3. Tạo bảng Người dùng
CREATE TABLE NguoiDung (
    ID NVARCHAR(10) PRIMARY KEY,
    MatKhau NVARCHAR(100),
    TenDayDu NVARCHAR(100),
    NgaySinh DATE,
    GioiTinh NVARCHAR(10),
    Email NVARCHAR(100),
    ChucVu NVARCHAR(50)
);

-- 4. Tạo bảng Loại sản phẩm
CREATE TABLE LoaiSanPham (
    ID NVARCHAR(50) PRIMARY KEY,
    Ten NVARCHAR(100),
    MoTa NVARCHAR(255)
);

-- 5. Tạo bảng Sản phẩm
CREATE TABLE SanPham (
    ID NVARCHAR(10) PRIMARY KEY,
    Ten NVARCHAR(100),
    MoTa NVARCHAR(255),
    Gia DECIMAL(10,2),
    LoaiSanPhamID NVARCHAR(50),
    CONSTRAINT FKSanPhamLoaiSanPham FOREIGN KEY (LoaiSanPhamID)
        REFERENCES LoaiSanPham(ID)
);

-- 6. Tạo bảng Chi tiết khuyến mãi
CREATE TABLE ChiTietKhuyenMai (
    ID NVARCHAR(10) PRIMARY KEY,
    HinhThucGiam NVARCHAR(50),
    SoTienGiamGia DECIMAL(10,2),
    SanPhamID NVARCHAR(10),
    MucGiamGia DECIMAL(5,2),
    QuaTang NVARCHAR(100),
    CONSTRAINT FKChiTietKhuyenMaiSanPham FOREIGN KEY (SanPhamID)
        REFERENCES SanPham(ID)
);

-- 7. Tạo bảng Khuyến mãi
CREATE TABLE KhuyenMai (
    ID NVARCHAR(10) PRIMARY KEY,
    ChiTietKhuyenMaiID NVARCHAR(10),
    Ten NVARCHAR(100),
    MoTa NVARCHAR(255),
    SoLuong INT,
    ThoiGianApDung DATE,
	ThoiGianKetThuc DATE,
    CONSTRAINT FKKhuyenMaiChiTietKhuyenMai FOREIGN KEY (ChiTietKhuyenMaiID)
        REFERENCES ChiTietKhuyenMai(ID),
);

-- 8. Tạo bảng Hóa đơn
CREATE TABLE HoaDon (
    ID NVARCHAR(10) PRIMARY KEY,
    ThoiGian DATE,
    KhachHangID NVARCHAR(10),
    NguoiDungID NVARCHAR(10),
    TongTienGoc DECIMAL(12,2),
    MucGiamGia DECIMAL(10,2),
    TongTienSauGiamGia DECIMAL(12,2),
	TrangThai NVARCHAR(50),
    CONSTRAINT FKHoaDonKhachHang FOREIGN KEY (KhachHangID)
        REFERENCES KhachHang(ID),
    CONSTRAINT FKHoaDonNguoiDung FOREIGN KEY (NguoiDungID)
        REFERENCES NguoiDung(ID)
);

-- 9. Tạo bảng Chi tiết hóa đơn
CREATE TABLE ChiTietHoaDon (
    ID NVARCHAR(10) PRIMARY KEY,
    SoSanPhamThanhToan INT,
    HoaDonID NVARCHAR(10),
    SanPhamID NVARCHAR(10),
    GiaBanMoiSanPham DECIMAL(12,2),
    CONSTRAINT FKChiTietHoaDonHoaDon FOREIGN KEY (HoaDonID)
        REFERENCES HoaDon(ID),
    CONSTRAINT FKChiTietHoaDonSanPham FOREIGN KEY (SanPhamID)
        REFERENCES SanPham(ID)
);

-- 10. Tạo bảng Nguyên vật liệu
CREATE TABLE NguyenVatLieu (
    ID NVARCHAR(10) PRIMARY KEY,
    Ten NVARCHAR(100),
    DonVi NVARCHAR(50),
    SoLuongCoSan INT,
    MucCanDatThem INT
);

-- 11. Tạo bảng Nguyên vật liệu sản phẩm
CREATE TABLE NguyenVatLieuSanPham (
    ID NVARCHAR(10) PRIMARY KEY,
    SanPhamID NVARCHAR(10),
    NguyenVatLieuID NVARCHAR(10),
    SoLuongCan INT,
    CONSTRAINT FKNguyenVatLieuSanPhamSanPham FOREIGN KEY (SanPhamID)
        REFERENCES SanPham(ID),
    CONSTRAINT FKNguyenVatLieuSanPhamNguyenVatLieu FOREIGN KEY (NguyenVatLieuID)
        REFERENCES NguyenVatLieu(ID)
);

-- 12. Tạo bảng Giao dịch tại kho
CREATE TABLE GiaoDichTaiKho (
    ID NVARCHAR(10) PRIMARY KEY,
    NguyenVatLieuID NVARCHAR(10),
    ThoiGian DATETIME,
    LoaiGiaoDich NVARCHAR(50),
    SoLuongNhapXuat INT,
    GhiChu NVARCHAR(255),
    CONSTRAINT FKGiaoDichTaiKhoNguyenVatLieu FOREIGN KEY (NguyenVatLieuID)
        REFERENCES NguyenVatLieu(ID)
);

-- Thêm dữ liệu
INSERT INTO HangKhachHang (ID, Ten, MucGiamGia) VALUES
(N'HC001', N'Đồng', 1.00),
(N'HC002', N'Bạc', 3.00),
(N'HC003', N'Vàng', 5.00),
(N'HC004', N'Bạch kim', 7.00),
(N'HC005', N'Kim cương', 10.00);

INSERT INTO KhachHang (ID, Ten, DienThoai, DiaChi, HangKhachHangID) VALUES
(N'KH000', N'Khách vãng lai', NULL, NULL, NULL),
(N'KH001', N'Nguyễn Văn An', N'0987654321', N'Hà Nội', N'HC005'),
(N'KH002', N'Trần Thị Bình', N'0912345678', N'Hồ Chí Minh', N'HC001'),
(N'KH003', N'Lê Văn Cường', N'0909876543', N'Đà Nẵng', N'HC003'),
(N'KH004', N'Phạm Thị Dung', N'0965432109', N'Hải Phòng', N'HC002'),
(N'KH005', N'Đỗ Tuấn Anh', N'0943210987', N'Cần Thơ', N'HC005'),
(N'KH006', N'Hoàng Thị Lan', N'0923456789', N'Hải Dương', N'HC002'),
(N'KH007', N'Vũ Văn Quang', N'0890123456', N'Quảng Ninh', N'HC002'),
(N'KH008', N'Đặng Thị Hồng', N'0932104567', N'Ninh Bình', N'HC003'),
(N'KH009', N'Bùi Hoàng Nam', N'0976543210', N'Huế', N'HC001'),
(N'KH010', N'Ngô Thị Mai', N'0887654321', N'An Giang', N'HC004');

INSERT INTO NguoiDung (ID, MatKhau, TenDayDu, NgaySinh, GioiTinh, Email, ChucVu) VALUES
(N'ND001', N'pass123', N'Nguyễn Thanh Tùng', '1985-05-10', N'Nam', N'tungnt@example.com', N'Nhân viên'),
(N'ND002', N'abc123', N'Trần Thị Hương', '1990-08-20', N'Nữ', N'huongtt@example.com', N'Nhân viên'),
(N'ND003', N'qwerty', N'Lê Văn Minh', '1982-03-15', N'Nam', N'minhlv@example.com', N'Nhân viên'),
(N'ND004', N'123456', N'Phạm Quốc Dũng', '1979-12-01', N'Nam', N'dungpq@example.com', N'Nhân viên'),
(N'ND005', N'password', N'Đỗ Thị Yến', '1995-07-07', N'Nữ', N'yendt@example.com', N'Nhân viên'),
(N'ND006', N'nguyenvan', N'Hoàng Văn Lâm', '1988-11-11', N'Nam', N'lamhv@example.com', N'Nhân viên'),
(N'ND007', N'truong123', N'Vũ Ngọc Hà', '1992-06-30', N'Nữ', N'hava@example.com', N'Nhân viên'),
(N'ND008', N'hatuyet', N'Đặng Văn Đức', '1980-09-09', N'Nam', N'ducdv@example.com', N'Nhân viên'),
(N'ND009', N'lanhoe', N'Bùi Thị Lan', '1986-02-25', N'Nữ', N'lanbt@example.com', N'Nhân viên'),
(N'ND010', N'anhc123', N'Ngô Quốc Huy', '1994-04-18', N'Nam', N'huyqn@example.com', N'Quản lý');

INSERT INTO LoaiSanPham (ID, Ten, MoTa) VALUES
(N'001-Đồ uống', N'Đồ uống', N'Các loại nước uống giải khát'),
(N'002-Đồ ăn', N'Đồ ăn', N'Thức ăn chính'),
(N'003-Tráng miệng', N'Tráng miệng', N'Món tráng miệng ngọt'),
(N'004-Kem', N'Kem', N'Kem và kem que'),
(N'005-Đồ ăn nhanh', N'Đồ ăn nhanh', N'Đồ ăn nhanh vặt'),
(N'006-Salad', N'Salad', N'Cá các món salad rau'),
(N'007-Nước ép', N'Nước ép', N'Nước ép trái cây'),
(N'008-Bánh mì', N'Bánh mì', N'Bánh mì sandwich'),
(N'009-Mì xào', N'Mì xào', N'Các món mì xào'),
(N'010-Món chính', N'Món chính', N'Các món chính phong phú');

INSERT INTO SanPham (ID, Ten, MoTa, Gia, LoaiSanPhamID) VALUES
(N'SP001', N'Cà phê sữa đá', N'Cà phê pha phin, sữa đặc, đá', 20000, N'001-Đồ uống'),
(N'SP002', N'Phở bò', N'Phở bò đặc biệt', 40000, N'002-Đồ ăn'),
(N'SP003', N'Chè ba màu', N'Chè ba màu thập cẩm', 15000, N'003-Tráng miệng'),
(N'SP004', N'Kem vani', N'Kem vani mềm mịn', 18000, N'004-Kem'),
(N'SP005', N'Hamburger', N'Hamburger kẹp thịt bò', 30000, N'005-Đồ ăn nhanh'),
(N'SP006', N'Salad trộn', N'Salad rau củ', 25000, N'006-Salad'),
(N'SP007', N'Nước cam', N'Nước cam tươi', 20000, N'007-Nước ép'),
(N'SP008', N'Bánh mì thịt', N'Bánh mì kẹp thịt và pate', 15000, N'008-Bánh mì'),
(N'SP009', N'Mì xào hải sản', N'Mì xào hải sản đa dạng', 50000, N'009-Mì xào'),
(N'SP010', N'Gà rán', N'Gà rán giòn tan', 35000, N'010-Món chính');

INSERT INTO ChiTietKhuyenMai (ID, HinhThucGiam, SoTienGiamGia, SanPhamID, MucGiamGia, QuaTang) VALUES
(N'CTKM001', N'Phần trăm', 0.00, N'SP001', 10.00, N'Không'),
(N'CTKM002', N'Theo tiền', 6000.00, N'SP002', 0.00, N'Phiếu mua hàng'),
(N'CTKM003', N'Phần trăm', 0.00, N'SP003', 12.00, N'Không'),
(N'CTKM004', N'Theo tiền', 8000.00, N'SP004', 0.00, N'Phiếu mua hàng'),
(N'CTKM005', N'Phần trăm', 0.00, N'SP005', 14.00, N'Không'),
(N'CTKM006', N'Theo tiền', 10000.00, N'SP006', 0.00, N'Phiếu mua hàng'),
(N'CTKM007', N'Phần trăm', 0.00, N'SP007', 16.00, N'Không'),
(N'CTKM008', N'Theo tiền', 12000.00, N'SP008', 0.00, N'Phiếu mua hàng'),
(N'CTKM009', N'Phần trăm', 0.00, N'SP009', 18.00, N'Không'),
(N'CTKM010', N'Theo tiền', 14000.00, N'SP010', 0.00, N'Phiếu mua hàng');

INSERT INTO KhuyenMai (ID, ChiTietKhuyenMaiID, Ten, MoTa, SoLuong, ThoiGianApDung, ThoiGianKetThuc) VALUES
(N'KM001', N'CTKM001', N'Khuyến mãi 1', N'Mô tả khuyến mãi thứ 1', 100, '2025-01-01', '2025-12-01'),
(N'KM002', N'CTKM002', N'Khuyến mãi 2', N'Mô tả khuyến mãi thứ 2', 110, '2025-01-02', '2025-12-01'),
(N'KM003', N'CTKM003', N'Khuyến mãi 3', N'Mô tả khuyến mãi thứ 3', 120, '2025-01-03', '2025-12-01'),
(N'KM004', N'CTKM004', N'Khuyến mãi 4', N'Mô tả khuyến mãi thứ 4', 130, '2025-01-04', '2025-12-01'),
(N'KM005', N'CTKM005', N'Khuyến mãi 5', N'Mô tả khuyến mãi thứ 5', 140, '2025-01-05', '2025-12-01'),
(N'KM006', N'CTKM006', N'Khuyến mãi 6', N'Mô tả khuyến mãi thứ 6', 150, '2025-01-06', '2025-12-01'),
(N'KM007', N'CTKM007', N'Khuyến mãi 7', N'Mô tả khuyến mãi thứ 7', 160, '2025-01-07', '2025-12-01'),
(N'KM008', N'CTKM008', N'Khuyến mãi 8', N'Mô tả khuyến mãi thứ 8', 170, '2025-01-08', '2025-12-01'),
(N'KM009', N'CTKM009', N'Khuyến mãi 9', N'Mô tả khuyến mãi thứ 9', 180, '2025-01-09', '2025-12-01'),
(N'KM010', N'CTKM010', N'Khuyến mãi 10', N'Mô tả khuyến mãi thứ 10', 190, '2025-01-10', '2025-12-01');

INSERT INTO HoaDon (ID, ThoiGian, KhachHangID, NguoiDungID, TongTienGoc, MucGiamGia, TongTienSauGiamGia, TrangThai) VALUES
(N'HD001', '2025-07-01', N'KH001', N'ND002', 300000.00, 20000.00, 280000.00, N'Đã Thanh Toán'),
(N'HD002', '2025-07-02', N'KH002', N'ND003', 250000.00, 15000.00, 235000.00, N'Đã Thanh Toán'),
(N'HD003', '2025-07-03', N'KH003', N'ND004', 150000.00, 20000.00, 130000.00, N'Đã Thanh Toán'),
(N'HD004', '2025-07-04', N'KH004', N'ND005', 100000.00, 20000.00,  80000.00, N'Đã Thanh Toán'),
(N'HD005', '2025-07-05', N'KH005', N'ND006', 150000.00,  5000.00, 145000.00, N'Đã Thanh Toán'),
(N'HD006', '2025-07-06', N'KH006', N'ND007', 150000.00, 20000.00, 130000.00, N'Đã Thanh Toán'),
(N'HD007', '2025-07-07', N'KH007', N'ND008', 150000.00,  5000.00, 145000.00, N'Đã Thanh Toán'),
(N'HD008', '2025-07-08', N'KH008', N'ND009', 300000.00,  5000.00, 295000.00, N'Đã Thanh Toán'),
(N'HD009', '2025-07-09', N'KH009', N'ND010', 100000.00,  5000.00,  95000.00, N'Chưa Thanh Toán'),
(N'HD010', '2025-07-10', N'KH010', N'ND001', 250000.00,     0.00, 250000.00, N'Đã Hủy');

INSERT INTO ChiTietHoaDon (ID, SoSanPhamThanhToan, HoaDonID, SanPhamID, GiaBanMoiSanPham) VALUES
(N'CT001', 2, N'HD001', N'SP001', 20000.00),
(N'CT002', 2, N'HD002', N'SP002', 40000.00),
(N'CT003', 5, N'HD003', N'SP003', 15000.00),
(N'CT004', 4, N'HD004', N'SP004', 18000.00),
(N'CT005', 3, N'HD005', N'SP005', 30000.00),
(N'CT006', 3, N'HD006', N'SP006', 25000.00),
(N'CT007', 5, N'HD007', N'SP007', 20000.00),
(N'CT008', 2, N'HD008', N'SP008', 15000.00),
(N'CT009', 1, N'HD009', N'SP009', 50000.00),
(N'CT010', 4, N'HD010', N'SP010', 35000.00);

INSERT INTO NguyenVatLieu (ID, Ten, DonVi, SoLuongCoSan, MucCanDatThem) VALUES
(N'NVL001', N'Đường', N'kg', 100, 20),
(N'NVL002', N'Muối', N'kg', 200, 10),
(N'NVL003', N'Bột mì', N'kg', 150, 30),
(N'NVL004', N'Gạo', N'kg', 300, 50),
(N'NVL005', N'Thịt bò', N'kg',  50, 10),
(N'NVL006', N'Thịt gà', N'kg',  80, 20),
(N'NVL007', N'Rau cải', N'kg', 120, 15),
(N'NVL008', N'Trà', N'gói', 500, 100),
(N'NVL009', N'Cà phê', N'gói', 400, 50),
(N'NVL010', N'Sữa', N'lít', 200, 30);

INSERT INTO NguyenVatLieuSanPham (ID, SanPhamID, NguyenVatLieuID, SoLuongCan) VALUES
(N'NVS001', N'SP001', N'NVL001', 44),
(N'NVS002', N'SP002', N'NVL002', 48),
(N'NVS003', N'SP003', N'NVL003', 39),
(N'NVS004', N'SP004', N'NVL004', 43),
(N'NVS005', N'SP005', N'NVL005', 31),
(N'NVS006', N'SP006', N'NVL006', 78),
(N'NVS007', N'SP007', N'NVL007', 44),
(N'NVS008', N'SP008', N'NVL008', 90),
(N'NVS009', N'SP009', N'NVL009', 11),
(N'NVS010', N'SP010', N'NVL010', 67);

INSERT INTO GiaoDichTaiKho (ID, NguyenVatLieuID, ThoiGian, LoaiGiaoDich, SoLuongNhapXuat, GhiChu) VALUES
(N'GD001', N'NVL001', '2025-06-01 09:00:00', N'Nhập', 10, N'Nhập hàng'),
(N'GD002', N'NVL002', '2025-06-02 09:00:00', N'Xuất', 15, N'Nhập hàng'),
(N'GD003', N'NVL003', '2025-06-03 09:00:00', N'Nhập', 30, N'Nhập hàng'),
(N'GD004', N'NVL004', '2025-06-04 09:00:00', N'Xuất', 25, N'Nhập hàng'),
(N'GD005', N'NVL005', '2025-06-05 09:00:00', N'Nhập', 50, N'Nhập hàng'),
(N'GD006', N'NVL006', '2025-06-06 09:00:00', N'Xuất', 35, N'Nhập hàng'),
(N'GD007', N'NVL007', '2025-06-07 09:00:00', N'Nhập', 70, N'Nhập hàng'),
(N'GD008', N'NVL008', '2025-06-08 09:00:00', N'Xuất', 45, N'Nhập hàng'),
(N'GD009', N'NVL009', '2025-06-09 09:00:00', N'Nhập', 90, N'Nhập hàng'),
(N'GD010', N'NVL010', '2025-06-10 09:00:00', N'Xuất', 55, N'Nhập hàng');

--Muốn xóa database thì chạy khối này
USE master;
GO

ALTER DATABASE DoAnTotNghiep
SET SINGLE_USER
WITH ROLLBACK IMMEDIATE;
GO

DROP DATABASE DoAnTotNghiep;
GO

