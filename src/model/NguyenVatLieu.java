package model;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguyenVatLieu {

    private String id;
    private String ten;
    private String donVi;
    private int soLuong;
    private int mucCanDatThem;

    @Override
    public String toString() {
        return "NguyenVatLieu{"
                + "id=" + id
                + ", ten='" + ten + '\''
                + ", donVi=" + donVi
                + ", soLuong=" + soLuong
                + ", mucCanDatThem='" + mucCanDatThem + '\''
                + '}';
    }

}
