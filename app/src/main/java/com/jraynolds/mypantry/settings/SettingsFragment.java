package com.jraynolds.mypantry.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.jraynolds.mypantry.R;

/**
 * Created by Jasper on 2/24/2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_main);
    }

}
