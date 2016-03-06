package com.example.janhavibagwe.carpoolapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;


public class PostCarpool extends Fragment {

    View view;
    ImageButton add;
    Button selectDate;
    TextView dateValue;
    Button selectTime;
    TextView timeValue;

    String Start;
    String Destination;
    String Date;
    String Time;
    int fare;
    int passengers;
    String UserName;
    String contact;
    String UserID;

    private OnFragmentInteractionListener mListener;

    public static PostCarpool newInstance(String param1, String param2) {
        PostCarpool fragment = new PostCarpool();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public PostCarpool() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //showDateDialogOnClick();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_post_carpool, container, false);

        dateValue = (TextView) view.findViewById(R.id.tvDateValue);
        selectDate = (Button) view.findViewById(R.id.btnSelectDate);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                com.example.janhavibagwe.carpoolapplication.DatePicker pickerDialogue = new com.example.janhavibagwe.carpoolapplication.DatePicker(getActivity());
                pickerDialogue.show(getActivity().getFragmentManager(),"date_picker");
            }
        });

        timeValue = (TextView) view.findViewById(R.id.tvTimeValue);
        selectTime = (Button) view.findViewById(R.id.btnSelectTime);

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker timePickerDialogue = new TimePicker(getActivity());
                timePickerDialogue.show(getActivity().getFragmentManager(),"timer_picker");
            }
        });



        if(savedInstanceState != null) {

            Log.d("CARPOOLCHECK","In SavedInstanceState" + savedInstanceState.getString("Date").toString() );
            Log.d("CARPOOLCHECK","In SavedInstanceState" + savedInstanceState.getString("Time").toString() );
            ((TextView)view.findViewById(R.id.tvDateValue)).setText(savedInstanceState.getString("Date").toString());
            ((TextView)view.findViewById(R.id.tvTimeValue)).setText(savedInstanceState.getString("Time").toString());
        }

        add = (ImageButton)view.findViewById(R.id.imgbtnAdd);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("CARPOOLCHECK:","Adding OBJ to CARPOOL");

                Start = ((EditText)view.findViewById(R.id.etStart)).getText().toString();
                Destination = ((EditText)view.findViewById(R.id.etDestination)).getText().toString();
                Date = ((TextView)view.findViewById(R.id.tvDateValue)).getText().toString();
                Time = ((TextView)view.findViewById(R.id.tvTimeValue)).getText().toString();
                fare = Integer.parseInt(((EditText)view.findViewById(R.id.etFareValue)).getText().toString());
                passengers = Integer.parseInt(((EditText)view.findViewById(R.id.etPass)).getText().toString());
                contact = ((EditText) view.findViewById(R.id.etContactValue)).getText().toString();

                SharedPreferences prefs = getActivity().getSharedPreferences(getResources().getString(R.string.SharedPrefName),getActivity().MODE_PRIVATE);
                UserName = prefs.getString("UserName", null);
                UserID = prefs.getString("UserID", null);

                Log.d("CARPOOLCHECK:","Checking fields for empty");

                if(Start == "" || Destination == "" || Date == "" || Time == "" || contact == ""){

                    Toast.makeText(getActivity(),"Enter all values",Toast.LENGTH_LONG).show();
                }

                else{

                    Log.d("CARPOOLCHECK:","PostCarpool: Adding OBJ to CARPOOL");

                    ParseObject user = new ParseObject("CarPools");

                    user.put("Start",Start);
                    user.put("Destination",Destination);
                    user.put("Date",Date);
                    user.put("Time",Time);
                    user.put("Fare",fare);
                    user.put("Passengers",passengers);
                    user.put("Contact",UserName);
                    user.put("Phone",contact);
                    user.put("SeatsBooked",0);
                    user.put("FarePerPerson",fare);
                    user.put("Available","Yes");
                    user.put("PassMembers","");
                    user.put("UserID", UserID);

                    user.saveInBackground();

                    Toast.makeText(getActivity(),"CarPool Listed",Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Date = ((TextView)view.findViewById(R.id.tvDateValue)).getText().toString();
        Time = ((TextView)view.findViewById(R.id.tvTimeValue)).getText().toString();
        outState.putString("Date",Date);
        outState.putString("Time",Time);

        super.onSaveInstanceState(outState);
    }

}
