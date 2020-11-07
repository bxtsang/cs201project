package data;

import java.util.Collection;

public class Zone {
    private Collection<Taxi> taxis;
    private int demand;
    private double refereceLatitude;
    private double referenceLongtitude;
    private boolean isProceesed;

    public boolean isDeficit() {
        if (taxis.size() < demand) {
            return true;
        }
        return false;
    }
}
