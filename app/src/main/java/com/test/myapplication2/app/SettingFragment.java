package com.test.myapplication2.app;

/**
 * Created by mina on 7/11/16.
 */

//import android.support.v4.app.Fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Adapter;
import com.test.myapplication2.app.R;


public class SettingFragment extends PreferenceFragment implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_layout);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        getPreferenceManager().setDefaultValues(getActivity(), R.xml.preferences_layout, false);

        Adapter adapter = getPreferenceScreen().getRootAdapter();
        for (int i = 0 ; i < getPreferenceScreen().getRootAdapter().getCount();i++){
            Object object = adapter.getItem(i);
            updatePreference((Preference)object);
        }
        setHasOptionsMenu(true);

        
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.setting_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    //    @Override
//    public void onResume() {
//        super.onResume();
//        getPreferenceManager().getSharedPreferences();
//        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
//            Preference preference = getPreferenceScreen().getPreference(i);
//            updatePreference(getPreferenceManager().getSharedPreferences(), preference);
//        }
//    }

    @Override
    public void onClick(View view) {



    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference = findPreference(s);
        updatePreference( preference);
    }

    public void updatePreference(Preference preference) {

        if (preference instanceof NumberPickerPreference) {
            preference.setTitle(((NumberPickerPreference) preference).getDialogTitle() + " " + ((NumberPickerPreference) preference).getValue()  + " min");
        }
    }
}
