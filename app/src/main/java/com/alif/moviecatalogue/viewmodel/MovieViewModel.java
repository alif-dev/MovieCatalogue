package com.alif.moviecatalogue.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alif.moviecatalogue.repository.model.MovieResponse;
import com.alif.moviecatalogue.repository.model.MovieResult;
import com.alif.moviecatalogue.repository.remotedatasource.retrofit.ApiClient;
import com.alif.moviecatalogue.repository.remotedatasource.retrofit.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends ViewModel {
    private static final String API_KEY = "13781a22a1b2140624ab4e366bec9eb8";
    private static final String LANGUAGE = "en-US";
    private MutableLiveData<ArrayList<MovieResult>> movies = new MutableLiveData<>();
    public String dataRetrieved = "";

    public LiveData<ArrayList<MovieResult>> getMovies() {
        return movies;
    }

    public void setMovies() {
        // the url is "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=" + LANGUAGE
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getMoviesInformation(API_KEY, LANGUAGE);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                // Response<MovieResponse> contains an object of MovieResponse
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // MovieResponse object contains a List of MovieResult objects
                        // a MovieResult object contains information of a movie
                        movies.postValue(response.body().getMovieResults());
                        dataRetrieved = "success";
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable error) {
                Log.d("onMovieFailure", error.getMessage());
                dataRetrieved = "failed";
            }
        });
    }

    public void searchMovie(String movieTitle) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.searchMovie(API_KEY, LANGUAGE, movieTitle);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        movies.postValue(response.body().getMovieResults());
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable error) {
                Log.d("onMovieSearchFailure", error.getMessage());
            }
        });
    }

    public void getMoviesReleasedToday() {
        // get today's date
        Date now = new Date(); // new Date() will get today's date and time
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // only get the date and use this format
        String todayDate = simpleDateFormat.format(now);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getMoviesReleasedToday(API_KEY, todayDate, todayDate);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        movies.postValue(response.body().getMovieResults());
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable error) {
                Log.d("onReleaseTodayFailure", error.getMessage());
            }
        });
    }
}
