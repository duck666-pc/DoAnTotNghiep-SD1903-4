package Model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHoaDon {
    private String id;
    private int soLuong;
    private String idHoaDon;
    private String idSanPham;
    private double donGia;

    @Override
    public String toString() {
        return "ChiTietHoaDon{"
                + "id=" + id
                + ", soLuong=" + soLuong + '\'' 
                + ", idHoaDon=" + idHoaDon
                + ", idSanPham=" + idSanPham
                + ", donGia=" + donGia + '\''                
                + '}';
    }
}