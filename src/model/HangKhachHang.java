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
public class HangKhachHang {
    private String id;
    private String ten;
    private float mucGiamGia;
    
    @Override
    public String toString() {
        return "KhachHang(){"
                + "id=" + id
                + ", ten='" + ten + '\''
                + ", mucGiamGia=" + mucGiamGia + '\''
                + '}';
    }    
}
