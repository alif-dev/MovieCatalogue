package com.alif.moviecatalogue.viewmodel;

import android.util.Log;

import com.alif.moviecatalogue.BuildConfig;
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

public class NotificationViewModel {

    public static ArrayList<MovieResult> getMoviesReleasedToday() {
        String API_KEY = BuildConfig.TMDB_API_KEY;
        
        // get today's date
        Date now = new Date(); // new Date() will get today's date and time
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // only get the date and use this format
        String todayDate = simpleDateFormat.format(now);

        final ArrayList<MovieResult> todayReleasedMovies = new ArrayList<>();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getMoviesReleasedToday(API_KEY, todayDate, todayDate);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        todayReleasedMovies.addAll(response.body().getMovieResults());
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable error) {
                Log.d("onReleaseTodayFailure", error.getMessage());
            }
        });

        return todayReleasedMovies;
    }
}
