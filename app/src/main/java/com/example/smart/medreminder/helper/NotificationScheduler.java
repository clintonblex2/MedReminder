package com.example.smart.medreminder.helper;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.smart.medreminder.R;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by smart on 10/04/2018, 3:10 PM.
 */


    public class NotificationScheduler {

        public static final int DAILY_REMINDER_REQUEST_CODE=100;
        public static final String TAG = "NOTIFICATIONSCHEDULER";

        public NotificationScheduler() {

        }

        public static void showNotification(Context context, Class<?> cls, String title, String content) {

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Intent notificationIntent = new Intent(context, cls);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(cls);
            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                    DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            Notification notification = builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent).build();

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification);
        }

        public static void setReminder(Context context, Class<?> cls, int hour, int min) {

            Calendar calendar = Calendar.getInstance();

            Calendar setCalendar = Calendar.getInstance();
            setCalendar.set(Calendar.HOUR_OF_DAY, hour);
            setCalendar.set(Calendar.MINUTE, min);
            setCalendar.set(Calendar.SECOND, 0);
//        setCalendar.set(Calendar.YEAR, year);
//        setCalendar.set(Calendar.MONTH, month);
//        setCalendar.set(Calendar.DAY_OF_MONTH, dom);

            // Cancel already scheduled reminders
            cancelReminder(context, cls);

            if (setCalendar.before(calendar))
                setCalendar.add(Calendar.DATE, 1);

            // Enable a receiver
            ComponentName receiver = new ComponentName(context, cls);
            PackageManager packageManager = context.getPackageManager();

            packageManager.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

            Intent intent = new Intent(context, cls);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    DAILY_REMINDER_REQUEST_CODE, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        public static void cancelReminder(Context context, Class<?> cls) {

            // Disable a receiver
            ComponentName receiver = new ComponentName(context, cls);
            PackageManager packageManager = context.getPackageManager();

            packageManager.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);

            Intent intent = new Intent(context, cls);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    DAILY_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }


}
