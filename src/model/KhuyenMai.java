/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


import java.util.Date;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhuyenMai {
    private String id;
    private String chiTietid; 
    private String ten; 
    private String moTa;
    private int soLuong;
    private Date thoiGianApDung;
    private Date thoiGianKetThuc;

    @Override
    public String toString() {
        return "KhuyenMai{" 
                + "id=" + id 
                + ", chiTietid=" + chiTietid 
                + ", ten=" + ten 
                + ", moTa=" + moTa 
                + ", soLuong=" + soLuong 
                + ", thoiGianApDung=" + thoiGianApDung 
                + ", thoiGianKetThuc=" + thoiGianKetThuc 
                + '}';
    }
  
}
