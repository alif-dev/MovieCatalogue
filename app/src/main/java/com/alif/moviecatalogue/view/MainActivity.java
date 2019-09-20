package com.alif.moviecatalogue.view;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.alif.moviecatalogue.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_movies);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragment;

                    switch (menuItem.getItemId()) {
                        case R.id.navigation_movies:
                            fragment = new MoviesFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                                    .commit();
                            return true;
                        case R.id.navigation_tvshows:
                            fragment = new TVShowsFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                                    .commit();
                            return true;
                        case R.id.navigation_favorites:
                            fragment = new FavoritesFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                                    .commit();
                            return true;
                    }
                    return false;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
