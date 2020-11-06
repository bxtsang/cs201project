package data;

import java.util.List;
import java.util.Map;

public class Response {
    private String type;
    private List<Feature> features;

    public void setType(String type) {
        this.type = type;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public String getType() {
        return type;
    }

    public List<Feature> getFeatures() {
        return features;
    }
}
