import java.util.List;
import java.util.Map;

public class BatchOptRequest {
    private List<String> intersectionIds;
    private Map<String, Integer> basePhaseFlow;
    private int minGreen;
    private int maxCycle;

    public List<String> getIntersectionIds() {
        return intersectionIds;
    }

    public void setIntersectionIds(List<String> intersectionIds) {
        this.intersectionIds = intersectionIds;
    }

    public Map<String, Integer> getBasePhaseFlow() {
        return basePhaseFlow;
    }

    public void setBasePhaseFlow(Map<String, Integer> basePhaseFlow) {
        this.basePhaseFlow = basePhaseFlow;
    }

    public int getMinGreen() {
        return minGreen;
    }

    public void setMinGreen(int minGreen) {
        this.minGreen = minGreen;
    }

    public int getMaxCycle() {
        return maxCycle;
    }

    public void setMaxCycle(int maxCycle) {
        this.maxCycle = maxCycle;
    }
}