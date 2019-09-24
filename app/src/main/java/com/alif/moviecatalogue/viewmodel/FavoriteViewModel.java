package com.alif.moviecatalogue.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alif.moviecatalogue.repository.FavoriteRepository;
import com.alif.moviecatalogue.repository.model.Favorite;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {
    private FavoriteRepository mRepository;
    private LiveData<List<Favorite>> mFavoriteMovies;
    private LiveData<List<Favorite>> mFavoriteTVShows;

    public FavoriteViewModel(Application application) {
        super(application);
        mRepository = new FavoriteRepository(application);
        mFavoriteMovies = mRepository.getFavoriteMovies();
        mFavoriteTVShows = mRepository.getFavoriteTVShows();
    }

    public LiveData<List<Favorite>> getFavoriteMovies() {
        return mFavoriteMovies;
    }

    public LiveData<List<Favorite>> getFavoriteTVShows() {
        return mFavoriteTVShows;
    }

    public void insert(Favorite favorite) {
        mRepository.insert(favorite);
    }

    public void delete(Favorite favorite) {
        mRepository.delete(favorite);
    }

    public int count(int favoriteId) {
        return mRepository.count(favoriteId);
    }
}
