package model;
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
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }    
  
    @Override
    public String toString() {
        return "LoaiSanPham{"
                + "id=" + id
                + ", ten='" + ten + '\''
                + ", moTa='" + moTa + '\''
                + '}';
    }
}
