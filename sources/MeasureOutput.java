import data.Taxi;

import java.util.List;

public class MeasureOutput {
    public static void measureOutput(List<Taxi> assignedTaxis) {
        double distance = 0.0;
        double maxDistance = 0.0;

        for (Taxi taxi : assignedTaxis) {
            Address nearestAddress = AddressUtilities.findNearestAddress(taxi);
            double distanceMoved = AddressUtilities.calculateDistance(taxi.getLon(), taxi.getLat(), nearestAddress.getLon(), nearestAddress.getLat());

            distance += distanceMoved;
            if (distanceMoved > maxDistance) {
                maxDistance = distanceMoved;
            }
        }

        System.out.println("Total distance by all taxis --> " + distance);
        System.out.println("Maximum distance moved by a single taxi --> " + maxDistance);
    }
}
