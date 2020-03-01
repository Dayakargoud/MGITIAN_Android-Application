package com.dayakar.mgitian.Fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.dayakar.mgitian.R;

public class SettingsPrefFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);


    }
}
