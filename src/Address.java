public class Address {
    private double lon;
    private double lat;
    private int clusterNumber;

    public Address(double lon, double lat, int clusterNumber){
        this.lon = lon;
        this.lat = lat;
        this.clusterNumber = clusterNumber;
    }

    public double getLon(){
        return lon;
    }

    public double getLat(){
        return lat;
    }


    public int getClusterNumber(){
        return clusterNumber;
    }




}
