package com.google.devrel.ar.codelab;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.LocationProvider;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddText extends AppCompatActivity {

    @Override
    public void onStart()
    {
        super.onStart();

        GSAndroidPlatform.gs().start();
        Log.i("GOTHEREGS", "started GS");
    }

    LocationManager locationManager;
    LocationListener locationListener;

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

    public Location checkPermissionLocation(){
        boolean permission;
        Location location;
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permission = false;
            location = null;
            ActivityCompat.requestPermissions(
                    this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,},
                    101);


        }
        else{
            permission = true;
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            onProviderEnabled(LocationManager.NETWORK_PROVIDER);
//            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationManager =  (LocationManager)getSystemService(LOCATION_SERVICE);


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
                Location location = checkPermissionLocation();
//                boolean permission = checkPermissionLocation();
                double lat;
                double lon;
                double bearing;

//                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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
                        .setEventAttribute("TEXT", "android connect")
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

}