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
import com.alif.moviecatalogue.repository.model.MovieResult;
import com.alif.moviecatalogue.view.MainActivity;
import com.alif.moviecatalogue.view.MovieDetailActivity;
import com.alif.moviecatalogue.view.ReminderPreferencesFragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("FieldCanBeLocal")
public class AlarmReceiver extends BroadcastReceiver {
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_ID = "alarmId";
    public static final String EXTRA_RELEASED_MOVIES = "released_movies";

    private final int ID_DAILY_REMINDER = 991;
    private final int ID_RELEASE_REMINDER = 992;

    private int idNotif = 0;
    private int maxNotif = 3;
    private final static String GROUP_KEY_MOVIE_CATALOGUE = "group_key_movie_catalogue";

    public AlarmReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        ArrayList<MovieResult> releasedMovies = intent.getParcelableArrayListExtra(EXTRA_RELEASED_MOVIES);
        int reminderId = intent.getIntExtra(EXTRA_ID, 0);
        if (reminderId != 0) {
            if (reminderId == ID_RELEASE_REMINDER) {
                // show 3 of today's released movies by calling notification 3 times
                for (int i = 0; i < maxNotif; i++) {
                    showReleaseReminderNotification(context, releasedMovies);
                    idNotif++;
                }
                // reset idNotif to 0 after showing notification
                idNotif = 0;
            } else if (reminderId == ID_DAILY_REMINDER) {
                String title = context.getResources().getString(R.string.title_daily_reminder);
                showDailyReminderNotification(context, title, message, reminderId);
            }
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

    public void setReleaseReminderAlarm(Context context, ArrayList<MovieResult> moviesReleasedToday) {
        // set the alarm to start at approximately 8 a.m.
        long alarmTime = setTime(11, 1, 0);
        long now = Calendar.getInstance().getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_ID, ID_RELEASE_REMINDER);
        intent.putParcelableArrayListExtra(EXTRA_RELEASED_MOVIES, moviesReleasedToday);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_REMINDER, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmTime > now) {
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }

    public void cancelReleaseReminderAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_REMINDER, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            // reset releaseReminder idNotif to 0
            idNotif = 0;
        }
    }

    private void showReleaseReminderNotification(Context context, ArrayList<MovieResult> moviesReleasedToday) {
        if (moviesReleasedToday != null) {
            if (!moviesReleasedToday.isEmpty()) {
                sendNotif(context, moviesReleasedToday);
            }
        }
    }

    private void sendNotif(Context context, ArrayList<MovieResult> moviesReleasedToday) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // default notification bitmap
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.clapperboard);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder;

        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "AlarmManager channel2";
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_DATA_KEY, moviesReleasedToday.get(idNotif));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, idNotif, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(moviesReleasedToday.get(idNotif).getOriginalTitle())
                .setContentText(moviesReleasedToday.get(idNotif).getOriginalTitle() + context.getString(R.string.notification_movie_released_message))
                .setSmallIcon(R.drawable.ic_movie_black_24dp)
                .setLargeIcon(largeIcon)
                .setGroup(GROUP_KEY_MOVIE_CATALOGUE)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = mBuilder.build();
        if (mNotificationManager != null) {
            mNotificationManager.notify(idNotif, notification);
        }
    }

    public void setDailyReminderAlarm(Context context, String message) {
        // set the alarm to start at approximately 7 a.m.
        long alarmTime = setTime(7, 0, 0);
        long now = Calendar.getInstance().getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_ID, ID_DAILY_REMINDER);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmTime > now) {
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }

    public void cancelDailyReminderAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
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
