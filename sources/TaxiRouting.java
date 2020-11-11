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
    private static List<Taxi> availableTaxis = TaxiData.getAvailableTaxis();
    private static List<Taxi> assignedTaxis = new ArrayList<>();
    private static List<Zone> zones = new ArrayList<>();
    private static List<Zone> uZones = new ArrayList<>();

    public static void main(String[] args) {
//        preProcessing();
//        System.out.println("-------------------------------------");
//        System.out.println("Routing Algorithm 1 - direct with no zones");
//        System.out.println("-------------------------------------");
//        routing1();
//        MeasureOutput.measureOutput(assignedTaxis);
//        int count = 0;
//        for (Zone zone : zones) {
//            if (zone.getDeficitAmount() > 0) {
//                count ++;
//            }
//        }
//        System.out.println("deficit zones: " + count);
//
//        assignedTaxis = new ArrayList<>();
//        zones = new ArrayList<>();
//        preProcessing();
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println("-------------------------------------");
//        System.out.println("Routing Algorithm 2 - direct from surplus zones");
//        System.out.println("-------------------------------------");
//        routing2();
//        MeasureOutput.measureOutput(assignedTaxis);
//        count = 0;
//        for (Zone zone : zones) {
//            if (zone.getDeficitAmount() > 0) {
//                count ++;
//            }
//        }
//        System.out.println("deficit zones: " + count);
//
//        assignedTaxis = new ArrayList<>();
//        zones = new ArrayList<>();
        preProcessing();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("-------------------------------------");
        System.out.println("Routing Algorithm 3 - optimized with greedy");
        System.out.println("-------------------------------------");
        routing3();
        MeasureOutput.measureOutput(assignedTaxis);
        int count = 0;
        for (Zone zone : zones) {
            if (zone.getDeficitAmount() > 0) {
                count ++;
            }
        }
        System.out.println("deficit zones: " + count);


    }

    public static void routing1() {
        Set<Zone> deficitZones = new HashSet<>();
        Set<Zone> surplusZones = new HashSet<>();
        Set<Zone> neutralZones = new HashSet<>();

        // Fill three lists above
        for (Zone zone : zones) {
            categoriseEachZone(zone, deficitZones, surplusZones, neutralZones);
        }

        for (Zone deficitZone : deficitZones) {
            int deficit = deficitZone.getDeficitAmount();

            for (int i = 0; i < deficit; i++) {
                for (Taxi taxi : availableTaxis) {
                    if (surplusZones.contains(taxi.getZone()) && taxi.getAssignedZone() == null) {
                        taxi.setAssignedZone(deficitZone);
                        assignedTaxis.add(taxi);
                        taxi.getZone().removeTaxi(taxi);
                        deficitZone.addTaxi(taxi);
                        break;
                    }
                }
            }
        }
    }

    public static void routing2() {
        // numDeficitZones * surplusZones * deficit
        // sum of all zones' deficit
        // less complex, not optimised
        Set<Zone> deficitZones = new HashSet<>();
        Set<Zone> surplusZones = new HashSet<>();
        Set<Zone> neutralZones = new HashSet<>();

        // Fill three lists above
        for (Zone zone : zones) {
            categoriseEachZone(zone, deficitZones, surplusZones, neutralZones);
        }


        for (Zone deficitZone : deficitZones) {
            int deficit = deficitZone.getDeficitAmount();

            // for each zone in deficit:
            // look for zones with surplus
            // check how much surplus there is and if it can cover the deficit

            for (Zone surplusZone : surplusZones) {
                int surplus = Math.abs(surplusZone.getDeficitAmount());

                if (surplus == 0) {
                    continue;
                }

                if (surplus > deficit) {
                    moveTaxis(deficit, deficitZone, surplusZone);
                    deficit = deficitZone.getDeficitAmount();
                    neutralZones.add(deficitZone);
                    break;
//                    deficitZones.remove(deficitZone);
                } else if (surplus <= deficit) {
                    moveTaxis(surplus, deficitZone, surplusZone);
                    deficit = deficitZone.getDeficitAmount();
                    neutralZones.add(surplusZone);
//                    surplusZones.remove(surplusZone);
                }
            }

        }

        // System.out.println("SURPLUS MAP AFTER");
        // for (Map.Entry<Integer, Integer> entry : surplusMap.entrySet()) {
        //     System.out.print(entry.getKey());
        //     System.out.print(": ");
        //     System.out.println(entry.getValue());
        // }
    }

    public static void routing3() {
        Queue<Zone> deficitZonesQueue = new ArrayBlockingQueue<>(28);

        for (Zone zone : zones) {
            if (zone.getDeficitAmount() > 0) {
                zone.setDeficit(true);
                deficitZonesQueue.add(zone);
            }
        }

        // ----------------------- processing ------------------------
        // while queue is not empty, dequeue and process zone
        while (deficitZonesQueue.size() > 0) {
            // complexity: allDeficit * numTaxis
            // not efficient but optimized
            Zone current = deficitZonesQueue.poll();
            try {
                process(current, deficitZonesQueue);
            } catch (NoClosestTaxiException e) {
                uZones.add(current);
                System.out.println("no closest taxi");
            }
        }

        // meeasure the output of the algorithm
    }

    public static void preProcessing() {
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
//        for (int i = 0; i < zones.size(); i++){
//            // counter+= zones.get(i).getTaxis().size();
//            System.out.println("Zone # " + zones.get(i).getZoneNumber() + " Has " + zones.get(i).getTaxis().size() + " Taxis and has "
//                    + zones.get(i).getDemand() + " in demand");
//        }
    }



    public static void process(Zone zone, Queue<Zone> deficitZonesQueue) throws NoClosestTaxiException {
        // find closest taxis
        int deficitAmount = zone.getDeficitAmount();
        for (int i = 0; i < deficitAmount; i++) {
            Taxi closestTaxi = getClosestTaxi(zone, availableTaxis, zones);
            if (closestTaxi == null) {
                throw new NoClosestTaxiException();
            }
            closestTaxi.setAssignedZone(zone);
            zone.addTaxi(closestTaxi);
            closestTaxi.setAssigned(true);

            assignedTaxis.add(closestTaxi);

            Zone originalZone = closestTaxi.getZone();
            originalZone.removeTaxi(closestTaxi);

            if (!originalZone.isDeficit()) {
                if (originalZone.getDeficitAmount() > 0) {
                    System.out.println("new deficit zone");
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
        int indexOfMinDistance = -1;

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

        if (indexOfMinDistance == -1) {
            return null;
        }
        return availableTaxis.get(indexOfMinDistance);
    }

    public static void categoriseEachZone(Zone zone, Set<Zone> deficitZones, Set<Zone> surplusZones, Set<Zone> neutralZones) {
        if (zone.getDeficitAmount() > 0) {
            deficitZones.add(zone);
        } else if (zone.getDeficitAmount() < 0) {
            surplusZones.add(zone);
        } else {
            neutralZones.add(zone);
        }
    }

    private static void moveTaxis(int taxisNum, Zone deficitZone, Zone surplusZone) {
        Set<Taxi> deficitZoneTaxis = deficitZone.getTaxis();
        Set<Taxi> surplusZoneTaxis = surplusZone.getTaxis();

        for (int i = 0; i < taxisNum; i++) {
            Iterator<Taxi> surplusZoneIterator = surplusZoneTaxis.iterator();
            if (!surplusZoneIterator.hasNext()) {
                return;
            }
            Taxi taxi = surplusZoneIterator.next();

            taxi.setAssignedZone(deficitZone);
            assignedTaxis.add(taxi);

//            taxi.setZone(deficitZone);
            surplusZone.removeTaxi(taxi); // modify later to remove only when the surplus is gone
            deficitZone.addTaxi(taxi);
        }
    }

    
}
