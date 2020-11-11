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

//        List<Zone> zonesMock = mockTaxiData();

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

        // Perform direct routing
        directRoutingWithoutZones(deficitZones, surplusZones, neutralZones);
        MeasureOutput.measureOutput(assignedTaxis);
    }

    private static void directRoutingWithoutZones(List<Zone> deficitZones, List<Zone> surplusZones, List<Zone> neutralZones) {
        for (Zone deficitZone : deficitZones) {
            System.out.println("Current Deficit Zone: " + deficitZone.getZoneNumber());
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

    private static void directRouting(List<Zone> deficitZones, List<Zone> surplusZones, List<Zone> neutralZones) {
        for (Zone deficitZone : deficitZones) {
            System.out.println("Current Deficit Zone: " + deficitZone.getZoneNumber());
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
                    neutralZones.add(deficitZone);
//                    deficitZones.remove(deficitZone);
                } else if (surplus <= deficit) {
                    moveTaxis(surplus, deficitZone, surplusZone);
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

//            taxi.setZone(deficitZone);
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
}
