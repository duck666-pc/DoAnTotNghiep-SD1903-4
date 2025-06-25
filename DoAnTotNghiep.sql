create database DoAnTotNghiep;
use DoAnTotNghiep;

-- 1. Tạo bảng Hạng khách hàng
CREATE TABLE [Hạng khách hàng] (
    [id hạng khách hàng] NVARCHAR(10) PRIMARY KEY,
    [Tên] NVARCHAR(100),
    [Mức giảm giá] DECIMAL(5,2)
);

-- 2. Tạo bảng Khách hàng
CREATE TABLE [Khách hàng] (
    [id khách hàng] NVARCHAR(10) PRIMARY KEY,
    [Tên] NVARCHAR(100),
    [Điện thoại] NVARCHAR(15),
    [Địa chỉ] NVARCHAR(255),
    [id hạng khách hàng] NVARCHAR(10),
    CONSTRAINT FK_KhachHang_HangKhachHang FOREIGN KEY ([id hạng khách hàng])
        REFERENCES [Hạng khách hàng]([id hạng khách hàng])
);

-- 3. Tạo bảng Người dùng
CREATE TABLE [Người dùng] (
    [id người dùng] NVARCHAR(10) PRIMARY KEY,
    [Mật khẩu] NVARCHAR(100),
    [Tên đầy đủ] NVARCHAR(100),
    [Ngày sinh] DATE,
    [Giới tính] NVARCHAR(10),
    [Email] NVARCHAR(100),
    [Chức vụ] NVARCHAR(50)
);

-- 4. Tạo bảng Loại sản phẩm
CREATE TABLE [Loại sản phẩm] (
    [id loại sản phẩm] NVARCHAR(50) PRIMARY KEY,
    [Tên] NVARCHAR(100),
    [Mô tả] NVARCHAR(255)
);

-- 5. Tạo bảng Sản phẩm
CREATE TABLE [Sản phẩm] (
    [id sản phẩm] NVARCHAR(10) PRIMARY KEY,
    [Tên] NVARCHAR(100),
    [Mô tả] NVARCHAR(255),
    [Giá] DECIMAL(10,2),
    [id loại sản phẩm] NVARCHAR(50),
    CONSTRAINT FK_SanPham_LoaiSanPham FOREIGN KEY ([id loại sản phẩm])
        REFERENCES [Loại sản phẩm]([id loại sản phẩm])
);

-- 6. Tạo bảng Chi tiết khuyến mãi
CREATE TABLE [Chi tiết khuyến mãi] (
    [id chi tiết khuyến mãi] NVARCHAR(10) PRIMARY KEY,
    [Hình thức giảm] NVARCHAR(50),
    [Số tiền giảm giá] DECIMAL(10,2),
    [id sản phẩm ảnh hưởng] NVARCHAR(10),
    [Mức giảm giá] DECIMAL(5,2),
    [Quà tặng] NVARCHAR(100),
    CONSTRAINT FK_CTKhuyenMai_SanPham FOREIGN KEY ([id sản phẩm ảnh hưởng])
        REFERENCES [Sản phẩm]([id sản phẩm])
);

-- 7. Tạo bảng Khuyến mãi
CREATE TABLE [Khuyến mãi] (
    [id khuyến mãi] NVARCHAR(10) PRIMARY KEY,
    [id chi tiết khuyến mãi] NVARCHAR(10),
    [id khách hàng ảnh hưởng] NVARCHAR(10),
    [Tên] NVARCHAR(100),
    [Mô tả] NVARCHAR(255),
    [Số lượng] INT,
    [Thời gian áp dụng] DATETIME,
    CONSTRAINT FK_KhuyenMai_CTKhuyenMai FOREIGN KEY ([id chi tiết khuyến mãi])
        REFERENCES [Chi tiết khuyến mãi]([id chi tiết khuyến mãi]),
    CONSTRAINT FK_KhuyenMai_KhachHang FOREIGN KEY ([id khách hàng ảnh hưởng])
        REFERENCES [Khách hàng]([id khách hàng])
);

-- 8. Tạo bảng Hóa đơn
CREATE TABLE [Hóa đơn] (
    [id hóa đơn] NVARCHAR(10) PRIMARY KEY,
    [Thời gian] DATETIME,
    [id khách hàng] NVARCHAR(10),
    [id người dùng] NVARCHAR(10),
    [Tổng tiền gốc] DECIMAL(12,2),
    [Mức giảm giá] DECIMAL(10,2),
    [Tổng tiền sau giảm giá] DECIMAL(12,2),
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY ([id khách hàng])
        REFERENCES [Khách hàng]([id khách hàng]),
    CONSTRAINT FK_HoaDon_NguoiDung FOREIGN KEY ([id người dùng])
        REFERENCES [Người dùng]([id người dùng])
);

-- 9. Tạo bảng Chi tiết hóa đơn
CREATE TABLE [Chi tiết hóa đơn] (
    [id chi tiết hóa đơn] NVARCHAR(10) PRIMARY KEY,
    [Số sản phẩm thanh toán] INT,
    [id hóa đơn] NVARCHAR(10),
    [id sản phẩm] NVARCHAR(10),
    [Giá bán mỗi sản phẩm] DECIMAL(12,2),
    CONSTRAINT FK_CTHoaDon_HoaDon FOREIGN KEY ([id hóa đơn])
        REFERENCES [Hóa đơn]([id hóa đơn]),
    CONSTRAINT FK_CTHoaDon_SanPham FOREIGN KEY ([id sản phẩm])
        REFERENCES [Sản phẩm]([id sản phẩm])
);

-- 10. Tạo bảng Nguyên vật liệu
CREATE TABLE [Nguyên vật liệu] (
    [id nguyên vật liệu] NVARCHAR(10) PRIMARY KEY,
    [Tên] NVARCHAR(100),
    [Đơn vị] NVARCHAR(50),
    [Số lượng có sẵn] INT,
    [Mức cần đặt thêm] INT
);

-- 11. Tạo bảng Nguyên vật liệu sản phẩm
CREATE TABLE [Nguyên vật liệu sản phẩm] (
    [id nguyên vật liệu sản phẩm] NVARCHAR(10) PRIMARY KEY,
    [id sản phẩm] NVARCHAR(10),
    [id nguyên vật liệu] NVARCHAR(10),
    [Số lượng cần] INT,
    CONSTRAINT FK_NVLSanPham_SanPham FOREIGN KEY ([id sản phẩm])
        REFERENCES [Sản phẩm]([id sản phẩm]),
    CONSTRAINT FK_NVLSanPham_NVL FOREIGN KEY ([id nguyên vật liệu])
        REFERENCES [Nguyên vật liệu]([id nguyên vật liệu])
);

-- 12. Tạo bảng Giao dịch tại kho
CREATE TABLE [Giao dịch tại kho] (
    [id giao dịch] NVARCHAR(10) PRIMARY KEY,
    [id nguyên vật liệu] NVARCHAR(10),
    [Thời gian] DATETIME,
    [Loại giao dịch] NVARCHAR(50),
    [Số lượng nhập/xuất] INT,
    [Ghi chú] NVARCHAR(255),
    CONSTRAINT FK_GiaoDich_NVL FOREIGN KEY ([id nguyên vật liệu])
        REFERENCES [Nguyên vật liệu]([id nguyên vật liệu])
);

-- Thêm dữ liệu cho bảng Hạng khách hàng
INSERT INTO [Hạng khách hàng] ([id hạng khách hàng], [Tên], [Mức giảm giá]) VALUES
(N'HC001', N'Đồng', 1.00),
(N'HC002', N'Bạc', 3.00),
(N'HC003', N'Vàng', 5.00),
(N'HC004', N'Bạch kim', 7.00),
(N'HC005', N'Kim cương', 10.00);

-- Thêm dữ liệu cho bảng Khách hàng
INSERT INTO [Khách hàng] ([id khách hàng], [Tên], [Điện thoại], [Địa chỉ], [id hạng khách hàng]) VALUES
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

-- Thêm dữ liệu cho bảng Người dùng
INSERT INTO [Người dùng] ([id người dùng], [Mật khẩu], [Tên đầy đủ], [Ngày sinh], [Giới tính], [Email], [Chức vụ]) VALUES
(N'ND001', N'pass123', N'Nguyễn Thanh Tùng', '1985-05-10', N'Nam', N'tungnt@example.com', N'Nhân viên'),
(N'ND002', N'abc123', N'Trần Thị Hương', '1990-08-20', N'Nữ', N'huongtt@example.com', N'Quản lý'),
(N'ND003', N'qwerty', N'Lê Văn Minh', '1982-03-15', N'Nam', N'minhlv@example.com', N'Quản lý'),
(N'ND004', N'123456', N'Phạm Quốc Dũng', '1979-12-01', N'Nam', N'dungpq@example.com', N'Nhân viên'),
(N'ND005', N'password', N'Đỗ Thị Yến', '1995-07-07', N'Nữ', N'yendt@example.com', N'Nhân viên'),
(N'ND006', N'nguyenvan', N'Hoàng Văn Lâm', '1988-11-11', N'Nam', N'lamhv@example.com', N'Nhân viên'),
(N'ND007', N'truong123', N'Vũ Ngọc Hà', '1992-06-30', N'Nữ', N'hava@example.com', N'Quản lý'),
(N'ND008', N'hatuyet', N'Đặng Văn Đức', '1980-09-09', N'Nam', N'ducdv@example.com', N'Nhân viên'),
(N'ND009', N'lanhoe', N'Bùi Thị Lan', '1986-02-25', N'Nữ', N'lanbt@example.com', N'Nhân viên'),
(N'ND010', N'anhc123', N'Ngô Quốc Huy', '1994-04-18', N'Nam', N'huyqn@example.com', N'Quản lý');

-- Thêm dữ liệu cho bảng Loại sản phẩm
INSERT INTO [Loại sản phẩm] ([id loại sản phẩm], [Tên], [Mô tả]) VALUES
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

-- Thêm dữ liệu cho bảng Sản phẩm
INSERT INTO [Sản phẩm] ([id sản phẩm], [Tên], [Mô tả], [Giá], [id loại sản phẩm]) VALUES
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

-- Thêm dữ liệu cho bảng Chi tiết khuyến mãi
INSERT INTO [Chi tiết khuyến mãi] ([id chi tiết khuyến mãi], [Hình thức giảm], [Số tiền giảm giá], [id sản phẩm ảnh hưởng], [Mức giảm giá], [Quà tặng]) VALUES
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

-- Thêm dữ liệu cho bảng Khuyến mãi
INSERT INTO [Khuyến mãi] ([id khuyến mãi], [id chi tiết khuyến mãi], [id khách hàng ảnh hưởng], [Tên], [Mô tả], [Số lượng], [Thời gian áp dụng]) VALUES
(N'KM001', N'CTKM001', N'KH001', N'Khuyến mãi 1', N'Mô tả khuyến mãi thứ 1', 100, '2025-01-01 00:00:00'),
(N'KM002', N'CTKM002', N'KH002', N'Khuyến mãi 2', N'Mô tả khuyến mãi thứ 2', 110, '2025-02-01 00:00:00'),
(N'KM003', N'CTKM003', N'KH003', N'Khuyến mãi 3', N'Mô tả khuyến mãi thứ 3', 120, '2025-03-01 00:00:00'),
(N'KM004', N'CTKM004', N'KH004', N'Khuyến mãi 4', N'Mô tả khuyến mãi thứ 4', 130, '2025-04-01 00:00:00'),
(N'KM005', N'CTKM005', N'KH005', N'Khuyến mãi 5', N'Mô tả khuyến mãi thứ 5', 140, '2025-05-01 00:00:00'),
(N'KM006', N'CTKM006', N'KH006', N'Khuyến mãi 6', N'Mô tả khuyến mãi thứ 6', 150, '2025-06-01 00:00:00'),
(N'KM007', N'CTKM007', N'KH007', N'Khuyến mãi 7', N'Mô tả khuyến mãi thứ 7', 160, '2025-07-01 00:00:00'),
(N'KM008', N'CTKM008', N'KH008', N'Khuyến mãi 8', N'Mô tả khuyến mãi thứ 8', 170, '2025-08-01 00:00:00'),
(N'KM009', N'CTKM009', N'KH009', N'Khuyến mãi 9', N'Mô tả khuyến mãi thứ 9', 180, '2025-09-01 00:00:00'),
(N'KM010', N'CTKM010', N'KH010', N'Khuyến mãi 10', N'Mô tả khuyến mãi thứ 10', 190, '2025-10-01 00:00:00');

-- Thêm dữ liệu cho bảng Hóa đơn
INSERT INTO [Hóa đơn] ([id hóa đơn], [Thời gian], [id khách hàng], [id người dùng], [Tổng tiền gốc], [Mức giảm giá], [Tổng tiền sau giảm giá]) VALUES
(N'HD001', '2025-07-01 10:00:00', N'KH001', N'ND002', 300000.00, 20000.00, 280000.00),
(N'HD002', '2025-07-02 10:00:00', N'KH002', N'ND003', 250000.00, 15000.00, 235000.00),
(N'HD003', '2025-07-03 10:00:00', N'KH003', N'ND004', 150000.00, 20000.00, 130000.00),
(N'HD004', '2025-07-04 10:00:00', N'KH004', N'ND005', 100000.00, 20000.00, 80000.00),
(N'HD005', '2025-07-05 10:00:00', N'KH005', N'ND006', 150000.00, 5000.00, 145000.00),
(N'HD006', '2025-07-06 10:00:00', N'KH006', N'ND007', 150000.00, 20000.00, 130000.00),
(N'HD007', '2025-07-07 10:00:00', N'KH007', N'ND008', 150000.00, 5000.00, 145000.00),
(N'HD008', '2025-07-08 10:00:00', N'KH008', N'ND009', 300000.00, 5000.00, 295000.00),
(N'HD009', '2025-07-09 10:00:00', N'KH009', N'ND010', 100000.00, 5000.00, 95000.00),
(N'HD010', '2025-07-10 10:00:00', N'KH010', N'ND001', 250000.00, 0.00, 250000.00);

-- Thêm dữ liệu cho bảng Chi tiết hóa đơn
INSERT INTO [Chi tiết hóa đơn] ([id chi tiết hóa đơn], [Số sản phẩm thanh toán], [id hóa đơn], [id sản phẩm], [Giá bán mỗi sản phẩm]) VALUES
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

-- Thêm dữ liệu cho bảng Nguyên vật liệu
INSERT INTO [Nguyên vật liệu] ([id nguyên vật liệu], [Tên], [Đơn vị], [Số lượng có sẵn], [Mức cần đặt thêm]) VALUES
(N'NVL001', N'Đường', N'kg', 100, 20),
(N'NVL002', N'Muối', N'kg', 200, 10),
(N'NVL003', N'Bột mì', N'kg', 150, 30),
(N'NVL004', N'Gạo', N'kg', 300, 50),
(N'NVL005', N'Thịt bò', N'kg', 50, 10),
(N'NVL006', N'Thịt gà', N'kg', 80, 20),
(N'NVL007', N'Rau cải', N'kg', 120, 15),
(N'NVL008', N'Trà', N'gói', 500, 100),
(N'NVL009', N'Cà phê', N'gói', 400, 50),
(N'NVL010', N'Sữa', N'lít', 200, 30);

-- Thêm dữ liệu cho bảng Nguyên vật liệu sản phẩm
INSERT INTO [Nguyên vật liệu sản phẩm] ([id nguyên vật liệu sản phẩm], [id sản phẩm], [id nguyên vật liệu], [Số lượng cần]) VALUES
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

-- Thêm dữ liệu cho bảng Giao dịch tại kho
INSERT INTO [Giao dịch tại kho] ([id giao dịch], [id nguyên vật liệu], [Thời gian], [Loại giao dịch], [Số lượng nhập/xuất], [Ghi chú]) VALUES
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

drop database DoAnTotNghiep;
