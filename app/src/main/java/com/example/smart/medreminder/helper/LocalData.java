package com.example.smart.medreminder.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by smart on 10/04/2018, 3:07 PM.
 */

public class LocalData {

    private static final String APP_SHARED_PREFS = "RemindMePref";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;

    private static final String reminderStatus = "reminderStatus";
    private static final String hour = "hour";
    private static final String minute = "minute";

    public LocalData(Context context) {
        this.sharedPreferences = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefEditor = sharedPreferences.edit();
    }

    // Setting Page set reminder

    public boolean getReminderStatus() {

        return sharedPreferences.getBoolean(reminderStatus, false);
    }

    public void setReminderStatus(boolean status) {

        prefEditor.putBoolean(reminderStatus, status);
        prefEditor.commit();
    }

    // Setting page reminder time (Hour)

    public int get_hour() {

        return sharedPreferences.getInt(hour, 20);
    }

    public void set_hour(int h) {

        prefEditor.putInt(hour, h);
        prefEditor.commit();
    }

    // Setting page reminder time (Minutes)
    public int get_minute() {
        return sharedPreferences.getInt(minute, 0);
    }

    public void set_minute(int m) {

        prefEditor.putInt(minute, m);
        prefEditor.commit();
    }
}
