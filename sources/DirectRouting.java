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

        List<Zone> zonesMock = mockTaxiData();
        
        
    }

    private List<Zone> mockTaxiData() {
        List<Zone> zones = mockZoneData();
        // create surplus in zone1 (demand: 1, taxis: 2)
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(103.5);
        coordinates.add(170.5);
        Taxi taxi1 = new Taxi(coordinates, 1);
        Taxi taxi2 = new Taxi(coordinates, 2);

        zones.get(0).addTaxi(taxi1);
        zones.get(0).addTaxi(taxi2);

        // create deficit in zone2 (demand: 10, taxis: 2)
        Taxi taxi3 = new Taxi(coordinates, 3);
        Taxi taxi4 = new Taxi(coordinates, 4);

        zones.get(1).addTaxi(taxi3);
        zones.get(1).addTaxi(taxi4);

        return zones;
    }

    private List<Zone> mockZoneData() {
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(103.5);
        coordinates.add(170.5);
        List<Zone> zones = new ArrayList<>();
        Zone zone1 = new Zone(coordinates, 1);
        Zone zone2 = new Zone(coordinates, 10);
        Zone zone3 = new Zone(coordinates, 100);
        Zone zone4 = new Zone(coordinates, 5);
        Zone zone5 = new Zone(coordinates, 15);

        zones.add(zone1);
        zones.add(zone2);
        zones.add(zone3);
        zones.add(zone4);
        zones.add(zone5);

        return zones;
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

}
