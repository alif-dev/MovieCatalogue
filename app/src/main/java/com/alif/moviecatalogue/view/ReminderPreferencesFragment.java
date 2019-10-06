package com.alif.moviecatalogue.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.alif.moviecatalogue.R;
import com.alif.moviecatalogue.repository.model.MovieResult;
import com.alif.moviecatalogue.view.utility.AlarmReceiver;
import com.alif.moviecatalogue.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class ReminderPreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SwitchPreference releaseReminderPreference;
    private SwitchPreference dailyReminderPreference;

    private String RELEASE_REMINDER_KEY;
    private String DAILY_REMINDER_KEY;

    private AlarmReceiver alarmReceiver;
    private String dailyReminderMessage;
    public static ArrayList<MovieResult> todayReleasedMovies;

    private Observer<ArrayList<MovieResult>> getMovie = new Observer<ArrayList<MovieResult>>() {
        @Override
        public void onChanged(ArrayList<MovieResult> movies) {
            if (movies != null) {
                todayReleasedMovies = movies;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, getMovie);
        movieViewModel.getMoviesReleasedToday();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        init();
    }

    private void init() {
        RELEASE_REMINDER_KEY = getString(R.string.key_release_reminder);
        DAILY_REMINDER_KEY = getString(R.string.key_daily_reminder);
        dailyReminderMessage = getString(R.string.message_daily_reminder);

        releaseReminderPreference = findPreference(RELEASE_REMINDER_KEY);
        dailyReminderPreference = findPreference(DAILY_REMINDER_KEY);

        alarmReceiver = new AlarmReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(RELEASE_REMINDER_KEY)) {
            boolean isChecked = sharedPreferences.getBoolean(RELEASE_REMINDER_KEY, false);
            // isChecked can be true or false
            releaseReminderPreference.setChecked(isChecked);
            if (isChecked) {
                // activate notification for release reminder
                alarmReceiver.setReleaseReminderAlarm(Objects.requireNonNull(getActivity()));
                // show release reminder toast
                Toast.makeText(getActivity(), R.string.toast_release_reminder_set, Toast.LENGTH_SHORT).show();
            } else {
                // inactivate notification for release reminder
                alarmReceiver.cancelReleaseReminderAlarm(Objects.requireNonNull(getActivity()));
                // show cancel release reminder toast
                Toast.makeText(getActivity(), R.string.toast_release_reminder_cancel, Toast.LENGTH_SHORT).show();
            }
        }

        if (key.equals(DAILY_REMINDER_KEY)) {
            boolean isChecked = sharedPreferences.getBoolean(DAILY_REMINDER_KEY, false);
            // isChecked can be true or false
            dailyReminderPreference.setChecked(isChecked);
            if (isChecked) {
                // activate notification for daily reminder
                alarmReceiver.setDailyReminderAlarm(Objects.requireNonNull(getActivity()), dailyReminderMessage);
                // show daily reminder toast
                Toast.makeText(getActivity(), R.string.toast_daily_reminder_set, Toast.LENGTH_SHORT).show();
            } else {
                // inactivate notification for daily reminder
                alarmReceiver.cancelDailyReminderAlarm(Objects.requireNonNull(getActivity()));
                // show cancel daily release reminder toast
                Toast.makeText(getActivity(), R.string.toast_daily_reminder_cancel, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
