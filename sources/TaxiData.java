import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import data.Response;

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
}
