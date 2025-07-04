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
public class ChiTietKhuyenMai {

    private String id;
    private String hinhThucGiam;
    private float soTienGiamGia;
    private String sanPhamid;
    private float mucGiamGia;
    private String quaTang;

    @Override
    public String toString() {
        return "ChiTietKhuyenMai{"
                + "id=" + id
                + ", hinhThucGiam=" + hinhThucGiam
                + ", soTienGiamGia=" + soTienGiamGia
                + ", sanPhamid=" + sanPhamid
                + ", mucGiamGia=" + mucGiamGia
                + ", quaTang=" + quaTang
                + '}';
    }
}
