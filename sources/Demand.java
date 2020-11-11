import java.util.HashMap;
import java.util.Map;

public class Demand {
    public static Map<Integer, Integer> getDemand(int numAvailableTaxis) {
        int demand = 150;
        HashMap<Integer, Integer> DemandForAllZones = new HashMap<>();
        for (int i = 1; i < 29; i++) {
            DemandForAllZones.put(i, demand);
        }

        return DemandForAllZones;
    }
}
