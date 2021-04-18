package com.example.calorietrackerv1;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ScheduledIntentService extends IntentService {
    static int counter=0;
    private StepDatabase db;
    private SharedPreferences preferences;


    public ScheduledIntentService() {
        super("ScheduledIntentService");
        db = Room.databaseBuilder(getApplicationContext(),
                StepDatabase.class, "StepsDatabase")
                .allowMainThreadQueries()//.fallbackToDestructiveMigration()
                .build();
        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);


    }

    @Override
    protected void onHandleIntent(Intent intent) {
        CreateReportAsyncTask createReportAsyncTask = new CreateReportAsyncTask();
        createReportAsyncTask.execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent,flags,startId);
    }

    private class CreateReportAsyncTask extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground (Void...params){
            List<Step> steps = db.stepDao().getAll();
            int numberOfSteps = Input.calculateTotalNumberOfSteps(steps);
            String userId = preferences.getString("userId", null);
            String calorieGoal = preferences.getString("calorieGoal", null);
            String actualDate = Input.getActualDate("dateYearFirst");
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
                java.sql.Date reportDate = Input.manageDate(actualDate);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
                User user = gson.fromJson(RESTClientAPI.findByUserId(userId), User.class);
                Report report=new Report(user, reportDate, calCon, calBurned, numberOfSteps, calGoal);
                RESTClientAPI.createReport(report);
                Log.i("Report:","Report was created");
                //Clear SharedPreferences
                //preferences.edit().clear().commit();
                //Clear DataBase
                //db.stepDao().deleteAll();

                return  numberOfSteps + "," +calCon + "," + calBurned;
            } else
                return numberOfSteps + ",";
        }
        @Override
        protected void onPostExecute (String result){

        }
    }

}
