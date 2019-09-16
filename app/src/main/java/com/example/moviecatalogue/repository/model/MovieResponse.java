package com.example.moviecatalogue.repository.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("results")
    private ArrayList<MovieResult> movieResults;

    public MovieResponse(int page, int totalResults, int totalPages, ArrayList<MovieResult> movieResults) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.movieResults = movieResults;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public ArrayList<MovieResult> getMovieResults() {
        return movieResults;
    }

    public void setMovieResults(ArrayList<MovieResult> movieResults) {
        this.movieResults = movieResults;
    }
}
