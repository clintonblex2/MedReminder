package com.example.smart.medreminder.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.smart.medreminder.activity.MainActivity;
import com.example.smart.medreminder.helper.LocalData;
import com.example.smart.medreminder.helper.NotificationScheduler;

public class AlarmReceiver extends BroadcastReceiver {

    public final String PREF_NAME = "mypref";
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;

    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        preferences = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        String title = preferences.getString("TITLE", "");
        String description = preferences.getString("DESCRIPTION", "");

        Log.d("title:... ", title);
        Log.d("content:... ", description);

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
                NotificationScheduler.setReminder(context, AlarmReceiver.class, localData.get_hour(), localData.get_minute());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        //Trigger the notification
        NotificationScheduler.showNotification(context, MainActivity.class,
                title, description);

    }
}