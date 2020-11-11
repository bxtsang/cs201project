import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import data.Feature;
import data.Response;
import data.Taxi;

public class TaxiData {
    private static final String endpoint = "https://api.data.gov.sg/v1/transport/taxi-availability";
    private static final Gson gson = new Gson();

    public static Response getData() throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        StringBuffer content = new StringBuffer();

        int responseCode = con.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        }
        con.disconnect();

        Response response = gson.fromJson(String.valueOf(content), Response.class);
        return response;
    }

    public static List<Taxi> getAvailableTaxis() {
        List<Taxi> availableTaxis = new ArrayList<>();

        try {
            Response response = getData();
            List<Feature> features = response.getFeatures();
            Feature feature = features.get(0);
            List<List<Double>> coordinates = feature.getGeometry().getCoordinates();

            for (int i = 0; i < coordinates.size(); i++) {
                availableTaxis.add(new Taxi(coordinates.get(i), i));
            }
        } catch (Exception e) {
            System.out.println("Error when accessing API");
            return null;
        }
        return availableTaxis;
    }
}
