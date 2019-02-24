package com.google.devrel.ar.codelab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gamesparks.sdk.GSEventConsumer;
import com.gamesparks.sdk.android.GSAndroidPlatform;
import com.gamesparks.sdk.api.autogen.GSResponseBuilder;

public class AddText extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        GSAndroidPlatform.initialise(this, "u374201md1E4", "ktbBEnAi7UjgzEFdlY8s9E892AqZoVnR", "device", true, true);

        GSAndroidPlatform.gs().setOnAvailable(new GSEventConsumer<Boolean>() {
            @Override
            public void onEvent(Boolean available) {

                if (available) {
                    //If we connect, authenticate our player
                    GSAndroidPlatform.gs().getRequestBuilder().createAuthenticationRequest().setUserName("username").setPassword("password").send(new GSEventConsumer<GSResponseBuilder.AuthenticationResponse>() {
                        @Override
                        public void onEvent(GSResponseBuilder.AuthenticationResponse authenticationResponse) {

                            if(!authenticationResponse.hasErrors()){
                                //Do something when we authenticate
                                Log.i("GOTHERE:)", "the connection worked");

                            }
                            else{
                                Log.i("GOTHERE:)", "failed");
                            }

                        }
                    });
                }
            }
        });

        //took this out of original (works?)
        GSAndroidPlatform.gs().start();

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
                        .setEventAttribute("LAT", "0.01")
                        .setEventAttribute("LONG","0.01")
                            .setEventAttribute("TEXT", "android connect")
                        .send(logEventResponse -> {
                            if(logEventResponse.hasErrors()){
                                //DO something
                                Log.i("GOTHERE", "the connection worked");
                            }
                            else{
                                //Do something
                                Log.i("GOTHEREDATA", "failed");
                            }
                        });
                startActivity(new Intent(AddText.this, MainActivity.class));
            }
        });
    }

}
