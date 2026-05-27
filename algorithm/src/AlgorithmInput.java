import java.util.Map;

public class AlgorithmInput {
    private String intersectionId;
    private Map<String, Integer> phaseFlow;
    private int minGreen;
    private int maxCycle;

    public AlgorithmInput(String intersectionId,
                          Map<String, Integer> phaseFlow,
                          int minGreen,
                          int maxCycle) {
        this.intersectionId = intersectionId;
        this.phaseFlow = phaseFlow;
        this.minGreen = minGreen;
        this.maxCycle = maxCycle;
    }

    public String getIntersectionId() {
        return intersectionId;
    }

    public Map<String, Integer> getPhaseFlow() {
        return phaseFlow;
    }

    public int getMinGreen() {
        return minGreen;
    }

    public int getMaxCycle() {
        return maxCycle;
    }
}