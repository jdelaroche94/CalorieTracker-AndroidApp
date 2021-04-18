package com.example.calorietrackerv1;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import java.util.List;

/**
 * This Class is responsible to create the My Daily Diet Screen which permits the user
 * to select food by category. This class create the consumption by each food item selected
 */
public class DailyDietFragment extends Fragment {
    private View vDaily;
    private Spinner inputCategory;
    private Spinner inputFood;
    private String userId;

    /**
     * This method display the information about the food by category which should be selected
     * by the user to create a new Consumption.
     * @param inflater A inflater which displays the fragment inside the navigation drawer.
     * @param container A group of elements which are contained by the Daily Diet Fragment.
     * @param savedInstanceState
     * @return A view with the Daily Diet Fragment Screen
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        vDaily = inflater.inflate(R.layout.fragment_daily_diet, container,
                false);
        inputCategory = (Spinner) vDaily.findViewById(R.id.categorySpinner);
        inputFood = (Spinner) vDaily.findViewById(R.id.foodSpinner);
        userId = getArguments().getString("userId");

        //Input of Categories
        FindCategoryAsyncTask categoryAsyncTask = new FindCategoryAsyncTask();
        categoryAsyncTask.execute();
        inputCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * This method is responsible to detect which category was selected and sends and
             * async task looking all the foods which are part of this Category
             * @param parent An Adapter View with the information of the Spinner
             * @param view A view of Daily Diet Fragment.
             * @param position An integer with the position selected in the list.
             * @param id An integer with an ID of the element selected.
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                if (!category.equals(" ")){
                    FindFoodAsyncTask foodAsyncTask = new FindFoodAsyncTask();
                    foodAsyncTask.execute(category);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        inputFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * This method is responsible to detect which food was selected and sends and
             * async task which creates the Consumption
             * @param parent An Adapter View with the information of the Spinner
             * @param view A view of Daily Diet Fragment.
             * @param position An integer with the position selected in the list.
             * @param id An integer with an ID of the element selected.
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String food = parent.getItemAtPosition(position).toString();
                if (!food.equals(" ")){
                    CreateConsumptionAsyncTask consumptionAsyncTask = new CreateConsumptionAsyncTask();
                    consumptionAsyncTask.execute(food);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return vDaily;
    }

    /**
     * This class is responsible to create an Async Task which finds the Food Categories
     * and updates the list of categories of this screen.
     */
    private class FindCategoryAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * This method is responsible to query the server using the RESTClientAPI.
         * @return A string with the result.
         */
        @Override
        protected String doInBackground(String... params) {
            String categories = RESTClientAPI.findAllCategories();
            return categories;
        }

        /**
         * This method is responsible to update the fields on the screen with the list of
         * categories.
         * @param result A String with the result of the previous query
         */
        @Override
        protected void onPostExecute(String result) {
            List<String> listCategory = null;
            try {
                listCategory = Input.jsonToArrayList(result,"categoryName");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (listCategory != null){
                final ArrayAdapter<String> categoryAdapter;
                categoryAdapter = new ArrayAdapter<String>(getActivity()
                        ,android.R.layout.simple_spinner_item , listCategory);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                inputCategory.setPrompt("Category:");
                inputCategory.setAdapter(categoryAdapter);
            }
        }
    }

    /**
     * This class is responsible to create an Async Task which finds the Food  by an
     * specific category and updates the list of food of this screen.
     */
    private class FindFoodAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * This method is responsible to query the server using the RESTClientAPI.
         * @return A string with the result.
         */
        @Override
        protected String doInBackground(String... params) {
            String food = RESTClientAPI.findFoodByCategoryName(params[0]);
            return food;
        }
        /**
         * This method is responsible to update the fields on the screen with the food of the
         * respectively category.
         * @param result A String with the result of the previous query
         */
        @Override
        protected void onPostExecute(String result) {
            List<String> listFood = null;
            try {
                listFood = Input.jsonToArrayList(result,"foodName");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (listFood != null){
                final ArrayAdapter<String> foodAdapter;
                foodAdapter = new ArrayAdapter<String>(getActivity()
                        ,android.R.layout.simple_spinner_item , listFood);
                foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                inputFood.setPrompt("Food:");
                inputFood.setAdapter(foodAdapter);
            }
        }
    }

    /**
     * This class is responsible to create an Async Task which POST the information of a
     * Consumption of the day to the RESTClientAPI Consumption POST method.
     */
    private class CreateConsumptionAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * This method is responsible to POST the information to the Consumption in the RESTClientAPI
         * @return A String value with the result of the Consumption POST.
         */
        @Override
        protected String doInBackground(String... params) {
            String result = "Item no added";
            String actualDate = Input.getActualDate("date");
            java.sql.Date consumptionDate = Input.manageDate(actualDate);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
            String infoUser = RESTClientAPI.findByUserId(userId);
            User user = gson.fromJson(infoUser, User.class);
            String infoFood = RESTClientAPI.findByFoodName(params[0]);
            if(infoFood.length()>2){
                String jsonFood = infoFood.substring(1, infoFood.length() - 1);
                Food food = gson.fromJson(jsonFood, Food.class);
                Consumption consumption = new Consumption(null, user, food, consumptionDate, 1);
                result = RESTClientAPI.createConsumption(consumption);
            }
            return result;
        }
        /**
         * This method is responsible to show the information about the creation of the Consumption
         * @param result A String value with the information of the result of the POST.
         */
        @Override
        protected void onPostExecute(String result) {
            Toast toast =
                    Toast.makeText(getActivity().getApplicationContext(),
                            result, Toast.LENGTH_SHORT);
            toast.show();

        }
    }


}
