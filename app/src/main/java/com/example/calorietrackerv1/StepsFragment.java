package com.example.calorietrackerv1;

import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class is responsible to create the My Steps Screen which permits the user
 * to create and update the steps of one day.
 */
public class StepsFragment extends Fragment implements View.OnClickListener {
    private View vSteps;
    private StepDatabase db = null;
    private TextInputLayout inputLayoutSteps;
    private TextInputEditText inputSteps;
    private Spinner stepList;
    private ArrayList<String> list;
    private ArrayAdapter<String> stepsAdapter;
    private TextInputLayout inputLayoutUpdateSteps;
    private TextInputEditText inputUpdateSteps;
    private Button addButton;
    private Button updateButton;


    /**
     * This method display the information about the Steps which should be added by the user.
     * It also allows to update the steps previosly created by the user.
     * @param inflater A inflater which displays the fragment inside the navigation drawer.
     * @param container A group of elements which are contained by the Steps Fragment.
     * @param savedInstanceState
     * @return A view with the Steps Fragment Screen
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        vSteps = inflater.inflate(R.layout.fragment_steps, container,
                false);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                StepDatabase.class, "StepsDatabase")
                .allowMainThreadQueries()//.fallbackToDestructiveMigration()
                .build();
        inputLayoutSteps = vSteps.findViewById(R.id.textInputSteps);
        inputSteps = vSteps.findViewById(R.id.textSteps);
        addButton = vSteps.findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        list = new ArrayList<String>();
        stepList = (Spinner) vSteps.findViewById(R.id.stepsSpinner);
        stepsAdapter = new ArrayAdapter<String>(getActivity()
                , android.R.layout.simple_spinner_item, list);
        stepsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stepList.setPrompt("Registered Steps:");
        stepList.setAdapter(stepsAdapter);

        inputLayoutUpdateSteps = vSteps.findViewById(R.id.textUpdateSteps);
        inputUpdateSteps = vSteps.findViewById(R.id.updateSteps);
        updateButton = vSteps.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(this);

        stepList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String step = parent.getItemAtPosition(position).toString();
                if (!(step.equals("Select information to update"))){
                    String[] details= step.split(":");
                    inputUpdateSteps.setText(details[2]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ReadDatabaseAsyncTask readDatabaseAsyncTask = new ReadDatabaseAsyncTask();
        readDatabaseAsyncTask.execute();
        return vSteps;
    }

    /**
     * This method is responsible to launch a Step entrance when  the add button is pushed and
     * it is responsible to launch an update when the update button is pushed.
     * @param v A view of Home Fragment.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addButton: {
                String steps = inputSteps.getText().toString();
                Log.i("Steps:",steps);
                if (!inputSteps.getText().toString().isEmpty()){
                    if (Input.isStringNumeric(steps)){
                        InsertDatabaseAsyncTask insertDatabaseAsyncTask = new InsertDatabaseAsyncTask();
                        insertDatabaseAsyncTask.execute();
                        inputLayoutSteps.setError(null);
                    }
                    else {
                        inputLayoutSteps.setError("Add a numeric value");
                    }
                }
                else {
                    inputLayoutSteps.setError("Step can't be empty");
                }
                break;
            }
            case R.id.updateButton: {
                String selection = stepList.getSelectedItem().toString();
                String infoUpdate = inputUpdateSteps.getText().toString();
                if (!selection.equals("Select information to update")) {
                    if (!infoUpdate.isEmpty()) {
                        if (Input.isStringNumeric(infoUpdate)) {
                            String[] details = selection.split("\\.");
                            UpdateDatabaseAsyncTask updateDatabaseAsyncTask = new UpdateDatabaseAsyncTask();
                            updateDatabaseAsyncTask.execute(details[0], infoUpdate);
                            inputUpdateSteps.setError(null);
                        } else
                            inputUpdateSteps.setError("Add a numeric value");
                    } else
                        inputUpdateSteps.setError("Update steps can't be empty");
                } else
                    inputUpdateSteps.setError("Select item to update");
                break;
            }

        }
    }

    /**
     * This class is responsible to insert new steps into the SQLRoom DataBase.
     */
    private class InsertDatabaseAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String details = inputSteps.getText().toString().trim();
            String result = "";
            if (Input.isStringNumeric(details)){
                int numberSteps = Integer.parseInt(details);
                String time = Input.getActualDate("time");
                Step step = new Step (time, numberSteps);
                long id = db.stepDao().insert(step);
                result = id + ". "+time+" - Steps:"+numberSteps;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(!result.equals("")){
                inputSteps.setText("");
                List<Step> steps = db.stepDao().getAll();
                if (!(steps.isEmpty() || steps == null) ) {
                    List<String> newList = Input.createListFromDB(steps);
                    list.clear(); // Limpia la lista anterior
                    list.addAll(newList); // agrega los nuevos elementos
                    stepsAdapter.notifyDataSetChanged();
                }
            }
            Toast toast =
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Information Added", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * This class is responsible to update steps into the SQLRoom DataBase.
     */
    private class UpdateDatabaseAsyncTask extends AsyncTask<String, Void, String>
    {
        @Override protected String doInBackground(String... params) {
            Step step=null;
            int id = Integer.parseInt(params[0]);
            String details = params[1];
            if (Input.isStringNumeric(details)) {
                int newNumberSteps = Integer.parseInt(params[1]);
                step = db.stepDao().findByID(id);
                step.setNumberSteps(newNumberSteps);
                db.stepDao().updateStep(step);
            }
            return "Information Updated";
        }

        @Override
        protected void onPostExecute(String details) {
            inputUpdateSteps.setText("");
            List<Step> steps = db.stepDao().getAll();
            if (!(steps.isEmpty() || steps == null) ) {
                List<String> newList = Input.createListFromDB(steps);
                list.clear();
                list.addAll(newList);
                stepsAdapter.notifyDataSetChanged();
            }
                Toast toast =
                    Toast.makeText(getActivity().getApplicationContext(),
                            details, Toast.LENGTH_SHORT);
                toast.show();

        }
    }

    /**
     * This class is responsible to read the steps created into the SQLRoom DataBase.
     */
    private class ReadDatabaseAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            List<Step> steps = db.stepDao().getAll();
            if (!(steps.isEmpty() || steps == null))
                return "read";
            else
                return "not read";
        }

        @Override
        protected void onPostExecute(String details) {
            if (details.equals("read")) {
                List<Step> steps = db.stepDao().getAll();
                List<String> newList = Input.createListFromDB(steps);
                list.clear();
                list.addAll(newList);
                stepsAdapter.notifyDataSetChanged();
            }
        }
    }
}
