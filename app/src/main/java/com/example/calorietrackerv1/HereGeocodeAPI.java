package com.example.calorietrackerv1;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is responsible to create a link between Calorie Tracker and HereGeocodeAPI, which
 * contains the information about POIs (Points of Interest). In this case, location of Parks.
 */
public class HereGeocodeAPI {
    private static final String APP_ID = "8dNkxblUCNm5CKu4l556";
    private static final String APP_CODE = "VNakV9DkGFmWJ3OP4q04Bw";

    /**
     * This method is responsible of create the connection between the Calorie Tracker and
     * Here Geocode Api
     * @param latitude A String value with the Latitude of the user.
     * @param longitude A String value with the Longitude of the user.
     * @return A String with the information of the Parks, 5 kilometers around the user.
     * @throws JSONException A exception if the JSON conversion fails.
     */
    public static String search(String latitude, String longitude) throws JSONException {
        URL url = null;
        HttpURLConnection connection = null;
        String textResult="";

        try {
            url = new URL("https://places.cit.api.here.com/places/v1/discover/search?" +
                    "app_id=" + APP_ID +
                    "&app_code=" + APP_CODE +
                    "&at="+latitude+","+longitude+";r=5000" +
                    "&q=parks" +
                    "&size=5");
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
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
     * This method is responsible of create an ArrayList with the latitude, longitude or
     * park name.
     * @param result A String with the JSONObject which contains the information about parks.
     * @param criteria A String which allow to the method filter by Latitude, longitude or park name.
     * @return An ArrayList with the information by criteria.
     * @throws JSONException A exception if the JSON conversion fails.
     */
    public static ArrayList<String> getInformationAboutParks (String result, String criteria) throws JSONException{
        ArrayList<String> list = new ArrayList<>();
        JSONObject jsonObject;
        JSONArray jsonArray=new JSONArray();
        try{
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONObject("results").getJSONArray("items");

        }catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0; i<jsonArray.length(); i++){
            try {
                if (criteria.equals("latitude") || criteria.equals("longitude")) {
                    String coordinateXY = jsonArray.getJSONObject(i).getString("position");
                    String coordinateXYFormat[] = coordinateXY.substring(1,coordinateXY.length()-1).split("\\,");
                    if(criteria.equals("latitude"))
                        list.add(coordinateXYFormat[0]);
                    if(criteria.equals("longitude"))
                        list.add(coordinateXYFormat[1]);
                }
                if (criteria.equals("parkName")){
                    String parkName = jsonArray.getJSONObject(i).getString("title");
                    list.add(parkName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}