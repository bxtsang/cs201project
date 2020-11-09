package data;

import java.util.Collection;

public class Zone {
    private Collection<Taxi> taxis;
    private int demand;
    private double referenceLatitude;
    private double referenceLongitude;
    private boolean isProcessed;

    public int getDeficit() {
        if (taxis.size() < demand) {
            return demand - taxis.size();
        }
        return 0;
    }
}
