import java.io.File;
import java.io.IOException;
import java.util.*;

public class AddressUtilities {

    //Given a Taxi and the reference points, iterate through the entire list of reference points to find the minimum value
    //This method will set the Taxi's Zone
    public static void findNearestZone(Taxi aTaxi, HashMap<Integer, Address> referencePoints){

    }

    //This method calculates the distance between two points - a taxi and a reference point
    public static double calculateDistance (double lon1, double lat1, double lon2, double lat2){
        
        return 0;
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