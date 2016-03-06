package com.example.janhavibagwe.carpoolapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class GoogleMaps extends FragmentActivity {

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting a reference to the map
        googleMap = supportMapFragment.getMap();

        // Setting a click event handler for the map
        googleMap.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String city = addresses.get(0).getLocality();

                Toast.makeText(getApplicationContext(), city, Toast.LENGTH_LONG).show();

                Intent intentFromMain = getIntent();
                SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.SharedPrefName), MODE_PRIVATE).edit();
                Log.d("CARPOOLCHECK:","Intent222 "+intentFromMain.getStringExtra("Map") + city);
                editor.putString(intentFromMain.getStringExtra("Map"), city);
                editor.commit();

                Intent i = new Intent(getApplicationContext(),MainScreen.class);
                startActivity(i);

            }
        });
    }
}
