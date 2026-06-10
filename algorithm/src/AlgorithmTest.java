import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AlgorithmTest {
    private static final String ROADNET_PATH = "data/roadnet_4_4.json";
    private static final String ANON_FILE_1 = "data/anon_4_4_hangzhou_real.json";
    private static final String ANON_FILE_2 = "data/anon_4_4_hangzhou_real_5734.json";
    private static final String ANON_FILE_3 = "data/anon_4_4_hangzhou_real_5816.json";

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        AlgorithmService service = new AlgorithmService();

        System.out.println("========== 1. 正常单路口测试（常规流量）==========");
        testNormalSingle(service);

        System.out.println("\n========== 2. 边界测试：单相位有流量，其余为0 ==========");
        testBoundarySinglePhase(service);

        System.out.println("\n========== 3. 异常测试：所有相位流量 = 0 ==========");
        testAllZeroFlow(service);

        System.out.println("\n========== 4. 异常测试：非法参数（最小绿灯过大）==========");
        testInvalidMinGreen(service);

        System.out.println("\n========== 5. 批量功能测试（3个路口）==========");
        testBatchNormal(service);

        System.out.println("\n========== 6. 性能测试（10个路口，校验耗时<1000ms）==========");
        testPerformance(service);

        System.out.println("\n========== 7. 真实路网+3份车辆数据测试 ==========");
        testWithRealJsonData(service);

        service.shutdown();
    }

    private static void testNormalSingle(AlgorithmService service) {
        Map<String, Integer> flow = new LinkedHashMap<>();
        flow.put(AlgorithmInput.PHASE_EW_STRAIGHT, 420);
        flow.put(AlgorithmInput.PHASE_EW_LEFT, 130);
        flow.put(AlgorithmInput.PHASE_NS_STRAIGHT, 550);
        flow.put(AlgorithmInput.PHASE_NS_LEFT, 100);

        AlgorithmInput input = new AlgorithmInput("test_001", flow, 15, 120);
        Result<AlgorithmOutput> out = service.singleOptimize(input);
        printResult(out);
    }

    private static void testBoundarySinglePhase(AlgorithmService service) {
        Map<String, Integer> flow = new LinkedHashMap<>();
        flow.put(AlgorithmInput.PHASE_EW_STRAIGHT, 600);
        flow.put(AlgorithmInput.PHASE_EW_LEFT, 0);
        flow.put(AlgorithmInput.PHASE_NS_STRAIGHT, 0);
        flow.put(AlgorithmInput.PHASE_NS_LEFT, 0);

        AlgorithmInput input = new AlgorithmInput("test_002", flow, 15, 120);
        Result<AlgorithmOutput> out = service.singleOptimize(input);
        printResult(out);
    }

    private static void testAllZeroFlow(AlgorithmService service) {
        Map<String, Integer> flow = new LinkedHashMap<>();
        flow.put(AlgorithmInput.PHASE_EW_STRAIGHT, 0);
        flow.put(AlgorithmInput.PHASE_EW_LEFT, 0);
        flow.put(AlgorithmInput.PHASE_NS_STRAIGHT, 0);
        flow.put(AlgorithmInput.PHASE_NS_LEFT, 0);

        AlgorithmInput input = new AlgorithmInput("test_003", flow, 15, 120);
        Result<AlgorithmOutput> out = service.singleOptimize(input);
        printResult(out);
    }

    private static void testInvalidMinGreen(AlgorithmService service) {
        Map<String, Integer> flow = new LinkedHashMap<>();
        flow.put(AlgorithmInput.PHASE_EW_STRAIGHT, 420);
        flow.put(AlgorithmInput.PHASE_EW_LEFT, 130);
        flow.put(AlgorithmInput.PHASE_NS_STRAIGHT, 550);
        flow.put(AlgorithmInput.PHASE_NS_LEFT, 100);

        try {
            AlgorithmInput input = new AlgorithmInput("test_004", flow, 70, 120);
            Result<AlgorithmOutput> out = service.singleOptimize(input);
            printResult(out);
        } catch (IllegalArgumentException e) {
            System.out.println("参数校验拦截成功：" + e.getMessage());
        }
    }

    private static void testBatchNormal(AlgorithmService service) {
        List<AlgorithmInput> inputs = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Integer> flow = new LinkedHashMap<>();
            flow.put(AlgorithmInput.PHASE_EW_STRAIGHT, 400 + i * 50);
            flow.put(AlgorithmInput.PHASE_EW_LEFT, 100 + i * 20);
            flow.put(AlgorithmInput.PHASE_NS_STRAIGHT, 500 + i * 30);
            flow.put(AlgorithmInput.PHASE_NS_LEFT, 100);
            inputs.add(new AlgorithmInput("batch_" + i, flow, 15, 120));
        }

        BatchOptRequest req = new BatchOptRequest();
        req.setIntersectionIds(new ArrayList<>());
        req.setBasePhaseFlow(inputs.get(0).getPhaseFlow());
        req.setMinGreen(15);
        req.setMaxCycle(120);
        for (AlgorithmInput input : inputs) {
            req.getIntersectionIds().add(input.getIntersectionId());
        }

        Result<List<AlgorithmOutput>> outs = service.batchOptimize(req);
        if (outs.getCode() == 200 && outs.getData() != null) {
            for (AlgorithmOutput out : outs.getData()) {
                printResult(Result.success(out));
            }
        } else {
            System.err.println("批量计算失败：" + outs.getMsg());
        }
    }

    private static void testPerformance(AlgorithmService service) {
        List<AlgorithmInput> inputList = new ArrayList<>();
        Map<String, Integer> flow = new LinkedHashMap<>();
        flow.put(AlgorithmInput.PHASE_EW_STRAIGHT, 420);
        flow.put(AlgorithmInput.PHASE_EW_LEFT, 130);
        flow.put(AlgorithmInput.PHASE_NS_STRAIGHT, 550);
        flow.put(AlgorithmInput.PHASE_NS_LEFT, 100);

        for (int i = 1; i <= 10; i++) {
            inputList.add(new AlgorithmInput("intersection_" + i, flow, 15, 120));
        }

        BatchOptRequest req = new BatchOptRequest();
        req.setIntersectionIds(new ArrayList<>());
        req.setBasePhaseFlow(flow);
        req.setMinGreen(15);
        req.setMaxCycle(120);
        for (AlgorithmInput input : inputList) {
            req.getIntersectionIds().add(input.getIntersectionId());
        }

        long start = System.currentTimeMillis();
        Result<List<AlgorithmOutput>> result = service.batchOptimize(req);
        long cost = System.currentTimeMillis() - start;

        System.out.println("共计算路口数：" + (result.getData() != null ? result.getData().size() : 0));
        System.out.println("批量计算耗时：" + cost + " ms");
        System.out.println("性能是否达标(<1000ms)：" + (cost < 1000));
        System.out.println("----------------------------------------");
    }

    // 7. 真实路网+3份车辆数据测试（修复：逐个路口独立计算）
    // 7. 真实路网+3份车辆数据测试（修复打印统计问题）
    private static void testWithRealJsonData(AlgorithmService service) {
        try {
            JSONDataLoader dataLoader = new JSONDataLoader();
            RoadPhaseMapper mapper = new RoadPhaseMapper();

            JsonNode root = objectMapper.readTree(new File(ROADNET_PATH));
            JsonNode interArr = root.get("intersections");
            if (interArr == null || !interArr.isArray()) {
                System.err.println("路网文件解析失败");
                return;
            }

            Map<String, Integer> roadTotalFlow = dataLoader.loadCombinedFlow(
                    ANON_FILE_1,
                    ANON_FILE_2,
                    ANON_FILE_3
            );
            System.out.println("合并后有流量的道路总数：" + roadTotalFlow.size());

            List<AlgorithmInput> realInputList = new ArrayList<>();
            // 放宽约束：最小绿灯8s，最大周期180s
            int minGreen = 8;
            int maxCycle = 180;

            // 遍历每个路口，单独计算
            for (JsonNode interNode : interArr) {
                String interId = interNode.get("id").asText();
                Map<String, Integer> phaseFlow = mapper.buildPhaseFlow(interId, roadTotalFlow, interNode);
                System.out.println("【" + interId + "】原始相位流量：" + phaseFlow);

                AlgorithmInput input = new AlgorithmInput(interId, phaseFlow, minGreen, maxCycle);
                realInputList.add(input); // 把对象加入列表，修复统计为0问题

                // 逐个调用单路口优化接口
                Result<AlgorithmOutput> singleResult = service.singleOptimize(input);
                printResult(singleResult);
            }

            // 现在可以正确打印路口总数
            System.out.println("从路网文件读取到路口总数：" + realInputList.size());

        } catch (Exception e) {
            System.err.println("读取/解析JSON数据集异常：" + e.getMessage());
        }
    }

    private static void printResult(Result<AlgorithmOutput> out) {
        if (out.getCode() == 200 && out.getData() != null) {
            AlgorithmOutput output = out.getData();
            System.out.println("路口ID：" + output.getIntersection_id());
            System.out.println("运行状态：成功");
            System.out.println("信号周期：" + output.getCycle_length() + " s");
            System.out.println("黄灯时长：" + output.getYellow_time() + " s");
            System.out.println("各相位绿灯：" + output.getTiming_plan());
            System.out.println("平均延误：" + output.getAverage_delay() + " s");
            System.out.println("通行能力：" + output.getCapacity() + "%");
        } else {
            System.out.println("运行状态：失败");
            System.out.println("错误信息：" + out.getMsg());
        }
        System.out.println("----------------------------------------");
    }
}
