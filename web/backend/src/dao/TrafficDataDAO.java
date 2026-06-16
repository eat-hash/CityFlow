package backend.src.dao;

import backend.src.entity.TrafficData;
import backend.src.util.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrafficDataDAO {

    public List<TrafficData> listByIntersection(String intersectionId) {
        List<TrafficData> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM traffic_data WHERE intersection_id = ? ORDER BY collect_time DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, intersectionId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                TrafficData t = new TrafficData();
                t.setDataId(rs.getInt("data_id"));
                t.setIntersectionId(rs.getString("intersection_id"));
                t.setCollectTime(rs.getTimestamp("collect_time"));
                t.setLaneFlow(rs.getString("lane_flow"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs, pstmt, conn);
        }
        return list;
    }

    public int add(TrafficData data) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "INSERT INTO traffic_data(intersection_id,collect_time,lane_flow) VALUES(?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, data.getIntersectionId());
            pstmt.setTimestamp(2, data.getCollectTime());
            pstmt.setString(3, data.getLaneFlow());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            JDBCUtil.close(pstmt, conn);
        }
    }
}