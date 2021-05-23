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
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.b11a.android.autosaver.R;

import java.text.DecimalFormat;

import static java.lang.Math.sqrt;

public class AccidentFirstCancelledActivity extends AppCompatActivity {

    private Context context = this;
    private TextView tvTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_cancelled_first);

        tvTimer = findViewById(R.id.tv_timer_cancelled_first);

        startTimer(3000);
    }

    public void startTimer(long timerSeconds) {
        Timer timer = new Timer(timerSeconds, 1000);
        timer.start();
    }

    class Timer extends CountDownTimer {

        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000));

            String second = String.valueOf((getMin % (60 * 1000)) / 1000);

            tvTimer.setText(second);
        }

        @Override
        public void onFinish() {
            tvTimer.setText("0");
            Intent intent = new Intent(context, DrivingActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}