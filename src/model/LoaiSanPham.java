package Model;

import java.util.logging.Logger;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoaiSanPham {

    private String id;
    private String ten;
    private String moTa;

    @Override
    public String toString() {
        return "LoaiSanPham{"
                + "id=" + id
                + ", ten='" + ten + '\''
                + ", moTa='" + moTa + '\''
                + '}';
    }
}
