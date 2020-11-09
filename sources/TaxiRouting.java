import data.Feature;
import data.Response;
import data.Taxi;
import data.Zone;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.HashMap;

public class TaxiRouting {
    public static void main(String[] args) {
        HashMap<Integer, List<Address>> clusteredAddresses = new HashMap<>(); 

        //Load all supporting dataset into arrayList
        ArrayList<Address> supportingAddresses = new ArrayList<>();
        FindClusters.loadAddresses(supportingAddresses);

        //Initialize a HashMap of key-value pairs of ZoneNumber & all its addresses
        clusteredAddresses = FindClusters.initHashMap(supportingAddresses);
        HashMap<Integer, Address> referencePoints = new HashMap<>();

        //Populate the HashMap with all Key-Value pairs
        for (Integer i : clusteredAddresses.keySet()){
            referencePoints.put(i, FindClusters.findReferencePoint(i, clusteredAddresses));
        }

        System.out.println(referencePoints);
      
        // -------------------------pre processing-------------------------------------
        // create global collection of taxi, taxiCollection
        List<Taxi> availableTaxis = getAvailableTaxis();
        if (availableTaxis == null) {
            return;
        }


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
