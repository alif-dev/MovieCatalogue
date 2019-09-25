package com.alif.moviecatalogue.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alif.moviecatalogue.repository.model.TVShowResponse;
import com.alif.moviecatalogue.repository.model.TVShowResult;
import com.alif.moviecatalogue.repository.remotedatasource.retrofit.ApiClient;
import com.alif.moviecatalogue.repository.remotedatasource.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowViewModel extends ViewModel {
    private static final String API_KEY = "13781a22a1b2140624ab4e366bec9eb8";
    private static final String LANGUAGE = "en-US";
    private MutableLiveData<ArrayList<TVShowResult>> tvShows = new MutableLiveData<>();
    public String dataRetrieved = "";

    public LiveData<ArrayList<TVShowResult>> getTVShows() {
        return tvShows;
    }

    public void setTvShows() {
        // the url is "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=" + LANGUAGE
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TVShowResponse> call = apiService.getTVShowInformation(API_KEY, LANGUAGE);

        call.enqueue(new Callback<TVShowResponse>() {
            @Override
            public void onResponse(Call<TVShowResponse> call, Response<TVShowResponse> response) {
                // Response<TVShowResponse> contains an object of TVShowResponse
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // TVShowResponse object contains a List of TVShowResult objects
                        // a TVShowResult object contains information of a tvshow
                        tvShows.postValue(response.body().getTvShowResults());
                        dataRetrieved = "success";
                    }
                }
            }

            @Override
            public void onFailure(Call<TVShowResponse> call, Throwable error) {
                Log.d("onTVShowFailure", error.getMessage());
                dataRetrieved = "failed";
            }
        });
    }

    public void searchTVShow(String tvShowName) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TVShowResponse> call = apiService.searchTVShow(API_KEY, LANGUAGE, tvShowName);

        call.enqueue(new Callback<TVShowResponse>() {
            @Override
            public void onResponse(Call<TVShowResponse> call, Response<TVShowResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        tvShows.postValue(response.body().getTvShowResults());
                    }
                }
            }

            @Override
            public void onFailure(Call<TVShowResponse> call, Throwable error) {
                Log.d("onTVShowSearchFailure", error.getMessage());
            }
        });
    }
}
