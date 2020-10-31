import java.io.File;
import java.io.IOException;
import java.util.*;

public class FindClusters{

    public static void main(String[] args){
        ArrayList<Address> myAddresses = new ArrayList<>();
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
            if (sc != null){
                sc.close();
            }
        }

        System.out.println(myAddresses.size());
    }


}