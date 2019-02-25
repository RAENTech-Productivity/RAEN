package com.google.devrel.ar.codelab;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
//import com.gamesparks.sdk.api.GSEventConsumer;

import java.util.UUID;

public class AddText extends AppCompatActivity {

    @Override
    public void onStart()
    {
        super.onStart();

        GSAndroidPlatform.gs().start();
        Log.i("GOTHEREGS", "started GS");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        GSAndroidPlatform.initialise(this, "u374201md1E4", "ktbBEnAi7UjgzEFdlY8s9E892AqZoVnR", "device", false, true);
        Log.i("GOTHEREGS", "initial GS");

//        GSAndroidPlatform.gs().setOnAvailable(new GSEventConsumer<Boolean>() {
//            @Override
//            public void onEvent(Boolean available) {
//
//                if (available) {
//                    //If we connect, authenticate our player
//                    GSAndroidPlatform.gs().getRequestBuilder().createAuthenticationRequest().setUserName("username").setPassword("password").send(new GSEventConsumer<GSResponseBuilder.AuthenticationResponse>() {
//                        @Override
//                        public void onEvent(GSResponseBuilder.AuthenticationResponse authenticationResponse) {
//
//                            if(!authenticationResponse.hasErrors()){
//                                //Do something when we authenticate
//                                Log.i("GOTHERE:)", "the connection worked");
//
//                            }
//                            else{
//                                Log.i("GOTHERE:)", "failed");
//                            }
//
//                        }
//                    });
//                }
//            }
//        });

//        Secure.getString(getContext().getContentResolver(),
//                Secure.ANDROID_ID);
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
//        int MY_PERMISSIONS_PHONE_STATE =0;
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//            Log.i("GOTHEREPermission", "permission?");
//            // Permission is not granted
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_CONTACTS},
//                    MY_PERMISSIONS_PHONE_STATE);
//        }
//        else {
//            tmDevice = "" + tm.getDeviceId();
//            tmSerial = "" + tm.getSimSerialNumber();
//            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//            UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
//            String deviceId = deviceUuid.toString();
//        }

//        GSAndroidPlatform.gs().getRequestBuilder().createDeviceAuthenticationRequest()
//                .send(authenticationResponse -> {
//            if (!authenticationResponse.hasErrors()) {
//                // reaches this point in the code
//                Log.i("GOTHEREworked","Device Authenticated...");
//                //tell message provider we have been authenticated
//
//            } else {
//                Log.i("GOTHEREfailed","Error authenticating...");
//            }
//
//        });

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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button btn = (Button)findViewById(R.id.add_text_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("GOTHERECLICK", "button");

                GSAndroidPlatform.gs().getRequestBuilder().createLogEventRequest()
                        .setEventKey("SAVE_GEO_MESSAGE")
                        .setEventAttribute("LAT", "0.001")
                        .setEventAttribute("LON", "0.001")
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


//                GSAndroidPlatform.gs().getRequestBuilder().createLogEventRequest().setEventKey("setDetails")
//                        .setEventAttribute("name", string)
//                        .setEventAttribute("age", int)
//                            .setEventAttribute("gender", string).send(new GSEventConsumer<GSResponseBuilder.LogEventResponse>() {
//                    @Override
//                    public void onEvent(GSResponseBuilder.LogEventResponse logEventResponse) {
//                        if(logEventResponse.hasErrors()){
//                            //DO something
//                        }
//                        else{
//                            //Do something
//                        }
//                    }
//                });
//            }
//        }
//    });

                Log.i("GOTHERE", "about to start");
                startActivity(new Intent(AddText.this, MainActivity.class));
                Log.i("GOTHERE", "started activity");

            }
        });
    }
}