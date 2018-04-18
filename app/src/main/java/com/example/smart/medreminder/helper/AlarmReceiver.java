package com.example.smart.medreminder.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import com.example.smart.medreminder.activity.MainActivity;

/**
 * Created by smart on 10/04/2018, 3:03 PM.
 */

public class AlarmReceiver extends BroadcastReceiver {

    /**
     * We need to associate alarm service with the broadcast receiver.
     * Alarm service will invoke this receiver on scheduled time.
     * Here we can trigger the local notification with the respective
     * details(title, content, icon). The below code will help you to
     * handle the broadcast.
     */

    private String TAG = "AlarmReceiver";
    EditText editText;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
                NotificationScheduler.setReminder(context, AlarmReceiver.class,
                        localData.get_hour(), localData.get_minute());

                return;
            }
        }
//         Trigger the notification
        NotificationScheduler.showNotification(context, MainActivity.class,
                "Hey! is time for your medication", "1 Dosage of Artesunate");

    }



}

