package com.traffic.model;

public class TrafficFlow {
    private int eastWestStraight;
    private int eastWestLeft;
    private int northSouthStraight;
    private int northSouthLeft;

    // getter/setter
    public int getEastWestStraight() { return eastWestStraight; }
    public void setEastWestStraight(int eastWestStraight) { this.eastWestStraight = eastWestStraight; }
    public int getEastWestLeft() { return eastWestLeft; }
    public void setEastWestLeft(int eastWestLeft) { this.eastWestLeft = eastWestLeft; }
    public int getNorthSouthStraight() { return northSouthStraight; }
    public void setNorthSouthStraight(int northSouthStraight) { this.northSouthStraight = northSouthStraight; }
    public int getNorthSouthLeft() { return northSouthLeft; }
    public void setNorthSouthLeft(int northSouthLeft) { this.northSouthLeft = northSouthLeft; }
}