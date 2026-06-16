package backend.src.dao;

import backend.src.entity.Intersection;
import backend.src.util.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IntersectionDAO {

    public List<Intersection> listAll() {
        List<Intersection> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM intersection";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Intersection i = new Intersection();
                i.setIntersectionId(rs.getString("intersection_id"));
                i.setName(rs.getString("name"));
                i.setCode(rs.getString("code"));
                i.setRegion(rs.getString("region"));
                i.setLaneCount(rs.getInt("lane_count"));
                list.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs, pstmt, conn);
        }
        return list;
    }

    public int add(Intersection intersection) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "INSERT INTO intersection(intersection_id,name,code,region,lane_count) VALUES(?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, intersection.getIntersectionId());
            pstmt.setString(2, intersection.getName());
            pstmt.setString(3, intersection.getCode());
            pstmt.setString(4, intersection.getRegion());
            pstmt.setInt(5, intersection.getLaneCount());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            JDBCUtil.close(pstmt, conn);
        }
    }
}