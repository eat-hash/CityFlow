import java.util.ArrayList;
import java.util.List;

/**
 * 第三周新增：批量路口优化器
 * 批量遍历调用单路口算法，解耦批量逻辑
 */
public class BatchOptimizer {
    // 复用单路口优化实例
    private final TrafficSignalOptimizer singleOpt = new TrafficSignalOptimizer();

    /**
     * 批量配时计算
     * @param inputList 批量路口入参
     * @return 批量优化结果
     */
    public List<AlgorithmOutput> batchOptimize(List<AlgorithmInput> inputList) {
        List<AlgorithmOutput> resultList = new ArrayList<>(inputList.size());
        for (AlgorithmInput input : inputList) {
            resultList.add(singleOpt.computeTimingPlan(input));
        }
        return resultList;
    }
}