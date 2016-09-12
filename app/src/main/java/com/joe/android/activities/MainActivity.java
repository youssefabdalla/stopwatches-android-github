package com.joe.android.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.joe.android.beans.CounterArrayListBean;
import com.joe.android.beans.SingleCounterBean;
import com.joe.android.helpers.AddNewCounterDialog;
import com.joe.android.helpers.CountersDataAdapter;
import com.joe.android.helpers.Utils;

/**
 * This is the the activity that holds the list of the counters names.
 */
public class MainActivity extends AppCompatActivity {
    RecyclerView countersRecyclerView;
    CounterArrayListBean<SingleCounterBean> countersArrayList;
    CountersDataAdapter countersDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        countersRecyclerView = (RecyclerView) this.findViewById(R.id.counters_recycler_view);
        countersRecyclerView.setHasFixedSize(true);

        Toolbar mainToolBar = (Toolbar) this.findViewById(R.id.main_toolbar);
        this.setSupportActionBar(mainToolBar);

    }

    protected void onResume() {
        super.onResume();
        //create the array list
        countersArrayList = CounterArrayListBean.create(this);
        countersDataAdapter = new CountersDataAdapter(countersArrayList);

        countersRecyclerView.setAdapter(countersDataAdapter);
        RecyclerView.LayoutManager countersLayoutManager = new LinearLayoutManager(this);
        countersRecyclerView.setLayoutManager(countersLayoutManager);
        Utils.incrementUsageCounter(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dos_list_menu_resource, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // adding new counter
        if (id == R.id.action_add_new) {
            AddNewCounterDialog addNewCounterDialog = new AddNewCounterDialog(this, countersDataAdapter);
            String newCounterLabel = addNewCounterDialog.getNewCounterLabel();
            return true;
        }
        if (id == R.id.action_delete_all) {
            AlertDialog.Builder deleteAllAlertBuilder = new AlertDialog.Builder(this);
            deleteAllAlertBuilder = deleteAllAlertBuilder.setMessage(R.string.delete_all_counters_confirmation_dialog_message);
            deleteAllAlertBuilder.setPositiveButton(R.string.delete_all_counters_positive_button_label, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    countersDataAdapter.removeAll();
                    countersDataAdapter.notifyDataSetChanged();
                }
            });
            deleteAllAlertBuilder.setNegativeButton(R.string.delete_all_counters_negative_button_label, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            deleteAllAlertBuilder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater itemContextMenuInflater = getMenuInflater();
        itemContextMenuInflater.inflate(R.menu.dos_list_item_contextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                deleteListViewItem(itemInfo);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // TODO save the colors
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    // TODO
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void deleteListViewItem(AdapterView.AdapterContextMenuInfo itemInfo) {
        Log.d("thisApp", "item should be deleted by now");

    }

}
