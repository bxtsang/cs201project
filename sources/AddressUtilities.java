import java.io.File;
import java.io.IOException;
import java.util.*;

import java.lang.Math;
import data.Taxi;
import data.Zone;

public class AddressUtilities {

    /**
     * This method takes in these four parameters, and then updates the list of zones with the zone's reference points, demand and their corresponding list of taxis
     * @param zones A list of zones to populate
     * @param referencePoints A HashMap of reference points to update each zone based on its cluster number
     * @param availableTaxis A list of all available taxis
     * @param demand A HashMap of key-value pairs of cluster number and their corresponding demands
     */
    public static void updateZones(List<Zone> zones, HashMap<Integer, Address> referencePoints, List<Taxi> availableTaxis, 
        HashMap<Integer, Integer> demand){
        //For each key in reference points, get all taxis in available taxis belonging to that cluster
        //Create a list, and along with the reference point, create a Zone

        List<Taxi> taxiList = new ArrayList<>();

        for (Integer i : referencePoints.keySet()){
            Address reference = referencePoints.get(i);
            for (Taxi T : availableTaxis){
                if (T.getClusterNum() == i){
                    taxiList.add(T);
                }
            }
            Integer demandForZone = demand.get(i);

            Zone newZone = new Zone(i, reference.getLon(), reference.getLat());
            newZone.setTaxis(taxiList);
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
    public static Address findReferencePoint(Integer clusterNumber, HashMap<Integer, List<Address>> clusteredAddresses){

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
     * @param myAddresses Supporting dataset to define the clusters within Singapore
     * @return 
     */
    public static HashMap<Integer, List<Address>> initHashMap(ArrayList<Address> myAddresses){
        //Since the address dataset is already pre-sorted in the ascending order based on clusters, currentCluster will be #1
        Integer currentCluster = myAddresses.get(0).getClusterNumber();
        List<Address> tempList = new ArrayList<>();

        HashMap<Integer, List<Address>> clusteredAddresses = new HashMap<>();

        //For every address that is given, if the address' cluster number matches the initialized value, add it into the list
        for (Address anAddress: myAddresses){
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
            System.out.println("cluster number = " + e.getKey() + ", count = " + e.getValue().size());
        }

        return clusteredAddresses;
    }


    /**
     * Method to load addresses from the CSV file (Supporting dataset)
     * @param myAddresses Variable to store the lines of addresses that are to be read
     */    
    public static void loadAddresses(ArrayList<Address> myAddresses) {

        Scanner sc = null;

        try {
            
            sc = new Scanner(new File("sources/data/add.csv"));
            sc.useDelimiter(",|\n|\r\n");
            while (sc.hasNext()) {

                double lon = sc.nextDouble();
                double lat = sc.nextDouble();
                int clusterNum = sc.nextInt();
                Address oneAddress = new Address(lon, lat, clusterNum);
                myAddresses.add(oneAddress);
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