package com.example.janhavibagwe.carpoolapplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    Context context;
    TextView timeValue;

    public TimePicker(Context context){
        this.context = context;
    }

    public TimePicker(){
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog;
        dialog = new TimePickerDialog(getActivity(),this,hour,minute, android.text.format.DateFormat.is24HourFormat(getActivity()));

        return dialog;
    }


    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {

        timeValue = (TextView) ((Activity)context).findViewById(R.id.tvTimeValue);
        timeValue.setText(hourOfDay+":"+minute);
    }
}
