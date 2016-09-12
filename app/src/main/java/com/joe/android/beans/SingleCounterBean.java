package com.joe.android.beans;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.joe.android.activities.R;
import com.joe.android.helpers.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Bean class for a single counter
 * Created by youssef abdalla on 12/30/15.
 */
public class SingleCounterBean {
    private String counterLabel;
    private long lastKnownCount;
    private String description;
    private long chronometerBase;
    private boolean isCounterRunning;
    private Context context;

    /**
     * Create a counter bean with default values
     */
    public SingleCounterBean(Context context) {
        Log.d(this.getClass().getName(), "generating counter with default properties");
        counterLabel = new String(context.getString(R.string.default_counter_label));
        description = "Test";
        chronometerBase = SystemClock.elapsedRealtime();
        this.context = context;
    }

    /**
     * This to create a {@link SingleCounterBean} based on label. this has two cases:
     * <ol>
     * <li> The counter is found in the {@link SharedPreferences} in this case the counter was already created some when and thus the properties will be loaded from there. </li>
     * <li> The counter is not found in SharedPreferences and thus default values will be loaded.</li>
     * </ol>
     */
    public SingleCounterBean(Context context, String label) {
        this(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.saved_values_file_key), Context.MODE_PRIVATE);
        boolean counterAlreadyCreatedInSharedResources = sharedPreferences.getStringSet(context.getString(R.string.counter_labels_string_array_key), Utils.
                getStringArrayAsSet(CounterArrayListBean.getDefaultCountersLabelsArray(context))).contains(label);

        if (counterAlreadyCreatedInSharedResources) {
            Log.d(this.getClass().getName(), "Counter already in resources file. retrieving properties");
            this.lastKnownCount = sharedPreferences.getLong(context.getString(R.string.counter_last_known_count_field_key) + label, lastKnownCount);
            this.description = sharedPreferences.getString(context.getString(R.string.counter_description_field_key) + label, "");
            this.chronometerBase = sharedPreferences.getLong(context.getString(R.string.chronometer_base_field_key) + label, chronometerBase);
            this.isCounterRunning = sharedPreferences.getBoolean(context.getString(R.string.counter_is_counter_running_field_key) + label, false);
        }

        this.counterLabel = label;
        CounterArrayListBean<SingleCounterBean> allCounterBeans = CounterArrayListBean.create(context);
        int index = allCounterBeans.contains(this);
        if (index != -1) {
            SingleCounterBean counter = allCounterBeans.get(index);
            this.lastKnownCount = counter.lastKnownCount;
            this.description = counter.description;
            this.chronometerBase = counter.chronometerBase;
            this.isCounterRunning = counter.isCounterRunning;

        }
        allCounterBeans.add(this);

    }

    public String getCounterLabel() {
        return counterLabel;
    }

    public void setCounterLabel(String counterLabel) {
        this.counterLabel = counterLabel;
    }

    public void putCounterLabelInSharedResources() {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.saved_values_file_key), Context.MODE_PRIVATE);
        Set<String> countersLabels = sharedPreferences.getStringSet(context.getString(R.string.counter_labels_string_array_key),
                new HashSet<String>(Arrays.asList(CounterArrayListBean.getDefaultCountersLabelsArray(this.context))));

        countersLabels.add(this.getCounterLabel());
        // put the new label
        CounterArrayListBean.putCountersLabelsInSharedResources(countersLabels, context);
    }

    public long getLastKnownCount() {
        return lastKnownCount;
    }

    public void setLastKnownCount(long lastKnownCount) {
        this.lastKnownCount = lastKnownCount;

    }

    public void putCounterLastKnownCountInSharedResources() {
        Log.d(this.getClass().getName(), "writing last known count to shared resources.");
        SharedPreferences.Editor editor = getSharedResourcesEditor();
        editor.putLong(context.getString(R.string.counter_last_known_count_field_key) + counterLabel, this.lastKnownCount);
        editor.apply();
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void putDescriptionInSharedResources() {
        Log.d(this.getClass().getName(), "writing Description to shared resources.");
        getSharedResourcesEditor().putString(context.getString(R.string.counter_description_field_key) + counterLabel, description).apply();

    }

    public long getChronometerBase() {
        return chronometerBase;
    }

    public void setChronometerBase(long chronometerBase) {
        this.chronometerBase = chronometerBase;
    }

    public void putChronometerBaseInSharedResources() {
        Log.d(this.getClass().getName(), "writing Chronometer Base to shared resources.");
        getSharedResourcesEditor().putLong(context.getString(R.string.chronometer_base_field_key) + counterLabel, chronometerBase).apply();

    }

    public boolean isCounterRunning() {
        return isCounterRunning;
    }

    public void setIsCounterRunning(boolean isCounterRunning) {
        this.isCounterRunning = isCounterRunning;
    }

    public void putIsCounterRunningInSharedResources() {
        Log.d(this.getClass().getName(), "writing is counter running  to shared resources.");
        getSharedResourcesEditor().putBoolean(context.getString(R.string.counter_is_counter_running_field_key) + counterLabel, isCounterRunning).apply();
    }

    /**
     * writes the counter data to shared resources
     */
    public void writeBeanToSharedResources() {
        this.putCounterLabelInSharedResources();
        this.putCounterLastKnownCountInSharedResources();
        this.putDescriptionInSharedResources();
        this.putIsCounterRunningInSharedResources();
        this.putChronometerBaseInSharedResources();

    }

    private SharedPreferences.Editor getSharedResourcesEditor() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.saved_values_file_key), Context.MODE_PRIVATE);
        return sharedPreferences.edit();
    }
}
