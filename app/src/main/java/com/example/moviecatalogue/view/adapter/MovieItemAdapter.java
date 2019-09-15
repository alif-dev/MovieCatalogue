package com.example.moviecatalogue.view.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviecatalogue.R;
import com.example.moviecatalogue.repository.model.Movie;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.moviecatalogue.view.utility.DateFormatter.formatDateToLocal;

public class MovieItemAdapter extends RecyclerView.Adapter<MovieItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Movie> movieList = new ArrayList<>();

    public MovieItemAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(ArrayList<Movie> movieArrayList) {
        movieList.clear();
        movieList.addAll(movieArrayList);
        notifyDataSetChanged();
    }

    private OnItemClickCallback onItemClickCallBack;

    public void setOnItemClickCallBack(OnItemClickCallback onItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack;
    }

    private void setRatingProgressBar(final ViewHolder holder, Movie movie) {
        // ProgressBar colors
        String red = "#FF0000";
        String orange = "#FF5722";
        String yellow = "#FFDD00";
        String green = "#4CAF50";
        String darkGreen = "#009688";

        float rating = Float.parseFloat(movie.getRating()) * 10;
        if (rating >= 0 && rating <= 20) {
            holder.pbRating.setProgressTintList(ColorStateList.valueOf(Color.parseColor(red)));
            holder.pbRating.setProgress((int) rating);
        } else if (rating > 20 && rating <= 40) {
            holder.pbRating.setProgressTintList(ColorStateList.valueOf(Color.parseColor(orange)));
            holder.pbRating.setProgress((int) rating);
        } else if (rating > 40 && rating <= 60) {
            holder.pbRating.setProgressTintList(ColorStateList.valueOf(Color.parseColor(yellow)));
            holder.pbRating.setProgress((int) rating);
        } else if (rating > 60 && rating <= 80) {
            holder.pbRating.setProgressTintList(ColorStateList.valueOf(Color.parseColor(green)));
            holder.pbRating.setProgress((int) rating);
        } else if (rating > 80 && rating <= 100) {
            holder.pbRating.setProgressTintList(ColorStateList.valueOf(Color.parseColor(darkGreen)));
            holder.pbRating.setProgress((int) rating);
        }

        holder.tvPbRating.setText(String.format(Locale.ENGLISH, "%.1f%%", rating));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        Glide.with(holder.itemView.getContext())
                .load("http://image.tmdb.org/t/p/w342" + movie.getPoster())
                .into(holder.imgPoster);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvReleaseDate.setText(formatDateToLocal(movie.getRelease_date()));
        setRatingProgressBar(holder, movie);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallBack.onItemClicked(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle;
        TextView tvReleaseDate;
        ProgressBar pbRating;
        TextView tvPbRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_tvshow_poster);
            tvTitle = itemView.findViewById(R.id.tv_tvshow_title);
            tvReleaseDate = itemView.findViewById(R.id.tv_tvshow_release_date_value);
            pbRating = itemView.findViewById(R.id.pb_tv_show_rating);
            tvPbRating = itemView.findViewById(R.id.tv_tvshow_pb_rating);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie movie);
    }
}
