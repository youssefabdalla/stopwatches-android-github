package com.joe.android.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.joe.android.activities.R;
import com.joe.android.beans.CounterArrayListBean;
import com.joe.android.beans.SingleCounterBean;

/**
 * Created by youssefabdalla on 2/29/16.
 */
public class AddNewCounterDialog {
    private String newCounterLabel;

    public AddNewCounterDialog(final Context context, final CountersDataAdapter countersDataAdapter) {
        newCounterLabel = context.getString(R.string.default_counter_label);
        AlertDialog.Builder newCounterDialog = new AlertDialog.Builder(context);
        newCounterDialog.setTitle(context.getString(R.string.add_new_counter_dialog_title));

        final EditText counterLabelEditText = new EditText(context);
        newCounterDialog.setView(counterLabelEditText);

        newCounterDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                newCounterLabel = counterLabelEditText.getText().toString();
                Log.d("this app", "new element of label: " + newCounterLabel + " will be added");
                CounterArrayListBean<SingleCounterBean> countersData = countersDataAdapter.getCountersData();
                countersData.add(new SingleCounterBean(context, newCounterLabel));
                countersDataAdapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(context, "new counter added", Toast.LENGTH_SHORT);
            }
        });
        newCounterDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast toast = Toast.makeText(context, "counters set unchanged", Toast.LENGTH_SHORT);
            }
        });
        newCounterDialog.create();
        newCounterDialog.show();
    }

    public String getNewCounterLabel() {
        return newCounterLabel;
    }

}
