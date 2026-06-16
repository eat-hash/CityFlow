import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TrafficSignalOptimizer {
    // 相位列表（中文，与前端完全一致）
    private static final List<String> PHASES = Collections.unmodifiableList(
            Arrays.asList(
                    AlgorithmInput.PHASE_EW_STRAIGHT,
                    AlgorithmInput.PHASE_EW_LEFT,
                    AlgorithmInput.PHASE_NS_STRAIGHT,
                    AlgorithmInput.PHASE_NS_LEFT
            )
    );

    // 算法常量
    private static final int YELLOW_TIME = 3;
    private static final double SATURATION_FLOW = 1800;
    private static final int LOST_TIME_PER_PHASE = 2;
    private static final double MAX_FLOW_RATIO = 0.95;

    public AlgorithmOutput computeTimingPlan(AlgorithmInput input) {
        String intersectionId = input.getIntersectionId();
        Map<String, Integer> phaseFlow = input.getPhaseFlow();
        int minGreen = input.getMinGreen();
        int maxCycle = input.getMaxCycle();

        // 总损失时间
        int totalLostTime = PHASES.size() * LOST_TIME_PER_PHASE;

        // 计算各相位流量比
        Map<String, Double> flowRatio = new LinkedHashMap<>();
        double totalY = 0.0;
        for (String phase : PHASES) {
            int flow = phaseFlow.getOrDefault(phase, 0);
            double y = flow / SATURATION_FLOW;
            y = Math.min(y, MAX_FLOW_RATIO);
            flowRatio.put(phase, y);
            totalY += y;
        }

        // 全流量为0，直接抛出业务异常
        if (totalY <= 0) {
            throw new RuntimeException("所有相位流量为0，无法计算配时");
        }

        // Webster 最佳周期
        double optimalCycle = (1.5 * totalLostTime + 5) / (1 - totalY);
        int minCycle = minGreen * PHASES.size() + PHASES.size() * YELLOW_TIME + totalLostTime;
        int cycle = (int) Math.round(Math.max(minCycle, Math.min(optimalCycle, maxCycle)));

        // 有效绿灯时间
        int totalYellowTime = PHASES.size() * YELLOW_TIME;
        int effectiveGreen = cycle - totalLostTime - totalYellowTime;
        effectiveGreen = Math.max(effectiveGreen, minGreen * PHASES.size());

        // 分配绿灯时长
        Map<String, Integer> timingPlan = new LinkedHashMap<>();
        int totalGreen = 0;
        for (String phase : PHASES) {
            double y = flowRatio.get(phase);
            int gt = (int) Math.round((y / totalY) * effectiveGreen);
            gt = Math.max(gt, minGreen);
            timingPlan.put(phase, gt);
            totalGreen += gt;
        }

        // 校准总绿灯时长
        if (totalGreen > effectiveGreen) {
            double scale = effectiveGreen * 1.0 / totalGreen;
            for (String phase : PHASES) {
                int gt = (int) Math.round(timingPlan.get(phase) * scale);
                gt = Math.max(gt, minGreen);
                timingPlan.put(phase, gt);
            }
        }

        // 计算延误
        double delay = 0.0;
        if (totalY < 1) {
            double numerator = 0.5 * cycle * Math.pow(1 - totalY, 2);
            double denominator = 1 - totalY * (cycle / optimalCycle);
            if (denominator > 0) {
                delay = numerator / denominator;
            }
        }
        delay = Math.max(0, 8.0 + delay);
        delay = Math.round(delay * 100) / 100.0;

        // 计算通行能力 + 限制 0~100%
        double capacity = SATURATION_FLOW * totalY * (cycle - totalLostTime) / (double) cycle;
        capacity = capacity / 20;
        capacity = Math.max(0, Math.min(100, capacity));
        capacity = Math.round(capacity * 10) / 10.0;

        return new AlgorithmOutput(intersectionId, cycle, YELLOW_TIME, timingPlan, delay, capacity);
    }
}
