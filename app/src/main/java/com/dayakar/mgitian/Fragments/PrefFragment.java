package com.dayakar.mgitian.Fragments;

import android.os.Bundle;


import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.dayakar.mgitian.BuildConfig;
import com.dayakar.mgitian.R;

public class PrefFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_about);
        Preference mp=findPreference("developer");
        mp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pref_container,new Developer()).commit();
                return false;
            }
        });

         mp=findPreference("appVersion");
        String current= BuildConfig.VERSION_NAME;

        mp.setSummary("current version "+current);


    }
}

