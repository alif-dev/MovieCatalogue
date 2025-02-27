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

public class FavoriteMovieItemAdapter extends RecyclerView.Adapter<FavoriteMovieItemAdapter.ViewHolder> {
    private ArrayList<Favorite> favoriteList = new ArrayList<>();
    private OnItemClickCallback onItemClickCallBack;

    public void setOnItemClickCallBack(OnItemClickCallback onItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack;
    }

    public FavoriteMovieItemAdapter(Context context) {

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_favorite_movie, viewGroup, false);
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
        setRatingProgressBar(holder, favorite.getRating());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallBack.onItemClicked(favorite);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    private void setRatingProgressBar(final ViewHolder holder, float voteAverage) {
        // ProgressBar colors
        String red = "#FF0000";
        String orange = "#FF5722";
        String yellow = "#FFDD00";
        String green = "#4CAF50";
        String darkGreen = "#009688";

        int rating = (int) (voteAverage * 10);
        if (rating >= 0 && rating <= 20) {
            holder.pbRating.setProgressTintList(ColorStateList.valueOf(Color.parseColor(red)));
            holder.pbRating.setProgress(rating);
        } else if (rating > 20 && rating <= 40) {
            holder.pbRating.setProgressTintList(ColorStateList.valueOf(Color.parseColor(orange)));
            holder.pbRating.setProgress(rating);
        } else if (rating > 40 && rating <= 60) {
            holder.pbRating.setProgressTintList(ColorStateList.valueOf(Color.parseColor(yellow)));
            holder.pbRating.setProgress(rating);
        } else if (rating > 60 && rating <= 80) {
            holder.pbRating.setProgressTintList(ColorStateList.valueOf(Color.parseColor(green)));
            holder.pbRating.setProgress(rating);
        } else if (rating > 80 && rating <= 100) {
            holder.pbRating.setProgressTintList(ColorStateList.valueOf(Color.parseColor(darkGreen)));
            holder.pbRating.setProgress(rating);
        }

        holder.tvPbRating.setText(String.format(Locale.ENGLISH, "%d%%", rating));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle;
        TextView tvReleaseDate;
        ProgressBar pbRating;
        TextView tvPbRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_movie_poster);
            tvTitle = itemView.findViewById(R.id.tv_movie_title);
            tvReleaseDate = itemView.findViewById(R.id.tv_movie_release_date_value);
            pbRating = itemView.findViewById(R.id.pb_movie_rating);
            tvPbRating = itemView.findViewById(R.id.tv_movie_pb_rating);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Favorite favorite);
    }
}
