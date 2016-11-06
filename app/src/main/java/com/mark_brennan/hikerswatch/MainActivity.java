package com.mark_brennan.hikerswatch;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements LocationListener {

    LocationManager locationManager;
    String provider;

    TextView latView;
    TextView lngView;
    TextView accuracyView;
    TextView speedView;
    TextView bearingView;
    TextView addressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        latView = (TextView)findViewById(R.id.lat);
        lngView = (TextView)findViewById(R.id.lng);
        accuracyView = (TextView)findViewById(R.id.accuracy);
        speedView = (TextView)findViewById(R.id.speed);
        bearingView = (TextView)findViewById(R.id.bearing);
        addressView = (TextView)findViewById(R.id.address);

        try {
            Location location = locationManager.getLastKnownLocation(provider);
            onLocationChanged(location);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onLocationChanged(Location location) {

        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        Float bearing = location.getBearing();
        Float speed = location.getSpeed();
        Float accuracy = location.getAccuracy();

        // Get location address
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<android.location.Address> listAddresses = geocoder.getFromLocation(lat, lng, 1);
            if (listAddresses != null && listAddresses.size() > 0) {

                String addressHolder = "";

                for (int i = 0; i <= listAddresses.get(0).getMaxAddressLineIndex(); i++) {
                    addressHolder += listAddresses.get(0).getAddressLine(i) + "\n";
                }
                addressView.setText("Address:\n" + addressHolder);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        latView.setText("Latitude: " + String.valueOf(lat));
        lngView.setText("Longitude: " + String.valueOf(lng));
        bearingView.setText("Bearing: " + String.valueOf(bearing));
        speedView.setText("Speed: " + String.valueOf(speed) + "m/s");
        accuracyView.setText("Accuracy: " + String.valueOf(accuracy) + "m");


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
