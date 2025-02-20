package com.alif.moviecatalogue.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.alif.moviecatalogue.R;
import com.alif.moviecatalogue.repository.model.room.entity.Favorite;
import com.alif.moviecatalogue.view.MovieDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteMoviesWidget extends AppWidgetProvider {

    private static final String TOAST_ACTION = "com.alif.moviecatalogue.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.alif.moviecatalogue.EXTRA_ITEM";
    public static final String FAVORITE_MOVIE_DATA = "com.alif.moviecatalogue.FAVORITE_MOVIE_DATA";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_movies_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, FavoriteMoviesWidget.class);
        toastIntent.setAction(FavoriteMoviesWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                Bundle bundleExtra = intent.getBundleExtra(FAVORITE_MOVIE_DATA);
                if (bundleExtra != null) {
                    Favorite favoriteMovie = bundleExtra.getParcelable(EXTRA_ITEM);
                    if (favoriteMovie != null) {
                        Toast.makeText(context, context.getString(R.string.toast_widget_open_favorite_movie) + favoriteMovie.getTitle(), Toast.LENGTH_SHORT).show();
                        Intent detailIntent = new Intent(context, MovieDetailActivity.class);
                        detailIntent.putExtra(MovieDetailActivity.FAVORITE_MOVIE_DATA_KEY, favoriteMovie);
                        detailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(detailIntent);
                    }
                }
            } else if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName favoriteWidget = new ComponentName(context, FavoriteMoviesWidget.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(favoriteWidget);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

