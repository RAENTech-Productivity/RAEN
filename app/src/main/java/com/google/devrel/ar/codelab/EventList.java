package com.google.devrel.ar.codelab;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.devrel.ar.codelab.DataBaseHelper;

import java.util.ArrayList;

public class EventList extends AppCompatActivity {

    DataBaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("GOTHERE", "event list started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        Log.i("GOTHERE", "event list content view");

        ListView listView = findViewById(R.id.listView);
        Log.i("GOTHERE", "event list list view");
        myDB = new DataBaseHelper(this);
        Log.i("GOTHERE", "event list database");

        //populate an ArrayList<String> from the database and then view it
        ArrayList<String> theList = new ArrayList<>();
        Log.i("GOTHERE", "event list array list");
        Cursor data = myDB.getListContents();
        Log.i("GOTHERE", "event list get DB contents");
        Log.i("GOTHERE", "DB contents: " + myDB.getListContents());
        if(data.getCount() == 0){
            Log.i("GOTHERE", "event list no data");
            Toast.makeText(this, "No events yet",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                Log.i("GOTHERE", "event list show data");
                theList.add(data.getString(1));
                Log.i("GOTHERE", "data.getString() returns: "+ data.getString(1));
                Log.i("GOTHERE", "event list get string");
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                Log.i("GOTHERE", "make Array Adapter");
                listView.setAdapter(listAdapter);
                Log.i("GOTHERE", "set view to adapter");
            }
        }

    }
}