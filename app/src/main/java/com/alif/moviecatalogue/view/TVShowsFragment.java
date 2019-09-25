package com.alif.moviecatalogue.view;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alif.moviecatalogue.R;
import com.alif.moviecatalogue.repository.model.TVShowResult;
import com.alif.moviecatalogue.view.adapter.TVShowItemAdapter;
import com.alif.moviecatalogue.viewmodel.TVShowViewModel;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TVShowsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("FieldCanBeLocal")
public class TVShowsFragment extends Fragment {
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

    private TVShowViewModel tvShowViewModel;
    private TVShowItemAdapter tvShowItemAdapter;


    public TVShowsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TVShowsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TVShowsFragment newInstance(String param1, String param2) {
        TVShowsFragment fragment = new TVShowsFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvShowItemAdapter = new TVShowItemAdapter(getActivity());
        tvShowItemAdapter.notifyDataSetChanged();
        tvShowItemAdapter.setOnItemClickCallBack(new TVShowItemAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TVShowResult tvShow) {
                Intent intent = new Intent(getActivity(), TVShowDetailActivity.class);
                intent.putExtra(TVShowDetailActivity.TVSHOW_DATA_KEY, tvShow);
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerview_tvshows);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(tvShowItemAdapter);
        progressBar = view.findViewById(R.id.progressbar_tvshows);
        tvNetworkErrorMessage = view.findViewById(R.id.tv_tvshows_error_message);
        showLoading(true);

        // TVShow ViewModel
        tvShowViewModel = ViewModelProviders.of(this).get(TVShowViewModel.class);
        tvShowViewModel.getTVShows().observe(this, getTVShow);
        tvShowViewModel.setTvShows();

        // show error message if data is failed to fetch from the server or API request doesn't happen
        if (tvShowViewModel.dataRetrieved.equals("")) {
            // wait for 4 seconds for data to be retrieved
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(4000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        public void run() {
                            showLoading(false);
                            // check whether data is still unattained after 4 seconds. If so, show error message
                            if (tvShowViewModel.dataRetrieved.equals("") || tvShowViewModel.dataRetrieved.equals("failed")) {
                                showNetworkErrorMessage(true);
                            }
                        }
                    });
                }
            }).start();
        }
    }

    private Observer<ArrayList<TVShowResult>> getTVShow = new Observer<ArrayList<TVShowResult>>() {
        @Override
        public void onChanged(ArrayList<TVShowResult> tvShows) {
            if (tvShows != null) {
                showNetworkErrorMessage(false);
                showLoading(false);
                tvShowItemAdapter.setTVShowList(tvShows);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        // add SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.action_search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_tvshow_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.toast_search) + query, Toast.LENGTH_SHORT).show();
                    tvShowViewModel.searchTVShow(query);
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_change_language_setting) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
