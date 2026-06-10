import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        AlgorithmService service = new AlgorithmService();

        // 1. 测试：获取测试数据
        printTestData(service);

        // 2. 测试：单路口优化
        printSingleOptimizeResult(service);

        // 关闭线程池
        service.shutdown();
    }

    private static void printTestData(AlgorithmService service) {
        System.out.println("===== 测试：获取测试数据 =====");
        Result<List<AlgorithmInput>> testDataResult = service.getTestData();
        if (testDataResult.getCode() == 200) {
            System.out.println("获取测试数据成功，共" + testDataResult.getData().size() + "条");
        } else {
            System.out.println("获取失败：" + testDataResult.getMsg());
        }
    }

    private static void printSingleOptimizeResult(AlgorithmService service) {
        System.out.println("\n===== 测试：单路口优化 =====");
        Map<String, Integer> flow = new LinkedHashMap<>();
        flow.put(AlgorithmInput.PHASE_EW_STRAIGHT, 420);
        flow.put(AlgorithmInput.PHASE_EW_LEFT, 130);
        flow.put(AlgorithmInput.PHASE_NS_STRAIGHT, 550);
        flow.put(AlgorithmInput.PHASE_NS_LEFT, 100);

        AlgorithmInput input = new AlgorithmInput("test_001", flow, 15, 120);
        Result<AlgorithmOutput> result = service.singleOptimize(input);

        if (result.getCode() == 200 && result.getData() != null) {
            AlgorithmOutput output = result.getData();
            System.out.println("路口ID：" + output.getIntersection_id());
            System.out.println("信号周期：" + output.getCycle_length() + "s");
            System.out.println("黄灯时间：" + output.getYellow_time() + "s");
            System.out.println("各相位绿灯：" + output.getTiming_plan());
            System.out.println("平均延误：" + output.getAverage_delay() + "s");
            System.out.println("通行能力：" + output.getCapacity() + "%");
            System.out.println("状态：" + output.getStatus());
        } else {
            System.err.println("计算失败：" + result.getMsg());
        }
    }
}