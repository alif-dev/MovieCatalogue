package com.alif.moviecatalogue.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alif.moviecatalogue.repository.model.room.FavoriteDao;
import com.alif.moviecatalogue.repository.model.room.FavoriteRoomDatabase;

public class FavoriteMoviesProvider extends ContentProvider {
    public static final String AUTHORITY = "com.alif.moviecatalogue";
    public static final String TABLE_NAME = "favorite";
    public static final int FAVORITE = 1;
    public static final int FAVORITE_ID = 2;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    // uri example
    // public static final Uri URI_FAVORITE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    static {
        URI_MATCHER.addURI(AUTHORITY, TABLE_NAME, FAVORITE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_NAME + "/#", FAVORITE_ID);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = URI_MATCHER.match(uri);
        if (code == FAVORITE || code == FAVORITE_ID) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            FavoriteDao favoriteDao = FavoriteRoomDatabase.getDatabase(context).favoriteDao();
            final Cursor cursor;
            if (code == FAVORITE) {
                cursor = favoriteDao.selectFavoriteMovies();
            } else {
                cursor = favoriteDao.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        switch (URI_MATCHER.match(uri)) {
            case FAVORITE:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case FAVORITE_ID:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = FavoriteRoomDatabase.getDatabase(context).favoriteDao().deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
