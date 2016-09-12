package com.joe.android.helpers;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.joe.android.activities.R;
import com.joe.android.beans.SingleCounterBean;

/**
 * Created by youssefabdalla on 12/24/15.
 */
public class ChronometerHandler implements View.OnClickListener {

    Button pauseResumeButton;
    Button startStopButton;

    private Chronometer chronometer;
    private Activity activity;
    private SingleCounterBean counterBean;

    private long timeElapsedWhileCounterPaused;
    private long timeStartClicked;
    private long timePauseClicked;

    private ChronometerHandler(Chronometer chronometer) {
        this.chronometer = chronometer;

        if (chronometer.getContext() instanceof Activity) {
            activity = (Activity) chronometer.getContext();
            pauseResumeButton = (Button) activity.findViewById(R.id.activity_counter_pause_resume);
            startStopButton = (Button) activity.findViewById(R.id.activity_counter_start_stop);
        } else {
            throw new NullPointerException("The Chronometer is not attached to an activity. " +
                    "Use the constructor:" +
                    " ChronometerHandler(Chronometer chronometer, Button pauseResumeButton, Button startStopButton)");
        }
    }

    public ChronometerHandler(Chronometer chronometer, SingleCounterBean counterBean) {
        this(chronometer);
        this.counterBean = counterBean;
        //TODO I am here
        chronometer.setBase(SystemClock.elapsedRealtime() - counterBean.getLastKnownCount());
        Log.d(this.getClass().getName(), "Label: " + counterBean.getCounterLabel() + " * last count: " + counterBean.getLastKnownCount());
        Log.d(this.getClass().getName(), "isCounterRunning " + counterBean.isCounterRunning());

        if (counterBean.isCounterRunning()) {
            this.startChronometer();
            startStopButton.setText(chronometer.getContext().getString(R.string.chronometer_stop_button_text));
            pauseResumeButton.setText(chronometer.getContext().getString(R.string.chronometer_pause_button_text));
            pauseResumeButton.setEnabled(true);
        }

    }

    public void startChronometer() {
        timeStartClicked = System.currentTimeMillis();
        if (timePauseClicked == 0) {
            timeElapsedWhileCounterPaused = 0;
        } else {
            timeElapsedWhileCounterPaused = timeStartClicked - timePauseClicked;
        }
        chronometer.setBase(chronometer.getBase() + timeElapsedWhileCounterPaused);
        chronometer.start();
        counterBean.setIsCounterRunning(true);
        Log.d(this.getClass().getName(), "chronometer started");

    }

    public CharSequence pauseChronometer() {
        chronometer.stop();
        timePauseClicked = System.currentTimeMillis();

        counterBean.setIsCounterRunning(false);

        Log.d(this.getClass().getName(), "chronometer paused");
        return chronometer.getText();
    }

    public CharSequence stopChronometer() {
        pauseChronometer();
        chronometer.setBase(SystemClock.elapsedRealtime());
        Log.d(this.getClass().getName(), "chronometer base reset");
        return chronometer.getText();
    }

    @Override

    public void onClick(View v) {
        final int id = v.getId();
        final int startStopId = startStopButton.getId();
        final int pauseResumeId = pauseResumeButton.getId();
        if (v instanceof Button) {
            Button chronometerButton = (Button) v;
            String currentText = chronometerButton.getText().toString();
            if (id == startStopId) {
                if (currentText.equals(activity.getString(R.string.chronometer_start_button_text))) {
                    // if the button text is start switch it to stop after clicked
                    chronometerButton.setText(R.string.chronometer_stop_button_text);
                    // start the chronometer
                    this.startChronometer();

                    // Enable the pause button
                    pauseResumeButton.setEnabled(true);
                    // make sure the text on the pause button is 'PAUSE'
                    pauseResumeButton.setText(activity.getText(R.string.chronometer_pause_button_text));

                } else {
                    chronometerButton.setText(R.string.chronometer_start_button_text);
                    this.stopChronometer();

                    // disable the pause resume button
                    pauseResumeButton.setEnabled(false);
                    // make sure the text on the pause button is 'PAUSE'
                    pauseResumeButton.setText(activity.getText(R.string.chronometer_pause_button_text));
                }
            }
            if (id == pauseResumeId) {
                if (currentText.equals(activity.getString(R.string.chronometer_pause_button_text))) {
                    chronometerButton.setText(R.string.chronometer_resume_button_text);
                    this.pauseChronometer();

                } else {
                    chronometerButton.setText(R.string.chronometer_pause_button_text);
                    this.startChronometer();
                }
            }


        }
    }


}

