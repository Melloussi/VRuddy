package com.app.vruddy.Models.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.app.vruddy.Views.Activities.HomeActivity;

public class InternetConnectionBroadcastReceiver extends BroadcastReceiver {
    private boolean connected;
    HomeActivity homeActivity = new HomeActivity();
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Connection State Changed!");
        connected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if(connected){
            Toast.makeText(context, "Disconnected", Toast.LENGTH_LONG).show();
            homeActivity.disconnected();
        }else {
            Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();
            homeActivity.reconnected();
        }
    }
}
