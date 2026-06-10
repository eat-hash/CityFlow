package backend.src.test;

import backend.src.dao.IntersectionDAO;
import backend.src.dao.TrafficDataDAO;
import backend.src.dao.TimingPlanDAO;
import backend.src.dao.StatisticsDAO;
import backend.src.entity.Intersection;
import backend.src.entity.TrafficData;
import backend.src.entity.TimingPlan;
import java.util.List;
import java.util.Map;

public class DAOTest {
    public static void main(String[] args) {
        testIntersectionDAO();
        testTrafficDataDAO();
        testTimingPlanDAO();
        testStatisticsDAO();
    }

    private static void testIntersectionDAO() {
        IntersectionDAO dao = new IntersectionDAO();
        List<Intersection> list = dao.listAll();
        System.out.println("路口列表：" + list.size() + " 条");
        list.forEach(i -> System.out.println(i.getName()));
    }

    private static void testTrafficDataDAO() {
        TrafficDataDAO dao = new TrafficDataDAO();
        List<TrafficData> list = dao.listByIntersection("INT001");
        System.out.println("交通数据：" + list.size() + " 条");
    }

    private static void testTimingPlanDAO() {
        TimingPlanDAO dao = new TimingPlanDAO();
        List<TimingPlan> list = dao.listAll();
        System.out.println("配时方案：" + list.size() + " 条");
    }

    private static void testStatisticsDAO() {
        StatisticsDAO dao = new StatisticsDAO();
        Map<String, Integer> map = dao.countTrafficByIntersection();
        System.out.println("各路口流量统计：");
        map.forEach((k, v) -> System.out.println(k + ": " + v));
    }
}