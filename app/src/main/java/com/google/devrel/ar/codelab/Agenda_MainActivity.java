package com.google.devrel.ar.codelab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import java.util.ArrayList;

public class Agenda_MainActivity extends AppCompatActivity {

    CalendarView cv;

    Button btnAdd = null;
    ArrayList<Day> Sched = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_agenda);
        Log.i("GOTHERE", "Agenda activity started");

        FilingStuff fh = new FilingStuff();
        Sched = fh.deserializeObject();

        btnAdd = this.findViewById(R.id.button2);
    }

    public void sendMessage(View view)
    {
        Intent intent = new Intent(Agenda_MainActivity.this, EventSave.class);
        startActivity(intent);
    }




}
