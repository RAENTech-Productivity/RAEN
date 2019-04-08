package com.example.android.afafafafa;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button one = (Button) findViewById(R.id.button_mem);
        one.setOnClickListener(this); // calling onClick() method
        Button two = (Button) findViewById(R.id.button_note);
        two.setOnClickListener(this);
        Button three = (Button) findViewById(R.id.button_agenda);
        three.setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_mem:
                Intent i = new Intent(MainActivity.this, memos.class);
                startActivity(i);
            case R.id.button_agenda:
                Intent ii = new Intent(MainActivity.this, Agenda.class);
                startActivity(ii);
            case R.id.button_note:
                Intent iii = new Intent(MainActivity.this, Notes.class);
                startActivity(iii);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
