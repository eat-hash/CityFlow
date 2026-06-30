import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AlgorithmService {
    private final TrafficSignalOptimizer optimizer;
    private final BatchOptimizer batchOptimizer;

    public AlgorithmService() {
        this.optimizer = new TrafficSignalOptimizer();
        this.batchOptimizer = new BatchOptimizer(optimizer);
    }

    // ===================== 1. 获取测试数据接口 =====================
    public Result<List<AlgorithmInput>> getTestData() {
        List<AlgorithmInput> dataList = new ArrayList<>();
        // 构造几个样例
        Map<String, Integer> flow1 = new LinkedHashMap<>();
        flow1.put(AlgorithmInput.PHASE_EW_STRAIGHT, 420);
        flow1.put(AlgorithmInput.PHASE_EW_LEFT, 130);
        flow1.put(AlgorithmInput.PHASE_NS_STRAIGHT, 550);
        flow1.put(AlgorithmInput.PHASE_NS_LEFT, 100);
        dataList.add(new AlgorithmInput("test_001", flow1, 15, 120));

        Map<String, Integer> flow2 = new LinkedHashMap<>();
        flow2.put(AlgorithmInput.PHASE_EW_STRAIGHT, 450);
        flow2.put(AlgorithmInput.PHASE_EW_LEFT, 120);
        flow2.put(AlgorithmInput.PHASE_NS_STRAIGHT, 530);
        flow2.put(AlgorithmInput.PHASE_NS_LEFT, 100);
        dataList.add(new AlgorithmInput("batch_1", flow2, 15, 120));

        Map<String, Integer> flow3 = new LinkedHashMap<>();
        flow3.put(AlgorithmInput.PHASE_EW_STRAIGHT, 500);
        flow3.put(AlgorithmInput.PHASE_EW_LEFT, 140);
        flow3.put(AlgorithmInput.PHASE_NS_STRAIGHT, 560);
        flow3.put(AlgorithmInput.PHASE_NS_LEFT, 100);
        dataList.add(new AlgorithmInput("batch_2", flow3, 15, 120));

        Map<String, Integer> flow4 = new LinkedHashMap<>();
        flow4.put(AlgorithmInput.PHASE_EW_STRAIGHT, 550);
        flow4.put(AlgorithmInput.PHASE_EW_LEFT, 160);
        flow4.put(AlgorithmInput.PHASE_NS_STRAIGHT, 590);
        flow4.put(AlgorithmInput.PHASE_NS_LEFT, 100);
        dataList.add(new AlgorithmInput("batch_3", flow4, 15, 120));

        Map<String, Integer> flow5 = new LinkedHashMap<>();
        flow5.put(AlgorithmInput.PHASE_EW_STRAIGHT, 0);
        flow5.put(AlgorithmInput.PHASE_EW_LEFT, 0);
        flow5.put(AlgorithmInput.PHASE_NS_STRAIGHT, 0);
        flow5.put(AlgorithmInput.PHASE_NS_LEFT, 0);
        dataList.add(new AlgorithmInput("test_002", flow5, 15, 120));

        return Result.success(dataList);
    }

    // ===================== 2. 单路口优化接口 =====================
    public Result<AlgorithmOutput> singleOptimize(AlgorithmInput input) {
        try {
            AlgorithmOutput output = optimizer.computeTimingPlan(input);
            return Result.success(output);
        } catch (RuntimeException e) {
            return Result.fail(400, e.getMessage());
        } catch (Exception e) {
            return Result.fail(500, "服务内部异常");
        }
    }

    // ===================== 3. 批量优化接口（兼容当前 BatchOptRequest）=====================
    public Result<List<AlgorithmOutput>> batchOptimize(BatchOptRequest request) {
        try {
            // 校验入参
            if (request.getIntersectionIds() == null || request.getIntersectionIds().isEmpty()) {
                return Result.fail(400, "路口ID列表不能为空");
            }
            if (request.getBasePhaseFlow() == null) {
                return Result.fail(400, "基础相位流量不能为空");
            }

            List<AlgorithmInput> inputList = new ArrayList<>();
            List<String> ids = request.getIntersectionIds();
            Map<String, Integer> baseFlow = request.getBasePhaseFlow();
            int minG = request.getMinGreen();
            int maxC = request.getMaxCycle();

            // 为每个路口复制一份基础流量（和当前前端接口兼容）
            for (String id : ids) {
                inputList.add(new AlgorithmInput(id, new LinkedHashMap<>(baseFlow), minG, maxC));
            }

            List<AlgorithmOutput> outputList = batchOptimizer.batchOptimize(inputList);
            return Result.success(outputList);
        } catch (RuntimeException e) {
            return Result.fail(400, e.getMessage());
        } catch (Exception e) {
            return Result.fail(500, "批量计算服务异常");
        }
    }

    // 关闭线程池
    public void shutdown() {
        batchOptimizer.shutdown();
    }
}
