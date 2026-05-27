package backend.controller;

import org.springframework.web.bind.annotation.*;
import backend.service.TrafficService;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/traffic")
public class TrafficController {

    @Resource
    private TrafficService trafficService;

    // 1. 单路口配时优化接口
    @PostMapping("/optimize/single")
    public Map<String, Object> optimizeSingle(@RequestBody Map<String, Object> params) {
        String intersectionId = (String) params.get("intersectionId");
        Map<String, Integer> trafficFlow = (Map<String, Integer>) params.get("trafficFlow");
        int minGreen = (int) params.getOrDefault("minGreen", 15);
        int maxCycle = (int) params.getOrDefault("maxCycle", 120);

        Map<String, Object> result = trafficService.optimizeSingle(intersectionId, trafficFlow, minGreen, maxCycle);

        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("msg", "success");
        resp.put("data", result);
        return resp;
    }

    // 2. 批量路口配时优化接口
    @PostMapping("/optimize/batch")
    public Map<String, Object> optimizeBatch(@RequestBody Map<String, List<Map<String, Object>>> params) {
        List<Map<String, Object>> intersectionList = params.get("intersectionList");
        List<Map<String, Object>> results = trafficService.optimizeBatch(intersectionList);

        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("msg", "success");
        resp.put("data", results);
        return resp;
    }

    // 3. 获取模拟测试数据接口
    @GetMapping("/data/test")
    public Map<String, Object> getTestData() {
        List<Map<String, Object>> testData = trafficService.getTestData();

        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("msg", "success");
        resp.put("data", testData);
        return resp;
    }
}