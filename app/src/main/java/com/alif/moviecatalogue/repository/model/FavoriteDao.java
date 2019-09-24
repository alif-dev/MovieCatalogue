package com.alif.moviecatalogue.repository.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

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
}
