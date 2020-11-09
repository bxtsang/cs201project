import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaxiRouting {
    
    public static void main(String[] args) {
        HashMap<Integer, List<Address>> clusteredAddresses = new HashMap<>(); 

        //Load all supporting dataset into arrayList
        ArrayList<Address> supportingAddresses = new ArrayList<>();
        FindClusters.loadAddresses(supportingAddresses);

        //Initialize a HashMap of key-value pairs of ZoneNumber & all its addresses
        clusteredAddresses = FindClusters.initHashMap(supportingAddresses);
        HashMap<Integer, Address> referencePoints = new HashMap<>();

        //Populate the HashMap with all Key-Value pairs
        for (Integer i : clusteredAddresses.keySet()){
            referencePoints.put(i, FindClusters.findReferencePoint(i, clusteredAddresses));
        }

        System.out.println(referencePoints);
    }
}
