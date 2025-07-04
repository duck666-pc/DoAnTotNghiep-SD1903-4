package model;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPham {

    private String id;
    private String ten;
    private String moTa;
    private float gia;
    private String loaiSanPham;

    @Override
    public String toString() {
        return "SanPham{"
                + "id=" + id
                + ", ten='" + ten + '\''
                + ", moTa=" + moTa
                + ", gia=" + gia
                + ", loaiSanPham='" + loaiSanPham + '\''
                + '}';
    }
}
