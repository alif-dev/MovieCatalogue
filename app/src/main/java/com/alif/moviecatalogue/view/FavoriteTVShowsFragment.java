package com.alif.moviecatalogue.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alif.moviecatalogue.R;
import com.alif.moviecatalogue.repository.model.room.entity.Favorite;
import com.alif.moviecatalogue.view.adapter.FavoriteTVShowItemAdapter;
import com.alif.moviecatalogue.viewmodel.FavoriteViewModel;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteTVShowsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("FieldCanBeLocal")
public class FavoriteTVShowsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private FavoriteViewModel viewModel;
    private FavoriteTVShowItemAdapter favoriteTVShowItemAdapter;


    public FavoriteTVShowsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteTVShowsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteTVShowsFragment newInstance(String param1, String param2) {
        FavoriteTVShowsFragment fragment = new FavoriteTVShowsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tvshows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favoriteTVShowItemAdapter = new FavoriteTVShowItemAdapter(getActivity());
        favoriteTVShowItemAdapter.notifyDataSetChanged();
        favoriteTVShowItemAdapter.setOnItemClickCallBack(new FavoriteTVShowItemAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Favorite favorite) {
                Intent intent = new Intent(getActivity(), TVShowDetailActivity.class);
                intent.putExtra(TVShowDetailActivity.FAVORITE_TVSHOW_DATA_KEY, favorite);
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerview_favoritetvshows);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(favoriteTVShowItemAdapter);

        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        viewModel.getFavoriteTVShows().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                favoriteTVShowItemAdapter.setFavoriteList(favorites);
            }
        });

        // add the swipe-to-delete functionality to the recyclerview to delete the favorite tvshows
        addSwipeToDeleteFunctionality();
    }

    private void addSwipeToDeleteFunctionality() {
        ItemTouchHelper helper = new ItemTouchHelper(
                // swipe left to delete favorite tvshow
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Favorite favorite = favoriteTVShowItemAdapter.getFavoriteAtPosition(position);
                        Toast.makeText(getActivity(),favorite.getTitle() + " "
                                + getContext().getString(R.string.toast_favorite_deleted), Toast.LENGTH_LONG).show();

                        // delete the favorite
                        viewModel.delete(favorite);
                    }
                });
        helper.attachToRecyclerView(recyclerView);
    }
}
