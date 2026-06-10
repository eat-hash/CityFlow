package backend.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class StatisticsDAO {

    private final JdbcTemplate jdbcTemplate;

    public StatisticsDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 全局统计：方案总数、启用数、路口数
    public Map<String, Object> getGlobalStats() {
        Map<String, Object> map = new HashMap<>();

        // 方案总数
        Long totalPlan = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM timing_plan", Long.class);
        // 启用方案数
        Long activePlan = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM timing_plan WHERE status=1", Long.class);
        // 关联路口数（去重）
        Long totalIntersection = jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT intersection_id) FROM timing_plan", Long.class);

        map.put("totalPlan", totalPlan);
        map.put("activePlan", activePlan);
        map.put("totalIntersection", totalIntersection);
        return map;
    }
}