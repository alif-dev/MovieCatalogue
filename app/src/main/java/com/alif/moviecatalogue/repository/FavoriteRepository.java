package com.alif.moviecatalogue.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.alif.moviecatalogue.repository.model.room.FavoriteDao;
import com.alif.moviecatalogue.repository.model.room.FavoriteRoomDatabase;
import com.alif.moviecatalogue.repository.model.room.entity.Favorite;

import java.util.List;

public class FavoriteRepository {
    private FavoriteDao mFavoriteDao;
    private LiveData<List<Favorite>> mFavoriteMovies;
    private LiveData<List<Favorite>> mFavoriteTVShows;

    public FavoriteRepository(Application application) {
        FavoriteRoomDatabase db = FavoriteRoomDatabase.getDatabase(application);
        mFavoriteDao = db.favoriteDao();

        String categoryMovie = "movie";
        String categoryTVShow = "tvshow";
        mFavoriteMovies = mFavoriteDao.getFavoriteMovies(categoryMovie);
        mFavoriteTVShows = mFavoriteDao.getFavoriteTVShow(categoryTVShow);
    }

    public LiveData<List<Favorite>> getFavoriteMovies() {
        return mFavoriteMovies;
    }

    public LiveData<List<Favorite>> getFavoriteTVShows() {
        return mFavoriteTVShows;
    }

    public void insert (Favorite favorite) {
        new insertAsyncTask(mFavoriteDao).execute(favorite);
    }

    public void delete(Favorite favorite) {
        new deleteAsyncTask(mFavoriteDao).execute(favorite);
    }

    public int count(int id) {
        return mFavoriteDao.count(id);
    }

    private static class insertAsyncTask extends AsyncTask<Favorite, Void, Void> {
        private FavoriteDao mAsyncTaskDao;

        insertAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            mAsyncTaskDao.insert(favorites[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Favorite, Void, Void> {
        private FavoriteDao mAsyncTaskDao;

        deleteAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            mAsyncTaskDao.delete(favorites[0]);
            return null;
        }
    }
}
