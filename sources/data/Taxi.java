package data;

import java.util.List;

public class Taxi {
    private double latitude;
    private double longitude;
    private int id;
    private Integer clusterNum;

    private Zone zone;
    private boolean isAssigned;

    public Taxi(List<Double> coordinates, int id) {
        this.longitude = coordinates.get(0);
        this.latitude = coordinates.get(1);
        this.id = id;
    }

    public double getLat(){
        return latitude;
    }

    public double getLon(){
        return longitude;
    }

    public Zone getZone(){
        return zone;
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
}
