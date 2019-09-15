package com.example.moviecatalogue.repository.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {
    @SerializedName("original_title")
    private String title;
    @SerializedName("vote_average")
    private String rating;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("poster_path")
    private String poster;
    @SerializedName("overview")
    private String description;
    @SerializedName("genre_ids")
    private List<Integer> genre_ids;

    public Movie() {

    }

    public Movie(JSONObject object) {
        try {
            String title = object.getString("original_title");
            String rating = object.getString("vote_average");
            String release_date = object.getString("release_date");
            String poster = object.getString("poster_path");
            String description = object.getString("overview");
            JSONArray genre_ids_json = object.getJSONArray("genre_ids");
            List<Integer> genre_ids = new ArrayList<>();
            for (int i = 0; i < genre_ids_json.length(); i++) {
                genre_ids.add(genre_ids_json.getInt(i));
            }

            this.title = title;
            this.rating = rating;
            this.release_date = release_date;
            this.poster = poster;
            this.description = description;
            this.genre_ids = genre_ids;
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

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        rating = in.readString();
        release_date = in.readString();
        poster = in.readString();
        description = in.readString();
        genre_ids = new ArrayList<>();
        in.readList(genre_ids, Integer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(rating);
        dest.writeString(release_date);
        dest.writeString(poster);
        dest.writeString(description);
        dest.writeList(genre_ids);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
