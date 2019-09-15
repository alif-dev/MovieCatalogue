package com.example.moviecatalogue.view;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviecatalogue.R;
import com.example.moviecatalogue.repository.model.TVShow;

import java.util.Locale;

public class TVShowDetailActivity extends AppCompatActivity {
    private TextView tvTitle;
    private ImageView imgPoster;
    private TextView tvReleaseDate;
    private ProgressBar pbRating;
    private ObjectAnimator objAnimator;
    private TextView tvPbRating;
    private TextView tvDescription;
    private TextView tvGenres;
    public static String TVSHOW_DATA_KEY = "tvShowData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_tvshow_actionbar);
        }

        tvTitle = findViewById(R.id.tv_dtl_tvshow_title);
        imgPoster = findViewById(R.id.img_dtl_tvshow_poster);
        tvReleaseDate = findViewById(R.id.tv_dtl_tvshow_release_date_value);
        pbRating = findViewById(R.id.pb_dtl_tvshow_rating);
        tvPbRating = findViewById(R.id.tv_dtl_pb_tvshow_rating);
        tvDescription = findViewById(R.id.tv_dtl_tvshow_description_value);
        tvGenres = findViewById(R.id.tv_dtl_tvshow_genres_value);

        TVShow tvShow = getIntent().getParcelableExtra(TVSHOW_DATA_KEY);
        tvTitle.setText(tvShow.getTitle());
        Glide.with(this)
                .load(tvShow.getPoster())
                .into(imgPoster);
        tvReleaseDate.setText(tvShow.getRelease_date());
        setRatingProgressBar(tvShow);
        tvDescription.setText(tvShow.getDescription());
        tvGenres.setText(tvShow.getGenres());
    }

    private void setRatingProgressBar(TVShow tvShow) {
        // ProgressBar colors
        String red = "#FF0000";
        String orange = "#FF5722";
        String yellow = "#FFDD00";
        String green = "#4CAF50";
        String darkGreen = "#009688";

        float rating = Float.parseFloat(tvShow.getRating()) * 10;
        if (rating >= 0 && rating <= 20) {
            setRatingProgressBarAnimation(red, (int) rating);
        } else if (rating > 20 && rating <= 40) {
            setRatingProgressBarAnimation(orange, (int) rating);
        } else if (rating > 40 && rating <= 60) {
            setRatingProgressBarAnimation(yellow, (int) rating);
        } else if (rating > 60 && rating <= 80) {
            setRatingProgressBarAnimation(green, (int) rating);
        } else if (rating > 80 && rating <= 100) {
            setRatingProgressBarAnimation(darkGreen, (int) rating);
        }

        tvPbRating.setText(String.format(Locale.ENGLISH, "%.1f%%", rating));
    }

    private void setRatingProgressBarAnimation(String color, int rating) {
        pbRating.setProgressTintList(ColorStateList.valueOf(Color.parseColor(color)));
        objAnimator = ObjectAnimator.ofInt(pbRating, "progress", 0, rating);
        objAnimator.setDuration(2000);
        objAnimator.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_change_language_setting:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
