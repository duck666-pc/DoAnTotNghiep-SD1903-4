/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.*;

/**
 *
 * @author Admin
 */
public class MyConnection {

    public MyConnection() {
        
    }
    
    public Connection DBConnect() throws ClassNotFoundException, SQLException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://localhost\\SQLEXPRESS01:1433;databaseName=DoAnTotNghiep;trustServerCertificate=true";
        String user = "sa";
        String pass = "123456789";
        return DriverManager.getConnection(url, user, pass);
    }
}
