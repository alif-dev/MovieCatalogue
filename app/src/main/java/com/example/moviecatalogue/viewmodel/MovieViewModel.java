package com.example.moviecatalogue.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviecatalogue.repository.model.MovieResponse;
import com.example.moviecatalogue.repository.model.MovieResult;
import com.example.moviecatalogue.repository.remotedatasource.ApiClient;
import com.example.moviecatalogue.repository.remotedatasource.ApiInterface;

import java.util.ArrayList;

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
        // String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=en-US";
        // final ArrayList<MovieResponse> movieList = new ArrayList<>();
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
                        //Log.d("movieResult: ", response.body().getMovieResults().get(0).getOriginalTitle());
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable error) {
                Log.d("onFailure", error.getMessage());
                dataRetrieved = "failed";
            }
        });

        /*AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        Movie movieItems = new Movie(movie);
                        movieList.add(movieItems);
                    }
                    movies.postValue(movieList);
                    dataRetrieved = "success";
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
                dataRetrieved = "failed";
            }
        });*/
    }
}
