package com.joe.android.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.joe.android.activities.R;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by youssefabdalla on 1/12/16.
 */
public class Utils {
    public static final String USAGE_COUNTER_KEY = Utils.class.getName() + "USAGE_COUNTER";


    public static Set<String> getStringArrayAsSet(String[] stringArray) {
        Set<String> stringsSet = new HashSet<>();
        Collections.addAll(stringsSet, stringArray);
        return stringsSet;
    }

    public static int readUsageCounterFromSharedResources(Context context) {
        SharedPreferences countersValuesSharedPreferences = context.getSharedPreferences(context.getString(R.string.saved_values_file_key), Context.MODE_PRIVATE);
        return countersValuesSharedPreferences.getInt(USAGE_COUNTER_KEY, 0);
    }


    public static synchronized void incrementUsageCounter(Context context) {
        SharedPreferences countersValuesSharedPreferences = context.getSharedPreferences(context.getString(R.string.saved_values_file_key), Context.MODE_PRIVATE);
        int usageCounter = countersValuesSharedPreferences.getInt(USAGE_COUNTER_KEY, 0);
        usageCounter++;
        Log.d(Utils.class.getName(), "usage counter increamented to: " + usageCounter);
        SharedPreferences.Editor editor = countersValuesSharedPreferences.edit();
        editor.putInt(USAGE_COUNTER_KEY, usageCounter);
        editor.apply();
    }

    public static String convertTimeInMillisToTimeString(long timeInMillis) {


        long hours = TimeUnit.MILLISECONDS.toHours(timeInMillis);
        Log.d(Utils.class.getName(), "hours= " + hours);

        timeInMillis = timeInMillis - (hours * 60 * 60 * 1000);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis);
        Log.d(Utils.class.getName(), "minutes= " + minutes);

        timeInMillis = timeInMillis - (minutes * 60 * 1000);

        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis);
        Log.d(Utils.class.getName(), "seconds= " + seconds);

        return new String(hours + ":" + minutes + ":" + seconds);
    }

    public static long convertCounterReadingToMilliseconds(String counterReading) {

        String[] readingParts = counterReading.split(":");
        long readingInMills = 0;
        if (readingParts.length == 1) {
            readingInMills = Long.parseLong(readingParts[0]) * 1000;
        } else if (readingParts.length == 2) {
            long min = Long.parseLong(readingParts[0]);
            long sec = Long.parseLong(readingParts[1]);

            readingInMills = (sec * 1000) + (min * 60 * 1000);

        } else if (readingParts.length == 3) {
            long hrs = Long.parseLong(readingParts[0]);
            long min = Long.parseLong(readingParts[1]);
            long sec = Long.parseLong(readingParts[2]);
            readingInMills = (sec * 1000) + (min * 60 * 1000) + (hrs * 60 * 60 * 1000);
        }
        Log.d(Utils.class.getName(), "Chronometer reading: " + counterReading + " = " + readingInMills + " milliseconds.");
        return readingInMills;
    }

}
