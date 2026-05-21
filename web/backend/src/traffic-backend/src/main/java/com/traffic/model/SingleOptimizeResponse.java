package com.traffic.model;

public class SingleOptimizeResponse {
    private String intersectionId;
    private int cycleLength;
    private int yellowTime = 3;
    private TrafficFlow timingPlan;
    private double averageDelay;
    private double capacity;

    // getter/setter
    public String getIntersectionId() { return intersectionId; }
    public void setIntersectionId(String intersectionId) { this.intersectionId = intersectionId; }
    public int getCycleLength() { return cycleLength; }
    public void setCycleLength(int cycleLength) { this.cycleLength = cycleLength; }
    public int getYellowTime() { return yellowTime; }
    public void setYellowTime(int yellowTime) { this.yellowTime = yellowTime; }
    public TrafficFlow getTimingPlan() { return timingPlan; }
    public void setTimingPlan(TrafficFlow timingPlan) { this.timingPlan = timingPlan; }
    public double getAverageDelay() { return averageDelay; }
    public void setAverageDelay(double averageDelay) { this.averageDelay = averageDelay; }
    public double getCapacity() { return capacity; }
    public void setCapacity(double capacity) { this.capacity = capacity; }
}