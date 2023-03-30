package com.example.todolist.Model.ui.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.todolist.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.layout.fragment_settings, rootKey);
    }
}