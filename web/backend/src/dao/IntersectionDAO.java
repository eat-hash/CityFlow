package backend.dao;

import backend.entity.Intersection;
import backend.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IntersectionDAO {

    // 新增路口
    public boolean addIntersection(Intersection intersection) {
        String sql = "INSERT INTO intersection(intersection_id, name, code, region, lane_count) VALUES (?,?,?,?,?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, intersection.getIntersectionId());
            pstmt.setString(2, intersection.getName());
            pstmt.setString(3, intersection.getCode());
            pstmt.setString(4, intersection.getRegion());
            pstmt.setInt(5, intersection.getLaneCount());
            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根据ID查询路口
    public Intersection getById(String intersectionId) {
        String sql = "SELECT * FROM intersection WHERE intersection_id=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, intersectionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Intersection in = new Intersection();
                in.setIntersectionId(rs.getString("intersection_id"));
                in.setName(rs.getString("name"));
                in.setCode(rs.getString("code"));
                in.setRegion(rs.getString("region"));
                in.setLaneCount(rs.getInt("lane_count"));
                return in;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 分页查询所有路口
    public List<Intersection> listByPage(int page, int size) {
        List<Intersection> list = new ArrayList<>();
        int start = (page - 1) * size;
        String sql = "SELECT * FROM intersection LIMIT ?,?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, start);
            pstmt.setInt(2, size);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Intersection in = new Intersection();
                in.setIntersectionId(rs.getString("intersection_id"));
                in.setName(rs.getString("name"));
                in.setCode(rs.getString("code"));
                in.setRegion(rs.getString("region"));
                in.setLaneCount(rs.getInt("lane_count"));
                list.add(in);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}