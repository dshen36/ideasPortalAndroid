package com.example.brich200.mobileideasportal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.brich200.mobileideasportal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SuccessStoriesMain extends ActionBarActivity {

    String asynchTaskType;
    int[] availableIds;
    Spinner dropDownSpinner;
    String[] subMenus = {"(Select Page)","Ideas","Lab Weeks","Challenges","Partners","Success Stories"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_stories_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,subMenus);

        dropDownSpinner = (Spinner) findViewById(R.id.spinner);
        dropDownSpinner.setAdapter(adapter);
        dropDownSpinner.setOnItemSelectedListener(spinnerListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_success_stories_main, menu);
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

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            System.out.println(parent.getItemAtPosition(position).toString());
            if (parent.getItemAtPosition(position).toString().equals("Ideas")) {
                startActivity(new Intent(SuccessStoriesMain.this, Directory.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Partners")) {
                startActivity(new Intent(SuccessStoriesMain.this, Partners.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Success Stories")) {
                startActivity(new Intent(SuccessStoriesMain.this, SuccessStoriesMain.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Challenges")) {
                startActivity(new Intent(SuccessStoriesMain.this, Challenges.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Lab Weeks")) {
                startActivity(new Intent(SuccessStoriesMain.this, LabWeekDirectory.class));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            dropDownSpinner.setOnItemSelectedListener(spinnerListener);
        }

    };

    public void cxInnovationsClick(View view) {
        startActivity(new Intent(this, CxInnovationMain.class));
    }

    public void createIdea(View view) {
        startActivity(new Intent(this, SubmitActivity.class));
    }

    public void startWelcome(View view) {startActivity(new Intent(this, MyActivity.class));}
}
