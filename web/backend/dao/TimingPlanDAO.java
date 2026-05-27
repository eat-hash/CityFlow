package backend.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import web.backend.entity.TimingPlan;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class TimingPlanDAO {

    @Resource
    private JdbcTemplate jdbcTemplate;

    // 新增
    public int insert(TimingPlan plan) {
        String sql = "INSERT INTO timing_plan (name, intersection_id, phase_count, cycle, min_green, max_cycle, status, create_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        return jdbcTemplate.update(sql,
                plan.getName(),
                plan.getIntersectionId(),
                plan.getPhaseCount(),
                plan.getCycle(),
                plan.getMinGreen(),
                plan.getMaxCycle(),
                plan.getStatus());
    }

    // 更新
    public int update(TimingPlan plan) {
        String sql = "UPDATE timing_plan SET name=?, intersection_id=?, phase_count=?, cycle=?, min_green=?, max_cycle=?, status=? WHERE id=?";
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

    // ID查询
    public TimingPlan selectById(Integer id) {
        String sql = "SELECT id,name,intersection_id,phase_count,cycle,min_green,max_cycle,status,create_time FROM timing_plan WHERE id=?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id},
                    new BeanPropertyRowMapper<>(TimingPlan.class));
        } catch (Exception e) {
            return null;
        }
    }

    // 分页+名称模糊查询（管理页常用）
    public List<TimingPlan> selectPage(String name, int pageNum, int pageSize) {
        String sql = "SELECT id,name,intersection_id,phase_count,cycle,min_green,max_cycle,status,create_time " +
                "FROM timing_plan " +
                "WHERE (? IS NULL OR name LIKE CONCAT('%',?,'%')) " +
                "ORDER BY create_time DESC LIMIT ?, ?";
        return jdbcTemplate.query(sql,
                new Object[]{name, name, (pageNum - 1) * pageSize, pageSize},
                new BeanPropertyRowMapper<>(TimingPlan.class));
    }

    // 总数（带条件）
    public int count(String name) {
        String sql = "SELECT COUNT(id) FROM timing_plan WHERE (? IS NULL OR name LIKE CONCAT('%',?,'%'))";
        Integer cnt = jdbcTemplate.queryForObject(sql, new Object[]{name, name}, Integer.class);
        return cnt == null ? 0 : cnt;
    }
}