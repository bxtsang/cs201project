import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

import data.Taxi;
import data.Zone;

public class DirectRouting {
    private static List<Taxi> availableTaxis = TaxiData.getAvailableTaxis();
    private static List<Taxi> assignedTaxis = new ArrayList<>();
    private static List<Zone> zones = new ArrayList<>();

    public static void main(String[] args) {
        AddressUtilities.loadAddresses();

        //Initialize a HashMap of key-value pairs of ZoneNumber & all its addresses
        Map<Integer, List<Address>> clusteredAddresses = AddressUtilities.initHashMap();
        HashMap<Integer, Address> referencePoints = new HashMap<>();

        //Populate the HashMap with all Key-Value pairs
        for (Integer i : clusteredAddresses.keySet()){
            referencePoints.put(i, AddressUtilities.findReferencePoint(i, clusteredAddresses));
        }

        for (Taxi t : availableTaxis){
            AddressUtilities.findNearestZone(t, referencePoints);
        }
        //Populate all the zones with taxis - This is only run once by calling updateZones()
        AddressUtilities.updateZones(zones, referencePoints, availableTaxis);

        List<Zone> zonesMock = mockTaxiData();

        List<Zone> deficitZones = new ArrayList<>();
        List<Zone> surplusZones = new ArrayList<>();
        List<Zone> neutralZones = new ArrayList<>();

        // Fill three lists above 
        for (Zone zone : zones) {
            categoriseEachZone(zone, deficitZones, surplusZones, neutralZones);
        }

        System.out.println(deficitZones.size());
        System.out.println(surplusZones.size());
        System.out.println(neutralZones.size());

        List<Zone> surplusZonesDuplicate = new ArrayList<>(surplusZones);

        // Perform direct routing
        directRouting(deficitZones, surplusZones, surplusZonesDuplicate, neutralZones);
        MeasureOutput.measureOutput(assignedTaxis);
    }


    // For each zone in surplus, store the quantity of surplus with the zone id in a hashmap
    // For each zone in deficit, zone can 'shop' for surplus
    // Amount taken is subtracted accordingly from the surplus zones
    private static Map<Integer, Integer> mapSurplus(List<Zone> surplusZones) {
        Map<Integer, Integer> surplusMap = new HashMap<>();
        int idCount = 0;
        for (Zone zone : surplusZones) {
            // index numbers 0, 1, 2,... as key and surplus amount as value
            surplusMap.put(idCount++, Math.abs(zone.getDeficitAmount()));
        }

        System.out.println("SURPLUS MAP BEFORE");
        for (Map.Entry<Integer, Integer> entry : surplusMap.entrySet()) {
            System.out.print(entry.getKey());
            System.out.print(": ");
            System.out.println(entry.getValue());
        }

        return surplusMap;
    }

    private static void directRouting(List<Zone> deficitZones, List<Zone> surplusZones, List<Zone> surplusZonesDuplicate, List<Zone> neutralZones) {
        Map<Integer, Integer> surplusMap = mapSurplus(surplusZones);
        
        for (Zone deficitZone : deficitZones) {
            System.out.println("Current Deficit Zone: " + deficitZone.getZoneNumber());
            int deficit = deficitZone.getDeficitAmount();

            // for each zone in deficit:
            // look for zones with surplus
            // check how much surplus there is and if it can cover the deficit

            for (Zone surplusZone : surplusZones) {
                int surplus = Math.abs(surplusZone.getDeficitAmount());

                if (surplus > deficit) {
                    moveTaxis(surplus - deficit, deficitZone, surplusZone);
                    neutralZones.add(deficitZone);
                    deficitZone.remove(deficitZone);
                } else if (surplus <= deficit) {
                    moveTaxis(deficit - surplus, deficitZone, surplusZone);
                    neutralZones.add(surplusZone);
                    surplusZone.remove(surplusZone);
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

            // Debug
            System.out.print("Taxi ");
            System.out.print(taxi.getId());
            System.out.print(": Zone ");
            System.out.print(surplusZone.getZoneNumber());
            System.out.print(" -> Zone ");
            System.out.println(deficitZone.getZoneNumber());

            taxi.setZone(deficitZone);
            surplusZone.removeTaxi(taxi); // modify later to remove only when the surplus is gone
            deficitZone.addTaxi(taxi);
        }
    }


    public static void categoriseEachZone(Zone zone, List<Zone> deficitZones, List<Zone> surplusZones, List<Zone> neutralZones) {
        if (zone.getDeficitAmount() > 0) {
            deficitZones.add(zone);
        } else if (zone.getDeficitAmount() < 0) {
            surplusZones.add(zone);
        } else {
            neutralZones.add(zone);
        }
    }

    // MOCK DATA
    private static List<Zone> mockTaxiData() {
        List<Zone> zones = mockZoneData();
        // create surplus in zone0 (demand: 1, taxis: 2)
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(103.5);
        coordinates.add(170.5);
        Taxi taxi1 = new Taxi(coordinates, 1);
        Taxi taxi2 = new Taxi(coordinates, 2);

        zones.get(0).addTaxi(taxi1);
        zones.get(0).addTaxi(taxi2);

        // create deficit in zone1 (demand: 3, taxis: 2)
        Taxi taxi3 = new Taxi(coordinates, 3);
        Taxi taxi4 = new Taxi(coordinates, 4);

        zones.get(1).addTaxi(taxi3);
        zones.get(1).addTaxi(taxi4);

        // create deficit in zone2 (demand: 5, taxis: 4)
        Taxi taxi5 = new Taxi(coordinates, 5);
        Taxi taxi6 = new Taxi(coordinates, 6);
        Taxi taxi7 = new Taxi(coordinates, 7);
        Taxi taxi8 = new Taxi(coordinates, 8);

        zones.get(2).addTaxi(taxi5);
        zones.get(2).addTaxi(taxi6);
        zones.get(2).addTaxi(taxi7);
        zones.get(2).addTaxi(taxi8);
        
        // create surplus in zone3 (demand: 2, taxis: 4)
        Taxi taxi9 = new Taxi(coordinates, 9);
        Taxi taxi10 = new Taxi(coordinates, 10);
        Taxi taxi11 = new Taxi(coordinates, 11);
        Taxi taxi12 = new Taxi(coordinates, 12);

        zones.get(3).addTaxi(taxi9);
        zones.get(3).addTaxi(taxi10);
        zones.get(3).addTaxi(taxi11);
        zones.get(3).addTaxi(taxi12);

        // create surplus in zone4 (demand: 1, taxis: 4)
        Taxi taxi13 = new Taxi(coordinates, 13);
        Taxi taxi14 = new Taxi(coordinates, 14);
        Taxi taxi15 = new Taxi(coordinates, 15);
        Taxi taxi16 = new Taxi(coordinates, 16);

        zones.get(4).addTaxi(taxi13);
        zones.get(4).addTaxi(taxi14);
        zones.get(4).addTaxi(taxi15);
        zones.get(4).addTaxi(taxi16);

        return zones;
    }

    private static List<Zone> mockZoneData() {
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(103.5);
        coordinates.add(170.5);
        List<Zone> zones = new ArrayList<>();
        Zone zone1 = new Zone(coordinates, 1, 0);
        Zone zone2 = new Zone(coordinates, 3, 1);
        Zone zone3 = new Zone(coordinates, 5, 2);
        Zone zone4 = new Zone(coordinates, 2, 3);
        Zone zone5 = new Zone(coordinates, 1, 4);

        zones.add(zone1);
        zones.add(zone2);
        zones.add(zone3);
        zones.add(zone4);
        zones.add(zone5);

        return zones;
    }
}
