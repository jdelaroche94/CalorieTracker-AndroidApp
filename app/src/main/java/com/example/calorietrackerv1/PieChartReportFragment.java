package com.example.calorietrackerv1;


import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible to create a Pie Chart with the calories consumed by a user in
 * a particular day.
 */
public class PieChartReportFragment extends Fragment implements View.OnClickListener {
    private PieChart pieChart;
    private DatePicker dpDate;
    private Button buttonCreateReport;
    private String userId;
    private View vReport;

    public PieChartReportFragment() {
    }

    /**
     * This method display a Screen which allows the user to select a date and then it creates
     * a pie chart with the information about the calories of that day.
     * @param inflater A inflater which displays the fragment inside the navigation drawer.
     * @param container A group of elements which are contained by the Pie Chart Report Fragment.
     * @param savedInstanceState
     * @return A view with the Pie chart report fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        vReport = inflater.inflate(R.layout.fragment_report_piechart, container, false);
        dpDate = (DatePicker) vReport.findViewById(R.id.inputReportDate);
        userId = getArguments().getString("userId");
        // init
        //dpDate.init(2019, 05, 06, null);
        buttonCreateReport = (Button) vReport.findViewById(R.id.buttonCreateReport);
        pieChart = (PieChart) vReport.findViewById(R.id.piechart);
        buttonCreateReport.setOnClickListener(this);
        return vReport;
    }

    /**
     * This method is responsible to activate actions when the button Create Report is click on.
     * @param v A view of Pie Chart Report Fragment.
     */
    @Override
    public void onClick(View v) {
        int year = dpDate.getYear();
        int month = dpDate.getMonth();
        int day = dpDate.getDayOfMonth();
        String date = Input.formatDateUserInput ("dateYearFirst", day, month, year,
                0,0);
        ReportCalorieByUserAsyncTask reportCalorieByUserAsyncTask = new ReportCalorieByUserAsyncTask();
        reportCalorieByUserAsyncTask.execute(date);
    }

    /**
     * This class is responsible to create an Async Task which GET the information of the calories
     * consumed and burned by a user in a particular Day. This is information is taken from the
     * RESTClientAPI.
     */
    private class ReportCalorieByUserAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * This method is responsible to query the server using RESTClientAPI to get the information
         * about the calories consumed and burned by a user in a particular day.
         * @param params A String with the information of the user and the date to be searched.
         * @return A String with the report.
         */
        @Override
        protected String doInBackground(String... params) {
            String report = RESTClientAPI.reportCaloriesBalanceByUser(userId,params[0]);
            return report;
        }

        /**
         * This method is responsible to create the parameters required by the pie chart and
         * then it is displayed on the screen.
         * @param result A String with the information obtained from the user.
         */
        @Override
        protected void onPostExecute(String result) {
            String caloriesConsumed = null;
            String caloriesBurned = null;
            String remainingCalorie = null;
            try {
                caloriesConsumed = Input.jsonToString(result,"calorieConsumed");
                caloriesBurned = Input.jsonToString(result,"caloriesBurned");
                remainingCalorie = Input.jsonToString(result,"remainingCalorie");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!(caloriesConsumed == "" || caloriesBurned == "" || remainingCalorie == "")){
                int calConsumed = Integer.parseInt(caloriesConsumed);
                int calBurned = Integer.parseInt(caloriesBurned);
                int remCalorie = Integer.parseInt(remainingCalorie);
                String consumed = "Consumed:" +
                        Integer.toString((calConsumed*100)/(calConsumed + calBurned + remCalorie))
                        +"%";
                String burned = "Burned:" +
                        Integer.toString((calBurned*100)/(calConsumed + calBurned + remCalorie))
                        +"%";
                String remaining = "Remaining:" +
                        Integer.toString((remCalorie*100)/(calConsumed + calBurned + remCalorie))
                        +"%";
                List<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(calConsumed, consumed));
                entries.add(new PieEntry(calBurned, burned));
                entries.add(new PieEntry(remCalorie, remaining));
                PieDataSet set = new PieDataSet(entries, "Calories Report");
                PieData data = new PieData(set);
                pieChart.getDescription().setEnabled(false);
                pieChart.setEntryLabelColor(Color.BLACK);
                pieChart.getLegend().setEnabled(false);
                data.setValueTextSize(12f);
                pieChart.setData(data);
                pieChart.invalidate(); // refresh
                set.setColors(ColorTemplate.COLORFUL_COLORS);
                pieChart.animateXY(5000, 5000);
            }
        }
    }
}