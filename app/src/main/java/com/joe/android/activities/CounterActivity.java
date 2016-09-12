package com.joe.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.joe.android.beans.CounterArrayListBean;
import com.joe.android.beans.SingleCounterBean;
import com.joe.android.helpers.ChronometerHandler;
import com.joe.android.helpers.Utils;

public class CounterActivity extends AppCompatActivity {


    ChronometerHandler chronometerHandler;
    private TextView counterTitleTextView;
    private Chronometer chronometer;
    private Button startStopButton, pauseResumeButton;
    private SingleCounterBean counterBean;
    private ImageButton deleteCounterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getClass().getName(), "onCreate called");
        this.setContentView(R.layout.activity_counter);

        Intent callerIntent = this.getIntent();

        String counterTitle = callerIntent.getStringExtra("counterLabel");

        counterBean = new SingleCounterBean(this, counterTitle);

        counterTitleTextView = (TextView) this.findViewById(R.id.timer_name);
        counterTitleTextView.setText(counterTitle);

        chronometer = (Chronometer) this.findViewById(R.id.chronometer);
        startStopButton = (Button) this.findViewById(R.id.activity_counter_start_stop);
        pauseResumeButton = (Button) this.findViewById(R.id.activity_counter_pause_resume);
        deleteCounterButton = (ImageButton) this.findViewById(R.id.delete_counter_button);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(this.getClass().getName(), "onSave called");
        long lastBase = chronometer.getBase();
        String startStopButtonText = startStopButton.getText().toString();
        String pauseResumeButtonText = pauseResumeButton.getText().toString();
        boolean isPauseResumeButtonEnabled = pauseResumeButton.isEnabled();

        outState.putLong("chronometer_saved_time", lastBase);
        outState.putString("startStopButtonText", startStopButtonText);
        outState.putString("pauseResumeButtonText", pauseResumeButtonText);
        outState.putBoolean("isPauseResumeButtonEnabled", isPauseResumeButtonEnabled);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(this.getClass().getName(), "onRestore called");
        long lastBase = savedInstanceState.getLong("chronometer_saved_time");


        String startStopButtonText = savedInstanceState.getString("startStopButtonText");
        String pauseResumeButtonText = savedInstanceState.getString("pauseResumeButtonText");
        boolean isPauseResumeButtonEnabled = savedInstanceState.getBoolean("isPauseResumeButtonEnabled");

        startStopButton.setText(startStopButtonText);
        pauseResumeButton.setText(pauseResumeButtonText);
        pauseResumeButton.setEnabled(isPauseResumeButtonEnabled);
    }

    @Override
    protected void onPause() {
        Log.d(this.getClass().getName(), "onPause called");
        String chronometerCount = chronometer.getText().toString();
        long lastKnownCount = Utils.convertCounterReadingToMilliseconds(chronometerCount);
        counterBean.setChronometerBase(chronometer.getBase());
        counterBean.setLastKnownCount(lastKnownCount);
        CounterArrayListBean.create(this).add(counterBean);
        counterBean.writeBeanToSharedResources();
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(this.getClass().getName(), "onResume called");
        chronometerHandler = new ChronometerHandler(chronometer, counterBean);
        startStopButton.setOnClickListener(chronometerHandler);
        pauseResumeButton.setOnClickListener(chronometerHandler);
        deleteCounterButton.setOnClickListener(chronometerHandler);
    }


}