import java.util.Map;

public class AlgorithmOutput {
    private String status;
    private String message;
    private String intersectionId;
    private int cycleLength;
    private int yellowTime;
    private Map<String, Integer> timingPlan;
    private double averageDelay;
    private double capacity;

    // 1. 成功时的6参数构造器（和你TrafficSignalOptimizer里调用的完全匹配）
    public AlgorithmOutput(
            String intersectionId,
            int cycleLength,
            int yellowTime,
            Map<String, Integer> timingPlan,
            double averageDelay,
            double capacity
    ) {
        this.status = "success";
        this.intersectionId = intersectionId;
        this.cycleLength = cycleLength;
        this.yellowTime = yellowTime;
        this.timingPlan = timingPlan;
        this.averageDelay = averageDelay;
        this.capacity = capacity;
        this.message = null;
    }

    // 2. 失败时的静态工厂方法
    public static AlgorithmOutput error(String message) {
        AlgorithmOutput output = new AlgorithmOutput();
        output.setStatus("error");
        output.setMessage(message);
        return output;
    }

    // 无参构造器（必须有，否则上面的error方法会报错）
    public AlgorithmOutput() {}

    // Getter & Setter（必须补全，否则测试类调用时会继续报红）
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getIntersectionId() { return intersectionId; }
    public int getCycleLength() { return cycleLength; }
    public int getYellowTime() { return yellowTime; }
    public Map<String, Integer> getTimingPlan() { return timingPlan; }
    public double getAverageDelay() { return averageDelay; }
    public double getCapacity() { return capacity; }
}