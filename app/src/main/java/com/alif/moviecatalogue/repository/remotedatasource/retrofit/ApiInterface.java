package com.alif.moviecatalogue.repository.remotedatasource.retrofit;

import com.alif.moviecatalogue.repository.model.MovieResponse;
import com.alif.moviecatalogue.repository.model.TVShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    // https://api.themoviedb.org/3/discover/movie?api_key=13781a22a1b2140624ab4e366bec9eb8&language=en-US
    @GET("movie")
    Call<MovieResponse> getMoviesInformation(@Query("api_key") String apiKey, @Query("language") String language);

    // https://api.themoviedb.org/3/discover/tv?api_key=13781a22a1b2140624ab4e366bec9eb8&language=en-US
    @GET("tv")
    Call<TVShowResponse> getTVShowInformation(@Query("api_key") String apiKey, @Query("language") String language);
}
