package backend.test;

import backend.entity.Intersection;
import backend.entity.TrafficData;
import backend.dao.IntersectionDAO;
import backend.dao.TrafficDataDAO;

import java.util.Date;
import java.util.List;

public class DAOTest {
    public static void main(String[] args) {

        // 测试路口DAO
        IntersectionDAO intersectionDAO = new IntersectionDAO();

        // 1. 测试新增
        Intersection in = new Intersection();
        in.setIntersectionId("I004");
        in.setName("测试路口");
        in.setCode("TEST01");
        in.setRegion("测试区域");
        in.setLaneCount(4);
        boolean addOk = intersectionDAO.addIntersection(in);
        System.out.println("新增路口：" + addOk);

        // 2. 测试分页查询
        List<Intersection> pageList = intersectionDAO.listByPage(1, 10);
        System.out.println("分页查询路口数量：" + pageList.size());

        // 测试交通数据DAO
        TrafficDataDAO trafficDataDAO = new TrafficDataDAO();

        // 1. 测试新增
        TrafficData data = new TrafficData();
        data.setIntersectionId("I004");
        data.setCollectTime(new Date());
        data.setLaneFlow("直15,左10,右8");
        boolean dataAdd = trafficDataDAO.addTrafficData(data);
        System.out.println("新增交通数据：" + dataAdd);

        // 2. 测试分页
        List<TrafficData> dataPage = trafficDataDAO.listByPage(1, 10);
        System.out.println("分页查询交通数据：" + dataPage.size());
    }
}