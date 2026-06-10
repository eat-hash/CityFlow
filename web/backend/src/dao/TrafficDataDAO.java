package backend.dao;

import backend.entity.TrafficData;
import backend.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TrafficDataDAO {

    // 新增交通数据
    public boolean addTrafficData(TrafficData data) {
        String sql = "INSERT INTO traffic_data(intersection_id, collect_time, lane_flow) VALUES (?,?,?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, data.getIntersectionId());
            pstmt.setTimestamp(2, new java.sql.Timestamp(data.getCollectTime().getTime()));
            pstmt.setString(3, data.getLaneFlow());
            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根据路口ID查询数据
    public List<TrafficData> getByIntersectionId(String intersectionId) {
        List<TrafficData> list = new ArrayList<>();
        String sql = "SELECT * FROM traffic_data WHERE intersection_id=?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, intersectionId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TrafficData data = new TrafficData();
                data.setDataId(rs.getInt("data_id"));
                data.setIntersectionId(rs.getString("intersection_id"));
                data.setCollectTime(rs.getTimestamp("collect_time"));
                data.setLaneFlow(rs.getString("lane_flow"));
                list.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 分页查询交通数据
    public List<TrafficData> listByPage(int page, int size) {
        List<TrafficData> list = new ArrayList<>();
        int start = (page - 1) * size;
        String sql = "SELECT * FROM traffic_data LIMIT ?,?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, start);
            pstmt.setInt(2, size);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TrafficData data = new TrafficData();
                data.setDataId(rs.getInt("data_id"));
                data.setIntersectionId(rs.getString("intersection_id"));
                data.setCollectTime(rs.getTimestamp("collect_time"));
                data.setLaneFlow(rs.getString("lane_flow"));
                list.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}