package backend.dao;

import backend.entity.TimingPlan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TimingPlanDAO {

    private final JdbcTemplate jdbcTemplate;

    public TimingPlanDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 新增配时方案
    public int insert(TimingPlan plan) {
        String sql = "INSERT INTO timing_plan(name,intersection_id,phase_count,cycle,min_green,max_cycle,status,create_time) VALUES (?,?,?,?,?,?,?,NOW())";
        return jdbcTemplate.update(sql,
                plan.getName(),
                plan.getIntersectionId(),
                plan.getPhaseCount(),
                plan.getCycle(),
                plan.getMinGreen(),
                plan.getMaxCycle(),
                plan.getStatus());
    }

    // 更新方案
    public int update(TimingPlan plan) {
        String sql = "UPDATE timing_plan SET name=?,intersection_id=?,phase_count=?,cycle=?,min_green=?,max_cycle=?,status=? WHERE id=?";
        return jdbcTemplate.update(sql,
                plan.getName(),
                plan.getIntersectionId(),
                plan.getPhaseCount(),
                plan.getCycle(),
                plan.getMinGreen(),
                plan.getMaxCycle(),
                plan.getStatus(),
                plan.getId());
    }

    // 删除
    public int deleteById(Integer id) {
        String sql = "DELETE FROM timing_plan WHERE id=?";
        return jdbcTemplate.update(sql, id);
    }

    // 按ID查询
    public TimingPlan findById(Integer id) {
        String sql = "SELECT * FROM timing_plan WHERE id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new TimingPlanRowMapper());
    }

    // 分页查询（优化：只查需要字段、支持索引）
    public List<TimingPlan> findPage(int page, int size) {
        String sql = "SELECT * FROM timing_plan ORDER BY create_time DESC LIMIT ?,?";
        return jdbcTemplate.query(sql, new Object[]{(page - 1) * size, size}, new TimingPlanRowMapper());
    }

    // 总数
    public long count() {
        String sql = "SELECT COUNT(*) FROM timing_plan";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    private static class TimingPlanRowMapper implements RowMapper<TimingPlan> {
        @Override
        public TimingPlan mapRow(ResultSet rs, int rowNum) throws SQLException {
            TimingPlan p = new TimingPlan();
            p.setId(rs.getInt("id"));
            p.setName(rs.getString("name"));
            p.setIntersectionId(rs.getInt("intersection_id"));
            p.setPhaseCount(rs.getInt("phase_count"));
            p.setCycle(rs.getInt("cycle"));
            p.setMinGreen(rs.getInt("min_green"));
            p.setMaxCycle(rs.getInt("max_cycle"));
            p.setStatus(rs.getInt("status"));
            p.setCreateTime(rs.getTimestamp("create_time"));
            return p;
        }
    }
}