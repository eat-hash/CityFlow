package com.backend.model;

public class SingleOptimizeRequest {
    private String intersectionId;
    private TrafficFlow trafficFlow;
    private int minGreen;
    private int maxCycle;

    // getter/setter
    public String getIntersectionId() { return intersectionId; }
    public void setIntersectionId(String intersectionId) { this.intersectionId = intersectionId; }
    public TrafficFlow getTrafficFlow() { return trafficFlow; }
    public void setTrafficFlow(TrafficFlow trafficFlow) { this.trafficFlow = trafficFlow; }
    public int getMinGreen() { return minGreen; }
    public void setMinGreen(int minGreen) { this.minGreen = minGreen; }
    public int getMaxCycle() { return maxCycle; }
    public void setMaxCycle(int maxCycle) { this.maxCycle = maxCycle; }
}