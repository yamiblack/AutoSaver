package com.b11a.android.autosaver.ui.driving;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.b11a.android.autosaver.R;

public class AccidentDetectedActivity extends AppCompatActivity {

    private String currentTime, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_detected);
//
        Intent intent = getIntent();
        currentTime = intent.getStringExtra("currentTime");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
//
        Log.e("currentTime", currentTime);
        Log.e("latitude", String.valueOf(latitude));
        Log.e("longitude", String.valueOf(longitude));

//        intent.putExtra("currentTime", getCurrentTime());
//        intent.putExtra("latitude", decimalFormat.format(latitude));
//        intent.putExtra("longitude", decimalFormat.format(longitude));


    }

}