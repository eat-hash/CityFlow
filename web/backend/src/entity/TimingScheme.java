package web.backend.entity;

public class TimingScheme {
    private Integer schemeId;
    private String intersectionId;
    private Integer cycleLength;
    private String phaseGreen;
    private String optimizeEffect;

    public TimingScheme() {}

    public Integer getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(Integer schemeId) {
        this.schemeId = schemeId;
    }

    public String getIntersectionId() {
        return intersectionId;
    }

    public void setIntersectionId(String intersectionId) {
        this.intersectionId = intersectionId;
    }

    public Integer getCycleLength() {
        return cycleLength;
    }

    public void setCycleLength(Integer cycleLength) {
        this.cycleLength = cycleLength;
    }

    public String getPhaseGreen() {
        return phaseGreen;
    }

    public void setPhaseGreen(String phaseGreen) {
        this.phaseGreen = phaseGreen;
    }

    public String getOptimizeEffect() {
        return optimizeEffect;
    }

    public void setOptimizeEffect(String optimizeEffect) {
        this.optimizeEffect = optimizeEffect;
    }
}