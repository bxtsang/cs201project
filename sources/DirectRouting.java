import data.Taxi;
import java.util.List;
import java.util.ArrayList;

public class DirectRouting {

    /*
        Naive Approach:
        Given a collection of clusters, find a cluster with a surplus, and another with a deficit.
        Pick a Taxi in surplus to be moved to deficit
        Rebalance accordingly
        Output: Taxi moved
    */

    public static void main(String[] args) {
        HashMap<Integer, List<Address>> refPoints = getReferencePoints();
        List<Taxi> taxis = TaxiRouting.getAvailableTaxis();

        // // 1 surplus zone, many deficit zones
        // List<Zone> zonesWithDeficit = new ArrayList<>();
        // int min = 0;
        // Zone zoneWithSurplus;

        // for (Zone zone : zones) {
        //     if (zone.getDeficit() < min) {
        //         min = zone.getDeficit();
        //         surplus = zone;
        //     }
        // }
        
        // Many surplus zones, many deficit zones
         List<Zone> zonesWithDeficit = new ArrayList<>(); // to be fetched
         
        
    }

    private HashMap<Integer, Address> getReferencePoints() {

        //Load all supporting dataset into arrayList
        ArrayList<Address> supportingAddresses = new ArrayList<>();
        FindClusters.loadAddresses(supportingAddresses);

        //Initialize a HashMap of key-value pairs of ZoneNumber & all its addresses
        HashMap<Integer, List<Address>> clusteredAddresses = new HashMap<>();
        clusteredAddresses = FindClusters.initHashMap(supportingAddresses);
        HashMap<Integer, Address> referencePoints = new HashMap<>();
        for (Integer i : clusteredAddresses.keySet()){
            referencePoints.put(i, FindClusters.findReferencePoint(i, clusteredAddresses));
        }

        return referencePoints;
    }

    private List<Zone> mockZoneData() {
        List<Zone> zones = new ArrayList<>();
        Zone zone1 = new Zone();
        Zone zone2 = new Zone();
        Zone zone3 = new Zone();
        Zone zone4 = new Zone();
        Zone zone5 = new Zone();

        zones.add(zone1);
        zones.add(zone2);
        zones.add(zone3);
        zones.add(zone4);
        zones.add(zone5);

        return zones;
    }


}
