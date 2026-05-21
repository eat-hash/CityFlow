import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.*;

public class JSONDataLoader {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> loadIntersectionIds(String roadnetFilePath) throws Exception {
        JsonNode root = objectMapper.readTree(new File(roadnetFilePath));
        List<String> intersectionIds = new ArrayList<>();
        for (JsonNode intersectionNode : root.get("intersections")) {
            intersectionIds.add(intersectionNode.get("id").asText());
        }
        return intersectionIds;
    }

    public Map<String, Integer> loadSingleFlowFile(String anonFilePath) throws Exception {
        JsonNode root = objectMapper.readTree(new File(anonFilePath));
        Map<String, Integer> roadFlowCount = new HashMap<>();

        for (JsonNode vehicleNode : root) {
            // 关键修复：加 null 判断
            JsonNode vehicle = vehicleNode.get("vehicle");
            if (vehicle == null) continue;

            JsonNode routeNode = vehicle.get("route");
            if (routeNode == null || !routeNode.isArray()) continue;

            for (JsonNode roadNode : routeNode) {
                String roadId = roadNode.asText();
                roadFlowCount.put(roadId, roadFlowCount.getOrDefault(roadId, 0) + 1);
            }
        }
        return roadFlowCount;
    }

    public Map<String, Integer> loadCombinedFlow(String... anonFiles) throws Exception {
        Map<String, Integer> total = new HashMap<>();
        for (String path : anonFiles) {
            Map<String, Integer> flow = loadSingleFlowFile(path);
            for (Map.Entry<String, Integer> entry : flow.entrySet()) {
                total.put(entry.getKey(), total.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
        return total;
    }
}