package com.example.calorietrackerv1;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * This class is responsible to create a link between the Calorie Tracker and Custom Google Search
 * Api.
 */
public class GoogleSearchAPI {
    //private static final String API_KEY = "1234";


    private static final String API_KEY = "1234";
    private static final String SEARCH_ID_cx = "1234";
    private static final String SEARCH_ID_cx1 = "1234";

    /**
     * This method is responsible of create the connection between the Calorie Tracker and
     * Google Custom Search
     * @param keyword A String with the word to be search
     * @param params An Array with the different parameters to be used in the connection.
     * @param values An Array with the values taken by those parameters.
     * @return A String with a JSON Object which contains the information searched.
     */
    public static String search(String keyword, String[] params, String[] values) {
        String newKeyword[];
        if(keyword.indexOf(" ")!=-1){
            newKeyword = keyword.split("\\ ");
            if (newKeyword.length == 2)
                keyword = "\"" +newKeyword[0] +" "+newKeyword[1]+"\"";
            if (newKeyword.length >=3)
                keyword = "\"" +newKeyword[0] +" "+newKeyword[1] + " " + newKeyword[2] + "\"";
        }
        keyword = keyword + " food";
        keyword = keyword.replace(" ",
                "+");
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String query_parameter="";
        if (params!=null && values!=null){
            for (int i =0; i < params.length; i ++){
                query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];
            }
        }
        try {
            if (!params[0].equals("searchType"))
                url = new URL("https://www.googleapis.com/customsearch/v1?key="+
                        API_KEY+ "&cx="+ SEARCH_ID_cx + "&q="+ keyword + query_parameter);
            else
                url = new URL("https://www.googleapis.com/customsearch/v1?key="+
                        API_KEY+ "&cx="+ SEARCH_ID_cx1 + "&q="+ keyword + query_parameter);
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
     * This method obtain a Snippet from the JSONObject received from Google.
     * @param result A String with a JSON Objects which contains the snippet.
     * @return A String with the Snippet.
     * @throws JSONException A exception if the JSON conversion fails.
     */
    public static String getSnippet(String result) throws JSONException {
        String snippet = null;

        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if(jsonArray != null && jsonArray.length() > 0) {
                snippet =jsonArray.getJSONObject(0).getString("snippet");
                snippet = snippet.replaceAll("(\n|\r)", "").split("\\.")[0];
            }
        }catch (Exception e){
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        return snippet;
    }

    /**
     * This method is responsible to obtain a link with a picture regarding the food searched
     * @param result A String with the JSONObject which contains the link
     * @return A String with the URL where the Image is.
     * @throws JSONException A exception if the JSON conversion fails.
     */
    public static String getPictureUrl(String result) throws JSONException {
        String link = "";
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            if(jsonArray != null && jsonArray.length() > 0) {
                link = jsonArray.getJSONObject(0).getString("link");
                Log.i("link",link);
            }
        }catch (Exception e){
            e.printStackTrace();
            link = "NO INFO FOUND";
        }
        return link;
    }


}
