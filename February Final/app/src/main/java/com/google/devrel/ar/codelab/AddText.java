package com.google.devrel.ar.codelab;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gamesparks.sdk.GSEventConsumer;
import com.gamesparks.sdk.android.GSAndroidPlatform;
import com.gamesparks.sdk.api.GSData;
import com.gamesparks.sdk.api.autogen.GSResponseBuilder;
import com.gamesparks.sdk.api.autogen.GSRequestBuilder.LogEventRequest;
import com.gamesparks.sdk.api.autogen.GSResponseBuilder.LogEventResponse;
import com.gamesparks.sdk.api.autogen.GSTypes.*;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;


import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddText extends AppCompatActivity {

    LocationManager locationManager;
    double longitudeBest, latitudeBest;
    double longitudeGPS, latitudeGPS;
    double longitudeNetwork, latitudeNetwork;

    LocationListener locationListener;

    @Override
    public void onStart()
    {
        super.onStart();

        GSAndroidPlatform.gs().start();
        Log.i("GOTHEREGS", "started GS");
    }



//    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(
                AddText.this, "Please Enable GPS and Internet",
                Toast.LENGTH_SHORT).show();
    }

//    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(
                AddText.this, "GPS and Internet Enabled",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        locationManager =  (LocationManager)getSystemService(Context.LOCATION_SERVICE);


        GSAndroidPlatform.initialise(this, "u374201md1E4", "ktbBEnAi7UjgzEFdlY8s9E892AqZoVnR", "device", false, true);
        Log.i("GOTHEREGS", "initial GS");
        GSAndroidPlatform.gs().setOnAvailable(new GSEventConsumer<Boolean>() {
            @Override
            public void onEvent(Boolean available) {
                Log.i("GOTHEREGS", "on event 1");

                if (available) {
                    Log.i("GOTHEREGS", "connection to GS");

                    //If we connect, authenticate our player
                    GSAndroidPlatform.gs().getRequestBuilder().createDeviceAuthenticationRequest().
                            send(new GSEventConsumer<GSResponseBuilder.AuthenticationResponse>() {
                        @Override
                        public void onEvent(GSResponseBuilder.AuthenticationResponse authenticationResponse) {

                            if(!authenticationResponse.hasErrors()){
                                //Do something when we authenticate
                                Log.i("GOTHEREGS", "authenticated");
                            }
                            else {
                                Log.i("GOTHEREGS", "authentication failed");
                            }

                        }
                    });
                }
            }
        });



        Button btn = (Button)findViewById(R.id.add_text_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("GOTHERECLICK", "button");
                Location location = toggleBestUpdates();
                Log.i("GOTHERECLICK", ""+location);

                double lat;
                double lon;
                double bearing;

                if (location != null) {
                    lat = location.getLatitude();

                    lon = location.getLongitude();
                    bearing = location.getBearing();
                }
                else{
                    lat = 0;
                    lon = 0;
                    bearing = 0;
                }


                GSAndroidPlatform.gs().getRequestBuilder().createLogEventRequest()
                        .setEventKey("SAVE_GEO_MESSAGE")
                        .setEventAttribute("LAT", ""+ lat )
                        .setEventAttribute("LON", "" + lon)
                        .setEventAttribute("TEXT", "gs connection")
                        .send(new GSEventConsumer<GSResponseBuilder.LogEventResponse>()
                        {
                            @Override
                            public void onEvent(GSResponseBuilder.LogEventResponse logEventResponse)
                            {
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

                Log.i("GOTHERE", "about to start");
                startActivity(new Intent(AddText.this, MainActivity.class));
                Log.i("GOTHERE", "started activity");

            }
        });
    }

    private boolean checkLocation(){
        if(!isLocationEnabled()){
            Log.i("GOTHERE", "location is not enabled");
            showAlert();
        }
        Log.i("GOTHERE", "return location permission status:" + isLocationEnabled());
        return isLocationEnabled();
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
        if (LocationManager.GPS_PROVIDER!=null && LocationManager.NETWORK_PROVIDER!=null  && locationManager!=null) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return false;
    }

    public void toggleGPSUpdates() {
        if(!checkLocation())
            return;

        else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                int REQUEST_LOCATION = 2;
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            } else {
                // permission has been granted, continue as usual
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 2 * 60 * 1000, 10, locationListenerGPS);

            }
        }
    }

    public Location toggleBestUpdates() {
        Log.i("GOTHERE", "best updates");
        if(!checkLocation()){
            Log.i("GOTHERE", "check if location is enabled");
            return null;
        }
        else {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider = locationManager.getBestProvider(criteria, true);
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

    public void toggleNetworkUpdates(View view) {
        if(!checkLocation())
            return;

        else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                int REQUEST_LOCATION = 2;
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            } else {
                // permission has been granted, continue as usual
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 60 * 1000, 10, locationListenerNetwork);
                Toast.makeText(this, "Network provider started running", Toast.LENGTH_LONG).show();
            }

        }
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
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeNetwork = location.getLongitude();
            latitudeNetwork = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private final LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeGPS = location.getLongitude();
            latitudeGPS = location.getLatitude();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };



}