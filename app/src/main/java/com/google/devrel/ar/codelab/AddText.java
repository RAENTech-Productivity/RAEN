package com.google.devrel.ar.codelab;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.gamesparks.sdk.GSEventConsumer;
import com.gamesparks.sdk.android.GSAndroidPlatform;
import com.gamesparks.sdk.api.autogen.GSResponseBuilder;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

public class AddText extends AppCompatActivity {

    LocationManager locationManager;
    double longitudeBest, latitudeBest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        MultiAutoCompleteTextView txtView = (MultiAutoCompleteTextView) findViewById(R.id.add_message);
        txtView.setGravity(Gravity.CENTER_HORIZONTAL);
        setSupportActionBar(toolbar);

        Button btn = (Button)findViewById(R.id.add_text_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Log.i("GOTHERECLICK", "button");
                    Location location = toggleBestUpdates();
                    Log.i("GOTHERELOCATION", "" + location);

                    double lat;
                    double lon;
                    double bearing;
                    String messageText;

                    if (location != null) {
                        lat = location.getLatitude();

                        lon = location.getLongitude();
                        bearing = location.getBearing();

//                    Log.i("GOTHERECLICK", "LAT: "+ lat);
//                    Log.i("GOTHERECLICK", "LAT: "+ lat);
                    } else {
                        lat = 0;
                        lon = 0;
                        bearing = 0;
                    }
                    MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.add_message);
                    messageText = textView.getText().toString();
                    GSAndroidPlatform.gs().getRequestBuilder().createLogEventRequest()
                            .setEventKey("SAVE_GEO_MESSAGE")
                            .setEventAttribute("LAT", "" + lat)
                            .setEventAttribute("LON", "" + lon)
                            .setEventAttribute("TEXT", "" + messageText)
                            .send(new GSEventConsumer<GSResponseBuilder.LogEventResponse>() {
                                @Override
                                public void onEvent(GSResponseBuilder.LogEventResponse logEventResponse) {
                                    if (!logEventResponse.hasErrors()) {
                                        //DO something
                                        Log.i("GOTHERE", "the connection worked");
                                    } else {
                                        Log.i("GOTHERE", "connection failed");
                                        Log.i("GOTHEREDATA", logEventResponse.toString());
                                    }
                                    Log.i("GOTHEREDATA", "completed event");
                                }

                            });
                }

                finally {
                    Log.i("GOTHERE", "about to start");
                    startActivity(new Intent(AddText.this, ArMainActivity.class));
                    Log.i("GOTHERE", "started activity");
                }

            }
        });
    }

    private boolean checkLocation(){
        if(!isLocationEnabled()){
            Log.i("GOTHERE", "location is not enabled");
            showAlert();
        }
        Boolean locationStatus = isLocationEnabled();
        Log.i("GOTHERE", "return location permission status:" + locationStatus);
        return locationStatus;
    }

    private void showAlert() {
        Log.i("GOTHERE", "Location Alert");
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.i("GOTHERE", "location Manager: " + locationManager);
        Log.i("GOTHERE", "location Manager GPS: " + LocationManager.GPS_PROVIDER);
        Log.i("GOTHERE", "location Manager NETWORK: " + LocationManager.NETWORK_PROVIDER);
        if (LocationManager.GPS_PROVIDER!=null && LocationManager.NETWORK_PROVIDER!=null  && locationManager!=null) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return false;
    }

   public Location toggleBestUpdates() {
        Log.i("GOTHERE", "best updates");
        if(!checkLocation()){
            Log.i("GOTHERE", "check if location is enabled");
            return null;
        }
        else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider = locationManager.getBestProvider(criteria, false);
            provider = LocationManager.GPS_PROVIDER;
            Log.i("GOTHERELOC", "provider:"+ provider);
            if(provider != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Check Permissions Now
                    Log.i("GOTHERELOC", "no permission");
                    int REQUEST_LOCATION = 2;
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION);

                } else {
                    Log.i("GOTHERELOC", "permission granted");
                    // permission has been granted, continue as usual
                    locationManager.requestLocationUpdates(provider, 2 * 60 * 1000, 10,
                            locationListenerBest);
//                    Toast.makeText(this, "Best Provider is " + provider, Toast.LENGTH_LONG).show();
                    return locationManager.getLastKnownLocation(provider);
                }
            }
        }
        return null;
    }

    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeBest = location.getLongitude();
            latitudeBest = location.getLatitude();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

            @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(
                    AddText.this, "Please Enable GPS and Internet",
                    Toast.LENGTH_SHORT).show();
        }

            @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(
                    AddText.this, "GPS and Internet Enabled",
                    Toast.LENGTH_SHORT).show();
        }
    };
}