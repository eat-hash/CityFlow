package backend.src.entity;

import java.sql.Timestamp;

public class TrafficData {
    private int dataId;
    private String intersectionId;
    private Timestamp collectTime;
    private String laneFlow;

    public int getDataId() { return dataId; }
    public void setDataId(int dataId) { this.dataId = dataId; }
    public String getIntersectionId() { return intersectionId; }
    public void setIntersectionId(String intersectionId) { this.intersectionId = intersectionId; }
    public Timestamp getCollectTime() { return collectTime; }
    public void setCollectTime(Timestamp collectTime) { this.collectTime = collectTime; }
    public String getLaneFlow() { return laneFlow; }
    public void setLaneFlow(String laneFlow) { this.laneFlow = laneFlow; }
}