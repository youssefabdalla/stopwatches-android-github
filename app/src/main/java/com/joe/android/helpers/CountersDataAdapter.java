package com.joe.android.helpers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.joe.android.activities.CounterActivity;
import com.joe.android.activities.R;
import com.joe.android.beans.CounterArrayListBean;
import com.joe.android.beans.SingleCounterBean;

/**
 * This is the utility that is responsible for saving the data. Currently we use shared preferences as the data are simple.
 */
public class CountersDataAdapter extends RecyclerView.Adapter<CountersDataAdapter.ViewHolder> {


    // this holds the counter names and its associated data
    private CounterArrayListBean<SingleCounterBean> countersData;

    public CountersDataAdapter(CounterArrayListBean<SingleCounterBean> countersData) {
        this.countersData = countersData;

    }


    @Override
    public CountersDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View counterView = layoutInflater.inflate(R.layout.item_counter, parent, false);
        CountersDataAdapter.ViewHolder viewHolder = new CountersDataAdapter.ViewHolder(counterView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CountersDataAdapter.ViewHolder viewHolder, int position) {
        final SingleCounterBean counter = getCountersData().get(position);
        viewHolder.itemCounterLabel.setText(counter.getCounterLabel());
        viewHolder.itemCounterTime.setBase(SystemClock.elapsedRealtime() - counter.getLastKnownCount());
        if (counter.isCounterRunning()) viewHolder.itemCounterTime.start();
        if (counter.isCounterRunning()) {
            viewHolder.itemCounterStatus.setVisibility(View.VISIBLE);
            viewHolder.itemCounterStatus.setText(viewHolder.itemCounterTime.getContext().getString(R.string.counter_status_active));
        } else {
            viewHolder.itemCounterStatus.setText(viewHolder.itemCounterTime.getContext().getString(R.string.counter_status_not_active));
        }

        viewHolder.itemCounterLabel.setOnClickListener(new InvokeChronometerListener(viewHolder));
        viewHolder.itemCounterTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                Log.d(this.getClass().getName(), "onChronometerTick called");
                String chronometerText = chronometer.getText().toString();
                long chronometerReading = Utils.convertCounterReadingToMilliseconds(chronometerText);
                counter.setLastKnownCount(chronometerReading);
                getCountersData().add(counter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.getCountersData().size();
    }

    public void add(int position, SingleCounterBean newCounterData) {
        this.getCountersData().add(position, newCounterData);
        this.notifyItemInserted(position);
    }

    public boolean remove(String counterLabel) {
        Log.d(this.getClass().getName(), " remove the first counter with label " + counterLabel);
        return this.getCountersData().remove(counterLabel);
    }

    public boolean removeAll() {
        Log.d(this.getClass().getName(), "remove all counters");
        this.getCountersData().removeAll();
       if (this.getItemCount() == 0) return true;
        return false;
    }

    public CounterArrayListBean<SingleCounterBean> getCountersData() {
        return countersData;
    }

    public void setCountersData(CounterArrayListBean<SingleCounterBean> countersData) {
        this.countersData = countersData;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemCounterLabel;
        public Chronometer itemCounterTime;
        public TextView itemCounterStatus;

        public ViewHolder(View view) {
            super(view);
            itemCounterLabel = (TextView) view.findViewById(R.id.item_counter_label);
            itemCounterTime = (Chronometer) view.findViewById(R.id.item_counter_time);
            itemCounterStatus = (TextView) view.findViewById(R.id.item_counter_status);

        }
    }

    /**
     * this listener will invoke the activity of the chronometer if attached to a view
     */
    private class InvokeChronometerListener implements RecyclerView.OnClickListener {
        ViewHolder viewHolder;

        public InvokeChronometerListener(ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        public void onClick(View counterLabelView) {
            //String text = ((TextView) counterLabelView).getText().toString();
            int position = viewHolder.getAdapterPosition();

            //long chronometerLastKnownCount = Utils.convertCounterReadingToMilliseconds(viewHolder.itemCounterTime.getText().toString());

            SingleCounterBean counterBean = CountersDataAdapter.this.countersData.get(position);
            //counterBean.setLastKnownCount(chronometerLastKnownCount);

            Intent chronometerInvocationIntent = new Intent(counterLabelView.getContext(), CounterActivity.class);

            chronometerInvocationIntent.putExtra("counterLabel", counterBean.getCounterLabel());
            chronometerInvocationIntent.putExtra("isCounterRunning", counterBean.isCounterRunning());
            //chronometerInvocationIntent.putExtra("lastKnownCount", counterBean.getLastKnownCount());

            counterLabelView.getContext().startActivity(chronometerInvocationIntent);
        }

    }
}
