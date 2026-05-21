package web.backend.entity;

public class Intersection {
    private String intersectionId;
    private String name;
    private String code;
    private String region;
    private Integer laneCount;

    public Intersection() {}

    public String getIntersectionId() {
        return intersectionId;
    }

    public void setIntersectionId(String intersectionId) {
        this.intersectionId = intersectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getLaneCount() {
        return laneCount;
    }

    public void setLaneCount(Integer laneCount) {
        this.laneCount = laneCount;
    }
}