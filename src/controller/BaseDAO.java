package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {

    private final MyConnection conn;

    public BaseDAO() {
        conn = new MyConnection();
    }

    protected abstract String getTableName();
    protected abstract String getPrimaryKeyColumn();
    protected abstract T mapResultSetToObject(ResultSet rs) throws SQLException;

    public List<T> getAll() throws SQLException, ClassNotFoundException {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();
        
        try (Connection con = conn.DBConnect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSetToObject(rs));
            }
        }
        return list;
    }

    public T getRow(String id) throws SQLException, ClassNotFoundException {
        T obj = null;
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getPrimaryKeyColumn() + " = ?";
        
        try (Connection con = conn.DBConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    obj = mapResultSetToObject(rs);
                }
            }
        }
        return obj;
    }

    public int add(T obj) throws SQLException, ClassNotFoundException {
        String sql = getInsertQuery();
        try (Connection con = conn.DBConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            setInsertParameters(ps, obj);
            return ps.executeUpdate();
        }
    }

    public int edit(T obj, String oldId) throws SQLException, ClassNotFoundException {
        String sql = getUpdateQuery();
        try (Connection con = conn.DBConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            setUpdateParameters(ps, obj);
            ps.setString(getUpdateWhereIndex(), oldId);
            return ps.executeUpdate();
        }
    }

    public int delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getPrimaryKeyColumn() + " = ?";
        try (Connection con = conn.DBConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, id);
            return ps.executeUpdate();
        }
    }

    protected abstract String getInsertQuery();
    protected abstract void setInsertParameters(PreparedStatement ps, T obj) throws SQLException;
    protected abstract String getUpdateQuery();
    protected abstract void setUpdateParameters(PreparedStatement ps, T obj) throws SQLException;
    protected abstract int getUpdateWhereIndex();
}
