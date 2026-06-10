package backend.src.dao;

import backend.src.entity.TimingPlan;
import backend.src.util.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimingPlanDAO {

    // 查询所有配时方案
    public List<TimingPlan> listAll() {
        List<TimingPlan> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM timing_plan ORDER BY create_time DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                TimingPlan plan = new TimingPlan();
                plan.setId(rs.getInt("id"));
                plan.setName(rs.getString("name"));
                plan.setIntersectionId(rs.getInt("intersection_id"));
                plan.setPhaseCount(rs.getInt("phase_count"));
                plan.setCycle(rs.getInt("cycle"));
                plan.setMinGreen(rs.getInt("min_green"));
                plan.setMaxCycle(rs.getInt("max_cycle"));
                plan.setStatus(rs.getInt("status"));
                plan.setCreateTime(rs.getTimestamp("create_time"));
                list.add(plan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs, pstmt, conn);
        }
        return list;
    }

    // 更新方案状态
    public int updateStatus(int id, int status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "UPDATE timing_plan SET status = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            JDBCUtil.close(pstmt, conn);
        }
    }
}