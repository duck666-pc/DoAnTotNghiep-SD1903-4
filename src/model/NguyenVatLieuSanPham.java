/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguyenVatLieuSanPham {
    private String id;
    private String sanPhamId;
    private String nguyenVatLieuId;
    private int soLuongCan;

    @Override
    public String toString() {
        return "NguyenVatLieuSanPham{"
                + "id=" + id
                + ", sanPhamId='" + sanPhamId + '\''
                + ", nguyenVatLieuId=" + nguyenVatLieuId
                + ", soLuongCan='" + soLuongCan + '\''
                + '}';
    }    
}
