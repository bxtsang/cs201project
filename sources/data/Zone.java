package data;

import java.util.Collection;

public class Zone {
    private Collection<Taxi> taxis;
    private int demand;
    private double referenceLatitude;
    private double referenceLongitude;
    private boolean isProcessed;

    public Zone(List<Double> coordinates) {
        this.referenceLatitude = coordinates.get(0);
        this.referenceLongitude = coordinates.get(1);
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public void setTaxis(Collection<Taxi> taxis) {
        this.taxis = taxis;
    }

    public Collection<Taxi> getTaxis() {
        return this.taxis;
    }

    public boolean getIsProcessed() {
        return this.isProcessed;
    }

    public void addTaxi(Taxi taxi) {
        this.taxis.add(taxi);
    }

    public int getDeficit() {
        // positive : deficit
        // negative : surplus
        return demand - taxis.size();
    }
}
