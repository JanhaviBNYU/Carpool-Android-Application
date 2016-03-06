///referred YouTUbe: https://www.youtube.com/watch?v=Wzx9mlkNHfg
package com.example.janhavibagwe.carpoolapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Context context;
    TextView dateValue;

    public DatePicker(Context context){
        this.context = context;
    }

    public DatePicker(){

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog;

        dialog = new DatePickerDialog(getActivity(),this,year,month,day);
        dialog.getDatePicker().setMinDate(c.getTimeInMillis());

        return dialog;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        dateValue = (TextView) ((Activity)context).findViewById(R.id.tvDateValue);
        dateValue.setText(monthOfYear+"/"+dayOfMonth+"/"+year);
    }
}
