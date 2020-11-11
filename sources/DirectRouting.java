import java.util.*;

import data.Taxi;
import data.Zone;

public class DirectRouting {
    public static void main(String[] args) {
        List<Zone> zonesMock = mockTaxiData();

        List<Zone> deficitZones = new ArrayList<>();
        List<Zone> surplusZones = new ArrayList<>();
        List<Zone> neutralZones = new ArrayList<>();

        // Fill three lists above 
        for (Zone zone : zonesMock) {
            categoriseEachZone(zone, deficitZones, surplusZones, neutralZones);
        }

        directRouting(deficitZones, surplusZones, neutralZones);
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

    private static void directRouting(List<Zone> deficitZones, List<Zone> surplusZones, List<Zone> neutralZones) {
        Map<Integer, Integer> surplusMap = mapSurplus(surplusZones);

        for (Zone deficitZone : deficitZones) {
            int deficit = deficitZone.getDeficitAmount();

            for (Map.Entry<Integer, Integer> entry : surplusMap.entrySet()) {
                int taxisToMove = 0;
                if (entry.getValue() < deficit) {
                    // eg. surplus has 3 extra taxis, deficit needs 5 more
                    // put all surplus into deficit, then move surplus to neutral
                    taxisToMove = entry.getValue();

                } else if (entry.getValue() > deficit) {
                    taxisToMove = entry.getValue() - deficit;
                } else {
                    taxisToMove = deficit;
                }

                // Debugging purposes
                System.out.println("BEFORE:");
                System.out.println(surplusZones.get(entry.getKey()).getDeficitAmount());
                System.out.println("AFTER:");
                moveTaxis(taxisToMove, deficitZone, surplusZones.get(entry.getKey()));
                System.out.println(surplusZones.get(entry.getKey()).getDeficitAmount());
            }
        }
    }

    private static void moveTaxis(int taxisNum, Zone deficitZone, Zone surplusZone) {
        Set<Taxi> deficitZoneTaxis = deficitZone.getTaxis();
        Set<Taxi> surplusZoneTaxis = surplusZone.getTaxis();
        Iterator<Taxi> surplusIterator = surplusZoneTaxis.iterator();

        for (int i = 0; i < taxisNum; i++) {
            Taxi taxi = surplusIterator.next();
            surplusZone.removeTaxi(taxi);
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
}
