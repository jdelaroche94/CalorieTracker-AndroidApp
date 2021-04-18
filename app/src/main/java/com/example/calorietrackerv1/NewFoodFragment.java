package com.example.calorietrackerv1;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import fatsecret.platform.FatSecretException;

/**
 * This Class is responsible to the New Food Fragment on the screen and allows the user to
 * introduce a new food. This food is looking for in the FATSecretAPI and in Google, a picture
 * and a description of the food is displayed on the screen. Finally, this is created as a New
 * food using the regarding POST Method.
 */
public class NewFoodFragment extends Fragment implements View.OnClickListener {
    private View vNewFood;
    private TextInputLayout inputFoodName;
    private String userId;
    private Button bSearchFood;
    private ImageView foodImageView;
    private TextView tvResult;
    private ArrayList<Category> categoryList;
    private ArrayList<Serving> servingList;
    private ArrayList<Food> foodListInServer;
    private ArrayList<Food> foodListInSearching;

    /**
     * This method display a Screen which allows the user to search a new food, display an image
     * and description about this food.
     * @param inflater A inflater which displays the fragment inside the navigation drawer.
     * @param container A group of elements which are contained by the New Food Fragment.
     * @param savedInstanceState
     * @return A view with the New Food Screen
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        vNewFood = inflater.inflate(R.layout.fragment_new_food, container,
                false);
        inputFoodName = (TextInputLayout) vNewFood.findViewById(R.id.textInputFoodName);
        bSearchFood = (Button) vNewFood.findViewById(R.id.searchFoodButton);
        foodImageView = (ImageView) vNewFood.findViewById(R.id.foodImageView);
        tvResult = (TextView) vNewFood.findViewById(R.id.tvResultFood);
        userId = getArguments().getString("userId");
        bSearchFood.setOnClickListener(this);

        return vNewFood;
    }

    /**
     * This method is responsible to activate actions when the button Search is click on.
     * @param v A view of New Food Fragment.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchFoodButton: {
                String foodName = inputFoodName.getEditText().getText().toString();
                if (foodName.isEmpty()) {
                    inputFoodName.setError("Add a value");
                    return;
                }
                else {
                    SearchOnFatSecretAPIAsyncTask searchFSAPIAsyncTask=new SearchOnFatSecretAPIAsyncTask();
                    searchFSAPIAsyncTask.execute(foodName);
                }
                break;
            }
        }
    }

    /**
     * This class is responsible to create an Async Task which POST the information of a
     * new Food to be created by the RESTClientAPI Food POST method.
     */
    private class CreateFoodAsyncTask extends AsyncTask<Food, Void, String> {
        /**
         * This method is responsible to POST the information to the Food in the RESTClientAPI
         * @return A String value with the result of the Food POST.
         */
        @Override
        protected String doInBackground(Food... params) {
            String jsonArrayCategory = RESTClientAPI.findAllCategories();
            String jsonArrayServing = RESTClientAPI.findAllServings();
            Gson gson = new Gson();
            categoryList = gson.fromJson(jsonArrayCategory, new TypeToken<ArrayList<Category>>(){}.getType());
            servingList = gson.fromJson(jsonArrayServing, new TypeToken<ArrayList<Serving>>(){}.getType());
            Food food = params[0];
            Category category = null;
            Serving serving = null;
            food.setFoodId(null);
            if(Input.itemInListOfCategory(food.getCategoryId().getCategoryName(),categoryList)!=null)
                food.setCategoryId(Input.itemInListOfCategory(food.getCategoryId().getCategoryName(),categoryList));
            else{
                String categoryString = "";
                categoryString = RESTClientAPI.createCategory(
                                 new Category(null, food.getCategoryId().getCategoryName()));
                categoryString = RESTClientAPI.findByCategoryName(food.getCategoryId().getCategoryName());
                try {
                    categoryString = new JSONObject(categoryString).getString("categoryId");
                    food.setCategoryId(new Category(Long.parseLong(categoryString),food.getCategoryId().getCategoryName()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(Input.itemInListOfServing(food.getServingId().getServingUnit(),servingList)!=null)
                food.setServingId(Input.itemInListOfServing(food.getServingId().getServingUnit(),servingList));
            else {
                String servingString = "";
                servingString = RESTClientAPI.createServing(
                      new Serving(null, food.getServingId().getServingUnit()));
                servingString = RESTClientAPI.findByServingUnit(food.getServingId().getServingUnit());
                try {
                    servingString = new JSONObject(servingString).getString("servingId");
                    food.setServingId(new Serving(Long.parseLong(servingString),food.getServingId().getServingUnit()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String result = RESTClientAPI.createFood(food);
            return result;
        }
        /**
         * This method is responsible to show the information about the creation of the Food
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

    /**
     * This class is responsible to create an Async Task which GET the information of a
     * Food searched in FatSecretAPI, creates a list with five items which are not in the Server.
     */
    private class SearchOnFatSecretAPIAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * This method is responsible to GET the information about the Food in the FatSecretAPI
         * @return A String value with the result of the Food GET.
         */
        @Override
        protected String doInBackground(String... params) {
            foodListInServer = null;
            foodListInSearching = null;
            String response = "";
            Gson gson = new Gson();
            String jsonArrayFood = RESTClientAPI.findAllFood();
            foodListInServer = gson.fromJson(jsonArrayFood, new TypeToken<ArrayList<Food>>(){}.getType());
            FatSecretAPI fatSecretAPI = new FatSecretAPI("fac69fe6ddf04a54b95dc1e493a9d383","84d21c523ad74fee9757f09d473d20b2");
            try {
                response=fatSecretAPI.getFood(params[0]);
            } catch (FatSecretException e) {
                e.printStackTrace();
            }
            return response;
        }
        /**
         * This method is responsible to show the information about the query on FatSecretAPI
         * @param result A String value with the information of the result of the GET.
         */
        @Override
        protected void onPostExecute(String result) {
            //List<String> listFood = null;
            try {
                foodListInSearching = FatSecretAPI.getInfoAboutFood(result, foodListInServer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!(foodListInSearching==null || foodListInSearching.size()==0)){
                GoogleSearchAsyncTask googleSearchAsyncTask = new GoogleSearchAsyncTask();
                googleSearchAsyncTask.execute(foodListInSearching.get(0));
                CreateFoodAsyncTask createFoodAsyncTask = new CreateFoodAsyncTask();
                createFoodAsyncTask.execute(foodListInSearching.get(0));
            }
            else {
                Toast toast =
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Item exist in Food List", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    /**
     * This class is responsible to create an Async Task which GET the information of a
     * Food searched in GoogleSearchAPI, creates a String with a description of the item and
     * get a link with a photo of the new food.
     */
    private class GoogleSearchAsyncTask extends AsyncTask<Food, Void, String> {
        /**
         * This method is responsible to GET the information about the Food in the GoogleSearchAPI
         * @param params A String with the information to be searched.
         * @return A String value with the description and the link of the new Food.
         */
        @Override
        protected String doInBackground(Food... params) {
            String response = ""; //foodListInSearching.get(0).getFoodName()
            String description = GoogleSearchAPI.search(params[0].getFoodName(), new String[]{"num"}, new
                    String[]{"1"});
            String link = GoogleSearchAPI.search(params[0].getFoodName(),new String [] {"searchType","imgSize"}, new String []{"image","large"});
            try{
                response = "Name: "+params[0].getFoodName()+"\n"+
                        "Description: " + GoogleSearchAPI.getSnippet(description) + "\n" +
                        "Category: " +params[0].getCategoryId().getCategoryName()+ "\n"+
                        "Serving Unit: "+ params[0].getServingId().getServingUnit()+
                        " - Serving Amount: "+params[0].getServingAmount()+"\n"+
                        "Calorie Amount: "+ params[0].getCalorieAmount()+
                        " - Fat: "+params[0].getFat()+"\n"+
                        ";" +
                        GoogleSearchAPI.getPictureUrl(link);
            } catch (JSONException e){
                e.printStackTrace();
            }
            return response;
        }
        /**
         * This method is responsible to show the information about the query on GoogleSearchAPI
         * and launch the class which download the Image.
         * @param result A String value with the information of the result of the GET.
         */
        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("")) {
                String[] resultList = result.split("\\;");
                tvResult.setText(resultList[0]);
                String imageLink = resultList[1].trim();
                if (!imageLink.equals(""))
                    new DownloadImageTask(foodImageView).execute(imageLink);
            }
        }
    }

    /**
     * This class is responsible to create an Async Task which Download an Image from the
     * Internet using a Link.
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        /**
         * Non Default Constructor which receives an ImageView.
         * @param bmImage ImageView in the New Food Fragment.
         */
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        /**
         * This method is responsible to download the image using an URL.
         * @param urls A String with an URL link when the image is.
         * @return A Bitmap object which contains the picture downloaded.
         */
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap image = null;
            InputStream in = null;
            try {
                in = OpenHttpConnection(urldisplay);
                image = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return image;
        }

        /**
         * This method is responsible to display the image on the Screen
         * @param result A Bitmap object with the image downloaded.
         */
        protected void onPostExecute(Bitmap result) {
            if (result!=null)
                bmImage.setImageBitmap(result);
        }

        /**
         * This method is responsible to establish a connection with the URL that contains the image.
         * @param urlString A String with the URL which contains the image.
         * @return A input stream object with the connection established.
         * @throws IOException If the connection fails, return the trace.
         */
        private InputStream OpenHttpConnection(String urlString) throws IOException {
            InputStream in = null;
            int response = -1;

            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");

            try {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }
            } catch (Exception ex) {
                throw new IOException("Error connecting");
            }
            return in;
        }
    }

}
