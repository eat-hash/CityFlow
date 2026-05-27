import java.util.List;

public class AlgorithmService {
    private final TrafficSignalOptimizer optimizer;

    // 构造方法初始化优化器
    public AlgorithmService() {
        this.optimizer = new TrafficSignalOptimizer();
    }

    // 单路口优化接口
    public AlgorithmOutput optimizeSingle(AlgorithmInput input) {
        // 前置校验：非法输入检查
        if (input == null) {
            return AlgorithmOutput.error("输入参数不能为空");
        }
        if (input.getPhaseFlow() == null || input.getPhaseFlow().isEmpty()) {
            return AlgorithmOutput.error("相位流量数据不能为空");
        }
        if (input.getMinGreen() <= 0 || input.getMaxCycle() <= 0) {
            return AlgorithmOutput.error("最小绿灯/最大周期必须大于0");
        }
        if (input.getMaxCycle() - 12 < input.getMinGreen() * 4) {
            return AlgorithmOutput.error("最大周期过小，无法满足最小绿灯时长");
        }

        // 调用核心算法
        try {
            return optimizer.computeTimingPlan(input);
        } catch (Exception e) {
            return AlgorithmOutput.error("计算失败：" + e.getMessage());
        }
    }

    // 批量路口优化接口
    public List<AlgorithmOutput> optimizeBatch(List<AlgorithmInput> inputs) {
        if (inputs == null || inputs.isEmpty()) {
            throw new IllegalArgumentException("批量输入列表不能为空");
        }

        List<AlgorithmOutput> results = new java.util.ArrayList<>();
        for (AlgorithmInput input : inputs) {
            results.add(optimizeSingle(input));
        }
        return results;
    }
}
