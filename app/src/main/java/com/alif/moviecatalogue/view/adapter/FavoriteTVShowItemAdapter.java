package com.alif.moviecatalogue.view.adapter;

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

import com.alif.moviecatalogue.R;
import com.alif.moviecatalogue.repository.model.room.entity.Favorite;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.alif.moviecatalogue.view.utility.DateFormatter.formatDateToLocal;

public class FavoriteTVShowItemAdapter extends RecyclerView.Adapter<FavoriteTVShowItemAdapter.ViewHolder> {
    private ArrayList<Favorite> favoriteList = new ArrayList<>();

    public FavoriteTVShowItemAdapter(Context context) {

    }

    public void setFavoriteList(List<Favorite> favorites) {
        favoriteList.clear();
        favoriteList = (ArrayList<Favorite>) favorites;
        notifyDataSetChanged();
    }

    public Favorite getFavoriteAtPosition(int position) {
        return favoriteList.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_favorite_tvshow, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Favorite favorite = favoriteList.get(position);
        Glide.with(holder.itemView.getContext())
                .load("http://image.tmdb.org/t/p/w342" + favorite.getPosterPath())
                .into(holder.imgPoster);
        holder.tvTitle.setText(favorite.getTitle());
        holder.tvReleaseDate.setText(formatDateToLocal(favorite.getReleaseDate()));
        setRatingProgressBar(holder, favorite);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    private void setRatingProgressBar(final ViewHolder holder, Favorite favorite) {
        // ProgressBar colors
        String red = "#FF0000";
        String orange = "#FF5722";
        String yellow = "#FFDD00";
        String green = "#4CAF50";
        String darkGreen = "#009688";

        float rating = favorite.getRating() * 10;
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
            pbRating = itemView.findViewById(R.id.pb_tvshow_rating);
            tvPbRating = itemView.findViewById(R.id.tv_tvshow_pb_rating);
        }
    }
}
