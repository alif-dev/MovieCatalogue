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
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.alif.moviecatalogue.BuildConfig;
import com.alif.moviecatalogue.R;
import com.alif.moviecatalogue.repository.model.MovieResponse;
import com.alif.moviecatalogue.repository.model.MovieResult;
import com.alif.moviecatalogue.repository.remotedatasource.retrofit.ApiClient;
import com.alif.moviecatalogue.repository.remotedatasource.retrofit.ApiInterface;
import com.alif.moviecatalogue.view.MovieDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("FieldCanBeLocal")
public class ReleaseReminderAlarmReceiver extends BroadcastReceiver {
    public static final String EXTRA_ID = "releaseReminderAlarmId";
    private final int ID_RELEASE_REMINDER = 990;

    private int idNotif = 0;
    private int maxNotif = 3;
    private final static String GROUP_KEY_MOVIE_CATALOGUE = "group_key_movie_catalogue";


    public ReleaseReminderAlarmReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int reminderId = intent.getIntExtra(EXTRA_ID, 0);
        if (reminderId == ID_RELEASE_REMINDER) {
            getTodayReleasedMoviesAndShowNotification(context);
        }
    }

    public void getTodayReleasedMoviesAndShowNotification(final Context context) {
        String API_KEY = BuildConfig.TMDB_API_KEY;
        final ArrayList<MovieResult> todayReleasedMovies = new ArrayList<>();

        // get today's date
        Date now = new Date(); // new Date() will get today's date and time
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // only get the date and use this format
        final String todayDate = simpleDateFormat.format(now);

        // get today's released movies using retrofit
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getMoviesReleasedToday(API_KEY, todayDate, todayDate);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // contain all todayReleasedMovies data in an ArrayList
                        todayReleasedMovies.addAll(response.body().getMovieResults());
                        // show 3 of today's released movies by calling notification 3 times
                        idNotif = 0;
                        for (int i = 0; i < maxNotif; i++) {
                            Log.d("todayreleasedmovies", todayReleasedMovies.get(i).getOriginalTitle());
                            showReleaseReminderNotification(context, todayReleasedMovies);
                            idNotif++;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable error) {
                Log.d("onReleaseMoviesFailure", error.getMessage());
            }
        });
    }

    public long setTime(int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTimeInMillis();
    }

    public void setReleaseReminderAlarm(Context context) {
        // set the alarm to start at approximately 8 a.m.
        long alarmTime = setTime(8, 0, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminderAlarmReceiver.class);
        intent.putExtra(EXTRA_ID, ID_RELEASE_REMINDER);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_REMINDER, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelReleaseReminderAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminderAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_REMINDER, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
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
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.clapperboard);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder;

        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "AlarmManager channel2";
        Intent intent = new Intent(context, MovieDetailActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
}
