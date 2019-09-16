package com.example.moviecatalogue.repository.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TVShowResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("results")
    private ArrayList<TVShowResult> tvShowResults;

    public TVShowResponse(int page, int totalResults, int totalPages, ArrayList<TVShowResult> tvShowResults) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.tvShowResults = tvShowResults;
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

    public ArrayList<TVShowResult> getTvShowResults() {
        return tvShowResults;
    }

    public void setTvShowResults(ArrayList<TVShowResult> tvShowResults) {
        this.tvShowResults = tvShowResults;
    }
}
