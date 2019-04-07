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
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.gamesparks.sdk.android.GSAndroidPlatform;

public class ArIntro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_intro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GSAndroidPlatform.initialise(this, "u374201md1E4",
                "ktbBEnAi7UjgzEFdlY8s9E892AqZoVnR", "device",
                false, true);
        Log.i("GOTHEREGS", "initial GS");
        Log.i("GOTHEREGS", "GS:" + GSAndroidPlatform.gs());
    authenticateGS();

        Button btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateGS();

                Log.i("GOTHERE", "about to start");
                startActivity(new Intent(ArIntro.this, AddText.class));
                Log.i("GOTHERE", "started activity");

            }
        });

    }
    @Override
    public void onStart()
    {
        super.onStart();

        GSAndroidPlatform.gs().start();
        Log.i("GOTHEREGS", "started GS");

    }
    private void authenticateGS(){
        GSAndroidPlatform.gs().setOnAvailable(available -> {
            Log.i("GOTHEREGS", "on event 1");

            if (available) {
                Log.i("GOTHEREGS", "connection to GS");

                //If we connect, authenticate our player
                GSAndroidPlatform.gs().getRequestBuilder().createDeviceAuthenticationRequest().
                        send(authenticationResponse -> {

                            if(!authenticationResponse.hasErrors()){
                                //Do something when we authenticate
                                Log.i("GOTHEREGS", "authenticated");
                            }
                            else {
                                Log.i("GOTHEREGS", "authentication failed");
                            }

                        });
            }
        });
    }
}
