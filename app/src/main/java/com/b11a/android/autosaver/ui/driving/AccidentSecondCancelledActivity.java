package com.b11a.android.autosaver.ui.driving;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.b11a.android.autosaver.R;

public class AccidentSecondCancelledActivity extends AppCompatActivity {

    private Context context = this;
    private TextView tvTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_cancelled_second);

        tvTimer = findViewById(R.id.tv_timer_cancelled_second);

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