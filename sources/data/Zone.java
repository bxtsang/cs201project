package data;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Zone {
    private Set<Taxi> taxis = new HashSet<>();
    private int demand;
    private double referenceLatitude;
    private double referenceLongitude;
    private boolean isProcessed;

    public Zone(List<Double> coordinates) {
        this.referenceLatitude = coordinates.get(0);
        this.referenceLongitude = coordinates.get(1);
    }

    public Zone(List<Double> coordinates, int demand) {
        this.referenceLatitude = coordinates.get(0);
        this.referenceLongitude = coordinates.get(1);
        this.demand = demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public void setTaxis(Set<Taxi> taxis) {
        this.taxis = taxis;
    }

    public Set<Taxi> getTaxis() {
        return this.taxis;
    }

    public boolean getIsProcessed() {
        return this.isProcessed;
    }

    public void addTaxi(Taxi taxi) {
        this.taxis.add(taxi);
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public int getDeficit() {
        // positive : deficit
        // negative : surplus
        return demand - taxis.size();
    }
}
