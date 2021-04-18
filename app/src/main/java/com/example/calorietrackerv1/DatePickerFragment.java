package com.example.calorietrackerv1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import java.util.Calendar;

/**
 * This class is responsible to create a Date picker fragment in the User Sign Up Activity.
 */
public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;

    /**
     * This method creates Date Picker Fragment
     * @param listener An Object which receives the action from user.
     * @return A Fragment with the Date Picker
     */
    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    /**
     * This method launch the date picker and shows it with the actual date.
     * @param savedInstanceState
     * @return A Dialog object which interacts with the user.
     */
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,listener,year,month,day);

        return datePickerDialog;
    }


}