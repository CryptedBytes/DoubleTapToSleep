package com.cryptedbytes.dtts;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
            refreshPreferenceSummaryTexts();
        }
        catch (Exception ignored)
        {

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
        catch (Exception ignored)
        {

        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        refreshPreferenceSummaryTexts();
    }


    public void refreshPreferenceSummaryTexts(){
        Log.d("DTTS","preference change");
        ListPreference workingMethodPreference = (ListPreference)  findPreference("screenlockmethod");

        if (workingMethodPreference != null) {
            workingMethodPreference.setSummary("Current mode: " + workingMethodPreference.getEntry().toString().substring(0,workingMethodPreference.getEntry().toString().indexOf('(') -1));
        }
    }
}
