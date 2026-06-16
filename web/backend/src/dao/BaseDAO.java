package backend.src.dao;

import backend.src.util.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO {
    protected Connection conn;
    protected PreparedStatement pstmt;
    protected ResultSet rs;

    protected int executeUpdate(String sql, Object... params) {
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            setParams(params);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            JDBCUtil.close(pstmt, conn);
        }
    }

    protected <T> List<T> query(String sql, RowMapper<T> mapper, Object... params) {
        List<T> list = new ArrayList<>();
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            setParams(params);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapper.mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs, pstmt, conn);
        }
        return list;
    }

    private void setParams(Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }

    @FunctionalInterface
    public interface RowMapper<T> {
        T mapRow(ResultSet rs) throws SQLException;
    }
}