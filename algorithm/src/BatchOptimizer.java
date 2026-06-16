import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BatchOptimizer {
    private final TrafficSignalOptimizer singleOpt;
    private final ExecutorService executor;

    public BatchOptimizer(TrafficSignalOptimizer optimizer) {
        this.singleOpt = optimizer;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public List<AlgorithmOutput> batchOptimize(List<AlgorithmInput> inputList) {
        if (inputList == null || inputList.isEmpty()) {
            return Collections.emptyList();
        }

        // 小批量直接串行
        if (inputList.size() < 5) {
            return inputList.stream()
                    .map(singleOpt::computeTimingPlan)
                    .collect(Collectors.toList());
        }

        // 大批量用线程池并行计算
        try {
            List<Callable<AlgorithmOutput>> tasks = new ArrayList<>();
            for (AlgorithmInput input : inputList) {
                tasks.add(() -> singleOpt.computeTimingPlan(input));
            }

            return executor.invokeAll(tasks).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new RuntimeException("批量任务执行失败", e);
                        }
                    })
                    .collect(Collectors.toList());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("批量计算被中断", e);
        }
    }

    // 关闭线程池（建议在程序退出时调用，避免资源泄漏）
    public void shutdown() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
