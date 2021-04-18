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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible to create a Bar Graph with the calories consumed and burned
 * by a user in period of time.
 */
public class BarGraphReportFragment extends Fragment implements View.OnClickListener {
    private BarChart chart;
    private DatePicker dpStartDate;
    private DatePicker dpEndDate;
    private Button buttonCreateReport;
    private String userId;
    private View vReport;

    public BarGraphReportFragment() {
    }

    /**
     * This method display a Screen which allows the user to select start date and end date
     * and then it creates a bar graph with the information about the calories of this period of
     * time
     * @param inflater A inflater which displays the fragment inside the navigation drawer.
     * @param container A group of elements which are contained by the Bar Graph Report Fragment.
     * @param savedInstanceState
     * @return A view with the Bar Graph report fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        vReport = inflater.inflate(R.layout.fragment_report_bargraph, container, false);
        dpStartDate = (DatePicker) vReport.findViewById(R.id.inputStartDate);
        dpEndDate = (DatePicker) vReport.findViewById(R.id.inputEndDate);
        userId = getArguments().getString("userId");
        buttonCreateReport = (Button) vReport.findViewById(R.id.buttonCreateReport2);
        chart = (BarChart) vReport.findViewById(R.id.barGraph);
        buttonCreateReport.setOnClickListener(this);
        return vReport;
    }

    /**
     * This method is responsible to activate actions when the button Create Report is click on.
     * @param v A view of Bar Graph Report Fragment.
     */
    @Override
    public void onClick(View v) {
        int year = dpStartDate.getYear();
        int month = dpStartDate.getMonth();
        int day = dpStartDate.getDayOfMonth();
        String startDate = Input.formatDateUserInput ("dateYearFirst", day, month, year,
                0,0);
        year = dpEndDate.getYear();
        month = dpEndDate.getMonth();
        day = dpEndDate.getDayOfMonth();
        String endDate = Input.formatDateUserInput ("dateYearFirst", day, month, year,
                0,0);

        ReportCalorieBalancePerPeriodAsyncTask reportCaloriePerPeriodAsyncTask = new ReportCalorieBalancePerPeriodAsyncTask();
        reportCaloriePerPeriodAsyncTask.execute(startDate, endDate);
    }

    /**
     * This class is responsible to create an Async Task which GET the information of the calories
     * consumed and burned by a user in a particular period of time. This information is taken
     * from the RESTClientAPI.
     */
    private class ReportCalorieBalancePerPeriodAsyncTask extends AsyncTask<String, Void, String> {
        /**
         * This method is responsible to query the server using RESTClientAPI to get the information
         * about the calories consumed and burned by a user in a particular period of time.
         * @param params A String with the information of the user and period of time to be searched.
         * @return A String with the report.
         */
        @Override
        protected String doInBackground(String... params) {
            String report = RESTClientAPI.reportCaloriesBalancePerPeriod(userId,params[0],params[1]);
            return report;
        }

        /**
         * This method is responsible to create the parameters required by the bar chart and
         * then it is displayed on the screen.
         * @param result A String with the information obtained from the user.
         */
        @Override
        protected void onPostExecute(String result) {
            List<String> reportDates = null;
            List<String> caloriesConsumed = null;
            List<String> caloriesBurned = null;
            try {
                reportDates = Input.jsonToArrayList(result,"reportDate");
                caloriesConsumed = Input.jsonToArrayList(result,"caloriesConsumed");
                caloriesBurned = Input.jsonToArrayList(result,"caloriesBurned");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!(reportDates == null || caloriesConsumed == null || caloriesBurned == null)){
                chart.setDrawBarShadow(false);
                chart.setDrawValueAboveBar(true);
                chart.getDescription().setEnabled(false);
                chart.setPinchZoom(false);
                chart.setDrawGridBackground(false);
                chart.getAxisRight().setEnabled(false);
                XAxis xAxis = chart.getXAxis();
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);
                xAxis.setCenterAxisLabels(true);
                xAxis.setDrawGridLines(true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues(reportDates)));
                BarData data = new BarData(getDataSet(caloriesConsumed,caloriesBurned));
                chart.setData(data);
                chart.animateXY(2000, 2000);
                chart.invalidate();
            }
        }

        /**
         * This method creates an ArrayList with the data to be set in the bar graph.
         * @param caloriesConsumed A list of calories consumed
         * @param caloriesBurned A list of calories burned
         * @return An ArrayList with the set to be used by the bar graph.
         */
        private ArrayList getDataSet(List<String> caloriesConsumed, List<String> caloriesBurned ) {
            ArrayList dataSets = null;
            ArrayList valueSet1 = new ArrayList();
            ArrayList valueSet2 = new ArrayList();
            int j=1;
            BarEntry bar = null;
            for (int i=1; i< caloriesConsumed.size();i++){
                bar = new BarEntry(j, Integer.parseInt(caloriesConsumed.get(i)));
                valueSet1.add(bar);
                j+=2;
            }
            j=2;
            for (int i=1; i< caloriesBurned.size();i++){
                bar = new BarEntry(j, Integer.parseInt(caloriesBurned.get(i)));
                valueSet2.add(bar);
                j+=2;
            }
            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Calories consumed");
            barDataSet1.setColor(Color.rgb(0, 155, 0));
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Calories burned");
            barDataSet2.setColors(Color.rgb(0, 0, 155));
            dataSets = new ArrayList();
            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);
            return dataSets;
        }

        /**
         * This method is responsible to create the xAxis of the Bar graph.
         * @param reportDates A list of Dates to be set in the x Axis.
         * @return An ArrayList with the information set.
         */
        private ArrayList getXAxisValues(List<String> reportDates) {
            String formatDate;
            ArrayList xAxis = new ArrayList();
            for (int i=1; i< reportDates.size();i++){
                String[] date=reportDates.get(i).split("\\-");
                formatDate = date[2] + "-" + date[1];
                xAxis.add(" ");
                xAxis.add(formatDate);
            }
            return xAxis;
        }
    }
}
