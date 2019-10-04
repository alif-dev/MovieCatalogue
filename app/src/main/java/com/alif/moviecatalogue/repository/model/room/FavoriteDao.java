package com.alif.moviecatalogue.repository.model.room;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alif.moviecatalogue.repository.model.room.entity.Favorite;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FavoriteDao {
    @Insert
    void insert(Favorite favorite);

    @Delete
    void delete(Favorite favorite);

    @Query("SELECT COUNT(*) FROM favorite WHERE id = :id")
    int count(int id);

    @Query("SELECT * FROM favorite WHERE category = :category ORDER BY id ASC")
    LiveData<List<Favorite>> getFavoriteMovies(String category);

    @Query("SELECT * FROM favorite WHERE category = :category ORDER BY id ASC")
    LiveData<List<Favorite>> getFavoriteTVShow(String category);

    @Query("SELECT * FROM favorite WHERE category = 'movie' ORDER BY id ASC")
    Cursor selectFavoriteMovies();

    @Query("SELECT * FROM favorite WHERE id = :id")
    Cursor selectById(long id);
}
