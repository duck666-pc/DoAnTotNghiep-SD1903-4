package model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang {

    private String id;
    private String ten;
    private String dienThoai;
    private String diaChi;
    private String hangKhachHangId;

    public String gethangKhachHangId() {
        return hangKhachHangId;
    }

    public void sethangKhachHangId(String hangKhachHangId) {
        this.hangKhachHangId = hangKhachHangId;
    }

    @Override
    public String toString() {
        return "KhachHang(){"
                + "id=" + id
                + ", ten='" + ten + '\''
                + ", dienThoai=" + dienThoai
                + ", diaChi=" + diaChi
                + ", hangKhachHangId=" + hangKhachHangId + '\''
                + '}';
    }
}
