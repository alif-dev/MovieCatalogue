package com.example.moviecatalogue.view;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.repository.model.Movie;
import com.example.moviecatalogue.view.adapter.MovieItemAdapter;
import com.example.moviecatalogue.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("FieldCanBeLocal")
public class MoviesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvNetworkErrorMessage;

    private MovieViewModel movieViewModel;
    private MovieItemAdapter movieItemAdapter;

    public MoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoviesFragment newInstance(String param1, String param2) {
        MoviesFragment fragment = new MoviesFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        movieItemAdapter = new MovieItemAdapter(getActivity());
        movieItemAdapter.notifyDataSetChanged();
        movieItemAdapter.setOnItemClickCallBack(new MovieItemAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie) {
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.MOVIE_DATA_KEY, movie);
                startActivity(intent);
            }
        });

        recyclerView = rootView.findViewById(R.id.recyclerview_movies);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(movieItemAdapter);
        progressBar = rootView.findViewById(R.id.progressbar_movies);
        tvNetworkErrorMessage = rootView.findViewById(R.id.tv_movies_error_message);
        showLoading(true);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, getMovie);
        movieViewModel.setMovies();

        // Show error message if data is failed to fetch from the server or API request doesn't happen
        if (movieViewModel.dataRetrieved.equals("failed")) {
            showLoading(false);
            showNetworkErrorMessage(true);
        } else if (movieViewModel.dataRetrieved.equals("")) {
            // Wait for 3 seconds for data to be retrieved
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        public void run() {
                            showLoading(false);
                            // Check whether data is still unattained or not after 3 seconds. If so, show error message
                            if (movieViewModel.dataRetrieved.equals("")) {
                                showNetworkErrorMessage(true);
                            }
                        }
                    });
                }
            }).start();
        }

        return rootView;
    }

    private Observer<ArrayList<Movie>> getMovie = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> movies) {
            if (movies != null) {
                showNetworkErrorMessage(false);
                showLoading(false);
                movieItemAdapter.setMovieList(movies);
            }
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showNetworkErrorMessage(Boolean state) {
        if (state) {
            tvNetworkErrorMessage.setVisibility(View.VISIBLE);
        } else {
            tvNetworkErrorMessage.setVisibility(View.GONE);
        }
    }
}
