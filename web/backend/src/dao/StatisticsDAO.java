package backend.src.dao;  // 这里改成你文件实际所在的包

import backend.src.util.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StatisticsDAO extends BaseDAO {

    // 统计各路口车流量
    public Map<String, Integer> countTrafficByIntersection() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT intersection_id, COUNT(*) AS count FROM traffic_data GROUP BY intersection_id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("intersection_id"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs, pstmt, conn);
        }
        return map;
    }
}