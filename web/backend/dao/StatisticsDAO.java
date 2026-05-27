package backend.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class StatisticsDAO {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 全局统计：路口总数、配时方案总数、在线设备数、今日通行量
     */
    public Map<String, Object> getGlobalStatistics() {
        Map<String, Object> map = new HashMap<>();
        // 路口总数
        map.put("intersectionCount", count("intersection", null, null));
        // 配时方案总数
        map.put("timingPlanCount", count("timing_plan", null, null));
        // 在线设备（status=1）
        map.put("deviceOnlineCount", count("device", "status=1", null));
        // 今日通行量
        map.put("todayPassCount", count("traffic_record", "DATE(create_time)=CURDATE()", null));
        return map;
    }

    /**
     * 时间段通行量统计（用于报表）
     */
    public int getTrafficCountBetween(String start, String end) {
        return count("traffic_record", "create_time BETWEEN ? AND ?", new Object[]{start, end});
    }

    // 通用count：只COUNT(id)，不走全表
    private int count(String table, String where, Object[] args) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(id) FROM ").append(table);
        if (where != null && !where.isEmpty()) {
            sql.append(" WHERE ").append(where);
        }
        try {
            Integer val = jdbcTemplate.queryForObject(sql.toString(), args, Integer.class);
            return val == null ? 0 : val;
        } catch (Exception e) {
            return 0;
        }
    }
}