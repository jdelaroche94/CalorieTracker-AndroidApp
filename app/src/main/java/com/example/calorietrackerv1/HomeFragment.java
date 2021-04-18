package com.example.calorietrackerv1;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.List;

/**
 * This Class is responsible to create the Home Screen which summarizes the information
 * of the calories consumed, burned, steps taken of the user by this day. This class
 * contains a button which allows to create a report by this day.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private View vHome;
    private StepDatabase db;
    private ImageView fitness;
    private TextView textActualDate;
    private TextView textFirstName;
    private TextInputLayout inputLayoutCalorieGoal;
    private TextInputEditText inputCalorieGoal;
    private CheckBox checkboxCalorieGoal;
    private TextInputLayout textLayoutSteps;
    private TextInputEditText textSteps;
    private TextInputLayout textLayoutCaloriesConsumed;
    private TextInputEditText textCaloriesConsumed;
    private TextInputLayout textLayoutCaloriesBurned;
    private TextInputEditText textCaloriesBurned;
    private Button buttonCreateAlarm;
    private String userId;
    private String actualDate;
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    private AlarmManager alarmManager;
    private Intent intent;
    private PendingIntent pendingIntent;

    /**
     * This method display the information about the Calories of one user by a particular day.
     * @param inflater A inflater which displays the fragment inside the navigation drawer.
     * @param container A group of elements which are contained by the Home Fragment.
     * @param savedInstanceState
     * @return A view with the Home Fragment Screen
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        vHome = inflater.inflate(R.layout.fragment_home, container,
                false);
        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                StepDatabase.class, "StepsDatabase")
                .allowMainThreadQueries()//.fallbackToDestructiveMigration()
                .build();

        fitness = (ImageView) vHome.findViewById(R.id.fitnessImageView);
        textActualDate = (TextView) vHome.findViewById(R.id.textDate);
        textActualDate.setText(Input.getActualDate("dateTime"));
        textFirstName = (TextView) vHome.findViewById(R.id.textFirstName);
        inputLayoutCalorieGoal = vHome.findViewById(R.id.textInputCalorieGoal);
        inputCalorieGoal = vHome.findViewById(R.id.textCalorieGoal);
        checkboxCalorieGoal = (CheckBox) vHome.findViewById(R.id.checkboxCalorieGoal);
        textLayoutSteps = vHome.findViewById(R.id.textLayoutStepsTaken);
        textSteps = vHome.findViewById(R.id.textStepsTaken);
        textLayoutCaloriesConsumed = vHome.findViewById(R.id.textLayoutCaloriesConsumed);
        textCaloriesConsumed = vHome.findViewById(R.id.textCaloriesConsumed);
        textLayoutCaloriesBurned = vHome.findViewById(R.id.textLayoutCaloriesBurned);
        textCaloriesBurned = vHome.findViewById(R.id.textCaloriesBurned);
        buttonCreateAlarm = vHome.findViewById(R.id.buttonActivateAlarm);
        textFirstName.setText("Hi, " +getArguments().getString("firstName"));
        preferences =
                getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String calorieGoal = preferences.getString("calorieGoal", null);
        userId = getArguments().getString("userId");
        preferencesEditor = preferences.edit();
        preferencesEditor.putString("userId", userId);
        preferencesEditor.apply();
        actualDate = Input.getActualDate("dateYearFirst");

        if (calorieGoal != null){
            inputCalorieGoal.setText(calorieGoal);
            CalorieTrackerAsyncTask calorieTrackerAsyncTask = new CalorieTrackerAsyncTask();
            calorieTrackerAsyncTask.execute();
        }

        inputCalorieGoal.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences",
                      //      Context.MODE_PRIVATE);
                    preferencesEditor.putString("calorieGoal", String.valueOf(inputCalorieGoal.getText()));
                    preferencesEditor.apply();
                    checkboxCalorieGoal.setChecked(false);
                    CalorieTrackerAsyncTask calorieTrackerAsyncTask = new CalorieTrackerAsyncTask();
                    calorieTrackerAsyncTask.execute();
                    return true;
                }
                return false;
            }
        });

        inputCalorieGoal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            /**
             * Method which updates the calorie goal and creates it in the SharePreferences.
             * @param v A view of Home Fragment
             * @param hasFocus A boolean value if the calorie goal layout were clicked.
             */
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    preferencesEditor.putString("calorieGoal", String.valueOf(inputCalorieGoal.getText()));
                    preferencesEditor.apply();
                    checkboxCalorieGoal.setChecked(false);
                    CalorieTrackerAsyncTask calorieTrackerAsyncTask = new CalorieTrackerAsyncTask();
                    calorieTrackerAsyncTask.execute();
                }
            }
        });

        checkboxCalorieGoal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * This method is responsible to manage the actions on the check box Calorie Goal
             * @param buttonView A compound button which contains the checkbox itself.
             * @param isChecked A boolean value, true if the check box was checked, false if not.
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inputCalorieGoal.setFocusableInTouchMode(true);
                    inputCalorieGoal.setFocusable(true);
                    inputLayoutCalorieGoal.setFocusable(true);
                }
                if (!isChecked) {
                    inputCalorieGoal.setFocusable(false);
                    inputLayoutCalorieGoal.setFocusable(false);
                }
            }
        });
        buttonCreateAlarm.setOnClickListener(this);
        return vHome;
    }

    /**
     * This method is responsible to launch the Alarm when the button is pushed.
     * @param v A view of Home Fragment.
     */
    @Override
    public void onClick(View v) {
        Calendar actualDate = Calendar.getInstance();
        int hour = actualDate.get(Calendar.HOUR_OF_DAY);
        int minutes = actualDate.get(Calendar.MINUTE);
        intent = new Intent(getActivity(), ScheduledIntentService.class);
        pendingIntent = PendingIntent.getService(getActivity(), 0,
                intent, 0);
        int alarmType = AlarmManager.ELAPSED_REALTIME;
        alarmManager = (AlarmManager)
                getActivity().getSystemService(getActivity().ALARM_SERVICE);

        // Set the alarm to start at 11:59 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);

        alarmManager.setRepeating(alarmType, calendar.getTimeInMillis(),
                1000 * 60 * 60 * 24, pendingIntent);

        String formatDate = Input.formatDateUserInput("time",0,0,0,hour,minutes);

        CreateReportAsyncTask createReportAsyncTask = new CreateReportAsyncTask();
        createReportAsyncTask.execute();

    }

    /**
     * This class is responsible to create an Async Task which reads the information
     * about the calories about the calories consumed calling the RESTClientAPI. It
     * read the internal database to know the number of steps consumed by a user in that day.
     */
    private class CalorieTrackerAsyncTask extends AsyncTask<String, Void, String>
    {
        /**
         * This method is responsible to read the calorie consumed by a user using an API,
         * read the internal database to get the number of steps.
         * @return A String value with the result.
         */
        @Override
        protected String doInBackground (String...params){
            List<Step> steps = db.stepDao().getAll();
            int numberOfSteps = Input.calculateTotalNumberOfSteps(steps);
            String calorieGoal = preferences.getString("calorieGoal", null);
            if (!(calorieGoal == null))
            {
                int calCon = 0;
                int calBurned = 0;
                String caloriesConsumedString = RESTClientAPI.calculateTotalCaloriesConsumedByUser(userId,actualDate);
                if (!caloriesConsumedString.equals("")){
                    calCon = Integer.parseInt(caloriesConsumedString.split("\\.")[0]);
                }
                String caloriesBurnedAtRestString = RESTClientAPI.calculateDailyCaloriesBurnedAtRest(userId);
                String caloriesBurnedPerStepString = RESTClientAPI.calculateCaloriesBurnedPerStep(userId);
                if (!(caloriesBurnedAtRestString.equals("") || caloriesBurnedPerStepString.equals(""))) {
                    Double caloriesBurned = Double.parseDouble(caloriesBurnedPerStepString) *
                            numberOfSteps + Double.parseDouble(caloriesBurnedAtRestString);
                    calBurned = (int) Math.round(caloriesBurned);
                }
                return  numberOfSteps + "," +calCon + "," + calBurned;
            } else
                return numberOfSteps + ",";
        }

        /**
         * This method is responsible to update the Home Fragment with the values red.
         * @param result A string with the result of the queries.
         */
        @Override
        protected void onPostExecute (String result){
            if (!result.equals("")){
                String [] calories = result.split("\\,");
                textSteps.setText(calories[0]);
                if (calories.length > 1)
                {
                    textCaloriesConsumed.setText(calories[1]);
                    textCaloriesBurned.setText(calories[2]);
                }
            }
        }
    }

    /**
     * This class is responsible to create an Async Task which POST the information of a
     * Report of the date to the RESTClientAPI Report POST method.
     */
    private class CreateReportAsyncTask extends AsyncTask<Void, Void, String>
    {
        /**
         * This method is responsible to POST the information to the Report in the RESTClientAPI
         * @return A String value with the result of the Report POST.
         */
        @Override
        protected String doInBackground (Void...params){
            List<Step> steps = db.stepDao().getAll();
            int numberOfSteps = Input.calculateTotalNumberOfSteps(steps);
            String userId = preferences.getString("userId", null);
            String calorieGoal = preferences.getString("calorieGoal", null);
            String repDate = Input.getActualDate("date");
            if (!(calorieGoal == null || userId == null))
            {
                int calCon = 0;
                int calBurned = 0;
                int calGoal = Integer.parseInt(calorieGoal);
                String caloriesConsumedString = RESTClientAPI.calculateTotalCaloriesConsumedByUser(userId,actualDate);
                if (!caloriesConsumedString.equals("")){
                    calCon = Integer.parseInt(caloriesConsumedString.split("\\.")[0]);
                }
                String caloriesBurnedAtRestString = RESTClientAPI.calculateDailyCaloriesBurnedAtRest(userId);
                String caloriesBurnedPerStepString = RESTClientAPI.calculateCaloriesBurnedPerStep(userId);
                if (!(caloriesBurnedAtRestString.equals("") || caloriesBurnedAtRestString.equals(""))) {
                    Double caloriesBurned = Double.parseDouble(caloriesBurnedPerStepString) *
                            numberOfSteps + Double.parseDouble(caloriesBurnedAtRestString);
                    calBurned = (int) Math.round(caloriesBurned);
                }
                java.sql.Date reportDate = Input.manageDate(repDate);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
                User user = gson.fromJson(RESTClientAPI.findByUserId(userId), User.class);
                Report report=new Report(user, reportDate, calCon, calBurned, numberOfSteps, calGoal);
                RESTClientAPI.createReport(report);
                //Clear SharedPreferences
                preferences.edit().clear().commit();
                //Clear DataBase
                db.stepDao().deleteAll();

                return  numberOfSteps + "," +calCon + "," + calBurned;
            } else
                return numberOfSteps + ",";
        }

        /**
         * This method is responsible to update the Screen Home Activity after a Report is
         * created
         * @param result A String value with the information of the result of the POST.
         */
        @Override
        protected void onPostExecute (String result){
            inputCalorieGoal.setText("");
            textSteps.setText("");
            textCaloriesConsumed.setText("");
            textCaloriesBurned.setText("");
        }
    }
}


