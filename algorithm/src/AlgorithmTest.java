import java.util.*;

public class AlgorithmTest {
    public static void main(String[] args) {
        AlgorithmService service = new AlgorithmService();

        System.out.println("===== 测试1：正常单路口优化 =====");
        normalTest(service);

        System.out.println("\n===== 测试2：批量路口优化 =====");
        batchTest(service);

        System.out.println("\n===== 测试3：异常输入（流量为0） =====");
        zeroFlowTest(service);

        System.out.println("\n===== 测试4：异常输入（参数非法） =====");
        invalidParamTest(service);

        // ==================== 第三周新增：性能测试（10路口测速） ====================
        System.out.println("\n===== 测试5：10路口性能测试（第三周要求） =====");
        performanceTest(service);
    }

    // 正常单路口测试（原样保留）
    private static void normalTest(AlgorithmService service) {
        Map<String, Integer> flow = new HashMap<>();
        flow.put("EAST_WEST_STRAIGHT", 420);
        flow.put("EAST_WEST_LEFT", 130);
        flow.put("NORTH_SOUTH_STRAIGHT", 550);
        flow.put("NORTH_SOUTH_LEFT", 100);

        AlgorithmInput input = new AlgorithmInput("test_001", flow, 15, 120);
        AlgorithmOutput out = service.optimizeSingle(input);

        printResult(out);
    }

    // 批量测试（原样保留）
    private static void batchTest(AlgorithmService service) {
        List<AlgorithmInput> inputs = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Integer> flow = new HashMap<>();
            flow.put("EAST_WEST_STRAIGHT", 400 + i * 50);
            flow.put("EAST_WEST_LEFT", 100 + i * 20);
            flow.put("NORTH_SOUTH_STRAIGHT", 500 + i * 30);
            flow.put("NORTH_SOUTH_LEFT", 100);

            inputs.add(new AlgorithmInput("batch_" + i, flow, 15, 120));
        }

        List<AlgorithmOutput> outs = service.optimizeBatch(inputs);
        for (AlgorithmOutput out : outs) {
            printResult(out);
        }
    }

    // 流量为0的异常测试（原样保留）
    private static void zeroFlowTest(AlgorithmService service) {
        Map<String, Integer> flow = new HashMap<>();
        flow.put("EAST_WEST_STRAIGHT", 0);
        flow.put("EAST_WEST_LEFT", 0);
        flow.put("NORTH_SOUTH_STRAIGHT", 0);
        flow.put("NORTH_SOUTH_LEFT", 0);

        AlgorithmInput input = new AlgorithmInput("test_002", flow, 15, 120);
        AlgorithmOutput out = service.optimizeSingle(input);
        printResult(out);
    }

    // 参数非法测试（原样保留）
    private static void invalidParamTest(AlgorithmService service) {
        Map<String, Integer> flow = new HashMap<>();
        flow.put("EAST_WEST_STRAIGHT", 420);
        flow.put("EAST_WEST_LEFT", 130);

        AlgorithmInput input = new AlgorithmInput("test_003", flow, 15, 30);
        AlgorithmOutput out = service.optimizeSingle(input);
        printResult(out);
    }

    // ==================== 第三周新增：10路口性能测速 ====================
    private static void performanceTest(AlgorithmService service) {
        // 构造10个路口
        List<AlgorithmInput> inputList = new ArrayList<>();
        Map<String, Integer> flow = new HashMap<>();
        flow.put("EAST_WEST_STRAIGHT", 420);
        flow.put("EAST_WEST_LEFT", 130);
        flow.put("NORTH_SOUTH_STRAIGHT", 550);
        flow.put("NORTH_SOUTH_LEFT", 100);

        for (int i = 1; i <= 10; i++) {
            inputList.add(new AlgorithmInput("intersection_" + i, flow, 15, 120));
        }

        // 计时开始
        long start = System.currentTimeMillis();
        List<AlgorithmOutput> result = service.optimizeBatch(inputList);
        long cost = System.currentTimeMillis() - start;

        System.out.println("✅ 10个路口批量计算完成");
        System.out.println("✅ 耗时：" + cost + " ms");
        System.out.println("✅ 是否达标（<1000ms）：" + (cost < 1000));
        System.out.println("------------------------");
    }

    // 打印结果（原样保留）
    private static void printResult(AlgorithmOutput out) {
        System.out.println("状态：" + out.getStatus());
        System.out.println("路口ID：" + out.getIntersectionId());
        if ("success".equals(out.getStatus())) {
            System.out.println("周期：" + out.getCycleLength() + "s");
            System.out.println("配时：" + out.getTimingPlan());
        } else {
            System.out.println("错误信息：" + out.getMessage());
        }
        System.out.println("------------------------");
    }
}
