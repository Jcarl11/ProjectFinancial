package com.example.ratio.PreferenceFragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.MenuItem;

import com.example.ratio.R;
import com.example.ratio.SettingsActivity;

import androidx.annotation.Nullable;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GeneralFragment extends PreferenceFragment {
    private static final String TAG = "GeneralFragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreate: Started");
        Preference preference = findPreference("email_address");
        preference.setOnPreferenceChangeListener(preferenceChangeListener());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Preference.OnPreferenceChangeListener preferenceChangeListener() {

        Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();
                Log.d(TAG, "onPreferenceChange: " + stringValue);
                //preference.setDefaultValue(stringValue);
                return true;
            }
        };
        return listener;
    }
}
