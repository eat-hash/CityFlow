package backend.src.entity;

import java.sql.Timestamp;

public class TimingPlan {
    private int id;
    private String name;
    private int intersectionId;
    private int phaseCount;
    private int cycle;
    private int minGreen;
    private int maxCycle;
    private int status;
    private Timestamp createTime;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getIntersectionId() { return intersectionId; }
    public void setIntersectionId(int intersectionId) { this.intersectionId = intersectionId; }
    public int getPhaseCount() { return phaseCount; }
    public void setPhaseCount(int phaseCount) { this.phaseCount = phaseCount; }
    public int getCycle() { return cycle; }
    public void setCycle(int cycle) { this.cycle = cycle; }
    public int getMinGreen() { return minGreen; }
    public void setMinGreen(int minGreen) { this.minGreen = minGreen; }
    public int getMaxCycle() { return maxCycle; }
    public void setMaxCycle(int maxCycle) { this.maxCycle = maxCycle; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }
}