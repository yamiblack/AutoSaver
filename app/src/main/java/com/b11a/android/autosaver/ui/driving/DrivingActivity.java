package com.b11a.android.autosaver.ui.driving;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.b11a.android.autosaver.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Math.sqrt;

public class DrivingActivity extends AppCompatActivity implements SensorEventListener {

    private int SHAKE_SKIP_TIME = 500;
    private double SHAKE_THRESHOLD_GRAVITY = 3.5f;

    private double shakeTime = 0L;

    SensorManager sensorManager;
    Sensor sensorAccelerometer;

    double longitude, latitude, speed, averageSpeed, totalSpeed = 0;
    static String staticLongitude, staticLatitude;

    Button btnEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);

        DecimalFormat decimalFormat = new DecimalFormat("#.######");

        btnEnd = findViewById(R.id.btn_end);

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DrivingActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        } else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            speed = location.getSpeed();

            final LocationListener gpsLocationListener = new LocationListener() {
                public void onLocationChanged(Location location) {

                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    staticLongitude = decimalFormat.format(longitude);
                    staticLatitude = decimalFormat.format(latitude);

                    (new Thread(new Runnable() {

                        @Override
                        public void run() {
                            while (!Thread.interrupted())
                                try {
                                    Thread.sleep(10000);
                                    runOnUiThread(new Runnable() // start actions in UI thread
                                    {

                                        @Override
                                        public void run() {
                                            speed = location.getSpeed();
                                            totalSpeed += speed;
                                            averageSpeed = totalSpeed / 10;
                                            Log.e("average", String.valueOf(averageSpeed));
                                        }
                                    });
                                } catch (InterruptedException e) {
                                }
                        }
                    })).start();


                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double axisX = sensorEvent.values[0];
            double axisY = sensorEvent.values[1];
            double axisZ = sensorEvent.values[2];

            double gravityX = axisX / SensorManager.GRAVITY_EARTH;
            double gravityY = axisY / SensorManager.GRAVITY_EARTH;
            double gravityZ = axisZ / SensorManager.GRAVITY_EARTH;

            double distanceSquare = gravityX * gravityX + gravityY * gravityY + gravityZ * gravityZ;
            double distance = sqrt(distanceSquare);

            if (distance > SHAKE_THRESHOLD_GRAVITY) {
                double currentTime = System.currentTimeMillis();
                if (shakeTime + SHAKE_SKIP_TIME > currentTime) {
                    return;
                }
                shakeTime = currentTime;
                Toast.makeText(this, "충돌이 감지되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AccidentDetectedActivity.class);

                intent.putExtra("currentTime", getCurrentTime());
                intent.putExtra("latitude", staticLatitude);
                intent.putExtra("longitude", staticLongitude);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public String getCurrentTime() {
        long time = System.currentTimeMillis();

        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String string = dayTime.format(new Date(time));

        return string;
    }

}