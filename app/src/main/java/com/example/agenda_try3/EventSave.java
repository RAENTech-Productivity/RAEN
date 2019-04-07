package com.example.agenda_try3;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EventSave extends AppCompatActivity {

    DataBaseHelper myDB;
    Button btnSave, btnView;
    EditText editT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_save);

        btnSave = this.findViewById(R.id.btnSave);
        editT = this.findViewById(R.id.editStuff);
        btnView = this.findViewById(R.id.btnView);
        myDB = new DataBaseHelper(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editT.getText().toString();

                if(editT.length()!=0){
                    AddData(newEntry);
                    editT.setText("");}
                    else
                {Toast.makeText(EventSave.this, "Put something in the text field",Toast.LENGTH_LONG).show();}
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventSave.this, EventList.class);
                startActivity(intent);
            }
        });


    }

    public void AddData(String newEntry) {

        long result = myDB.addEvents(newEntry);
        boolean insertData = myDB.addData(result);

        if(insertData==true){
            Toast.makeText(this, "Event Successfully Inserted!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
        }
    }
}
