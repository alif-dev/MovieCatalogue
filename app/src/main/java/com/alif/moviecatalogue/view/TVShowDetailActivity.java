package com.alif.moviecatalogue.view;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.alif.moviecatalogue.R;
import com.alif.moviecatalogue.repository.model.TVShowResult;
import com.alif.moviecatalogue.repository.model.room.entity.Favorite;
import com.alif.moviecatalogue.viewmodel.FavoriteViewModel;
import com.bumptech.glide.Glide;

import java.util.Locale;

import static com.alif.moviecatalogue.view.utility.DateFormatter.formatDateToLocal;
import static com.alif.moviecatalogue.view.utility.GenreConverter.convertGenreIdsToAStringOfNames;

@SuppressWarnings("FieldCanBeLocal")
public class TVShowDetailActivity extends AppCompatActivity {
    private TextView tvTitle;
    private ImageView imgPoster;
    private TextView tvReleaseDate;
    private ProgressBar progressBar;
    private ProgressBar pbRating;
    private ObjectAnimator objAnimator;
    private TextView tvPbRating;
    private TextView tvDescription;
    private TextView tvGenres;
    private ConstraintLayout detailInformationLayout;
    private Menu menu;
    public static String TVSHOW_DATA_KEY = "tvShowData";
    private TVShowResult tvShow;
    private String category = "tvshow";
    private FavoriteViewModel viewModel;
    private Favorite favorite;
    int[] favoriteCount = new int[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_tvshow_actionbar);
        }

        tvShow = getIntent().getParcelableExtra(TVSHOW_DATA_KEY);
        defineFavoriteData();

        detailInformationLayout = findViewById(R.id.tvshow_dtl_information_viewgroup);
        progressBar = findViewById(R.id.progressbar_dtl_tvshows);
        tvTitle = findViewById(R.id.tv_dtl_tvshow_title);
        imgPoster = findViewById(R.id.img_dtl_tvshow_poster);
        tvReleaseDate = findViewById(R.id.tv_dtl_tvshow_release_date_value);
        pbRating = findViewById(R.id.pb_dtl_tvshow_rating);
        tvPbRating = findViewById(R.id.tv_dtl_pb_tvshow_rating);
        tvDescription = findViewById(R.id.tv_dtl_tvshow_description_value);
        tvGenres = findViewById(R.id.tv_dtl_tvshow_genres_value);
        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        showDetails();
    }

    private void showDetails() {
        // use handler to show loading before showing tvshow details
        showLoading(true);
        detailInformationLayout.setVisibility(View.INVISIBLE);
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(800);
                    favoriteCount[0] = viewModel.count(tvShow.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    public void run() {
                        showLoading(false);
                        detailInformationLayout.setVisibility(View.VISIBLE);
                        if (tvShow != null) {
                            tvTitle.setText(tvShow.getOriginalName());
                            Glide.with(TVShowDetailActivity.this)
                                    .load("http://image.tmdb.org/t/p/w342" + tvShow.getPosterPath())
                                    .into(imgPoster);
                            tvReleaseDate.setText(formatDateToLocal(tvShow.getFirstAirDate()));
                            setRatingProgressBar(tvShow);
                            tvDescription.setText(tvShow.getOverview());
                            tvGenres.setText(convertGenreIdsToAStringOfNames(tvShow.getGenreIds()));

                            if (favoriteCount[0] > 0) {
                                menu.getItem(0).setIcon(R.drawable.ic_favorite_red);
                            } else {
                                menu.getItem(0).setIcon(R.drawable.ic_favorite_white_opacity_75);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void defineFavoriteData() {
        favorite = new Favorite();
        favorite.setId(tvShow.getId());
        favorite.setTitle(tvShow.getName());
        favorite.setPosterPath(tvShow.getPosterPath());
        favorite.setReleaseDate(tvShow.getFirstAirDate());
        favorite.setRating(tvShow.getVoteAverage());
        favorite.setOverview(tvShow.getOverview());
        favorite.setGenre(convertGenreIdsToAStringOfNames(tvShow.getGenreIds()));
        favorite.setCategory(category);
    }

    private void setRatingProgressBar(TVShowResult tvShow) {
        // ProgressBar colors
        String red = "#FF0000";
        String orange = "#FF5722";
        String yellow = "#FFDD00";
        String green = "#4CAF50";
        String darkGreen = "#009688";

        float rating = tvShow.getVoteAverage() * 10;
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

    private void insertFavoriteTVShowToDB() {
        viewModel.insert(favorite);
    }

    private void deleteFavoriteTVShowFromDB() {
        viewModel.delete(favorite);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_favorite:
                if (favoriteCount[0] > 0) {
                    deleteFavoriteTVShowFromDB();
                    favoriteCount[0] = 0;
                    menu.getItem(0).setIcon(R.drawable.ic_favorite_white_opacity_75);
                    Toast.makeText(this, getString(R.string.toast_unfavorite_tvshow), Toast.LENGTH_SHORT).show();
                } else {
                    insertFavoriteTVShowToDB();
                    favoriteCount[0] = 1;
                    menu.getItem(0).setIcon(R.drawable.ic_favorite_red);
                    Toast.makeText(this, getString(R.string.toast_favorite_tvshow), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_change_language_setting:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                break;
            case R.id.action_change_reminder_setting:
                Intent reminderSettingIntent = new Intent(this, ReminderSettingActivity.class);
                startActivity(reminderSettingIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
