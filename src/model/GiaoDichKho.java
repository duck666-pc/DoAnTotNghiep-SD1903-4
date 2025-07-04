package Model;

import java.sql.Timestamp;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiaoDichKho {
    private String id;
    private String idNguyenVatLieu;
    private Timestamp thoiGian;
    private String loai;
    private int soLuong;
    private String ghiChu;

    
    @Override
    public String toString() {
        return "GiaoDichKho(){" +
                "id=" + id +
                ", idNguyenVatLieu='" + idNguyenVatLieu + '\'' +
                ", thoiGian=" + thoiGian +
                ", loai=" + loai +  
                ", soLuong=" + soLuong +                   
                ", ghiChu=" + ghiChu + '\'' +
                '}';
    }    
}