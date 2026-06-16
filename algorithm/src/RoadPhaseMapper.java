import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class RoadPhaseMapper {
    public static final String PHASE_EW_STRAIGHT = AlgorithmInput.PHASE_EW_STRAIGHT;
    public static final String PHASE_EW_LEFT = AlgorithmInput.PHASE_EW_LEFT;
    public static final String PHASE_NS_STRAIGHT = AlgorithmInput.PHASE_NS_STRAIGHT;
    public static final String PHASE_NS_LEFT = AlgorithmInput.PHASE_NS_LEFT;

    /**
     * 根据 roadId 判断所属相位
     * road_0_1_0 / road_1_1_2  → 东西向
     * road_2_xx / road_3_xx   → 南北向
     * 末尾 0 = 左转，末尾 2 = 直行
     */
    public String getPhaseByRoadId(String roadId) {
        String[] parts = roadId.split("_");
        if (parts.length < 4) {
            return PHASE_EW_STRAIGHT;
        }
        int first = Integer.parseInt(parts[1]);
        int last = Integer.parseInt(parts[3]);

        // 区分方向
        boolean isEastWest = (first == 0 || first == 1);
        boolean isNorthSouth = (first == 2 || first == 3);

        // 区分直行/左转
        if (isEastWest) {
            return last == 0 ? PHASE_EW_LEFT : PHASE_EW_STRAIGHT;
        } else if (isNorthSouth) {
            return last == 0 ? PHASE_NS_LEFT : PHASE_NS_STRAIGHT;
        }
        return PHASE_EW_STRAIGHT;
    }

    /**
     * 精准匹配：路口关联的道路 → 汇总相位流量
     * 读取 roadnet 中路口自带的 roads 列表，不再模糊匹配
     */
    public Map<String, Integer> buildPhaseFlow(String interId, Map<String, Integer> allRoadFlow, JsonNode interNode) {
        Map<String, Integer> phaseFlow = new LinkedHashMap<>();
        phaseFlow.put(PHASE_EW_STRAIGHT, 0);
        phaseFlow.put(PHASE_EW_LEFT, 0);
        phaseFlow.put(PHASE_NS_STRAIGHT, 0);
        phaseFlow.put(PHASE_NS_LEFT, 0);

        // 拿到该路口绑定的所有道路
        JsonNode roadArr = interNode.get("roads");
        if (roadArr == null || !roadArr.isArray()) {
            return phaseFlow;
        }

        for (JsonNode roadNode : roadArr) {
            String roadId = roadNode.asText();
            int flow = allRoadFlow.getOrDefault(roadId, 0);
            String phase = getPhaseByRoadId(roadId);
            phaseFlow.put(phase, phaseFlow.get(phase) + flow);
        }
        return phaseFlow;
    }
}