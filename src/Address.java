public class Address {
    private double lon;
    private double lat;
    private Integer clusterNumber;

    public Address(double lon, double lat, Integer clusterNumber){
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


    public Integer getClusterNumber(){
        return clusterNumber;
    }

    @Override
    public String toString(){
        return "Lon: " + getLon() + "\nLat:" + getLat() + "\nZone: " + getClusterNumber()+ "\n";
    }


}
