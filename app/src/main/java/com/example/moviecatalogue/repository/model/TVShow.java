package com.example.moviecatalogue.repository.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class TVShow implements Parcelable {
    private String title;
    private String rating;
    private String release_date;
    private String poster;
    private String description;
    private String genres;

    public TVShow() {

    }

    public TVShow(JSONObject object) {
        try {
            String title = object.getString("original_name");
            String rating = object.getString("vote_average");
            String release_date = object.getString("first_air_date");
            String poster = object.getString("poster_path");
            String description = object.getString("overview");

            this.title = title;
            this.rating = rating;
            this.release_date = release_date;
            this.poster = poster;
            this.description = description;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    protected TVShow(Parcel in) {
        title = in.readString();
        rating = in.readString();
        release_date = in.readString();
        poster = in.readString();
        description = in.readString();
        genres = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(rating);
        dest.writeString(release_date);
        dest.writeString(poster);
        dest.writeString(description);
        dest.writeString(genres);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TVShow> CREATOR = new Creator<TVShow>() {
        @Override
        public TVShow createFromParcel(Parcel in) {
            return new TVShow(in);
        }

        @Override
        public TVShow[] newArray(int size) {
            return new TVShow[size];
        }
    };
}
