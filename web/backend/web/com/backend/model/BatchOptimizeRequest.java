package com.backend.model;

import java.util.List;

public class BatchOptimizeRequest {
    private List<BatchItem> intersectionList;

    public static class BatchItem {
        private String intersectionId;
        private TrafficFlow trafficFlow;

        public String getIntersectionId() { return intersectionId; }
        public void setIntersectionId(String intersectionId) { this.intersectionId = intersectionId; }
        public TrafficFlow getTrafficFlow() { return trafficFlow; }
        public void setTrafficFlow(TrafficFlow trafficFlow) { this.trafficFlow = trafficFlow; }
    }

    public List<BatchItem> getIntersectionList() { return intersectionList; }
    public void setIntersectionList(List<BatchItem> intersectionList) { this.intersectionList = intersectionList; }
}