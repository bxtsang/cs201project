package data;

import java.util.List;
import java.util.ArrayList;

public class Taxi {
    private double latitude;
    private double longitude;
    private int id;
    private Integer clusterNum;

    private Zone zone;
    private Zone assignedZone;
    private boolean isAssigned;

    public Taxi(List<Double> coordinates, int id) {
        this.longitude = coordinates.get(0);
        this.latitude = coordinates.get(1);
        this.id = id;
    }
  
    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public void setIsAssigned(boolean isAssigned) {
        this.isAssigned = isAssigned;
    }

    public void setNewCoordinates(List<Double> coordinates) {
        this.longitude = coordinates.get(0);
        this.latitude = coordinates.get(1);
    }

    public List<Double> getCoordinates() {
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(this.latitude);
        coordinates.add(this.longitude);
        return coordinates;
    }

    public Zone getZone() {
        return this.zone;
    }

    public boolean checkIfAssigned() {
        return this.isAssigned;
    }

    public double getLat(){
        return latitude;
    }

    public double getLon(){
        return longitude;
    }

    public boolean isAssigned(){
        return isAssigned;
    }
    public int getId(){
        return id;
    }

    public void setClusterNum(Integer clusterNum){
        this.clusterNum = clusterNum;
    }

    public Integer getClusterNum(){
        return clusterNum;
    }

    public void setAssignedZone(Zone assignedZone) {
        this.assignedZone = assignedZone;
    }
}
