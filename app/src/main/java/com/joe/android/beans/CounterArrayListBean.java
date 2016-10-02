package com.joe.android.beans;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.joe.android.activities.R;
import com.joe.android.helpers.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * represents an ArrayList of counters. *
 * Created by youssefabdalla on 12/30/15.
 */
public class CounterArrayListBean<T extends SingleCounterBean> extends ArrayList<SingleCounterBean> {


    private static CounterArrayListBean instance;
    private static SingleCounterBean[] singleCounterArray;


    private CounterArrayListBean() {
        super();
    }

    private CounterArrayListBean(SingleCounterBean[] singleCounterArray) {
        super(Arrays.asList(singleCounterArray));
        this.singleCounterArray = singleCounterArray;
    }


    public static synchronized CounterArrayListBean create(Context context) {
        if (null == instance) {
            String[]labels={""};
            int usageCounter = Utils.readUsageCounterFromSharedResources(context);
            if (usageCounter != 0) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.saved_values_file_key), Context.MODE_PRIVATE);
                Set<String> labelsAsSetFromSharedResorces = sharedPreferences.getStringSet(context.getString(R.string.counter_labels_string_array_key), new HashSet<String>(Arrays.asList(labels)));
                labels = new String[labelsAsSetFromSharedResorces.size()];
                labels = labelsAsSetFromSharedResorces.toArray(labels);
            } else{
                labels = CounterArrayListBean.getDefaultCountersLabelsArray(context);
            }

            instance = new CounterArrayListBean();

            for (String counterLabel : labels) {
                SingleCounterBean singleCounterBean = new SingleCounterBean(context, counterLabel);
                instance.add(singleCounterBean);
                // singleCounterBean.writeBeanToSharedResources();
            }
        }

        // TODO here
        Iterator<SingleCounterBean> iterator = instance.iterator();
        while (iterator.hasNext()) {
            SingleCounterBean counter = iterator.next();
            counter.setChronometerBase(SystemClock.elapsedRealtime() - counter.getLastKnownCount());
        }

        return instance;
    }

    /**
     * Single tone creator that returns a new instance of CounterArrayListBean based on an array of {@link SingleCounterBean}
     *
     * @param singleCounterArray
     * @return new COunterArrayListBean instance in case there is no instanse already created or the instanse once created
     */

    public static synchronized CounterArrayListBean create(SingleCounterBean[] singleCounterArray) {
        if (null == instance) {
            instance = new CounterArrayListBean(singleCounterArray);
            instance.singleCounterArray = singleCounterArray;
        }
        return instance;
    }


    public static SingleCounterBean[] getCountersArray() {
        return singleCounterArray;
    }

    /**
     * This will write the counters labels to the shared prefrences file
     */
    public synchronized static void putCountersLabelsInSharedResources(String[] countersLabelsArray, Context context) {
        Set<String> countersLabelsSet = Utils.getStringArrayAsSet(countersLabelsArray);
        putCountersLabelsInSharedResources(countersLabelsSet, context);
    }

    public static void putCountersLabelsInSharedResources(Set<String> countersLabelsSet, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.saved_values_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.d(CounterArrayListBean.class.getName(), "start writing labels to shared preferences");
        editor.putStringSet(context.getString(R.string.counter_labels_string_array_key), countersLabelsSet);
        editor.apply();
        Log.d(CounterArrayListBean.class.getName(), "End writing labels to shared preferences. shared preferences is updated now with new labels.");
    }


    /**
     * This returns an array of strings represents the labels of the counters as in the values/strings.xml
     * then put these labels to the file of the shared resources.
     * This is particularly useful in case default labels are needed as in the case of first time use.
     *
     * @param context to use. mostly you should pass the calling activity.
     * @return reference to an array of strings containing the default labels of the counters as in values/strings.xml.
     */
    public static String[] getDefaultCountersLabelsArray(Context context) {

        return context.getResources().getStringArray(R.array.default_counters_labels);


    }


    public int contains(SingleCounterBean singleCounterBean) {
        int index = -1;
        String counterLabelToSearchFor = singleCounterBean.getCounterLabel();
        Iterator<SingleCounterBean> iterator = this.iterator();

        while (iterator.hasNext()) {
            index++;
            if (iterator.next().getCounterLabel().equals(counterLabelToSearchFor)) {
                break;
            }
        }
        return index;
    }

    public boolean remove(SingleCounterBean singleCounterBean) {
        String counterLabelToDelete = singleCounterBean.getCounterLabel();
        Iterator<SingleCounterBean> iterator = this.iterator();
        boolean isDeleted = false;
        while (iterator.hasNext()) {
            SingleCounterBean counter = iterator.next();
            if (counter.getCounterLabel().equals(counterLabelToDelete)) {
                super.remove(counter);
                isDeleted = true;
                break;
            }
        }
        return isDeleted;
    }

    public void removeAll(Context context) {
        this.removeRange(0, this.size());

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.saved_values_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.d(CounterArrayListBean.class.getName(), "start removing counters information from shared preferences");
        Set<String> allKeys = sharedPreferences.getAll().keySet();
        Iterator<String> keysIterator = allKeys.iterator();
        while (keysIterator.hasNext()) {
            editor.remove(keysIterator.next());
        }
        editor.apply();
        Log.d(CounterArrayListBean.class.getName(), "End removing counters information from shared preferences. shared preferences is now empty.");
    }

    public boolean add(SingleCounterBean singleCounterBean) {
        int newIndex = contains(singleCounterBean);
        remove(singleCounterBean);
        if (newIndex != -1) {
            super.add(newIndex, singleCounterBean);
            return true;
        }
        return super.add(singleCounterBean);
    }
}
