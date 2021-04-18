package com.example.calorietrackerv1;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapquest.mapping.MapQuest;
import com.mapquest.mapping.maps.MapView;
import java.util.ArrayList;

/**
 * This class is responsible to create a Map with the location of the user and the parks which
 * are close to that user (5 kilometers around)
 */
public class MapFragment extends Fragment implements View.OnClickListener {
    private View vMap;
    private MapboxMap mMapboxMap;
    private MapView mMapView;
    private Button bSearch;
    private float latitude;
    private float longitude;
    private ArrayList<String> latitudeList;
    private ArrayList<String> longitudeList;
    private ArrayList<String> parkNameList;
    private String address;
    private String userId;

    /**
     * This method display a Screen which to observe from its location, what parks are 5 kilometers
     * around them.
     * @param inflater A inflater which displays the fragment inside the navigation drawer.
     * @param container A group of elements which are contained by the Map Fragment.
     * @param savedInstanceState
     * @return A view with the Map fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        vMap = inflater.inflate(R.layout.fragment_map, container,
                false);
        MapQuest.start(getActivity().getApplicationContext());
        mMapView = (MapView) vMap.findViewById(R.id.mapquestMapView);
        bSearch = (Button) vMap.findViewById(R.id.searchButtonMap);
        mMapView.onCreate(savedInstanceState);
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();
        parkNameList = new ArrayList<>();
        userId = getArguments().getString("userId");
        bSearch.setOnClickListener(this);
        return vMap;
    }

    /**
     * This method is responsible to activate actions when the button Search is click on.
     * @param view A view of Map Fragment.
     */
    @Override
    public void onClick(View view) {
        SearchAsyncTask searchAsyncTask=new SearchAsyncTask();
        searchAsyncTask.execute();
    }

    /**
     * This method permit to add Markers to the Map
     * @param mapboxMap A MapboxMap object when the markers will be positioned.
     * @param latitude A float value with the latitude.
     * @param longitude A float value with the longitude.
     * @param snippet A String value with tag of the marker.
     */
    private void addMarker(MapboxMap mapboxMap, float latitude, float longitude,String snippet) {
        LatLng location = new LatLng(latitude,
                longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
        markerOptions.title("My Home");
        markerOptions.snippet(snippet);
        mapboxMap.addMarker(markerOptions);
    }

    /**
     * This class is responsible to create an Async Task which GET the information of a
     * location of the user in MapQuestAPI and the parks around that user in the HereGeocodeAPI
     */
    private class SearchAsyncTask extends AsyncTask<Void, Void, String> {
        /**
         * This method is responsible to get the latitude and longitude of the location of the user
         * as well as the park that are 5 kilometers around them.
         * @return A String with the information obtained.
         */
        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            String home = "";
            String parks = "";
            String lat = null;
            String lng = null;
            String infoUser = RESTClientAPI.findByUserId(userId);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
            User user = gson.fromJson(infoUser, User.class);
            String addressToSend = user.getAddress() + " postalCode=" + user.getPostcode() + " Australia";
            address = user.getAddress();
            try {
                home = MapQuestAPI.search(addressToSend, "false", 1);
                lat = MapQuestAPI.getCoordinate(home, "lat");
                lng = MapQuestAPI.getCoordinate(home, "lng");
                parks = HereGeocodeAPI.search(lat,lng);
                latitude = Float.parseFloat(lat);
                longitude = Float.parseFloat(lng);
                latitudeList = HereGeocodeAPI.getInformationAboutParks(parks,"latitude");
                longitudeList = HereGeocodeAPI.getInformationAboutParks(parks,"longitude");
                parkNameList = HereGeocodeAPI.getInformationAboutParks(parks,"parkName");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!(latitudeList == null || longitudeList == null))
                response = "Successful Process";
            return response;
        }

        /**
         * This method is responsible to display in the map all the markers previously calculated.
         * @param result A String with the result of the search.
         */
        @Override
        protected void onPostExecute(String result) {

            mMapView.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(MapboxMap mapboxMap) {
                    mMapboxMap = mapboxMap;
                    mMapView.setStreetMode();
                    mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 11));
                    addMarker(mapboxMap, latitude,longitude,address);
                    for (int i=0; i<latitudeList.size();i++){
                        mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Float.parseFloat(latitudeList.get(i)),
                                        Float.parseFloat(longitudeList.get(i))))
                                .title(parkNameList.get(i)));
                    }
                }
            });
        }
    }

}
