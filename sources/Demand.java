import java.util.HashMap;
import java.util.Map;

public class Demand {
    public static Map<Integer, Integer> getDemand() {
        HashMap<Integer, Integer> DemandForAllZones = new HashMap<>();
        DemandForAllZones.put(1,3042); // no deficit
        DemandForAllZones.put(2, 1601); // no deficit
        DemandForAllZones.put(3, 1867); // no deficit
        DemandForAllZones.put(4,1318); // no deficit
        DemandForAllZones.put(5,4876); // no deficit
        DemandForAllZones.put(6,200); // deficit, current count = 187
        DemandForAllZones.put(7, 1702); // no deficit
        DemandForAllZones.put(8, 4270); // no deficit
        DemandForAllZones.put(9, 2247); // no deficit
        DemandForAllZones.put(10, 9790); // no deficit
        DemandForAllZones.put(11, 5942); // no deficit
        DemandForAllZones.put(12, 2746); // no deficit
        DemandForAllZones.put(13, 4888); // no deficit
        DemandForAllZones.put(14, 8417); // no deficit
        DemandForAllZones.put(15, 10000); // oversupply, current count = 14002
        DemandForAllZones.put(16, 7896); // no deficit
        DemandForAllZones.put(17, 2503); // no deficit
        DemandForAllZones.put(18, 3448); // no deficit
        DemandForAllZones.put(19, 17000); // oversupply, current count = 17535
        DemandForAllZones.put(20, 7869); // no deficit
        DemandForAllZones.put(21, 4611); // no deficit
        DemandForAllZones.put(22, 7499); // no deficit
        DemandForAllZones.put(23, 6263); // no deficit
        DemandForAllZones.put(24, 400); // deficit, current count = 289
        DemandForAllZones.put(25, 2883); // no deficit
        DemandForAllZones.put(26, 3227); // no deficit
        DemandForAllZones.put(27, 4567); // no deficit
        DemandForAllZones.put(28, 6150); // no deficit

        return DemandForAllZones;
    }
}
