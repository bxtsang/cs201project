public class TaxiRouting {
    public static void main(String[] args) {
        // -------------------------pre processing-------------------------------------
        // create global collection of taxi, taxiCollection

        // get all zones (28 zones)

        // create a empty queue of zones

        // iterate through zones and call getDeficit() on all of them
        // if getDeficit() > 0, enqueue to queue

        // ----------------------- processing ------------------------
        // while queue is not empty, dequeue and process zone

        // processing zone:
        // for i in getDeficit, find closest taxi to zone in global collection
        //    mark taxi as isAssigned, remove taxi from original zone
    }
}
