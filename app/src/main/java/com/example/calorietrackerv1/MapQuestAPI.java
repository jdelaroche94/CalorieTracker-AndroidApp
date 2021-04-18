package com.example.calorietrackerv1;


import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * This class is responsible to create a link between Calorie Tracker and MapQuestAPI, which
 * sends information about the location by latitude and longitude of the user.
 */
public class MapQuestAPI {
    private static final String CONSUMER_KEY = "4rnZG3uyTxYR3swwRBcb8gs2r41SdJVV";
    private static final String CONSUMER_SECRET = "GsddO443uG41uB2x";

    /**
     * This method is responsible of create the connection between the Calorie Tracker and
     * MapQuest Api
     * @param keyword A String with the Address and postcode to be searched
     * @param ignoreLatLng This parameter for the location always come in yes.
     * @param maxResults An Integer with the maximum of result.
     * @return A String with the information Latitude/Longitude of the User.
     */
    public static String search(String keyword, String ignoreLatLng, int maxResults) {
        URL url = null;
        HttpURLConnection connection = null;
        String textResult="";
        String query_parameter="";

        try {
            url = new URL("http://www.mapquestapi.com/geocoding/v1/address?key=" +
                    CONSUMER_KEY +"&location="+ keyword + "&ignoreLatLngInput="+ignoreLatLng+"&maxResults=" +
                    maxResults);
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }
        return textResult;
    }

    /**
     * This method is responsible to send a String with the latitude/longitude requested
     * @param result A String with the JSONObject which contains the information about the location
     *               of the user.
     * @param coordinate A String with the coordinate requested : Latitude/Longitude
     * @return A String with the coordinate requested.
     * @throws JSONException A exception if the JSON conversion fails.
     */
    public static String getCoordinate(String result, String coordinate) throws JSONException {
        String value = "";
        String latiLong = new JSONObject(result)
                .getJSONArray("results")
                .getJSONObject(0)
                .getJSONArray("locations")
                .getJSONObject(0)
                .getString("latLng");
        JSONObject jArray = new JSONObject(latiLong);
        try {
            value = jArray.getString(coordinate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }
}