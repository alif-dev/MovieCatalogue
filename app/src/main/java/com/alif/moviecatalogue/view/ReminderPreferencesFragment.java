package com.alif.moviecatalogue.view;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.alif.moviecatalogue.R;

public class ReminderPreferencesFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
