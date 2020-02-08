package com.alif.moviecatalogue.view.utility;

import android.database.Cursor;

import com.alif.moviecatalogue.repository.model.room.entity.Favorite;

import java.util.ArrayList;

public class CursorConverter {

    public static ArrayList<Favorite> convertCursorToArrayList(Cursor favoriteMoviesCursor) {
        ArrayList<Favorite> favoriteMovieList = new ArrayList<>();

        while (favoriteMoviesCursor.moveToNext()) {

            int id = favoriteMoviesCursor.getInt(favoriteMoviesCursor.getColumnIndexOrThrow("id"));
            String title = favoriteMoviesCursor.getString(favoriteMoviesCursor.getColumnIndexOrThrow("title"));
            String posterPath = favoriteMoviesCursor.getString(favoriteMoviesCursor.getColumnIndexOrThrow("poster_path"));
            String releaseDate = favoriteMoviesCursor.getString(favoriteMoviesCursor.getColumnIndexOrThrow("release_date"));
            float rating = favoriteMoviesCursor.getFloat(favoriteMoviesCursor.getColumnIndexOrThrow("rating"));
            String overview = favoriteMoviesCursor.getString(favoriteMoviesCursor.getColumnIndexOrThrow("overview"));
            String genre = favoriteMoviesCursor.getString(favoriteMoviesCursor.getColumnIndexOrThrow("genre"));
            String category = favoriteMoviesCursor.getString(favoriteMoviesCursor.getColumnIndexOrThrow("category"));
            favoriteMovieList.add(new Favorite(id, title, posterPath, releaseDate, rating, overview, genre, category));
        }

        return favoriteMovieList;
    }

    public static Favorite convertCursorToFavoriteObject(Cursor favoriteMoviesCursor) {
        favoriteMoviesCursor.moveToFirst();
        int id = favoriteMoviesCursor.getInt(favoriteMoviesCursor.getColumnIndexOrThrow("id"));
        String title = favoriteMoviesCursor.getString(favoriteMoviesCursor.getColumnIndexOrThrow("title"));
        String posterPath = favoriteMoviesCursor.getString(favoriteMoviesCursor.getColumnIndexOrThrow("poster_path"));
        String releaseDate = favoriteMoviesCursor.getString(favoriteMoviesCursor.getColumnIndexOrThrow("release_date"));
        float rating = favoriteMoviesCursor.getFloat(favoriteMoviesCursor.getColumnIndexOrThrow("rating"));
        String overview = favoriteMoviesCursor.getString(favoriteMoviesCursor.getColumnIndexOrThrow("overview"));
        String genre = favoriteMoviesCursor.getString(favoriteMoviesCursor.getColumnIndexOrThrow("genre"));
        String category = favoriteMoviesCursor.getString(favoriteMoviesCursor.getColumnIndexOrThrow("category"));

        return new Favorite(id, title, posterPath, releaseDate, rating, overview, genre, category);
    }
}
