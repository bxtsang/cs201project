package data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Zone {
    private Set<Taxi> taxis = new HashSet<>();
    private Integer ZoneNumber; 
    private int demand;
    private double referenceLatitude;
    private double referenceLongitude;
    private boolean isProcessed;
    private boolean isDeficit;

    public Zone(List<Double> coordinates) {
        this.referenceLatitude = coordinates.get(0);
        this.referenceLongitude = coordinates.get(1);
    }

    public Zone(List<Double> coordinates, int demand) {
        this.referenceLatitude = coordinates.get(0);
        this.referenceLongitude = coordinates.get(1);
        this.demand = demand;
    }

    public Zone(Integer zoneNumber, double referenceLongitude, double referenceLatitude){
        this.ZoneNumber = zoneNumber;
        this.referenceLongitude = referenceLongitude;
        this.referenceLatitude = referenceLatitude;
    }

    public List<Double> getCoordinates() {
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(this.referenceLatitude);
        coordinates.add(this.referenceLongitude);

        return coordinates;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int getDemand(){
        return demand;
    }

    public void setTaxis(Set<Taxi> taxis) {
        this.taxis = taxis;
    }

    public Set<Taxi> getTaxis() {
        return this.taxis;
    }

    public boolean isProcessed() {
        return this.isProcessed;
    }

    public void addTaxi(Taxi taxi) {
        this.taxis.add(taxi);
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public boolean contains(Taxi taxi) {
        return taxis.contains(taxi);
    }

    public int getDeficitAmount() {
        // positive : deficit
        // negative : surplus
        return demand - taxis.size();
    }

    public void setDeficit(boolean deficit) {
        isDeficit = deficit;
    }

    public boolean isDeficit() {
        return isDeficit;
    }

    public void removeTaxi(Taxi taxi) {
        taxis.remove(taxi);
    }

    public Integer getZoneNumber(){
        return ZoneNumber;
    }

    public double getReferenceLon(){
        return referenceLongitude;
    }

    public double getReferenceLat(){
        return referenceLatitude;
    }

    public boolean checkIfZoneIsProcessed(List<Zone> zones){
        for (Zone z: zones){
            if (z.getZoneNumber() == this.ZoneNumber){
                return z.isProcessed();
            }
        }
        return false;
    }

}
