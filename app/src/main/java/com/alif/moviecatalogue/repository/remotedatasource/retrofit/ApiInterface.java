package com.alif.moviecatalogue.repository.remotedatasource.retrofit;

import com.alif.moviecatalogue.repository.model.MovieResponse;
import com.alif.moviecatalogue.repository.model.TVShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    // https://api.themoviedb.org/3/discover/movie?api_key={API_KEY}&language=en-US
    @GET("discover/movie")
    Call<MovieResponse> getMoviesInformation(@Query("api_key") String apiKey, @Query("language") String language);

    // https://api.themoviedb.org/3/discover/tv?api_key={API_KEY}&language=en-US
    @GET("discover/tv")
    Call<TVShowResponse> getTVShowInformation(@Query("api_key") String apiKey, @Query("language") String language);

    // https://api.themoviedb.org/3/search/movie?api_key={API_KEY}&language=en-US&query={MOVIE_TITLE}
    @GET("search/movie")
    Call<MovieResponse> searchMovie(@Query("api_key") String apiKey, @Query("language") String language, @Query("query") String movieTitle);

    // https://api.themoviedb.org/3/search/tv?api_key={API_KEY}&language=en-US&query={TV_SHOW_NAME}
    @GET("search/tv")
    Call<TVShowResponse> searchTVShow(@Query("api_key") String apiKey, @Query("language") String language, @Query("query") String tvShowName);

    // https://api.themoviedb.org/3/discover/movie?api_key={API_KEY}&primary_release_date.gte={TODAY_DATE}&primary_release_date.lte={TODAY_DATE}
    @GET("discover/movie")
    Call<MovieResponse> getMoviesReleasedToday(@Query("api_key") String apiKey, @Query("primary_release_date.gte") String primaryReleaseDateGte,
                                               @Query("primary_release_date.lte") String primaryReleaseDateLte);

}
