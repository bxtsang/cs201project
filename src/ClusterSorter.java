import java.util.Comparator;
 
public class ClusterSorter implements Comparator<Address>{
    @Override
    public int compare(Address add01, Address add02){
        return add01.getClusterNumber().compareTo(add02.getClusterNumber());
    }
}
