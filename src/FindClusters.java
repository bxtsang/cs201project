import java.io.File;
import java.io.IOException;
import java.util.*;

public class FindClusters {

    public static void main(String[] args) {
        ArrayList<Address> myAddresses = new ArrayList<>();
        HashMap<Integer, ArrayList<Address>> clusterAddresses = new HashMap<>();
        
        loadAddresses(myAddresses);
        myAddresses.sort(new ClusterSorter());
        initHashMap(myAddresses, clusterAddresses);
        /*
            findMin(TaxiLon - AddLon, TaxiLat, AddLat) -> Return cluster Number
            referencePoint for each cluster, we'll just do mean(clusterAddress.get(clusterNumber))
            Do up the reference point for each cluster and the findMin function to determine which cluster each taxi belongs to

        */

    }

    public static void initHashMap(ArrayList<Address> myAddresses, HashMap<Integer, ArrayList<Address>> clusterAddresses){
        Integer currentCluster = myAddresses.get(0).getClusterNumber();
        ArrayList<Address> tempList = new ArrayList<>();

        for (Address anAddress: myAddresses){
            if (currentCluster == anAddress.getClusterNumber()){
                tempList.add(anAddress);
            } else {
                System.out.println(currentCluster);
                clusterAddresses.put(currentCluster, tempList);
                currentCluster = anAddress.getClusterNumber();
                tempList.clear();
            }
        }
    }

    public static void loadAddresses(ArrayList<Address> myAddresses) {

        Scanner sc = null;

        try {
            sc = new Scanner(new File("../data/add.csv"));
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