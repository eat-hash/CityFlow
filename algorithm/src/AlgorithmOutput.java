import java.util.Map;

/**
 * 单路口算法输出实体（严格对齐前端接口字段）
 */
public class AlgorithmOutput {
    private String intersection_id;
    private int cycle_length;
    private int yellow_time;
    private Map<String, Integer> timing_plan;
    private double average_delay;
    private double capacity;
    private String status;

    // 成功构造
    public AlgorithmOutput(String intersectionId, int cycle, int yellowTime,
                           Map<String, Integer> timingPlan, double delay, double capacity) {
        this.intersection_id = intersectionId;
        this.cycle_length = cycle;
        this.yellow_time = yellowTime;
        this.timing_plan = timingPlan;
        this.average_delay = delay;
        this.capacity = capacity;
        this.status = "success";
    }

    // getter & setter （JSON序列化必备）
    public String getIntersection_id() {
        return intersection_id;
    }

    public void setIntersection_id(String intersection_id) {
        this.intersection_id = intersection_id;
    }

    public int getCycle_length() {
        return cycle_length;
    }

    public void setCycle_length(int cycle_length) {
        this.cycle_length = cycle_length;
    }

    public int getYellow_time() {
        return yellow_time;
    }

    public void setYellow_time(int yellow_time) {
        this.yellow_time = yellow_time;
    }

    public Map<String, Integer> getTiming_plan() {
        return timing_plan;
    }

    public void setTiming_plan(Map<String, Integer> timing_plan) {
        this.timing_plan = timing_plan;
    }

    public double getAverage_delay() {
        return average_delay;
    }

    public void setAverage_delay(double average_delay) {
        this.average_delay = average_delay;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
