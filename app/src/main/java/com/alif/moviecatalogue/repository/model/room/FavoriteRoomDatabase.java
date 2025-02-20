package com.alif.moviecatalogue.repository.model.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.alif.moviecatalogue.repository.model.room.entity.Favorite;

@Database(entities = {Favorite.class}, version = 1, exportSchema = false)
public abstract class FavoriteRoomDatabase extends RoomDatabase {
    public abstract FavoriteDao favoriteDao();

    private static volatile FavoriteRoomDatabase INSTANCE;

    public static FavoriteRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavoriteRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteRoomDatabase.class, "favorite_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
