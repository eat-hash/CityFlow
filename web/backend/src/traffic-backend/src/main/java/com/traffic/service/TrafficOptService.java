package com.traffic.service;

import com.traffic.model.*;

import java.util.*;

public class TrafficOptService {

    public SingleOptimizeResponse optimizeSingle(String id, TrafficFlow flow, int minGreen, int maxCycle) {
        SingleOptimizeResponse resp = new SingleOptimizeResponse();
        resp.setIntersectionId(id);
        resp.setCycleLength(114);
        resp.setAverageDelay(32.45);
        resp.setCapacity(88.72);

        TrafficFlow plan = new TrafficFlow();
        plan.setEastWestStraight(32);
        plan.setEastWestLeft(10);
        plan.setNorthSouthStraight(44);
        plan.setNorthSouthLeft(8);
        resp.setTimingPlan(plan);

        return resp;
    }

    public List<SingleOptimizeResponse> optimizeBatch(List<BatchOptimizeRequest.BatchItem> list) {
        List<SingleOptimizeResponse> res = new ArrayList<>();
        for (BatchOptimizeRequest.BatchItem item : list) {
            res.add(optimizeSingle(item.getIntersectionId(), item.getTrafficFlow(), 15, 120));
        }
        return res;
    }

    public List<Map<String, Object>> getTestData() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("intersectionId", "100" + i);
            TrafficFlow flow = new TrafficFlow();
            flow.setEastWestStraight(400 + i * 20);
            flow.setEastWestLeft(100 + i * 10);
            flow.setNorthSouthStraight(500 + i * 30);
            flow.setNorthSouthLeft(80 + i * 5);
            map.put("trafficFlow", flow);
            list.add(map);
        }
        return list;
    }
}