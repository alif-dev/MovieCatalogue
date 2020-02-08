package com.alif.moviecatalogue.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.alif.moviecatalogue.R;
import com.alif.moviecatalogue.view.utility.DailyReminderAlarmReceiver;
import com.alif.moviecatalogue.view.utility.ReleaseReminderAlarmReceiver;

public class ReminderPreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SwitchPreference releaseReminderPreference;
    private SwitchPreference dailyReminderPreference;

    private String RELEASE_REMINDER_KEY;
    private String DAILY_REMINDER_KEY;

    private DailyReminderAlarmReceiver dailyAlarmReceiver;
    private ReleaseReminderAlarmReceiver releaseAlarmReceiver;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        init();
    }

    private void init() {
        RELEASE_REMINDER_KEY = getString(R.string.key_release_reminder);
        DAILY_REMINDER_KEY = getString(R.string.key_daily_reminder);

        releaseReminderPreference = findPreference(RELEASE_REMINDER_KEY);
        dailyReminderPreference = findPreference(DAILY_REMINDER_KEY);

        dailyAlarmReceiver = new DailyReminderAlarmReceiver();
        releaseAlarmReceiver = new ReleaseReminderAlarmReceiver();
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
                releaseAlarmReceiver.setReleaseReminderAlarm(getActivity());
                // show release reminder toast
                Toast.makeText(getActivity(), R.string.toast_release_reminder_set, Toast.LENGTH_SHORT).show();
            } else {
                // inactivate notification for release reminder
                releaseAlarmReceiver.cancelReleaseReminderAlarm(getActivity());
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
                dailyAlarmReceiver.setDailyReminderAlarm(getActivity());
                // show daily reminder toast
                Toast.makeText(getActivity(), R.string.toast_daily_reminder_set, Toast.LENGTH_SHORT).show();
            } else {
                // inactivate notification for daily reminder
                dailyAlarmReceiver.cancelDailyReminderAlarm(getActivity());
                // show cancel daily release reminder toast
                Toast.makeText(getActivity(), R.string.toast_daily_reminder_cancel, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
