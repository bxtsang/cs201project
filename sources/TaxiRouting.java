import data.Feature;
import data.Response;
import data.Taxi;
import data.Zone;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.HashMap;
import java.util.*;

public class TaxiRouting {
    public static void main(String[] args) {
        HashMap<Integer, List<Address>> clusteredAddresses = new HashMap<>(); 

        //Load all supporting dataset into arrayList
        ArrayList<Address> supportingAddresses = new ArrayList<>();
        AddressUtilities.loadAddresses(supportingAddresses);

        //Initialize a HashMap of key-value pairs of ZoneNumber & all its addresses
        clusteredAddresses = AddressUtilities.initHashMap(supportingAddresses);
        HashMap<Integer, Address> referencePoints = new HashMap<>();
        
        //Populate the HashMap with all Key-Value pairs
        for (Integer i : clusteredAddresses.keySet()){
            referencePoints.put(i, AddressUtilities.findReferencePoint(i, clusteredAddresses));
        }
        // System.out.println(referencePoints);

        // mock taxi demand for each zone
        HashMap<Integer, Integer> demand = new HashMap<>();
        demand.put(1,3042); // no deficit
        demand.put(2, 1601); // no deficit
        demand.put(3, 1867); // no deficit
        demand.put(4,1318); // no deficit
        demand.put(5,4876); // no deficit
        demand.put(6,200); // deficit, current count = 187
        demand.put(7, 1702); // no deficit
        demand.put(8, 4270); // no deficit
        demand.put(9, 2247); // no deficit
        demand.put(10, 9790); // no deficit
        demand.put(11, 5942); // no deficit
        demand.put(12, 2746); // no deficit
        demand.put(13, 4888); // no deficit
        demand.put(14, 8417); // no deficit
        demand.put(15, 10000); // oversupply, current count = 14002
        demand.put(16, 7896); // no deficit
        demand.put(17, 2503); // no deficit
        demand.put(18, 3448); // no deficit
        demand.put(19, 17000); // oversupply, current count = 17535
        demand.put(20, 7869); // no deficit
        demand.put(21, 4611); // no deficit
        demand.put(22, 7499); // no deficit
        demand.put(23, 6263); // no deficit
        demand.put(24, 400); // deficit, current count = 289
        demand.put(25, 2883); // no deficit
        demand.put(26, 3227); // no deficit
        demand.put(27, 4567); // no deficit
        demand.put(28, 6150); // no deficit

        // System.out.println(referencePoints);
      
        // -------------------------pre processing-------------------------------------
        // create global collection of taxi, taxiCollection
        List<Taxi> availableTaxis = getAvailableTaxis();
        if (availableTaxis == null) {
            return;
        }

        //Set clusters for all the taxis
        for (Taxi t : availableTaxis){
            AddressUtilities.findNearestZone(t, referencePoints);
            System.out.println("Taxi ID: " + t.getId() + " is in Cluster #" + t.getClusterNum());
        }

        // System.out.println(testing.getClusterNum());
        // AddressUtilities.calculateDistance(103.62432,1.28653,103.847401,1.28378453902038);

        // get all zones (28 zones)
        List<Zone> zones = new ArrayList<>();

        // create a empty queue of zones
        Queue<Zone> deficitZonesQueue = new ArrayBlockingQueue<Zone>(28);

        // iterate through zones and call getDeficit() on all of them
        // if getDeficit() > 0, enqueue to queue
        for (Zone zone : zones) {
            if (zone.getDeficit() > 0) {
                deficitZonesQueue.add(zone);
            }
        }

        // ----------------------- processing ------------------------
        // while queue is not empty, dequeue and process zone
        while (deficitZonesQueue.size() > 0) {
            Zone current = deficitZonesQueue.poll();
            process(current);
        }
        // processing zone:
        // for i in getDeficit, find closest taxi to zone in global collection
        //    mark taxi as isAssigned, remove taxi from original zone

        //
    }

    public static List<Taxi> getAvailableTaxis() {
        List<Taxi> availableTaxis = new ArrayList<>();

        try {
            Response response = TaxiData.getData();
            List<Feature> features = response.getFeatures();
            Feature feature = features.get(0);
            List<List<Double>> coordinates = feature.getGeometry().getCoordinates();

            for (int i = 0; i < coordinates.size(); i++) {
                availableTaxis.add(new Taxi(coordinates.get(i), i));
            }
        } catch (Exception e) {
            System.out.println("Error when accessing API");
            return null;
        }
        return availableTaxis;
    }

    public static void process(Zone zone) {
        //dosomething
    }
}
