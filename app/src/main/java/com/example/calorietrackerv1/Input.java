package com.example.calorietrackerv1;


import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Input {

    /**
     * This class create a List between two numbers in a range
     * @param minVal An Integer with the minimum value
     * @param maxVal An Integer with the maximum value
     * @param measure A string with the measure require by the list
     * @return A List of Strings which contains a number and the measure
     */
    public static List<String> createList (int minVal, int maxVal, String measure)
    {
        List<String> list = new ArrayList<String>();
        list.add(" ");
        for(int i = minVal; i <= maxVal ; i++)
            list.add(i+ " " + measure);
        return list;
    }

    /**
     * This method is responsible to create a List of Steps
     * @param steps A List of Steps
     * @return A List of String with the information of the steps.
     */
    public static List<String> createListFromDB (List<Step> steps) {
        List<String> list = new ArrayList<String>();
        list.add("Select information to update");
        if (!(steps.isEmpty() || steps == null) )
            for (Step temp : steps)
                list.add(temp.getId() + ". "+temp.getTime()+" - Steps:"+temp.getNumberSteps());
        return list;
    }

    /**
     * This method is responsible to create a List of foods
     * @param foods A List of Food
     * @return A List of String with the information of the foods.
     */
    public static List<String> createListFromFoodList (ArrayList<Food> foods) {
        List<String> list = new ArrayList<String>();
        list.add("Select food");
        if (!(foods.isEmpty() || foods == null) )
            for (Food temp : foods)
                list.add(temp.getFoodName());
        return list;
    }

    /**
     * This method is responsible to format the date depending on the option
     * @param option A String with the type of date required
     * @param day An Integer with the day.
     * @param month An Integer with the month
     * @param year An Integer with the year
     * @param hour An Integer with the hour
     * @param minutes An Integer with the minutes
     * @return A String with the format date.
     */
    public static String formatDateUserInput (String option, int day, int month,
                                              int year, int hour, int minutes){
        String formatDay = Integer.toString(day);
        String formatMonth = Integer.toString(month);
        String formatHour = Integer.toString(hour);
        String formatMinutes = Integer.toString(minutes);
        String date = "";
        if (day>=1 && day < 10 )
            formatDay = "0" + Integer.toString(day);
        if ((month + 1)>=1 && (month + 1) < 10 )
            formatMonth = "0" + (Integer.toString(month + 1));
        else
            formatMonth = (Integer.toString(month + 1));
        if (hour>=0 && hour < 10 )
            formatHour = "0" + Integer.toString(hour);
        if (minutes>=0 && minutes < 10 )
            formatMinutes = "0" + Integer.toString(minutes);
        if (option.equals("date"))
            date = formatDay + "-" + formatMonth + "-" + year;
        if (option.equals("time"))
            date = formatHour  + ":" + formatMinutes;
        if (option.equals("dateTime"))
            date = formatDay + "-" + formatMonth + "-" + year + " " + formatHour  +
                    ":" + formatMinutes;
        if (option.equals("dateYearFirst"))
            date = year + "-" + formatMonth + "-" + formatDay;
        if (option.equals("dateDayMonth"))
            date =  formatDay + "/" + formatMonth;
        return date;
    }

    /**
     * This is method is responsible to obtain the gender in a reduce form
     * @param gender A String Male or Female
     * @return A String M or F
     */
    public static String getGender(String gender){
        String simpleGender = "";
        if (gender.equals("Male"))
            simpleGender = "M";
        if (gender.equals("Female"))
            simpleGender = "F";
        return simpleGender;
    }

    /**
     * This method is responsible to calculate the total number of steps
     * @param steps A List of Step
     * @return An Integer with the total number of steps
     */
    public static int calculateTotalNumberOfSteps (List<Step> steps){
        int numberSteps = 0;
        if (!(steps.isEmpty() || steps == null) ){
            for (Step temp : steps)
                numberSteps += temp.getNumberSteps();
        }
        return numberSteps;
    }

    /**
     * This method is responsible to read a Internal File (Postcodes of Australia)
     * @param context An Activity which requires to read the file
     * @return A List of String with the information given in the file
     */
    public static List<String> readFromInternalFile(Context context) {
        List<String> list = new ArrayList<String>();
        list.add(" ");
        try {
            InputStream fileRaw = context.getResources().openRawResource(R.raw.auspost);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fileRaw));
            for (String line = buffer.readLine(); line != null; line = buffer.readLine()) {
                list.add(line);
            }
            fileRaw.close();
        }
        catch (Exception ex)
        {
            Log.e("File", "Issues to read from internal memory");
        }
        return list;
    }

    /**
     * This method is responsible to format the text without the least 3 chars
     * @param value A String with the value to be change
     * @return A String with the new string formatted
     */
    public static String formatText (String value){
        String result="";
        String temporalString = value.substring(0,value.length()-3).trim();
        return temporalString;
    }

    /**
     * This method is responsible to convert a String Date in a Date Sql
     * @param date A String with the date
     * @return A Date in Date Sql Form
     */
    public static Date manageDate (String date) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date temporalDate = null;
        try {
            temporalDate = sdf1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date finalDate = new java.sql.Date(temporalDate.getTime());
        return finalDate;
    }

    /**
     * This method is responsible to get the actual Date of the System
     * @param option The format which is required by the user
     * @return A String with the Actual Date formatted
     */
    public static String getActualDate(String option) {
        Calendar date = new GregorianCalendar();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int hour = date.get(Calendar.HOUR);
        int minutes = date.get(Calendar.MINUTE);
        return formatDateUserInput(option,day,month,year,hour,minutes);
    }

    /**
     * This method is responsible to obtain the first name from a String of Given Name
     * @param givenName A String with the given name.
     * @return A String with the first name
     */
    public static String getFirstName (String givenName){
        String firstName = givenName;
        try{
            firstName = givenName.substring(0,givenName.indexOf(" "));
        } catch (Exception e){}
        return firstName;
    }

    /**
     * This method is responsible to convert a JSON Object to an ArrayList
     * @param result A String with the JSON Object.
     * @param identification A String with the identification to be search.
     * @return A List of String by Identification
     * @throws JSONException If the conversion fails, a trace is returned.
     */
    public static List<String> jsonToArrayList(String result, String identification) throws JSONException {
        List<String> list = new ArrayList<String>();
        list.add(" ");
        JSONArray jsonArray = new JSONArray(result);
        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject json = jsonArray.getJSONObject(i);
                list.add(json.getString(identification));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * This method is responsible to convert a JSON Object to a String
     * @param result A String with the JSON Object.
     * @param identification A String with the identification to be search.
     * @return A String by Identification
     * @throws JSONException If the conversion fails, a trace is returned.
     */
    public static String jsonToString(String result, String identification) throws JSONException {
        String value = "";
        JSONArray jsonArray = new JSONArray(result);
        try {
            JSONObject json = jsonArray.getJSONObject(0);
            value = json.getString(identification);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * This is method is responsible to obtain a number from a String
     * @param text A String with the information
     * @return A String with the number
     */
    public static String captureNumberFromString(String text){
        StringBuffer number = new StringBuffer();
        char[] arrayChar = text.toCharArray();
        for (char character : arrayChar){
            if (Character.isDigit(character))
                number.append(character);
        }
        return number.toString();
    }

    /**
     * This method is responsible to check if a Item is a List of food.
     * @param item A String with the item to be searched
     * @param foodList A List of food
     * @return A boolean value, true if the item is in List of food, false if it is not.
     */
    public static boolean isItemInListOfFood(String item, ArrayList<Food> foodList){
        boolean validation = false;
        for (int i=0; i<foodList.size(); i++){
            if(item.equals(foodList.get(i).getFoodName()))
                validation = true;
        }
        return validation;
    }

    /**
     * This method is responsible to check if a Item is a List of Category.
     * @param item A String with the item to be searched
     * @param categoryList A List of Category
     * @return A boolean value, true if the item is in List of category, false if it is not.
     */
    public static Category itemInListOfCategory(String item, ArrayList<Category> categoryList){
        Category category = null;
        for (int i=0; i<categoryList.size(); i++){
            if(item.equals(categoryList.get(i).getCategoryName()))
                category = categoryList.get(i);
        }
        return category;
    }

    /**
     * This method is responsible to check if a Item is a List of Serving.
     * @param item A String with the item to be searched
     * @param servingList A List of Serving
     * @return A boolean value, true if the item is in List of serving, false if it is not.
     */
    public static Serving itemInListOfServing(String item, ArrayList<Serving> servingList){
        Serving serving = null;
        for (int i=0; i<servingList.size(); i++){
            if(item.equals(servingList.get(i).getServingUnit()))
                serving= servingList.get(i);
        }
        return serving;
    }

    /**
     * This method valid if a string is numeric
     * @param valueTyped A String with the value typed by the user.
     * @return A boolean value, true if value typed is numeric, false if it isn't.
     */
    public static boolean isStringNumeric(String valueTyped)
    {
        try
        {
            Integer.parseInt(valueTyped);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }
}
