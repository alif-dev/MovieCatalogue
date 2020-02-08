package com.alif.moviecatalogue.repository.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TVShowResult implements Parcelable {
    @SerializedName("original_name")
    private String originalName;
    @SerializedName("genre_ids")
    private List<Integer> genreIds;
    @SerializedName("name")
    private String name;
    @SerializedName("popularity")
    private float popularity;
    @SerializedName("origin_country")
    private List<String> originCountry;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("first_air_date")
    private String firstAirDate;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("original_language")
    private String  originalLanguage;
    @SerializedName("id")
    private int id;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;

    public TVShowResult() {

    }

    protected TVShowResult(Parcel in) {
        originalName = in.readString();
        genreIds = new ArrayList<>();
        in.readList(genreIds, Integer.class.getClassLoader());
        name = in.readString();
        popularity = in.readFloat();
        originCountry = new ArrayList<>();
        in.readList(originCountry, String.class.getClassLoader());
        voteCount = in.readInt();
        firstAirDate = in.readString();
        backdropPath = in.readString();
        originalLanguage = in.readString();
        id = in.readInt();
        voteAverage = in.readFloat();
        overview = in.readString();
        posterPath = in.readString();
    }

    public static final Creator<TVShowResult> CREATOR = new Creator<TVShowResult>() {
        @Override
        public TVShowResult createFromParcel(Parcel in) {
            return new TVShowResult(in);
        }

        @Override
        public TVShowResult[] newArray(int size) {
            return new TVShowResult[size];
        }
    };

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalName);
        parcel.writeList(genreIds);
        parcel.writeString(name);
        parcel.writeFloat(popularity);
        parcel.writeList(originCountry);
        parcel.writeInt(voteCount);
        parcel.writeString(firstAirDate);
        parcel.writeString(backdropPath);
        parcel.writeString(originalLanguage);
        parcel.writeInt(id);
        parcel.writeFloat(voteAverage);
        parcel.writeString(overview);
        parcel.writeString(posterPath);
    }
}
