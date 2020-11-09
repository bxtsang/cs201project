package data;

public class Properties {
    private String timestamp;
    private int taxiCount;

    public int getTaxiCount() {
        return taxiCount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTaxiCount(int taxiCount) {
        this.taxiCount = taxiCount;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
