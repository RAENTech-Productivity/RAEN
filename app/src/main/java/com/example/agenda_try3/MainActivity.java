package com.example.agenda_try3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import java.util.ArrayList;
import java.util.logging.FileHandler;

public class MainActivity extends AppCompatActivity {

    CalendarView cv;

    Button btnAdd = null;
    ArrayList<Day> Sched = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FilingStuff fh = new FilingStuff();
        Sched = fh.deserializeObject();

        btnAdd = this.findViewById(R.id.button2);
    }

    public void sendMessage(View view)
    {
        Intent intent = new Intent(MainActivity.this, EventSave.class);
        startActivity(intent);
    }




}
