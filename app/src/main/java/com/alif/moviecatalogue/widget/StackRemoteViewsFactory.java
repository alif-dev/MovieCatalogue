package com.alif.moviecatalogue.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.alif.moviecatalogue.R;
import com.alif.moviecatalogue.contentprovider.FavoriteMoviesProvider;
import com.alif.moviecatalogue.repository.model.room.entity.Favorite;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.alif.moviecatalogue.view.utility.CursorConverter.convertCursorToArrayList;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private final Context mContext;

    private static final String AUTHORITY = FavoriteMoviesProvider.AUTHORITY;
    private static final String TABLE_NAME = FavoriteMoviesProvider.TABLE_NAME;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    private ArrayList<Favorite> favoriteMovieList = new ArrayList<>();
    private Cursor cursor;

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();
        // query the database through content provider
        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);

        mWidgetItems.clear();
        favoriteMovieList.clear();
        if (cursor != null) {
            Log.d("cursorcount", String.valueOf(cursor.getCount()));
            if (cursor.getCount() != 0) {
                favoriteMovieList = convertCursorToArrayList(cursor);

                for (int i = 0; i < favoriteMovieList.size(); i++) {
                    Log.d("updatewidget", favoriteMovieList.get(i).getTitle());
                    try {
                        Bitmap bitmap = Glide.with(mContext)
                                .asBitmap()
                                .load("http://image.tmdb.org/t/p/w342" + favoriteMovieList.get(i).getPosterPath())
                                .submit(250, 250)
                                .get();

                        mWidgetItems.add(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        Bundle extras = new Bundle();
        Intent fillInIntent = new Intent();
        if (mWidgetItems.isEmpty()) {
            rv.setEmptyView(R.id.stack_view, R.id.empty_view);
        } else {
            rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));
            extras.putParcelable(FavoriteMoviesWidget.EXTRA_ITEM, favoriteMovieList.get(position));
            fillInIntent.putExtra(FavoriteMoviesWidget.FAVORITE_MOVIE_DATA, extras);
            rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
