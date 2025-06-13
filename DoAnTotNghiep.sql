USE [master]
GO

CREATE DATABASE DoAnTotNghiep
GO

USE DoAnTotNghiep
GO

-- Bảng Hạng Khách Hàng (mức giảm giá cho từng hạng khách hàng)
CREATE TABLE [dbo].[HangKhachHang](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [Ten] [nvarchar](50) NULL,
    [MucGiamGia] [decimal](5, 2) NULL
)

-- Bảng Khách Hàng
CREATE TABLE [dbo].[KhachHang](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [Ten] [nvarchar](100) NULL,
    [DienThoai] [varchar](20) NULL,
    [DiaChi] [nvarchar](100) NULL,
    [IdHangKhachHang] [uniqueidentifier] NULL,
    FOREIGN KEY ([IdHangKhachHang]) REFERENCES [dbo].[HangKhachHang]([Id])
)

-- Bảng Chức Vụ
CREATE TABLE [dbo].[ChucVu](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [Ten] [nvarchar](50) NULL,
    [MoTa] [nvarchar](100) NULL
)

-- Bảng Người Dùng
CREATE TABLE [dbo].[NguoiDung](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [MatKhau] [varchar](max) NULL,
    [TenDayDu] [nvarchar](100) NULL,
    [NgaySinh] [date] NULL,
    [GioiTinh] [int] NULL,
    [Email] [varchar](100) NULL
)

-- Bảng Chức Vụ Người Dùng (liên kết Người Dùng – Chức Vụ)
CREATE TABLE [dbo].[ChucVuNguoiDung](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [IdNguoiDung] [uniqueidentifier] NULL,
    [IdChucVu] [uniqueidentifier] NULL,
    FOREIGN KEY ([IdNguoiDung]) REFERENCES [dbo].[NguoiDung]([Id]),
    FOREIGN KEY ([IdChucVu]) REFERENCES [dbo].[ChucVu]([Id])
)

-- Bảng Loại Sản Phẩm
CREATE TABLE [dbo].[LoaiSanPham](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [Ten] [nvarchar](50) NULL,
    [MoTa] [nvarchar](100) NULL
)

-- Bảng Sản Phẩm
CREATE TABLE [dbo].[SanPham](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [Ten] [nvarchar](100) NULL,
    [MoTa] [nvarchar](100) NULL,
    [Gia] [decimal](18, 2) NULL,
    [IdLoaiSanPham] [uniqueidentifier] NULL,
    FOREIGN KEY ([IdLoaiSanPham]) REFERENCES [dbo].[LoaiSanPham]([Id])
)

-- Bảng Đơn Vị tính
CREATE TABLE [dbo].[DonVi](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [Ten] [nvarchar](50) NULL
)

-- Bảng Nguyên Vật Liệu
CREATE TABLE [dbo].[NguyenVatLieu](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [Ten] [nvarchar](100) NULL,
    [IdDonVi] [uniqueidentifier] NULL,
    [SoLuong] [int] NULL,
    [MucCanDatThemHang] [int] NULL,
    FOREIGN KEY ([IdDonVi]) REFERENCES [dbo].[DonVi]([Id])
)

-- Bảng Nguyên Vật Liệu Sản Phẩm (số lượng nguyên liệu cần cho mỗi sản phẩm)
CREATE TABLE [dbo].[NguyenVatLieuSanPham](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [IdSanPham] [uniqueidentifier] NULL,
    [IdNguyenVatLieu] [uniqueidentifier] NULL,
    [SoLuongCan] [int] NULL,
    FOREIGN KEY ([IdSanPham]) REFERENCES [dbo].[SanPham]([Id]),
    FOREIGN KEY ([IdNguyenVatLieu]) REFERENCES [dbo].[NguyenVatLieu]([Id])
)

-- Bảng Giao Dịch tại Kho (nhập xuất nguyên vật liệu)
CREATE TABLE [dbo].[GiaoDichKho](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [IdNguyenVatLieu] [uniqueidentifier] NULL,
    [ThoiGian] [datetime] NULL,
    [Loai] [nvarchar](20) NULL,
    [SoLuong] [int] NULL,
    [GhiChu] [nvarchar](100) NULL,
    FOREIGN KEY ([IdNguyenVatLieu]) REFERENCES [dbo].[NguyenVatLieu]([Id])
)

-- Bảng Hóa Đơn bán hàng
CREATE TABLE [dbo].[HoaDon](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [ThoiGian] [datetime] NULL,
    [IdKhachHang] [uniqueidentifier] NULL,
    [IdNguoiDung] [uniqueidentifier] NULL,
    [TongTienGoc] [decimal](18, 2) NULL,
    [MucGiamGia] [decimal](5, 2) NULL,
    [TongTienSauGiamGia] [decimal](18, 2) NULL,
    FOREIGN KEY ([IdKhachHang]) REFERENCES [dbo].[KhachHang]([Id]),
    FOREIGN KEY ([IdNguoiDung]) REFERENCES [dbo].[NguoiDung]([Id])
)

-- Bảng Chi Tiết Hóa Đơn (sản phẩm trong đơn hàng)
CREATE TABLE [dbo].[ChiTietHoaDon](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [SoLuong] [int] NULL,
    [IdHoaDon] [uniqueidentifier] NULL,
    [IdSanPham] [uniqueidentifier] NULL,
    [DonGia] [decimal](18, 2) NULL,
    FOREIGN KEY ([IdHoaDon]) REFERENCES [dbo].[HoaDon]([Id]),
    FOREIGN KEY ([IdSanPham]) REFERENCES [dbo].[SanPham]([Id])
)

-- Bảng Chi Tiết Khuyến Mãi
CREATE TABLE [dbo].[ChiTietKhuyenMai](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [Ten] [nvarchar](100) NULL,
    [SoTienGiamGia] [decimal](18, 2) NULL,
    [IdSanPhamAnhHuong] [uniqueidentifier] NULL,
    [MucGiamGia] [decimal](5, 2) NULL,
    [MoTa] [nvarchar](200) NULL,
    FOREIGN KEY ([IdSanPhamAnhHuong]) REFERENCES [dbo].[SanPham]([Id])
)

-- Bảng Khuyến Mãi (chương trình khuyến mãi áp dụng cho khách hàng)
CREATE TABLE [dbo].[KhuyenMai](
    [Id] [uniqueidentifier] PRIMARY KEY,
    [IdChiTietKhuyenMai] [uniqueidentifier] NULL,
    [IdKhachHang] [uniqueidentifier] NULL,
    [SoLuong] [int] NULL,
    [ThoiGianApDung] [datetime] NULL,
    FOREIGN KEY ([IdChiTietKhuyenMai]) REFERENCES [dbo].[ChiTietKhuyenMai]([Id]),
    FOREIGN KEY ([IdKhachHang]) REFERENCES [dbo].[KhachHang]([Id])
)
GO

-- Chèn dữ liệu mẫu vào bảng Hạng Khách Hàng
INSERT [dbo].[HangKhachHang] ([Id], [Ten], [MucGiamGia]) VALUES (N'419c647b-1e32-4806-a524-174c0fbaa0ae', N'Bạc', 5)
INSERT [dbo].[HangKhachHang] ([Id], [Ten], [MucGiamGia]) VALUES (N'd04d8f61-6c40-46b5-97d3-36b1e92a5610', N'Vàng', 10)

-- Chèn dữ liệu mẫu vào bảng Khách Hàng
INSERT [dbo].[KhachHang] ([Id], [Ten], [DienThoai], [DiaChi], [IdHangKhachHang]) 
    VALUES (N'd5d7e298-1c4a-49d0-8cbe-c3ac1013b5ce', N'Nguyen Van A', N'0912345678', N'Hanoi', N'419c647b-1e32-4806-a524-174c0fbaa0ae')
INSERT [dbo].[KhachHang] ([Id], [Ten], [DienThoai], [DiaChi], [IdHangKhachHang]) 
    VALUES (N'a4ee7d90-00ba-4e52-8e89-db617f762371', N'Tran Thi B', N'0987654321', N'Saigon', N'419c647b-1e32-4806-a524-174c0fbaa0ae')
INSERT [dbo].[KhachHang] ([Id], [Ten], [DienThoai], [DiaChi], [IdHangKhachHang]) 
    VALUES (N'1811f707-0368-427e-937b-8b8c8e2859bc', N'Le Van C', N'0911223344', N'Da Nang', N'd04d8f61-6c40-46b5-97d3-36b1e92a5610')

-- Chèn dữ liệu mẫu vào bảng Chức Vụ
INSERT [dbo].[ChucVu] ([Id], [Ten], [MoTa]) VALUES (N'494924fc-9abc-4166-8995-f9586ba0325b', N'Quản lý', N'Chức vụ quản lý')
INSERT [dbo].[ChucVu] ([Id], [Ten], [MoTa]) VALUES (N'97823c79-37ca-414b-afaa-502deb97ce73', N'Nhân viên', N'Chức vụ nhân viên')

-- Chèn dữ liệu mẫu vào bảng Người Dùng
INSERT [dbo].[NguoiDung] ([Id], [MatKhau], [TenDayDu], [NgaySinh], [GioiTinh], [Email]) 
    VALUES (N'7227f955-8d30-44ca-a5d9-1ca39f9fbae9', N'123456', N'Nguyen Van Admin', CAST(N'1990-01-01' AS Date), 1, N'admin@example.com')
INSERT [dbo].[NguoiDung] ([Id], [MatKhau], [TenDayDu], [NgaySinh], [GioiTinh], [Email]) 
    VALUES (N'9b113b68-4701-4443-b6dc-d102eceae39b', N'abcdef', N'Le Thi User', CAST(N'1992-05-10' AS Date), 0, N'user@example.com')

-- Chèn dữ liệu mẫu vào bảng Chức Vụ Người Dùng (liên kết)
INSERT [dbo].[ChucVuNguoiDung] ([Id], [IdNguoiDung], [IdChucVu]) 
    VALUES (N'0b90c402-bc66-4558-ac3a-56c6c6b9749d', N'7227f955-8d30-44ca-a5d9-1ca39f9fbae9', N'494924fc-9abc-4166-8995-f9586ba0325b')
INSERT [dbo].[ChucVuNguoiDung] ([Id], [IdNguoiDung], [IdChucVu]) 
    VALUES (N'a812c1f3-0085-4329-b2ac-0d47313541bc', N'9b113b68-4701-4443-b6dc-d102eceae39b', N'97823c79-37ca-414b-afaa-502deb97ce73')

-- Chèn dữ liệu mẫu vào bảng Loại Sản Phẩm
INSERT [dbo].[LoaiSanPham] ([Id], [Ten], [MoTa]) VALUES (N'70df787d-7d44-4978-9d3a-fedaa3bf4929', N'Đồ ăn', N'Các loại đồ ăn')
INSERT [dbo].[LoaiSanPham] ([Id], [Ten], [MoTa]) VALUES (N'5362c129-e92e-4395-96b3-c8fcd21c5eab', N'Đồ uống', N'Các loại đồ uống')

-- Chèn dữ liệu mẫu vào bảng Sản Phẩm
INSERT [dbo].[SanPham] ([Id], [Ten], [MoTa], [Gia], [IdLoaiSanPham]) 
    VALUES (N'f4435170-763e-420c-9a6f-8bcb544a2d26', N'Bánh mì', N'Banh mi thit', 10000, N'70df787d-7d44-4978-9d3a-fedaa3bf4929')
INSERT [dbo].[SanPham] ([Id], [Ten], [MoTa], [Gia], [IdLoaiSanPham]) 
    VALUES (N'abe42af8-a972-455f-ba2b-68c407a2abdd', N'Phở', N'Pho bo', 30000, N'70df787d-7d44-4978-9d3a-fedaa3bf4929')
INSERT [dbo].[SanPham] ([Id], [Ten], [MoTa], [Gia], [IdLoaiSanPham]) 
    VALUES (N'9886fc35-bf06-4232-a65e-5870af52b868', N'Coca Cola', N'Nuoc ngot', 12000, N'5362c129-e92e-4395-96b3-c8fcd21c5eab')

-- Chèn dữ liệu mẫu vào bảng Đơn Vị tính
INSERT [dbo].[DonVi] ([Id], [Ten]) VALUES (N'419a85b2-802a-4ad1-9f8a-54c04fef3f3e', N'Cái')
INSERT [dbo].[DonVi] ([Id], [Ten]) VALUES (N'077fad21-88ec-4d3c-a8ea-75cf76a0b1b2', N'Lít')

-- Chèn dữ liệu mẫu vào bảng Nguyên Vật Liệu
INSERT [dbo].[NguyenVatLieu] ([Id], [Ten], [IdDonVi], [SoLuong], [MucCanDatThemHang]) 
    VALUES (N'76f7773d-f430-4836-831c-163e2d0e4c88', N'Bột mì', N'419a85b2-802a-4ad1-9f8a-54c04fef3f3e', 100, 20)
INSERT [dbo].[NguyenVatLieu] ([Id], [Ten], [IdDonVi], [SoLuong], [MucCanDatThemHang]) 
    VALUES (N'969b512a-30fb-4287-a591-7f643407757b', N'Trứng', N'419a85b2-802a-4ad1-9f8a-54c04fef3f3e', 50, 10)
INSERT [dbo].[NguyenVatLieu] ([Id], [Ten], [IdDonVi], [SoLuong], [MucCanDatThemHang]) 
    VALUES (N'c3c78fe5-7e8f-42a7-bb5f-3835d7c8fb34', N'Đường', N'077fad21-88ec-4d3c-a8ea-75cf76a0b1b2', 200, 50)

-- Chèn dữ liệu mẫu vào bảng Nguyên Vật Liệu Sản Phẩm
INSERT [dbo].[NguyenVatLieuSanPham] ([Id], [IdSanPham], [IdNguyenVatLieu], [SoLuongCan]) 
    VALUES (N'76f7773d-f430-4836-831c-163e2d0e4c88', N'f4435170-763e-420c-9a6f-8bcb544a2d26', N'76f7773d-f430-4836-831c-163e2d0e4c88', 2)
INSERT [dbo].[NguyenVatLieuSanPham] ([Id], [IdSanPham], [IdNguyenVatLieu], [SoLuongCan]) 
    VALUES (N'969b512a-30fb-4287-a591-7f643407757b', N'abe42af8-a972-455f-ba2b-68c407a2abdd', N'969b512a-30fb-4287-a591-7f643407757b', 3)
INSERT [dbo].[NguyenVatLieuSanPham] ([Id], [IdSanPham], [IdNguyenVatLieu], [SoLuongCan]) 
    VALUES (N'c3c78fe5-7e8f-42a7-bb5f-3835d7c8fb34', N'9886fc35-bf06-4232-a65e-5870af52b868', N'c3c78fe5-7e8f-42a7-bb5f-3835d7c8fb34', 1)

-- Chèn dữ liệu mẫu vào bảng Giao Dịch tại Kho
INSERT [dbo].[GiaoDichKho] ([Id], [IdNguyenVatLieu], [ThoiGian], [Loai], [SoLuong], [GhiChu]) 
    VALUES (N'40aa20f3-dfd2-4eae-949d-d0b8a7f54478', N'76f7773d-f430-4836-831c-163e2d0e4c88', CAST(N'2023-01-01 08:00:00' AS DateTime), N'Nhập', 50, N'Nhập hàng về')
INSERT [dbo].[GiaoDichKho] ([Id], [IdNguyenVatLieu], [ThoiGian], [Loai], [SoLuong], [GhiChu]) 
    VALUES (N'e960c43c-d83a-4b11-b65b-c6464f141bd5', N'969b512a-30fb-4287-a591-7f643407757b', CAST(N'2023-02-15 09:30:00' AS DateTime), N'Xuất', 20, N'Bán ra')
INSERT [dbo].[GiaoDichKho] ([Id], [IdNguyenVatLieu], [ThoiGian], [Loai], [SoLuong], [GhiChu]) 
    VALUES (N'd0aa3c02-26c6-40ac-9f6d-67034317524f', N'c3c78fe5-7e8f-42a7-bb5f-3835d7c8fb34', CAST(N'2023-03-10 11:15:00' AS DateTime), N'Nhập', 100, N'Nhập đường')

-- Chèn dữ liệu mẫu vào bảng Hóa Đơn
INSERT [dbo].[HoaDon] ([Id], [ThoiGian], [IdKhachHang], [IdNguoiDung], [TongTienGoc], [MucGiamGia], [TongTienSauGiamGia]) 
    VALUES (N'70b2c441-c2c4-4d6a-9504-50570a83ee18', CAST(N'2023-06-01 10:00:00' AS DateTime), N'd5d7e298-1c4a-49d0-8cbe-c3ac1013b5ce', N'7227f955-8d30-44ca-a5d9-1ca39f9fbae9', 50000, 10, 45000)
INSERT [dbo].[HoaDon] ([Id], [ThoiGian], [IdKhachHang], [IdNguoiDung], [TongTienGoc], [MucGiamGia], [TongTienSauGiamGia]) 
    VALUES (N'c7892020-a32e-494d-8713-69e00d4ff55b', CAST(N'2023-06-02 11:30:00' AS DateTime), N'a4ee7d90-00ba-4e52-8e89-db617f762371', N'9b113b68-4701-4443-b6dc-d102eceae39b', 30000, 0, 30000)

-- Chèn dữ liệu mẫu vào bảng Chi Tiết Hóa Đơn
INSERT [dbo].[ChiTietHoaDon] ([Id], [SoLuong], [IdHoaDon], [IdSanPham], [DonGia]) 
    VALUES (N'a778df3b-3769-44ff-975f-4017e16a5791', 2, N'70b2c441-c2c4-4d6a-9504-50570a83ee18', N'f4435170-763e-420c-9a6f-8bcb544a2d26', 10000)
INSERT [dbo].[ChiTietHoaDon] ([Id], [SoLuong], [IdHoaDon], [IdSanPham], [DonGia]) 
    VALUES (N'394cd21b-519c-4ab7-b5fb-fe85b73eab87', 1, N'70b2c441-c2c4-4d6a-9504-50570a83ee18', N'9886fc35-bf06-4232-a65e-5870af52b868', 12000)
INSERT [dbo].[ChiTietHoaDon] ([Id], [SoLuong], [IdHoaDon], [IdSanPham], [DonGia]) 
    VALUES (N'34bb907e-6760-4f96-8309-a3826313b247', 1, N'c7892020-a32e-494d-8713-69e00d4ff55b', N'abe42af8-a972-455f-ba2b-68c407a2abdd', 30000)

-- Chèn dữ liệu mẫu vào bảng Chi Tiết Khuyến Mãi
INSERT [dbo].[ChiTietKhuyenMai] ([Id], [Ten], [SoTienGiamGia], [IdSanPhamAnhHuong], [MucGiamGia], [MoTa]) 
    VALUES (N'41ddb915-0c48-4543-969e-71d7b316af10', N'KhuyenMai1', 5000, N'9886fc35-bf06-4232-a65e-5870af52b868', 0.05, N'Giam gia Coca')
INSERT [dbo].[ChiTietKhuyenMai] ([Id], [Ten], [SoTienGiamGia], [IdSanPhamAnhHuong], [MucGiamGia], [MoTa]) 
    VALUES (N'8a37e1b5-fdb5-4fea-9560-5ffb79189fb6', N'KhuyenMai2', 2000, N'f4435170-763e-420c-9a6f-8bcb544a2d26', 0.10, N'Giam gia Banh mi')

-- Chèn dữ liệu mẫu vào bảng Khuyến Mãi
INSERT [dbo].[KhuyenMai] ([Id], [IdChiTietKhuyenMai], [IdKhachHang], [SoLuong], [ThoiGianApDung]) 
    VALUES (N'66e654fd-e5da-49f3-9965-c72c8e09109a', N'41ddb915-0c48-4543-969e-71d7b316af10', N'd5d7e298-1c4a-49d0-8cbe-c3ac1013b5ce', 1, CAST(N'2023-06-01' AS DateTime))
INSERT [dbo].[KhuyenMai] ([Id], [IdChiTietKhuyenMai], [IdKhachHang], [SoLuong], [ThoiGianApDung]) 
    VALUES (N'44befd46-ca69-441c-9983-15a5d1866a12', N'8a37e1b5-fdb5-4fea-9560-5ffb79189fb6', N'a4ee7d90-00ba-4e52-8e89-db617f762371', 2, CAST(N'2023-06-02' AS DateTime))
