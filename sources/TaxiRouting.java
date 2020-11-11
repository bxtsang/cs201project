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
    private static List<Taxi> availableTaxis = getAvailableTaxis();
    private static Queue<Zone> deficitZonesQueue = new ArrayBlockingQueue<Zone>(28);
    private static List<Taxi> assignedTaxis = new ArrayList<>();
    private static List<Zone> zones = new ArrayList<>();

    public static void main(String[] args) {

        //Load all supporting dataset into arrayList
        AddressUtilities.loadAddresses();

        //Initialize a HashMap of key-value pairs of ZoneNumber & all its addresses
        Map<Integer, List<Address>> clusteredAddresses = AddressUtilities.initHashMap();
        HashMap<Integer, Address> referencePoints = new HashMap<>();
        
        //Populate the HashMap with all Key-Value pairs
        for (Integer i : clusteredAddresses.keySet()){
            referencePoints.put(i, AddressUtilities.findReferencePoint(i, clusteredAddresses));
        }

        // System.out.println(referencePoints);
      
        // -------------------------pre processing-------------------------------------
        // create global collection of taxi, taxiCollection
        availableTaxis = getAvailableTaxis();
        
        // System.out.println("Data check: Number of taxis: " + availableTaxis.size());
        // get all zones (28 zones)


        for (Taxi t : availableTaxis){
            AddressUtilities.findNearestZone(t, referencePoints);
            //Set clusters for all the taxis
            // System.out.println("Taxi ID: " + t.getId() + " is in Cluster #" + t.getClusterNum());
        }

        //Populate all the zones with taxis - This is only run once by calling updateZones()
        AddressUtilities.updateZones(zones, referencePoints, availableTaxis);
        
        // int counter = 0;
        for (int i = 0; i < zones.size(); i++){
            // counter+= zones.get(i).getTaxis().size();
            System.out.println("Zone # " + zones.get(i).getZoneNumber() + " Has " + zones.get(i).getTaxis().size() + " Taxis and has " 
            + zones.get(i).getDemand() + " in demand");
        }

        // Sanity check for data
        // System.out.println(counter == availableTaxis.size());
        // create a empty queue of zones


        // iterate through zones and call getDeficit() on all of them
        // if getDeficit() > 0, enqueue to queue
        for (Zone zone : zones) {
            if (zone.getDeficitAmount() > 0) {
                zone.setDeficit(true);
                deficitZonesQueue.add(zone);
            }
        }

        // ----------------------- processing ------------------------
        // while queue is not empty, dequeue and process zone
        while (deficitZonesQueue.size() > 0) {
            Zone current = deficitZonesQueue.poll();
            process(current);
        }

        MeasureOutput.measureOutput(assignedTaxis);
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
        // find closest taxis
        for (int i = 0; i < zone.getDeficitAmount(); i++) {
            Taxi closestTaxi = getClosestTaxi(zone, availableTaxis, zones); // implement this, make sure taxis are not from any previously processed zones
            closestTaxi.setAssignedZone(zone);
            closestTaxi.setAssigned(true);

            assignedTaxis.add(closestTaxi);

            Zone originalZone = closestTaxi.getZone();
            originalZone.removeTaxi(closestTaxi);

            if (!originalZone.isDeficit()) {
                if (originalZone.getDeficitAmount() > 0) {
                    originalZone.setDeficit(true);
                    deficitZonesQueue.add(originalZone);
                }
            }
        }

        //Update the zone that it is now processed
        zone.setProcessed(true);
    }

    public static Taxi getClosestTaxi(Zone zone, List<Taxi> availableTaxis, List<Zone> zones){
        double referenceLon = zone.getReferenceLon();
        double referenceLat = zone.getReferenceLat();
        
        //Arbitrarily large distance initialized
        double minimumDistance = Integer.MAX_VALUE;

        //Pointer to find the taxi to be returned
        int indexOfMinDistance = 0;

        for (int i = 0; i < availableTaxis.size(); i++){
            //For each taxi, if the taxi is not assigned && its zone is not processed, then calculate it's distance from the zone's reference point
            Taxi T = availableTaxis.get(i);
            Zone currentTaxiZone = T.getZone();

            //If the taxi is not assigned and the current taxi's zone does not belong in a zone that has been processed
            //Also check that the Taxi we're checking is NOT within the current zone of deficit, calculate distance
            if (!T.isAssigned() && !currentTaxiZone.checkIfZoneIsProcessed(zones) && currentTaxiZone.getZoneNumber() != zone.getZoneNumber()){

                double measuredDistance = AddressUtilities.calculateDistance(referenceLon, referenceLat, T.getLon(), T.getLat());

                //Finding the smallest distance: Update the index of the taxi with the minimum distance 
                if (measuredDistance < minimumDistance){
                    minimumDistance = measuredDistance;
                    indexOfMinDistance = i;
                }
            }
        }

        return availableTaxis.get(indexOfMinDistance);
    }

    
}
