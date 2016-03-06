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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class SearchPool extends Fragment {

    ImageButton SearchBtn;
    String etStart;
    String etDest;
    View view;
    SearchObject searchObject;
    ArrayList<SearchObject> SearchList;
    GridView gridView;


    private OnFragmentInteractionListener mListener;

    public static SearchPool newInstance(String param1, String param2) {
        SearchPool fragment = new SearchPool();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public SearchPool() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search_pool, container, false);

        Log.d("CARPOOLCHECK:","SEARCHPOOL: OnCreateView");

        SearchBtn = (ImageButton) view.findViewById(R.id.btnSearch);
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etStart = ((EditText) view.findViewById(R.id.etStartSearch)).getText().toString();
                etDest = ((EditText) view.findViewById(R.id.etDestSearch)).getText().toString();

                SearchResults searchResults = new SearchResults();
                searchResults.execute();
            }
        });

        return view;
    }

    public void DisplayBookAlert(final SearchObject searchObject){

        AlertDialog.Builder bookDialogue = new AlertDialog.Builder(getActivity());
        bookDialogue.setTitle("Carpool");

        // set dialog message
        bookDialogue
                .setMessage("Book this CAR?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Log.d("CARPOOLCHECK:","OnClickYes");
                        BookCar(searchObject);
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = bookDialogue.create();
        alertDialog.show();
    }

    public void BookCar(SearchObject searchRow){

        Log.d("CARPOOLCHECK:","InBookCar");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CarPools");

        // Retrieve the object by id
        query.getInBackground(searchRow.carPoolID, new GetCallback<ParseObject>() {
            public void done(ParseObject carpool, ParseException e) {
                if (e == null) {

                   int seatsBooked = Integer.parseInt(carpool.get("SeatsBooked").toString()) + 1;
                   int noPass = Integer.parseInt(carpool.get("Passengers").toString());
                   int fare = Integer.parseInt(carpool.get("Fare").toString());
                   String poolMembers = carpool.get("PassMembers").toString();

                   int farePerPerson = fare/seatsBooked;
                   SharedPreferences prefs = getActivity().getSharedPreferences(getResources().getString(R.string.SharedPrefName),getActivity().MODE_PRIVATE);
                   String userID = prefs.getString("UserID", null);

                   if(poolMembers == null){

                       carpool.put("PassMembers", userID);

                   }
                    else{

                       carpool.put("PassMembers", poolMembers + ";" + userID);
                   }

                   carpool.put("FarePerPerson",farePerPerson);
                   carpool.put("SeatsBooked", seatsBooked);

                   if(noPass == seatsBooked){
                       carpool.put("Available", "No");

                   }

                   carpool.saveInBackground();

                   Toast.makeText(getActivity(),"CarPool Booked", Toast.LENGTH_LONG);

                }
            }
        });


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

    private class SearchResults extends AsyncTask<String, Void, ArrayList<SearchObject>> {

        protected ArrayList<SearchObject> doInBackground(String... urls) {

            Log.d("CARPOOLCHECK:","SearchPool:doInBackground");
            SearchList = new ArrayList<SearchObject>();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("CarPools");
            query.whereMatches("Start",etStart);
            query.whereMatches("Destination",etDest);
            query.whereMatches("Available","Yes");

            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {

                        for (ParseObject obj : objects) {

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

                            SearchList.add(searchObject);

                        }
                    } else {

                    }
                }
            });

            return SearchList;
        }

        protected void onPostExecute(final ArrayList<SearchObject> searchList) {
            gridView = (GridView) view.findViewById(R.id.gridView);
            gridView.setAdapter(new GridAdapter(getActivity(), searchList));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setBackgroundColor(Color.GRAY);
                    DisplayBookAlert(searchList.get(position));

                }
            });


        }
    }

}
