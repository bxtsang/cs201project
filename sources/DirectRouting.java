import data.Taxi;
import jdk.dynalink.beans.StaticClass;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class DirectRouting {

    /*
        Naive Approach:
        Given a collection of clusters, find a cluster with a surplus, and another with a deficit.
        Pick a Taxi in surplus to be moved to deficit
        Rebalance accordingly
        Output: Taxi moved, distance travelled
    */

    public static void main(String[] args) {

        // Get mock zones
        List<Zone> zonesMock = mockTaxiData();

        List<Zone> deficitZones = new ArrayList<>();
        List<Zone> surplusZones = new ArrayList<>();
        List<Zone> neutralZones = new ArrayList<>();

        // Fill three lists above 
        for (Zone zone : zonesMock) {
            if (zone.getDeficit() > 0) {
                deficitZones.add(zone);
            } else if (zone.getDeficit() < 0) {
                surplusZones.add(zone);
            } else {
                neutralZones.add(zone);
            }
        }

    }

    //----------------------Approaches-------------------------------

    // Approach 1:
    // Go through the list of zones and check if the zones are in surplus or deficit
    // If two zones are both in surplus and deficit, reallocate, then remove them from the list
    // Even if they're still in surplus / deficit since the numbers are brought closer to 0
    // At each pass, check if the zones are surplus / neutral and stop loop when they are all surplus / neutral
    private static List<Zone> alternateRecursiveInefficient(List<Zone> zonesMock) {

        // O(n)
        if (allSurplusOrNeutral(zonesMock)) {
            return zonesMock;
        }

        // O(n^2)
        for (Zone zone : zonesMock) {
            for (Zone zone2 : zonesMock) {
                if (isSurplus(zone) && isDeficit(zone2)) {
                    reallocateTwoZones(zone, zone2);
                    zonesMock.remove(zone);
                    zonesMock.remove(zone2);
                }
            }
        }

        alternateRecursiveInefficient(zonesMock);
    }

    // Approach 2:
    // For each zone in surplus, store the quantity of surplus with the zone id in a hashmap
    // For each zone in deficit, zone can 'shop' for surplus
    // Amount taken is subtracted accordingly from the surplus zones
    private Map<Integer, Integer> mapSurplus(List<Zone> surplusZones) {
        Map<Integer, Integer> surplusMap = new HashMap<>();
        int idCount = 0;
        for (Zone zone : surplusZones) {
            surplusMap.put(idCount++, zone.getDeficit());
        }

        return surplusMap;
    }

    private void alternateUsesMap(List<Zone> deficitZones, List<Zone> surplusZones, List<Zone> neutralZones) {
        Map<Integer, Integer> surplusMap = mapSurplus();

        for (Zone deficitZone : deficitZones) {
            int deficit = deficitZone.getDeficit();

            for (Entry<Integer, Integer> entry : surplusMap.entrySet()) {
                if (entry.getValue() < deficit) {
                    int taxisToMove = entry.getValue();
                    // move this number of taxis from zone entry.getKey() to deficitZone
                    // remove entry.getKey() from hashmap
                    // neutralZones.add(surplusZones.get(entry.getKey()))
                    // surplusZones.remove(surplusZones.get(entry.getKey()))
                    // might run into concurrent modification exception
                } else if (entry.getValue() > deficit) {
                    int taxisToMove = entry.getValue() - deficit;
                    // move this number of taxis from zone entry.getKey() to deficitZone
                    // neutralZones.add(deficitZone)
                    // deficitZones.remove(deficitZone)
                } else {
                    int taxisToMove = deficit;
                    // move this number of taxis from zone entry.getKey() to deficitZone
                    // neutralZones.add(surplusZones.get(entry.getKey()))
                    // neutralZones.add(deficitZone)
                    // deficitZones.remove(deficitZone)
                    // surplusZones.remove(surplusZones.get(entry.getKey()))
                }
            }
        }
    }


    //----------------Helper functions------------------------
    private static void reallocateTwoZones(Zone deficit, Zone surplus) {
        // take down amount to be reallocated
        // go through taxi list and move that number of taxis
        // remove from one, add to the other
    }


    private static reallocate(Zone zone, List<Zone> surplusZones) {
        int currentDeficit = zone.getDeficit();

        for (Zone surplusZone : surplusZones) {
            if (Math.abs(surplusZone.getDeficit()) > currentDeficit) {
                int taxisToMove = Math.abs(surplusZone.getDeficit()) - currentDeficit;
                // move taxi
                // subtract currentDeficit from surplusZone's surplus
            }
        }
    }



    //-----------------Mock data------------------------------

    private static List<Zone> mockTaxiData() {
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

    private static List<Zone> mockZoneData() {
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

    private static HashMap<Integer, Address> getReferencePoints() {

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