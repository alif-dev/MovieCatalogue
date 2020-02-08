package com.alif.moviecatalogue.view.utility;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.alif.moviecatalogue.R;
import com.alif.moviecatalogue.view.MainActivity;

import java.util.Calendar;

@SuppressWarnings("FieldCanBeLocal")
public class DailyReminderAlarmReceiver extends BroadcastReceiver {
    public static final String EXTRA_ID = "dailyReminderAlarmId";
    private final int ID_DAILY_REMINDER = 991;

    public DailyReminderAlarmReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int reminderId = intent.getIntExtra(EXTRA_ID, 0);

        if (reminderId == ID_DAILY_REMINDER) {
            String title = context.getResources().getString(R.string.title_daily_reminder);
            String message = context.getResources().getString(R.string.message_daily_reminder);
            showDailyReminderNotification(context, title, message, reminderId);
        }
    }

    public long setTime(int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTimeInMillis();
    }

    public void setDailyReminderAlarm(Context context) {
        // set the alarm to start at approximately 7 a.m.
        long alarmTime = setTime(7, 0, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderAlarmReceiver.class);
        intent.putExtra(EXTRA_ID, ID_DAILY_REMINDER);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelDailyReminderAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private void showDailyReminderNotification(Context context, String title, String message, int notifId) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ID_DAILY_REMINDER, intent, 0);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.clapperboard);

        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel 1";
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_black_24dp)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }
}
