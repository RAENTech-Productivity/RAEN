package com.google.devrel.ar.codelab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.devrel.ar.codelab.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Agenda_MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    String selDate;

    DataBaseHelper myDB;
    Button btnAdd,btnView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_agenda);

//        FileHandler fh = new FileHandler();
//        sched = fh.deserializeObject();
        calendarView = this.findViewById(R.id.calendarView);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//        sdf.applyPattern("MM/dd/yyyy");
//        Log.i("GOTHERE", "Date format: " + sdf);
//        selDate = sdf.format(new Date(calendarView.getDate()), "", );

        selDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                selDate = "" + month + "/" + date + "/" + year;
            }
        });

        editText = (EditText) findViewById(R.id.editStuff);
        btnAdd = (Button) findViewById(R.id.btnSave);
        btnView = (Button) findViewById(R.id.btnView);
        myDB = new DataBaseHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editText.getText().toString();

                if(editText.length()!= 0){
                    AddData(newEntry, selDate);
                    editText.setText("");
                }else{
                    Toast.makeText(Agenda_MainActivity.this, "You must put something in the text field!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Agenda_MainActivity.this, EventList.class);
                startActivity(intent);
            }
        });


    }

    public void AddData(String newEntry, String dayy) {

        boolean insertData = myDB.addData(newEntry, dayy);

        if(insertData==true){
            Toast.makeText(this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
        }
    }
}