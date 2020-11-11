import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Set;

import java.lang.Math;
import data.Taxi;
import data.Zone;

public class AddressUtilities {
    private static ArrayList<Address> supportingAddresses = new ArrayList<>();
    private static Map<Integer, List<Address>> clusteredAddresses = new HashMap<>();


    /**
     * Given a taxi, find the closest address in the taxi's assigned zone
     * Method also updates the taxi's travelling distance 
     */
    public static Address findNearestAddress(Taxi taxi){

        Zone assignedZone = taxi.getAssignedZone();
        List<Address> addresses = clusteredAddresses.get(assignedZone.getZoneNumber());
        double currentLon = taxi.getLon();
        double currentLat = taxi.getLat();
        double toTravel = 10000000.0;
        
        int addressPointer = 0;

        //For every address within a zone, find the nearest address to the taxi's current position
        for (int i = 0; i < addresses.size(); i++){
            Address add = addresses.get(i);
            double distance = calculateDistance(currentLon, currentLat, add.getLon(), add.getLat());
            if (distance < toTravel){
                toTravel = distance;
                addressPointer = i;
            }
        }

        taxi.setDistanceTravelled(toTravel);
        return addresses.get(addressPointer);

    }



    /**
     * This method takes in these four parameters, and then updates the list of zones with the zone's reference points, demand and their corresponding list of taxis
     * @param zones A list of zones to populate
     * @param referencePoints A HashMap of reference points to update each zone based on its cluster number
     * @param availableTaxis A list of all available taxis
     */
    public static void updateZones(List<Zone> zones, HashMap<Integer, Address> referencePoints, List<Taxi> availableTaxis){
        //For each key in reference points, get all taxis in available taxis belonging to that cluster
        //Create a list, and along with the reference point, create a Zone
        Map<Integer, Integer> demand = Demand.getDemand();

        List<Taxi> taxiList = new ArrayList<>();

        for (Integer i : referencePoints.keySet()){
            Address reference = referencePoints.get(i);
            for (Taxi T : availableTaxis){
                if (T.getClusterNum() == i){
                    taxiList.add(T);
                }
                //Set the zone for every taxi
                T.setZone(new Zone(i, reference.getLon(), reference.getLat()));
            }
            Integer demandForZone = demand.get(i);

            Zone newZone = new Zone(i, reference.getLon(), reference.getLat());
            Set<Taxi> taxiSet = new HashSet<>(taxiList);
            newZone.setTaxis(taxiSet);
            newZone.setDemand(demandForZone);
            //Put the demand hashmap as a param for this method and then setDemand here
            zones.add(newZone);
            
            taxiList = new ArrayList<>();
        }

    }

    //Given a Taxi and the reference points, iterate through the entire list of reference points to find the minimum value
    //This method will set the Taxi's Zone
    public static void findNearestZone(Taxi aTaxi, HashMap<Integer, Address> referencePoints){
        double shortestDistance = 10000.0;
        Integer clusterNum = 0;
        for (Integer i: referencePoints.keySet()){
            Address reference = referencePoints.get(i);
            double distance = calculateDistance(aTaxi.getLon(), aTaxi.getLat(), reference.getLon(), reference.getLat());
            if (distance < shortestDistance){
                shortestDistance = distance;
                clusterNum = i;
            }
        }
        // System.out.println("Shortest Distance: " + shortestDistance);
        aTaxi.setClusterNum(clusterNum);
    }

    //This method calculates the distance between two points - a taxi and a reference point
    public static double calculateDistance (double lon1, double lat1, double lon2, double lat2){
        //Straight line distance - SQRT ( (x1 - x2)^2 + (y1 - y2)^2)

        return Math.sqrt(Math.pow(lon2 - lon1, 2) + Math.pow(lat2 - lat1, 2));
    }

    /**
     * This method will return the mean location of all the points within a cluster to be used as the cluster's reference/representative point
     * @param clusterNumber The cluster number that the user wants to find out the reference point for
     * @param clusteredAddresses A HashMap of all the addresses
     * @return 
     */
    public static Address findReferencePoint(Integer clusterNumber, Map<Integer, List<Address>> clusteredAddresses){

        if (!clusteredAddresses.containsKey(clusterNumber)){
            //Cluster not found - Exception can be written if necessary
            System.out.println("Cluster is not found");
            return null;
        }

        List<Address> filteredAddress = clusteredAddresses.get(clusterNumber);
        double lon = 0;
        double lat = 0;
        
        for (Address address : filteredAddress){
            lon += address.getLon();
            lat += address.getLat();
        }


        lon /= filteredAddress.size();
        lat /= filteredAddress.size();

        return new Address(lon, lat, clusterNumber);
    }



    /**
     * Method is used to create a HashMap where the Key is the Zone/Cluster number, and the values will be all the addresses that falls within the cluster
     * @return 
     */
    public static Map<Integer, List<Address>> initHashMap(){
        //Since the address dataset is already pre-sorted in the ascending order based on clusters, currentCluster will be #1
        Integer currentCluster = supportingAddresses.get(0).getClusterNumber();
        List<Address> tempList = new ArrayList<>();

        //For every address that is given, if the address' cluster number matches the initialized value, add it into the list
        for (Address anAddress: supportingAddresses){
            if (currentCluster == anAddress.getClusterNumber()){
                tempList.add(anAddress);
            } else {

                /*
                    Code reaches this block if the cluster's number is different, meaning that we've reached the next cluster number
                    The Key-Value pair is then added into the HashMap
                */
                clusteredAddresses.put(currentCluster, tempList);

                //Update the current clusterNumber
                currentCluster = anAddress.getClusterNumber();
                //Reinitialize a new arraylist
                tempList = new ArrayList<>();
            }
            
        }
        //This will be the last cluster addition into our hashmap
        clusteredAddresses.put(currentCluster,tempList);

        // prints all cluster numbers and their taxi counts
        for (Map.Entry<Integer, List<Address>> e: clusteredAddresses.entrySet()) {
//            System.out.println("cluster number = " + e.getKey() + ", count = " + e.getValue().size());
        }

        return clusteredAddresses;
    }


    /**
     * Method to load addresses from the CSV file (Supporting dataset)
     */    
    public static void loadAddresses() {

        Scanner sc = null;

        try {
            
            sc = new Scanner(new File("sources/data/add.csv"));
            sc.useDelimiter(",|\n|\r\n");
            while (sc.hasNext()) {

                double lon = sc.nextDouble();
                double lat = sc.nextDouble();
                int clusterNum = sc.nextInt();
                sc.nextLine();
                Address oneAddress = new Address(lon, lat, clusterNum);
                supportingAddresses.add(oneAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataException();
        } finally {
            if (sc != null) {
                sc.close();
            }
        }

    }

}