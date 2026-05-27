package backend.entity;

import java.util.Date;

public class TimingPlan {
    private Integer id;
    private String name;
    private Integer intersectionId;
    private Integer phaseCount;
    private Integer cycle;
    private Integer minGreen;
    private Integer maxCycle;
    private Integer status;
    private Date createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getIntersectionId() { return intersectionId; }
    public void setIntersectionId(Integer intersectionId) { this.intersectionId = intersectionId; }

    public Integer getPhaseCount() { return phaseCount; }
    public void setPhaseCount(Integer phaseCount) { this.phaseCount = phaseCount; }

    public Integer getCycle() { return cycle; }
    public void setCycle(Integer cycle) { this.cycle = cycle; }

    public Integer getMinGreen() { return minGreen; }
    public void setMinGreen(Integer minGreen) { this.minGreen = minGreen; }

    public Integer getMaxCycle() { return maxCycle; }
    public void setMaxCycle(Integer maxCycle) { this.maxCycle = maxCycle; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}