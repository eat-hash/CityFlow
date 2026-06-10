import java.util.Map;

/**
 * 算法入参实体
 * 相位Key：东西直行、东西左转、南北直行、南北左转（对齐前端）
 */
public class AlgorithmInput {
    private final String intersectionId;
    private final Map<String, Integer> phaseFlow;
    private final int minGreen;
    private final int maxCycle;

    // 中文相位常量（全局统一，避免硬编码）
    public static final String PHASE_EW_STRAIGHT = "东西直行";
    public static final String PHASE_EW_LEFT = "东西左转";
    public static final String PHASE_NS_STRAIGHT = "南北直行";
    public static final String PHASE_NS_LEFT = "南北左转";

    public AlgorithmInput(String intersectionId, Map<String, Integer> phaseFlow, int minGreen, int maxCycle) {
        if (intersectionId == null || intersectionId.isBlank()) {
            throw new IllegalArgumentException("路口ID不能为空");
        }
        if (phaseFlow == null) {
            throw new IllegalArgumentException("相位流量不能为null");
        }
        if (minGreen <= 0 || minGreen > 60) {
            throw new IllegalArgumentException("最小绿灯必须在 1~60 秒之间");
        }
        if (maxCycle <= minGreen * 4 + 20) {
            throw new IllegalArgumentException("最大周期过小，无法满足最小绿灯时长");
        }

        this.intersectionId = intersectionId;
        this.phaseFlow = phaseFlow;
        this.minGreen = minGreen;
        this.maxCycle = maxCycle;
    }

    public String getIntersectionId() {
        return intersectionId;
    }

    public Map<String, Integer> getPhaseFlow() {
        return phaseFlow;
    }

    public int getMinGreen() {
        return minGreen;
    }

    public int getMaxCycle() {
        return maxCycle;
    }
}
