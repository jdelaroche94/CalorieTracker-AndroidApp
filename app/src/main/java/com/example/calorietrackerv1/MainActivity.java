package com.example.calorietrackerv1;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MainActivity extends AppCompatActivity {

    private ImageView initialImageView;
    private TextInputLayout inputUsername;
    private TextInputLayout inputPassword;
    private TextView signUp;

    /**
     * This method is responsible to create the log in UI and enable the app
     * with Activities Sign In and Home
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialImageView = findViewById(R.id.initialImageView);
        inputUsername = findViewById(R.id.textInputUsername);
        inputPassword = findViewById(R.id.textInputPasswordToggle);
        signUp = findViewById(R.id.signUpLink);
        signUp.setPaintFlags(signUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        signUp.setText("Not a User? Sign Up Here");
        final TextView signUpLink= (TextView)findViewById(R.id.signUpLink);
        signUpLink.setOnClickListener(new View.OnClickListener() {

            /**
             * This method is resposible to Connect Log In with Sign Up activity
             * @param v Receives the actual view Log in
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        SignUpActivity.class);
                startActivityForResult(intent, 1); //Launch the Intent and 1 is to recognize Intent

            }
        });

        Button btnSearch = (Button) findViewById(R.id.confirmButton);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is responsible to receive username and password typed by user
             * and open and AsyncTask which confirm if the information typed is correct
             * and corresponds to the information stored in the server
             * @param view Receives a view of Log In.
             */
            @Override
            public void onClick(View view) {
                if (confirmInput(view)) {
                    String username = inputUsername.getEditText().getText().toString().trim();
                    String password = inputPassword.getEditText().getText().toString().trim();
                    ConfirmAsyncTask confirmAsyncTask = new ConfirmAsyncTask();
                    confirmAsyncTask.execute(username, password);
                }
            }

            /**
             * This method is responsible to validate information typed by user in the field
             * username, if it is empty or if it is too long.
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
                }
                else {
                    inputUsername.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate information typed by user in the field
             * password, if it is empty.
             * @return A boolean value, true if the information is correct, false if the
             *          information typed is incorrect
             */
            private boolean validatePassword(){
                String password = inputPassword.getEditText().getText().toString().trim();
                if (password.isEmpty()) {
                    inputPassword.setError("Password can't be empty");
                    return false;
                }
                else {
                    inputPassword.setError(null);
                    return true;
                }
            }

            /**
             * This method is responsible to validate both, username and password typed
             * @param view Receives a view of Log in.
             * @return A boolean value, true if username and password are correctly typed
             *          or false if they don't.
             */
            public boolean confirmInput(View view) {
                if (!validateUsername() | !validatePassword())
                    return false;
                else
                    return true;
            }

        });
    }

    /**
     * This class is responsible to create an Async Task sending the credentials of the user
     * to the RESTClientAPI, receiving the confirmation, decripting the key and linking the
     * user to the Home Activity.
     */
    private class ConfirmAsyncTask extends AsyncTask<String, Void, String> {

        /**
         * This method is responsible to validate UserName and Password and enabling
         * the user to the Home Activity
         * @param params Receives two strings with the credentials of the user
         * @return A String value with the result of the validation.
         */
        @Override
        protected String doInBackground(String... params) {
            String result = "Username or password is incorrect";
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
            String infoCredential = RESTClientAPI.findByUsername(params[0]);
            if (infoCredential.length()!=2) {
                String jsonCredential = infoCredential.substring(1, infoCredential.length() - 1);
                Credential credential = gson.fromJson(jsonCredential, Credential.class);
                String password = "";
                try {
                    password = Security.decryptPassword(credential.getPassword());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!password.equals(""))
                    if (password.equals(params[1])) {
                        result = "Correct validation";
                        Intent intent = new Intent(MainActivity.this,
                                HomeActivity.class);
                        intent.putExtra("userId", Long.toString(credential.getUserId().getUserId()));
                        intent.putExtra("givenName", credential.getUserId().getGivenName());
                        //startActivity(intent); // Este linea me permite lanzar un intent pero sin esperar informacion de la siguiente actividad.
                        startActivityForResult(intent, 2); //Esto lanza el intent que va en el parametro,
                        // y le entrega un requestCode que es con el que reconocemos el intent. requestCode puede ser tipo final.
                    }
            }
            return result;
        }

        /**
         * This method is responsible to show the answer on the screen of the user.
         * @param result Receives a String with the result of the previous method.
         */
        @Override
        protected void onPostExecute(String result) {
            Toast toast =
                    Toast.makeText(getApplicationContext(),
                            result, Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    /**
     * This method is responsible to receive the answers from the Activities Home and Signup
     * @param requestCode Receives an integer with the ID of the Intent that finishes.
     * @param resultCode Receives an Integer with the result of the finishes transaction
     * @param data Receives a String with the message sent by the Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){ //Aunque resultCode es tipo INT, yo puedo preguntar asi, importante, hay otras opciones de respuesta.
                String message=data.getStringExtra("message"); //Esta linea permite la extracion de la informacion desde el Intent llamado data.
            }
            if(resultCode == RESULT_CANCELED){
                String message=data.getStringExtra("message");
            }
        }
        if (requestCode == 2) {
            if(resultCode == RESULT_CANCELED){ //Aunque resultCode es tipo INT, yo puedo preguntar asi, importante, hay otras opciones de respuesta.
                inputPassword.getEditText().setText("");
                inputUsername.getEditText().setText("");
            }
        }
    }
}