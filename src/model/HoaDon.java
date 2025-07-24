package Model;

import java.sql.Timestamp;
import java.math.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {
    private String id;
    private Timestamp thoiGian;
    private String idKhachHang;
    private String idNguoiDung;
    private BigDecimal tongTienGoc;
    private BigDecimal mucGiamGia;
    private BigDecimal tongTienSauGiamGia;

    @Override
    public String toString() {
        return "HoaDon(){" +
                "id=" + id +
                ", thoiGian='" + thoiGian + '\'' +
                ", idKhachHang=" + idKhachHang +
                ", idNguoiDung=" + idNguoiDung +  
                ", tongTienGoc=" + tongTienGoc +  
                ", mucGiamGia=" + mucGiamGia +                  
                ", tongTienSauGiamGia=" + tongTienSauGiamGia + '\'' +
                '}';
    }
}