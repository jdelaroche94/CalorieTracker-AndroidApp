package com.example.calorietrackerv1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;

/**
 * This Class is responsible to create the Activity SignUp and receives the information
 * from the user
 */
public class SignUpActivity extends AppCompatActivity {

    /**
     * This method is responsible to create the Sign Up UI and enable the creation of the
     * user in the Server
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final TextInputLayout inputGivenName = findViewById(R.id.textInputGivenName);
        final TextInputLayout inputSurname = findViewById(R.id.textInputSurname);
        final TextInputLayout inputEmail = findViewById(R.id.textInputEmail);
        final TextInputLayout inputDOB = findViewById(R.id.textInputDob);
        final EditText inputDate = (EditText) findViewById(R.id.editTextDob);
        final TextInputLayout inputHeightLayout = findViewById(R.id.textInputHeight);
        final Spinner inputHeight = (Spinner) findViewById(R.id.heightSpinner);
        final TextInputLayout inputWeightLayout = findViewById(R.id.textInputWeight);
        final Spinner inputWeight = (Spinner) findViewById(R.id.weightSpinner);
        final TextInputLayout inputGenderLayout = findViewById(R.id.textInputGender);
        final RadioGroup radioGenderGroup = (RadioGroup) findViewById(R.id.radioGender);
        final TextInputLayout inputAddress = findViewById(R.id.textInputAddress);
        final TextInputLayout inputPostcodeLayout = findViewById(R.id.textInputPostcode);
        final Spinner inputPostcode = (Spinner) findViewById(R.id.postcodeSpinner);
        final TextInputLayout inputLevelActLayout = findViewById(R.id.textInputLevelAct);
        final Spinner inputLevelAct = (Spinner) findViewById(R.id.levelActSpinner);
        final TextInputLayout inputStepsPerMile = findViewById(R.id.textInputStepsPerMile);
        final TextInputLayout inputUsername = findViewById(R.id.textInputUsername);
        final TextInputLayout inputPassword = findViewById(R.id.textInputPasswordToggle);
        final TextView logOutLink = findViewById(R.id.logOutLink);

        inputDate.setOnClickListener( new View.OnClickListener() {

            /**
             * This method is responsible to create DatePicker Dialog when Date of Birth
             * is clicked.
             * @param view Receives a view of Sign Up.
             */
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.editTextDob:
                        showDatePickerDialog();
                        break;
                }
            }

            /**
             * This method is responsible to show a Date Picker on the Screen
             */
            private void showDatePickerDialog() {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        final String selectedDate = Input.formatDateUserInput("date",day , month, year,0,0);
                        inputDate.setText(selectedDate);
                        if (inputDOB.getError()!=null)
                            inputDOB.setError(null);
                    }
                });
                newFragment.show(getSupportFragmentManager(), "datePicker"); // pilas, cual lo pase a fragment, getActivity(). getSupp..
            }
        });

        //Input of Height
        List<String> listHeight = Input.createList(100,233,"cms");
        final ArrayAdapter<String> heightAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_spinner_item , listHeight);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputHeight.setPrompt("Height:");
        inputHeight.setAdapter(heightAdapter);

        //Input of Weight
        List<String> listWeight = Input.createList(30,300,"kgs");
        final ArrayAdapter<String> weightAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_spinner_item , listWeight);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputWeight.setPrompt("Weight:");
        inputWeight.setAdapter(weightAdapter);

        //Input of Postcode
        List<String> listPostcode = Input.readFromInternalFile(this);
        final ArrayAdapter<String> postcodeAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_spinner_item , listPostcode);
        postcodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputPostcode.setPrompt("Postcode");
        inputPostcode.setAdapter(postcodeAdapter);

        //Input of Level Activity
        List<String> listLevelAct = Input.createList(1,5,"");
        final ArrayAdapter<String> levelActAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_spinner_item , listLevelAct);
        levelActAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputLevelAct.setPrompt("Level Activity");
        inputLevelAct.setAdapter(levelActAdapter);

        Button btnSearch = (Button) findViewById(R.id.confirmSignUpButton);
        btnSearch.setOnClickListener(new View.OnClickListener() {

            /**
             * This method is responsible to validate if All the information typed by user
             * are valid and enable the Create User Async Task if the information is correct.
             * @param view Receives a view of Sign Up Activity.
             */
            @Override
            public void onClick(View view) {
                if (confirmInput(view)) {
                    String givenName = inputGivenName.getEditText().getText().toString().trim();
                    String surname = inputSurname.getEditText().getText().toString().trim();
                    String email = inputEmail.getEditText().getText().toString().trim();
                    String dob = inputDOB.getEditText().getText().toString().trim();
                    String height = Input.formatText(inputHeight.getSelectedItem().toString());
                    String weight = Input.formatText(inputWeight.getSelectedItem().toString());
                    RadioButton radioGenderButton = (RadioButton) findViewById(radioGenderGroup.getCheckedRadioButtonId());;
                    String gender = Input.getGender(radioGenderButton.getText().toString());
                    String address = inputAddress.getEditText().getText().toString().trim();
                    String postcode = inputPostcode.getSelectedItem().toString().trim();
                    String levelAct = inputLevelAct.getSelectedItem().toString().trim();
                    String stepsMile = inputStepsPerMile.getEditText().getText().toString().trim();
                    String username = inputUsername.getEditText().getText().toString().trim();
                    String password = inputPassword.getEditText().getText().toString().trim();

                    CreateUserAsyncTask createAsyncTask = new CreateUserAsyncTask();
                    createAsyncTask.execute(givenName, surname, email,dob,height, weight,gender,
                            address,postcode,levelAct,stepsMile,username,password);


                }
            }

            /**
             * This method is responsible to validate information typed by user in the field
             * Given Name, if it is empty or if it is too long.
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validateGivenName (){
                String givenName = inputGivenName.getEditText().getText().toString().trim();
                if (givenName.isEmpty()) {
                    inputGivenName.setError("Given name can't be empty");
                    return false;
                } else if (givenName.length() > 20){
                    inputGivenName.setError("Given name too long");
                    return false;
                }
                else {
                    inputGivenName.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate information typed by user in the field
             * Surname, if it is empty or if it is too long.
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validateSurname (){
                String surname = inputSurname.getEditText().getText().toString().trim();
                if (surname.isEmpty()) {
                    inputSurname.setError("Surname can't be empty");
                    return false;
                } else if (surname.length() > 20){
                    inputSurname.setError("Surname too long");
                    return false;
                }
                else {
                    inputSurname.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate information typed by user in the field
             * email, if it is empty.
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validateEmail (){
                String email = inputEmail.getEditText().getText().toString().trim();
                if (email.isEmpty()) {
                    inputEmail.setError("Email name can't be empty");
                    return false;
                }
                else {
                    inputEmail.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate information typed by user in the field
             * date of birth, if it is empty.
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validateDoB (){
                String dob = inputDOB.getEditText().getText().toString().trim();
                if (dob.isEmpty()) {
                    inputDOB.setError("Date of Birth can't be empty");
                    return false;
                }
                else {
                    inputDOB.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate information typed by user in the spinner
             * Height, if it is empty.
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validateHeight(){
                String height = inputHeight.getSelectedItem().toString();
                if (height.equals(" ")) {
                    inputHeightLayout.setError("Select one height");
                    return false;
                }
                else {
                    inputHeightLayout.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate information typed by user in the spinner
             * weight, if it is empty.
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validateWeight(){
                String weight = inputWeight.getSelectedItem().toString();
                if (weight.equals(" ")) {
                    inputWeightLayout.setError("Select one weight");
                    return false;
                }
                else {
                    inputWeightLayout.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate information typed by user in the Group Button
             * Gender, if it is empty.
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validateGender() {
                RadioButton radioGenderButton = null;
                String gender = "";
                try {
                    radioGenderButton = (RadioButton) findViewById(radioGenderGroup.getCheckedRadioButtonId());
                    gender = Input.getGender(radioGenderButton.getText().toString());
                } catch (Exception ex) {

                }
                if (gender.isEmpty()) {
                    inputGenderLayout.setError("Choose gender");
                    return false;
                } else {
                    inputGenderLayout.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate information typed by user in the field
             * address, if it is empty.
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validateAddress (){
                String address = inputAddress.getEditText().getText().toString().trim();
                if (address.isEmpty()) {
                    inputAddress.setError("Address can't be empty");
                    return false;
                } else if (address.length() > 50){
                    inputAddress.setError("Address too long");
                    return false;
                }
                else {
                    inputAddress.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate information typed by user in the spinner
             * postcode, if it is empty.
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validatePostcode(){
                String postcode = inputPostcode.getSelectedItem().toString();
                if (postcode.equals(" ")) {
                    inputPostcodeLayout.setError("Select one postcode");
                    return false;
                }
                else {
                    inputPostcodeLayout.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate information typed by user in the spinner
             * Level Activity, if it is empty.
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validateLevelAct(){
                String levelAct = inputLevelAct.getSelectedItem().toString();
                if (levelAct.equals(" ")) {
                    inputLevelActLayout.setError("Select one Level of Activity");
                    return false;
                }
                else {
                    inputLevelActLayout.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate information typed by user in the field
             * steps per mile, if it is empty or if it is too long.
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validateStepsPerMile (){
                String stepsMile = inputStepsPerMile.getEditText().getText().toString().trim();
                if (stepsMile.isEmpty()) {
                    inputStepsPerMile.setError("Steps per mile can't be empty");
                    return false;
                } else if (stepsMile.length() > 6){
                    inputStepsPerMile.setError("Steps per mile too long");
                    return false;
                }
                else {
                    inputStepsPerMile.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate information typed by user in the field
             * username, if it is empty or if it is too long
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validateUsername (){
                String username = inputUsername.getEditText().getText().toString().trim();
                if (username.isEmpty()) {
                    inputUsername.setError("Username can't be empty");
                    return false;
                } else if (username.length() > 10){
                    inputUsername.setError("Username too long");
                    return false;
                } else {
                    inputUsername.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate information typed by user in the field
             * password, if it is empty or if it is less than 8 chars
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validatePassword(){
                String password = inputPassword.getEditText().getText().toString().trim();
                if (password.isEmpty()) {
                    inputPassword.setError("Password can't be empty");
                    return false;
                } else if (password.length() < 8) {
                    inputUsername.setError("Password should have at least 8 chars");
                    return false;
                } else {
                    inputPassword.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate all the information required by the form
             * to validate.
             * @param view Receives a view of Sign Up
             * @return A boolean value, true if the information were correctly typed
             *          or false if they weren't
             */
            public boolean confirmInput(View view) {
                if (!validateGivenName() | !validateSurname() | !validateEmail() | !validateDoB() |
                        !validateHeight() | !validateWeight() | !validateGender() | !validateAddress() |
                        !validatePostcode() | !validateLevelAct() | !validateStepsPerMile() |
                        !validateUsername() | !validatePassword())
                    return false;
                else
                    return true;
            }

        });

        logOutLink.setPaintFlags(logOutLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        logOutLink.setText("Are you a User? Log in Here");
        logOutLink.setOnClickListener(new View.OnClickListener() {

            /**
             * This method is responsible to enable user to the log in UI if they decide
             * to finalize the filling without validations or user creation.
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent returnIntent = getIntent();
                String message = "Sign up canceled";
                returnIntent.putExtra("message", message);
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    /**
     * This class is responsible to create an Async Task sending the information of the user
     * to the RESTClientAPI, receiving the confirmation, linking the user to the Main Activity.
     * If username or email is in the server, is responsible to show this information to
     * the user.
     */
    private class CreateUserAsyncTask extends AsyncTask<String, Void, String>
    {
        /**
         * This method is responsible to send the information to the RESTClientAPI class,
         * waiting the answer about the creation of the user and the credential and enable
         * the user to the Log In Activity.
         * @param params A string with the different information typed by the user in the form.
         * @return A String value with the result of the transaction.
         */
        @Override
        protected String doInBackground (String...params){
            String givenName = params[0];
            String surname = params[1];
            String email = params[2];
            String dateOfBirth = params[3];
            short height = Short.parseShort(params[4]);
            short weight = Short.parseShort(params[5]);
            String gender = params[6];
            String address = params[7];
            short postcode = Short.parseShort(params[8]);
            short levelAct = Short.parseShort(params[9]);
            int stepsMile = Integer.parseInt(params[10]);
            String username = params[11];
            String password = Security.encryptPassword(params[12]);
            String actualDate = Input.getActualDate("date");
            java.sql.Date dob = Input.manageDate(dateOfBirth);
            java.sql.Date signUpDate = Input.manageDate(actualDate);
            String infoUsername = RESTClientAPI.findByUsername(username);
            String infoEmail = RESTClientAPI.findByEmail(email);
            String result = "";
            if(infoUsername.length()==2 && infoEmail.length()==2) {
                User newUser = new User(null, givenName, surname, email, dob, height, weight, gender, address,
                        postcode, levelAct, stepsMile);
                String resultUser = RESTClientAPI.createUser(newUser);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
                String infoUser = RESTClientAPI.findByEmail(email);
                String jsonUser = infoUser.substring(1, infoUser.length() - 1);
                User user = gson.fromJson(jsonUser, User.class);
                Credential credential = new Credential(null, username, user, password, signUpDate);
                String resultCredential = RESTClientAPI.createCredential(credential);
                result = resultUser + " " + resultCredential;
                result = "User successfully enrolled";
            } else if (infoUsername.length()!=2 || (infoEmail.length()!=2))
                    result = "Username or Email actually in use";
            return result;
        }

        /**
         * This method is responsible to show the answer of the transaction to the user.
         * @param result Receives a string with the information about the result.
         */
        @Override
        protected void onPostExecute (String result){
            Toast toast =
                    Toast.makeText(getApplicationContext(),
                            result, Toast.LENGTH_SHORT);
            toast.show();
            if (result.equals("User successfully enrolled")) {
                Intent returnIntent = getIntent();
                String message = "Successfully User Created";
                returnIntent.putExtra("message", message);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        }
    }
}
