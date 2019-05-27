package com.heart_rate.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class Reference extends Activity{

    //INITIALIZING REQUIRED STRING ARRAYS
    String[] ages = {"18-25", "26-35", "36-45", "46-55", "55-65", "65+"};
    String[] status_strings = {"ATHLETIC", "EXCELLENT", "GOOD", "ABOVE-AVERAGE", "AVERAGE", "BELOW-AVERAGE", "POOR"};
    String[][] ranges = {{"49-55","56-61","62-65","66-69","70-73","74-81", "81+"}
            ,{"49-54","55-61","62-65","66-70","71-74","74-81", "81+"}
            ,{"50-56","57-62","63-66","67-70","71-75","76-82", "82+"}
            ,{"50-57","58-63","64-67","68-70","72-76","77-83", "83+"}
            ,{"51-56","57-61","62-67", "68-71","72-75","76-81", "81+"}
            ,{"50-55","56-61","62-65","66-69","70-73","74-79", "79+"}};

    int ranges_i, ranges_j;
    int heartbeatArray;


    //INITIALIZING SPINNERS
    Spinner ageRangeSelector;
    Spinner heartbeatRangeSelector;
    //INITIALIZING TEXTVIEWS
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reference);


        //INITIALIZING CONTROLS
        status = (TextView) findViewById(R.id.status);
        ageRangeSelector = (Spinner) findViewById(R.id.ages);
        heartbeatRangeSelector = (Spinner) findViewById(R.id.heartbeats);


        //FILLING AGES SPINNER WITH AGE RANGES
        ArrayAdapter<String> agesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ages);
        ageRangeSelector.setAdapter(agesAdapter);

        //SETTING AGE RANGE SELECTOR ONITEMSELECTED LISTENER
        ageRangeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> heartbeatsAdapter = new ArrayAdapter<String>(Reference.this, android.R.layout.simple_spinner_dropdown_item, ranges[position]);
                heartbeatRangeSelector.setAdapter(heartbeatsAdapter);
                ranges_i = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //IGNORE
            }
        });
        //SETTING HEART BEAT RANGE SELECTOR ONITEMSELECTED LISTENER
        heartbeatRangeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ranges_j = position;
                setStatusText(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //IGNORE
            }
        });



    }
    //METHODS
    private void setStatusText(int pos){
        if(pos == 2 || pos == 6 || pos == 5)
            status.setText("YOU HAVE A " + status_strings[pos] + " HEART");
        else
            status.setText("YOU HAVE AN " + status_strings[pos] + " HEART");
    }

}
