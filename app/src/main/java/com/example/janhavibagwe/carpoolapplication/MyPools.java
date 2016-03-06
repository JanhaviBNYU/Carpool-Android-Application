package com.example.janhavibagwe.carpoolapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class MyPools extends Fragment {


    private OnFragmentInteractionListener mListener;
    View view;
    SearchObject searchObject;
    ArrayList<SearchObject> SearchList;
    GridView myPoolsView;
    String userID;

    public static MyPools newInstance(String param1, String param2) {
        MyPools fragment = new MyPools();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public MyPools() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_pools, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(getResources().getString(R.string.SharedPrefName), getActivity().MODE_PRIVATE);
        userID = prefs.getString("UserID", null);

        Log.d("CARPOOLCHECK:", "OnCreateMyPools" + userID);

        SearchList = new ArrayList<SearchObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CarPools");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    Log.d("CARPOOLCHECK:", "Total Objects" + objects.size());

                    for (ParseObject obj : objects) {


                        if ((obj.get("PassMembers").toString()).contains(userID)) {

                            Log.d("CARPOOLCHECK: ", "In");

                            searchObject = new SearchObject();
                            searchObject.start = obj.get("Start").toString();
                            searchObject.destination = obj.get("Destination").toString();
                            searchObject.date = obj.get("Date").toString();
                            searchObject.time = obj.get("Time").toString();
                            searchObject.fare = Integer.parseInt(obj.get("Fare").toString());
                            searchObject.passengers = Integer.parseInt(obj.get("Passengers").toString());
                            searchObject.contact = obj.get("Contact").toString();
                            searchObject.phone = obj.get("Phone").toString();
                            searchObject.carPoolID = obj.getObjectId();
                            searchObject.farePP = Double.parseDouble(obj.get("FarePerPerson").toString());


                            SearchList.add(searchObject);

                        }
                    }

                }

                SetTableResults(SearchList);
            }
        });

        return view;
    }

    public void DisplayCancelAlert(final SearchObject searchObject) {


        Log.d("CARPOOLCHECK","2222");
        AlertDialog.Builder cancelDialogue = new AlertDialog.Builder(getActivity());
        cancelDialogue.setTitle("Carpool");

        Log.d("CARPOOLCHECK","33");
        cancelDialogue
                .setMessage("Cancel this booking?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("CARPOOLCHECK:", "OnClickYes");
                        CancelCar(searchObject);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        Log.d("CARPOOLCHECK","444444444444");
        AlertDialog alertDialog = cancelDialogue.create();
        alertDialog.show();
    }

    public void CancelCar(final SearchObject searchObject) {

        Log.d("CARPOOLCHECK:", "InCancelCar");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CarPools");
        query.getInBackground(searchObject.carPoolID, new GetCallback<ParseObject>() {

            int seatsBooked;
            int noPass;
            int fare;
            int farePerPerson;
            public void done(ParseObject carpool, ParseException e) {
                if (e == null) {
                    Log.d("CARPOOLCHECK","555");
                    seatsBooked = Integer.parseInt(carpool.get("SeatsBooked").toString()) - 1;
                    noPass = Integer.parseInt(carpool.get("Passengers").toString());
                    fare = Integer.parseInt(carpool.get("Fare").toString());
                    String poolMembers = carpool.get("PassMembers").toString();
                    Log.d("CARPOOLCHECK","66666666666666");
                    if(seatsBooked==0){
                        farePerPerson = fare;
                    }
                    else{
                        farePerPerson = fare / seatsBooked;
                    }
                    SharedPreferences prefs = getActivity().getSharedPreferences(getResources().getString(R.string.SharedPrefName), getActivity().MODE_PRIVATE);
                    String userID = prefs.getString("UserID", null);
                    poolMembers.replace(userID, "");
                    carpool.put("FarePerPerson", farePerPerson);
                    carpool.put("SeatsBooked", seatsBooked);

                    if (noPass == seatsBooked) {
                        carpool.put("Available", "No");

                    } else {
                        carpool.put("Available", "Yes");
                    }

                    carpool.saveInBackground();
                    Toast.makeText(getActivity(), "CarPool Cancelled", Toast.LENGTH_LONG);
                }
            }
        });

        SetTableResults(SearchList);
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


    public void SetTableResults(ArrayList<SearchObject> searchList) {

        TextView Start;
        TextView Destination;
        TextView Date;
        TextView Phone;
        TextView Contact;
        ImageView img;
        TextView FarePP;

        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.MyPoolsTable);
        TableRow tableRow;

        while (tableLayout.getChildCount() > 1)
            tableLayout.removeView(tableLayout.getChildAt(tableLayout.getChildCount() - 1));

        for (final SearchObject searchRow : searchList) {

            tableRow = new TableRow(getActivity());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            img = new ImageView(getActivity());
            img.setImageResource(R.drawable.tick);

            tableRow.addView(img);

            Start = new TextView(getActivity());
            Start.setText(searchRow.start);
            tableRow.addView(Start);

            Destination = new TextView(getActivity());
            Destination.setText(searchRow.destination);
            tableRow.addView(Destination);

            Date = new TextView(getActivity());
            Date.setText(searchRow.date);
            tableRow.addView(Date);

            Contact = new TextView(getActivity());
            Contact.setText(searchRow.contact);
            tableRow.addView(Contact);

            Phone = new TextView(getActivity());
            Phone.setText(searchRow.phone);
            tableRow.addView(Phone);

            FarePP = new TextView(getActivity());
            FarePP.setText(String.valueOf(searchRow.farePP));
            tableRow.addView(FarePP);


            tableRow.setClickable(true);  //allows you to select a specific row

            tableRow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    v.setBackgroundColor(Color.GRAY);
                    DisplayCancelAlert(searchRow);
                }
            });

            tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}