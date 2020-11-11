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

        directRouting(deficitZones, surplusZones, surplusZonesDuplicate, neutralZones);
        MeasureOutput.measureOutput(assignedTaxis);
    }

    // Approach 2:
    // For each zone in surplus, store the quantity of surplus with the zone id in a hashmap
    // For each zone in deficit, zone can 'shop' for surplus
    // Amount taken is subtracted accordingly from the surplus zones
    private static Map<Integer, Integer> mapSurplus(List<Zone> surplusZones) {
        Map<Integer, Integer> surplusMap = new HashMap<>();
        int idCount = 0;
        for (Zone zone : surplusZones) {
            surplusMap.put(idCount++, Math.abs(zone.getDeficitAmount()));
        }

        return surplusMap;
    }

    private static void directRouting(List<Zone> deficitZones, List<Zone> surplusZones, List<Zone> surplusZonesDuplicate, List<Zone> neutralZones) {
        Map<Integer, Integer> surplusMap = mapSurplus(surplusZones);

        for (Zone deficitZone : deficitZones) {
            int deficit = deficitZone.getDeficitAmount();

            for (int i = 0; i < surplusZonesDuplicate.size(); i++) {
                int taxisToMove = 0;
                int surplus = surplusMap.get(i);
                Zone surplusZone = surplusZonesDuplicate.get(i);

                if (surplus <= deficit) {
                    // eg. surplus has 3 extra taxis, deficit needs 5 more
                    // put all surplus into deficit, then move surplus zone to neutral
                    taxisToMove = surplus;
                    surplusZones.remove(surplusZone);
                    neutralZones.add(surplusZone);

                } else {
                    // surplus > deficit, there will be leftover surplus
                    // leave zone in surplusZones
                    // move deficitZone to neutral
                    taxisToMove = surplus - deficit;
                    deficitZones.remove(deficitZone);
                    neutralZones.add(deficitZone);
                }

                surplusMap.put(i, surplus - taxisToMove);
                moveTaxis(taxisToMove, deficitZone, surplusZone);
            }

        }
    }

    private static void moveTaxis(int taxisNum, Zone deficitZone, Zone surplusZone) {
        Set<Taxi> deficitZoneTaxis = deficitZone.getTaxis();
        Set<Taxi> surplusZoneTaxis = surplusZone.getTaxis();

        for (int i = 0; i < taxisNum; i++) {
            // Taxi taxi = deficitZoneTaxis.get(taxisNum - i - 1);
            Iterator<Taxi> surplusZoneIterator = surplusZoneTaxis.iterator();
            if (!surplusZoneIterator.hasNext()) {
                return;
            }
            Taxi taxi = surplusZoneIterator.next();

            // // Debug
            // System.out.print("round");
            // System.out.print(i);
            // System.out.print(": Taxi ");
            // System.out.println(taxi.getId());

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

        // create deficit in zone3 (demand: 100, taxis: 4)
        Taxi taxi5 = new Taxi(coordinates, 5);
        Taxi taxi6 = new Taxi(coordinates, 6);
        Taxi taxi7 = new Taxi(coordinates, 7);
        Taxi taxi8 = new Taxi(coordinates, 8);

        zones.get(2).addTaxi(taxi5);
        zones.get(2).addTaxi(taxi6);
        zones.get(2).addTaxi(taxi7);
        zones.get(2).addTaxi(taxi8);
        
        // create surplus in zone 4 (demand: 2, taxis: 4)
        Taxi taxi9 = new Taxi(coordinates, 9);
        Taxi taxi10 = new Taxi(coordinates, 10);
        Taxi taxi11 = new Taxi(coordinates, 11);
        Taxi taxi12 = new Taxi(coordinates, 12);

        zones.get(3).addTaxi(taxi9);
        zones.get(3).addTaxi(taxi10);
        zones.get(3).addTaxi(taxi11);
        zones.get(3).addTaxi(taxi12);

        // create surplus in zone 5 (demand: 1, taxis: 4)
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
        Zone zone2 = new Zone(coordinates, 10, 1);
        Zone zone3 = new Zone(coordinates, 100, 2);
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
