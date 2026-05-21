package web.backend.entity;
import java.util.Date;

public class TrafficData {
    private Integer dataId;
    private String intersectionId;
    private Date collectTime;
    private String laneFlow;

    public TrafficData() {}

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public String getIntersectionId() {
        return intersectionId;
    }

    public void setIntersectionId(String intersectionId) {
        this.intersectionId = intersectionId;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public String getLaneFlow() {
        return laneFlow;
    }

    public void setLaneFlow(String laneFlow) {
        this.laneFlow = laneFlow;
    }
}