import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlgorithmService {
    private final TrafficSignalOptimizer optimizer;
    // 第三周新增：批量优化实例，剥离循环逻辑
    private final BatchOptimizer batchOptimizer;

    // 构造方法初始化优化器、批量处理器
    public AlgorithmService() {
        this.optimizer = new TrafficSignalOptimizer();
        this.batchOptimizer = new BatchOptimizer();
    }

    // 单路口优化接口（原有代码完全不变）
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

    // 批量路口优化：改用BatchOptimizer实现
    public List<AlgorithmOutput> optimizeBatch(List<AlgorithmInput> inputs) {
        if (inputs == null || inputs.isEmpty()) {
            throw new IllegalArgumentException("批量输入列表不能为空");
        }
        return batchOptimizer.batchOptimize(inputs);
    }

    // ==================== 【第三周新增重载接口 原样保留】 ====================
    public AlgorithmOutput optimizeSingle(String intersectionId,
                                          Map<String, Integer> phaseFlow,
                                          int minGreen,
                                          int maxCycle) {
        AlgorithmInput input = new AlgorithmInput(intersectionId, phaseFlow, minGreen, maxCycle);
        return optimizeSingle(input);
    }

    public List<AlgorithmOutput> optimizeBatch(List<String> intersectionIds,
                                               Map<String, Integer> baseFlow,
                                               int minGreen,
                                               int maxCycle) {
        List<AlgorithmInput> inputList = new ArrayList<>();
        for (String id : intersectionIds) {
            inputList.add(new AlgorithmInput(id, baseFlow, minGreen, maxCycle));
        }
        return optimizeBatch(inputList);
    }
}
