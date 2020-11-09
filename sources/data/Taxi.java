package data;

import java.util.List;

public class Taxi {
    private double latitude;
    private double longitude;
    private int id;
    private Zone zone;
    private boolean isAssigned;

    public Taxi(List<Double> coordinates, int id) {
        this.longitude = coordinates.get(0);
        this.latitude = coordinates.get(1);
        this.id = id;
    }
}
