package model;

import java.util.Date;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhanVien {
    private String id;           
    private String matKhau;
    private String tenDayDu;
    private Date ngaySinh;
    private String gioiTinh;
    private String email;
    private String chucVu; 
    
    @Override
    public String toString() {
        return "NhanVien{" +
                "id=" + id +
                ", tenDayDu='" + tenDayDu + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", gioiTinh=" + gioiTinh +
                ", chucVu=" + chucVu +                
                ", email='" + email + '\'' +
                '}';
    }
}
