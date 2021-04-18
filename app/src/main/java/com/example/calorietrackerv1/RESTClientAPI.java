package com.example.calorietrackerv1;


import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


/**
 * This class is responsible to create a link between Calorie Tracker and the Server Calorie Tracker
 * This class contains all the GET and POST methods used by: User, Credential, Food, Category,
 * Serving, Consumption and Report.
 */
public class RESTClientAPI {
    private static final String BASE_URL = "http://10.0.2.2:31640/CalorieTrackerV2/webresources/";

    /**
     * This method GET all the Categories in the Server
     * @return A String with the JSONObject of Categories.
     */
    public static String findAllCategories(){
        String textResult = "";
        textResult = getInformation("restws.category/");
        return textResult;
    }

    /**
     * This method GET all the Servings in the Server
     * @return A String with the JSONObject of Servings.
     */
    public static String findAllServings(){
        String textResult = "";
        textResult = getInformation("restws.serving/");
        return textResult;
    }

    /**
     * This method GET all the Foods in the Server
     * @return A String with the JSONObject of Foods.
     */
    public static String findAllFood(){
        String textResult = "";
        textResult = getInformation("restws.food/");
        return textResult;
    }

    /**
     * This method GET all the Foods by Category in the Server
     * @return A String with the JSONObject of Food.
     */
    public static String findFoodByCategoryName(String categoryName){
        String textResult = "";
        textResult = getInformation("restws.food/findByCategoryName/" + categoryName);
        return textResult;
    }

    /**
     * This method GET  a Categories in the Server by Name
     * @return A String with the JSONObject of the Category by Name
     */
    public static String findByCategoryName(String categoryName){
        String textResult = "";
        textResult = getInformation("restws.category/findByCategoryName/" + categoryName);
        return textResult;
    }

    /**
     * This method GET  a Serving in the Server by serving Unit.
     * @return A String with the JSONObject of the Serving by serving unit.
     */
    public static String findByServingUnit(String servingUnit){
        String textResult = "";
        textResult = getInformation("restws.serving/findByServingUnit/" + servingUnit);
        return textResult;
    }

    /**
     * This method GET a Credential in the Server searching by username.
     * @return A String with the JSONObject of the Credential by username.
     */
    public static String findByUsername(String username){
        String textResult = "";
        textResult = getInformation("restws.credential/findByUsername/" + username);
        Log.i("Username:",textResult);
        return textResult;
    }

    /**
     * This method GET a User in the Server searching by email
     * @return A String with the JSONObject of the User by email
     */
    public static String findByEmail(String email){
        String textResult = "";
        textResult = getInformation("restws.users/findByEmail/" + email);
        Log.i("Email:",textResult);
        return textResult;
    }

    /**
     * This method GET a Food in the Server searching by food name
     * @return A String with the JSONObject of the Food by food name
     */
    public static String findByFoodName(String foodName){
        String textResult = "";
        textResult = getInformation("restws.food/findByFoodName/" + foodName);
        return textResult;
    }

    /**
     * This method GET a User in the Server searching by id
     * @return A String with the JSONObject of the User by id
     */
    public static String findByUserId(String id){
        String textResult = "";
        textResult = getInformation("restws.users/" + id);
        return textResult;
    }

    /**
     * This method GET the total calories consumed by user in a day
     * @return A String with the JSONObject the calories consumed.
     */
    public static String calculateTotalCaloriesConsumedByUser(String userId, String consumptionDate){
        String textResult = "";
        textResult = getInformation("restws.consumption/calculateTotalCaloriesConsumedByUser/"
                + userId +"/" + consumptionDate);
        return textResult;
    }

    /**
     * This method GET the total calories burned per step by user.
     * @return A String with the JSONObject the calories burned.
     */
    public static String calculateCaloriesBurnedPerStep(String userId){
        String textResult = "";
        textResult = getInformation("restws.users/calculateCaloriesBurnedPerStep/"
                + userId);
        return textResult;
    }

    /**
     * This method GET the total calories burned at rest by user.
     * @return A String with the JSONObject the calories burned at rest.
     */
    public static String calculateDailyCaloriesBurnedAtRest(String userId){
        String textResult = "";
        textResult = getInformation("restws.users/calculateDailyCaloriesBurnedAtRest/"
                + userId);
        return textResult;
    }

    /**
     * This method GET  a report with the balance of calories by user.
     * @return A String with the JSONObject with the balance of calories by user.
     */
    public static String reportCaloriesBalanceByUser(String userId, String date){
        String textResult = "";
        textResult = getInformation("restws.report/reportCaloriesBalanceByUser/"
                + userId + "/" + date);
        return textResult;
    }

    /**
     * This method GET  a report with the balance of calories by user per period.
     * @return A String with the JSONObject with the balance of calories per period by user.
     */
    public static String reportCaloriesBalancePerPeriod(String userId, String sdate, String edate){
        String textResult = "";
        textResult = getInformation("restws.report/reportCaloriesBalancePerPeriod/"
                + userId + "/" + sdate + "/" + edate);
        return textResult;
    }

    /**
     * This method POST a Serving to the server
     * @param serving A Serving object to be post.
     * @return A String with the result.
     */
    public static String createServing(Serving serving){
        final String methodPath="restws.serving/";
        Gson gson =new Gson();
        String stringJson = gson.toJson(serving);
        String result = postInformation(methodPath, stringJson);
        if (!result.equals("400"))
            return "Serving was created";
        else
            return "Serving was not created";
    }

    /**
     * This method POST a Category to the server
     * @param category A Category object to be post.
     * @return A String with the result.
     */
    public static String createCategory(Category category){
        final String methodPath="restws.category/";
        Gson gson =new Gson();
        String stringJson = gson.toJson(category);
        String result = postInformation(methodPath, stringJson);
        if (!result.equals("400"))
            return "Category was created";
        else
            return "Category was not created";
    }

    /**
     * This method POST a Consumption to the server
     * @param consumption A Consumption object to be post.
     * @return A String with the result.
     */
    public static String createConsumption(Consumption consumption) {
        final String methodPath = "restws.consumption/";
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
        String stringJson = gson.toJson(consumption);
        String result = postInformation(methodPath, stringJson);
        if (!result.equals("400"))
            return "This item was added";
        else
            return "This item was not created";
    }

    /**
     * This method POST a Credential to the server
     * @param credential A Credential object to be post.
     * @return A String with the result.
     */
    public static String createCredential(Credential credential) {
        final String methodPath = "restws.credential/";
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
        String stringJson = gson.toJson(credential);
        String result = postInformation(methodPath, stringJson);
        if (!result.equals("400"))
            return "Credential was created";
        else
            return "Credential was not created";
    }

    /**
     * This method POST a Food to the server
     * @param food A Food object to be post.
     * @return A String with the result.
     */
    public static String createFood(Food food){
        final String methodPath="restws.food/";
        Gson gson =new Gson();
        String stringJson = gson.toJson(food);
        String result = postInformation(methodPath, stringJson);
        if (!result.equals("400"))
            return "Food was created";
        else
            return "Food was not created";
    }

    /**
     * This method POST a Report to the server
     * @param report A Report object to be post.
     * @return A String with the result.
     */
    public static String createReport(Report report) {
        final String methodPath = "restws.report/";
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
        String stringJson = gson.toJson(report);
        String result = postInformation(methodPath, stringJson);
        if (!result.equals("400"))
            return "Report was created";
        else
            return "Report was not created";
    }

    /**
     * This method POST a User to the server
     * @param user A User object to be post.
     * @return A String with the result.
     */
    public static String createUser(User user) {
        final String methodPath = "restws.users/";
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
        String stringJson = gson.toJson(user);
        String result = postInformation(methodPath, stringJson);
        if (!result.equals("400"))
            return "User was created";
        else
            return "User was not created";
    }

    /**
     * This method establishes a connection with the server to GET information.
     * @param path A string with the path to be searched
     * @return A JSONObject with the result.
     */
    private static String getInformation(String path) {
        final String methodPath = path;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //Making HTTP request
        //Log.i("URL:",BASE_URL + methodPath);
        try {
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input stream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    /**
     * This method establishes a connection with the server to POST information.
     * @param methodPath A string with the path to be searched
     * @return A JSONObject with the result.
     */
    private static String postInformation(String methodPath, String stringJson)
    {
        String code = "0";
        URL url = null;
        HttpURLConnection conn = null;
        //final String methodPath="restws.serving/";
        try {
            //Gson gson =new Gson();
            //String stringCourseJson=gson.toJson(stringJson);
            url = new URL(BASE_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to POST
            conn.setRequestMethod("POST");
            //set the output to true
            conn.setDoOutput(true);
            //set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringJson.getBytes().length);
            //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
            //Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringJson);
            out.close();
            //Log.i("error",new Integer(conn.getResponseCode()).toString());
            //Log.i("error", conn.getResponseMessage());
            code = new Integer(conn.getResponseCode()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return code;
    }

}