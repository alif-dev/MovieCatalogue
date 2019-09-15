package com.example.moviecatalogue.repository.remotedatasource;

import com.example.moviecatalogue.repository.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie")
    Call<Movie> getMovieList(@Query("api_key") String apiKey, @Query("language") String language);
}
